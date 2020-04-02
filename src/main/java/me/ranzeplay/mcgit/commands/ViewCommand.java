package me.ranzeplay.mcgit.commands;

import me.ranzeplay.mcgit.Constants;
import me.ranzeplay.mcgit.managers.GitManager;
import me.ranzeplay.mcgit.models.Commit;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.io.File;
import java.text.ParseException;
import java.util.ArrayList;


public class ViewCommand {
    public static void Do(String[] args, CommandSender sender) throws ParseException {
        if (args.length > 1) {
            switch (args[1].toLowerCase()) {
                case "commits":
                    ViewCommits(sender);
                    break;
                case "commit":
                    if (args.length > 2) {
                        ViewCommit(sender, args[2]);
                    } else {
                        sender.sendMessage("Usage: /mcgit view commits <commitId>");
                    }
                    break;
                default:
                    sender.sendMessage("Usage: /mcgit view <commit|commits>");
            }
        }
    }

    public static void ViewCommit(CommandSender sender, String commitId) throws ParseException {
        File commitFile = new File(Constants.CommitsDirectory + "\\" + commitId + ".yml");
        if (!commitFile.exists()) {
            sender.sendMessage(ChatColor.AQUA + "Commit Not Found");
            return;
        }

        Commit commit = new Commit(null, null, null).getFromBukkitYmlFile(commitFile);

        sender.sendMessage("");
        sender.sendMessage("----------[MCGit : Commit Details]----------");
        sender.sendMessage(ChatColor.YELLOW + "Commit Id: " + ChatColor.GREEN + commit.getCommitId());
        sender.sendMessage(ChatColor.YELLOW + "Description: " + ChatColor.GREEN + commit.getDescription());
        sender.sendMessage(ChatColor.YELLOW + "Commit Time: " + ChatColor.GREEN + commit.getCreateTime());
        sender.sendMessage(ChatColor.YELLOW + "World: " + ChatColor.GREEN + commit.getWorld().getName());
        sender.sendMessage(ChatColor.YELLOW + "Commit Player: " + ChatColor.GREEN + commit.getPlayer().getName() + " (" + commit.getPlayer().getUniqueId() + ")");

        TextComponent actionsMessage = new TextComponent();
        actionsMessage.setText(ChatColor.RED + "[Rollback]");
        actionsMessage.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/mcgit rollback " + commit.getCommitId().toString()));
        sender.spigot().sendMessage(actionsMessage);

        sender.sendMessage("");
    }

    private static void ViewCommits(CommandSender sender) throws ParseException {
        ArrayList<Commit> existingCommits = GitManager.commitsList();
        existingCommits = reverseArrayList(existingCommits);

        sender.sendMessage("");
        sender.sendMessage("---------[MCGit : Existing Commits]---------");
        for (Commit commit : existingCommits) {
            TextComponent detailsMessage = new TextComponent();
            detailsMessage.setText(ChatColor.GREEN + commit.getDescription() + " " + ChatColor.YELLOW + Constants.DateFormat.format(commit.getCreateTime()));
            detailsMessage.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/mcgit view commit " + commit.getCommitId().toString()));
            sender.spigot().sendMessage(detailsMessage);
        }
        if (existingCommits.size() == 0) {
            sender.sendMessage(ChatColor.AQUA + "Nothing to show");
        }
        sender.sendMessage("");
    }

    private static ArrayList<Commit> reverseArrayList(ArrayList<Commit> alist) {
        ArrayList<Commit> revArrayList = new ArrayList<>();
        for (int i = alist.size() - 1; i >= 0; i--) {
            revArrayList.add(alist.get(i));
        }

        return revArrayList;
    }
}

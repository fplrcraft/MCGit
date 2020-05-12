package me.ranzeplay.mcgit.commands;

import me.ranzeplay.mcgit.Constants;
import me.ranzeplay.mcgit.Main;
import me.ranzeplay.mcgit.managers.BackupsManager;
import me.ranzeplay.mcgit.managers.GitManager;
import me.ranzeplay.mcgit.managers.MessageTemplateManager;
import me.ranzeplay.mcgit.models.Commit;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.File;
import java.text.ParseException;

public class RollbackCommand {
    public static void Do(String[] args, CommandSender sender) throws Exception {
        if (args.length > 1) {
            if (args[1].equalsIgnoreCase("abort")) {
                Abort(sender);
                return;
            }
            if (args.length > 2 && args[2].equalsIgnoreCase("confirm")) {
                Process(sender, args[1]);
                return;
            }
            RequestConfirm(sender, args[1]);
        } else {
            HelpCommand.Rollback(sender);
        }
    }

    private static void RequestConfirm(CommandSender sender, String commitId) throws ParseException {
        sender.sendMessage("");
        sender.sendMessage(MessageTemplateManager.title(10, "Request Confirm"));

        sender.sendMessage(ChatColor.AQUA + "You are requesting to rollback the server, you need to confirm your action!");
        ViewCommand.ViewCommit(sender, commitId);
        sender.sendMessage(ChatColor.AQUA + "Use \"/mcgit rollback " + commitId + " confirm\" to confirm rollback operation...");

        sender.sendMessage(MessageTemplateManager.ending(15));
        sender.sendMessage("");
    }

    private static void Process(CommandSender sender, String commitId) throws Exception {
        File commitFile = new File(Constants.CommitsDirectory + "/" + commitId + ".yml");
        if (!commitFile.exists()) {
            sender.sendMessage(ChatColor.AQUA + "Commit Not Found");
            return;
        }

        Commit commit = GitManager.getCommit(commitId);
        if (commit == null) {
            sender.sendMessage(ChatColor.RED + "Cannot read Commit file normally, it might be damaged");
            sender.sendMessage(ChatColor.RED + "Operation failed...");
            return;
        }

        for (Player targetPlayer : Main.Instance.getServer().getOnlinePlayers()) {
            targetPlayer.sendMessage("");
            // targetPlayer.sendMessage("-----[MCGit : Rollback Operation Summary]-----");
            targetPlayer.sendMessage(MessageTemplateManager.title(7, "Rollback Operation Summary"));

            TextComponent text = new TextComponent();
            text.setText(ChatColor.YELLOW + "Target CommitId: " + ChatColor.GREEN + commitId);
            text.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/mcgit view commit " + commitId));
            targetPlayer.spigot().sendMessage(text);

            targetPlayer.sendMessage(ChatColor.YELLOW + "Triggered by: " + ChatColor.GREEN + sender.getName());
            targetPlayer.sendMessage(ChatColor.AQUA + "Rollback will be start on next server startup automatically");

            TextComponent actionsMessage = new TextComponent();
            actionsMessage.setText(ChatColor.YELLOW + "Use " + ChatColor.GREEN + "/mcgit rollback abort" + ChatColor.YELLOW + " to abort rollback");
            actionsMessage.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/mcgit rollback abort"));
            targetPlayer.spigot().sendMessage(actionsMessage);

            targetPlayer.sendMessage(MessageTemplateManager.ending(15));
            targetPlayer.sendMessage("");
        }

        BackupsManager.Schedule(commitId);
    }

    private static void Abort(CommandSender sender) {
        if (BackupsManager.Abort()) {
            for (Player targetPlayer : Main.Instance.getServer().getOnlinePlayers()) {
                targetPlayer.sendMessage(ChatColor.YELLOW + "Rollback has been aborted by " + ChatColor.GREEN + sender.getName());
            }
        }else{
            sender.sendMessage(ChatColor.RED + "Nothing was scheduled");
        }
    }

}

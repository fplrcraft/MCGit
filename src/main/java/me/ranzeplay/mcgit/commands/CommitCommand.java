package me.ranzeplay.mcgit.commands;

import me.ranzeplay.mcgit.Main;
import me.ranzeplay.mcgit.managers.GitManager;
import me.ranzeplay.mcgit.managers.zip.ZipManager;
import me.ranzeplay.mcgit.models.Commit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class CommitCommand {
    public static void Do(String[] args, CommandSender sender) throws Exception {
        if (sender instanceof Player) {
            Player execPlayer = (Player) sender;

            World targetWorld = null;
            switch (args.length) {
                case 2:
                    targetWorld = execPlayer.getWorld();
                    break;
                case 3:
                    Main.Instance.getServer().getWorld(args[2]);
                    break;
                default:
                    sender.sendMessage("Usage: /mcgit commit <commitName> [worldName]");
                    return;
            }

            long operationStartTime = System.nanoTime();

            Commit commit = GitManager.makeCommit(args[1], execPlayer, targetWorld);
            ZipManager.zipWorld(targetWorld.getName(), false, false, commit.getCommitId().toString().replace("-", ""));

            long operationCompleteTime = System.nanoTime();

            sender.sendMessage(ChatColor.GREEN + "Commit " + ChatColor.YELLOW + commit.getCommitId().toString() + ChatColor.GREEN + " created successfully!");
            sender.sendMessage(ChatColor.YELLOW + "Size: " + String.format("%.4f", GitManager.GetCommitTotalSize(commit.getCommitId().toString()) / 1024 / 1024) + "MB");
            sender.sendMessage(ChatColor.YELLOW + "Time elapsed: " + String.format("%.4f", (double)(operationCompleteTime - operationStartTime) / 1000 / 1000 / 1000) + " seconds");

        } else {
            if (args.length > 2) {
                ZipManager.zipWorld(args[2], false, false, UUID.randomUUID().toString().replace("-", ""));
            } else {
                sender.sendMessage("Usage: /mcgit commit <commitName> <worldName>");
            }
        }
    }
}

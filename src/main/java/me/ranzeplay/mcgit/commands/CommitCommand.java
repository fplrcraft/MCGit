package me.ranzeplay.mcgit.commands;

import me.ranzeplay.mcgit.Main;
import me.ranzeplay.mcgit.managers.GitManager;
import me.ranzeplay.mcgit.managers.zip.ZipManager;
import me.ranzeplay.mcgit.models.Commit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.concurrent.CompletableFuture;

public class CommitCommand {
    public static void Do(String[] args, CommandSender sender) throws Exception {
        Player execPlayer = (Player) sender;

        World targetWorld;
        switch (args.length) {
            case 2:
                targetWorld = execPlayer.getWorld();
                break;
            case 3:
                targetWorld = Main.Instance.getServer().getWorld(args[2].replaceAll("_nether", "").replaceAll("_the_end", ""));
                break;
            default:
                HelpCommand.Commit(sender);
                return;
        }

        long operationStartTime = System.nanoTime();

        Commit commit = GitManager.makeCommit(args[1], execPlayer, targetWorld);

        if (targetWorld == null) {
            sender.sendMessage(ChatColor.RED + "World not found, it might be deleted");
            return;
        }

        CompletableFuture<Void> zipProcess = CompletableFuture.runAsync(() -> {
            if (Main.Instance.getConfig().getBoolean("compressNetherWorldByDefault")) {
                try {
                    ZipManager.zipWorld(targetWorld.getName().replaceAll("_nether", ""), commit.getCommitId().toString().replace("-", ""));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (Main.Instance.getConfig().getBoolean("compressTheEndByDefault")) {
                try {
                    ZipManager.zipWorld(targetWorld.getName().replaceAll("_the_end", ""), commit.getCommitId().toString().replace("-", ""));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            try {
                ZipManager.zipWorld(targetWorld.getName(), commit.getCommitId().toString().replace("-", ""));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).whenComplete((t, u) -> {
            long operationCompleteTime = System.nanoTime();

            sender.sendMessage(ChatColor.GREEN + "Commit " + ChatColor.YELLOW + commit.getCommitId().toString() + ChatColor.GREEN + " created successfully!");
            sender.sendMessage(ChatColor.YELLOW + "Size: " + String.format("%.4f", GitManager.GetCommitTotalSize(commit.getCommitId().toString()) / 1024 / 1024) + "MB");
            sender.sendMessage(ChatColor.YELLOW + "Time elapsed: " + String.format("%.4f", (double) (operationCompleteTime - operationStartTime) / 1000 / 1000 / 1000) + " seconds");
        });
    }
}

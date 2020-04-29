package me.ranzeplay.mcgit.commands;

import me.ranzeplay.mcgit.Constants;
import me.ranzeplay.mcgit.Main;
import me.ranzeplay.mcgit.managers.GitManager;
import me.ranzeplay.mcgit.managers.zip.ZipManager;
import me.ranzeplay.mcgit.models.Commit;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.text.ParseException;
import java.util.ArrayList;

public class RollbackCommand {
    public static void Do(String[] args, CommandSender sender) throws Exception {
        if (args.length > 1) {
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
        sender.sendMessage(ChatColor.AQUA + "You are requesting to rollback the server, you need to confirm your action!");
        ViewCommand.ViewCommit(sender, commitId);
        sender.sendMessage(ChatColor.AQUA + "Use \"/mcgit rollback " + commitId + " confirm\" to confirm rollback operation...");
    }

    private static void Process(CommandSender sender, String commitId) throws Exception {
        File commitFile = new File(Constants.CommitsDirectory + "/" + commitId + ".yml");
        if (!commitFile.exists()) {
            sender.sendMessage(ChatColor.AQUA + "Commit Not Found");
            return;
        }

        for (Player targetPlayer : Main.Instance.getServer().getOnlinePlayers()) {
            targetPlayer.sendMessage("");
            targetPlayer.sendMessage("-----[MCGit : Rollback Operation Summary]-----");

            TextComponent text = new TextComponent();
            text.setText(ChatColor.YELLOW + "Rollback point: " + ChatColor.GREEN + commitId);
            text.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/mcgit view commit " + commitId));
            targetPlayer.spigot().sendMessage(text);

            targetPlayer.sendMessage(ChatColor.YELLOW + "Triggered by: " + ChatColor.GREEN + sender.getName());
            targetPlayer.sendMessage("");
        }

        for (int t = 10; t > 0; t--) {
            for (Player targetPlayer : Main.Instance.getServer().getOnlinePlayers()) {
                targetPlayer.sendMessage(ChatColor.RED + "Rollback operation will start in " + t + " second(s)");
            }

            Thread.sleep(1000);
        }

        for (Player player : Main.Instance.getServer().getOnlinePlayers()) {
            player.kickPlayer("Rollback operation in progress");
        }

        Commit commit = GitManager.getCommit(commitId);
        if (commit == null) {
            sender.sendMessage(ChatColor.RED + "Cannot read Commit file normally, it might be damaged");
            return;
        }

        if (Main.Instance.getConfig().getBoolean("compressNetherWorldByDefault")) {
            ZipManager.unzipWorldFromBackup(commit.getWorld().getName().replaceAll("_nether", ""), commit.getCommitId().toString().replace("-", ""));
        }
        if (Main.Instance.getConfig().getBoolean("compressTheEndByDefault")) {
            ZipManager.unzipWorldFromBackup(commit.getWorld().getName().replaceAll("_the_end", ""), commit.getCommitId().toString().replace("-", ""));
        }

        ZipManager.unzipWorldFromBackup(commit.getWorld().getName(), commitId.replace("-", ""));

        new File(Constants.BackupDirectory.getAbsolutePath() + "/" + commitId + "/" + (commitId.replace("-", "") + "-" + commit.getWorld().getName() + ".zip")).delete();

        Thread.sleep(1000);

        restartMinecraftServer();
    }

    private static void restartMinecraftServer() {
        final File currentJar = new File(new File(new File(System.getProperty("user.dir")).getAbsolutePath()) + "/" + Main.Instance.getConfig().getString("serverJarFileName"));
        // System.out.println(currentJar.getAbsolutePath());

        if (!currentJar.getName().endsWith(".jar"))
            return;

        final ArrayList<String> command = new ArrayList<>();
        command.add("java");
        command.addAll(ManagementFactory.getRuntimeMXBean().getInputArguments());
        /* is it a jar file? */
        if (!currentJar.getName().endsWith(".jar"))
            return;

        /* Build command: java -jar application.jar */
        command.add("-jar");
        // command.add("nogui");
        command.add(currentJar.getPath());

        final ProcessBuilder builder = new ProcessBuilder(command);
        try {
            builder.start();
            System.exit(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

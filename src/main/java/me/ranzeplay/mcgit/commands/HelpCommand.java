package me.ranzeplay.mcgit.commands;

import me.ranzeplay.mcgit.managers.MessageTemplateManager;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class HelpCommand {
    public static void Root(CommandSender sender) {
        sender.sendMessage("");
        // sender.sendMessage(ChatColor.AQUA + "---------[MCGit : Command Helper]---------");
        sender.sendMessage(MessageTemplateManager.title(9, "Command Helper"));
        sender.sendMessage(ChatColor.YELLOW + "/mcgit commit <description> [worldName]" + ChatColor.WHITE + " - " + ChatColor.GREEN + "Commit a new backup");
        sender.sendMessage(ChatColor.YELLOW + "/mcgit view <commits|commit>" + ChatColor.WHITE + " - " + ChatColor.GREEN + "View all commits or a specific commit");
        sender.sendMessage(ChatColor.YELLOW + "/mcgit rollback <commitId>" + ChatColor.WHITE + " - " + ChatColor.GREEN + "Request a rollback operation");
        sender.sendMessage(ChatColor.YELLOW + "/mcgit gui" + ChatColor.WHITE + " - " + ChatColor.GREEN + "Open a GUI contains all commits");
        sender.sendMessage(MessageTemplateManager.ending(15));
        sender.sendMessage("");
    }

    public static void Commit(CommandSender sender) {
        sender.sendMessage("");
        // sender.sendMessage(ChatColor.AQUA + "---------[MCGit : Command Helper > Commit]---------");
        sender.sendMessage(MessageTemplateManager.title(13, "Command Helper > Commit"));
        sender.sendMessage(ChatColor.YELLOW + "/mcgit commit <description> [worldName]" + ChatColor.WHITE + " - " + ChatColor.GREEN + "Commit a new backup");
        sender.sendMessage(MessageTemplateManager.ending(17));
        sender.sendMessage("");
    }

    public static void View(CommandSender sender) {
        sender.sendMessage("");
        // sender.sendMessage("---------[MCGit : Command Helper > View]---------");
        sender.sendMessage(MessageTemplateManager.title(13, "Command Helper > View"));
        sender.sendMessage(ChatColor.YELLOW + "/mcgit view commits" + ChatColor.WHITE + " - " + ChatColor.GREEN + "View all commits");
        sender.sendMessage(ChatColor.YELLOW + "/mcgit view commit <commitId>" + ChatColor.WHITE + " - " + ChatColor.GREEN + "View a specific commit");
        sender.sendMessage(MessageTemplateManager.ending(17));
        sender.sendMessage("");
    }

    public static void Rollback(CommandSender sender) {
        sender.sendMessage("");
        // sender.sendMessage("---------[MCGit : Command Helper]---------");
        sender.sendMessage(MessageTemplateManager.title(13, "Command Helper"));
        sender.sendMessage(ChatColor.YELLOW + "/mcgit rollback <commitId>" + ChatColor.WHITE + " - " + ChatColor.GREEN + "Rollback to a specific commit");
        sender.sendMessage(ChatColor.YELLOW + "/mcgit rollback abort" + ChatColor.WHITE + " - " + ChatColor.GREEN + "Abort the rollback operation");
        sender.sendMessage(MessageTemplateManager.ending(17));
        sender.sendMessage("");
    }

    public static void Delete(CommandSender sender) {
        sender.sendMessage("");
        // sender.sendMessage("---------[MCGit : Command Helper]---------");
        sender.sendMessage(MessageTemplateManager.title(13, "Command Helper"));
        sender.sendMessage(ChatColor.YELLOW + "/mcgit delete <commitId>" + ChatColor.WHITE + " - " + ChatColor.GREEN + "Delete a specific commit");
        sender.sendMessage(MessageTemplateManager.ending(17));
        sender.sendMessage("");
    }
}

package me.ranzeplay.mcgit.commands;

import me.ranzeplay.mcgit.gui.CommitsPanel;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.text.ParseException;

public class CommandExec implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (command.isRegistered()) {
            if (commandSender instanceof Player) {
                if (command.getName().equalsIgnoreCase("mcgit") || command.getAliases().contains(s)) {
                    if (args.length > 0) {
                        switch (args[0].toLowerCase()) {
                            case "commit":
                                try {
                                    CommitCommand.Do(args, commandSender);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                break;
                            case "view":
                                try {
                                    ViewCommand.Do(args, commandSender);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                break;
                            case "rollback":
                                try {
                                    RollbackCommand.Do(args, commandSender);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                break;
                            case "gui":
                                ((Player) commandSender).openInventory(new CommitsPanel().getInventory());
                                break;
                        }
                    } else {
                        HelpCommand.Root(commandSender);
                    }

                    return true;
                }
            }

            commandSender.sendMessage(ChatColor.RED + "The command can only be executed by a Player");
        }

        return false;
    }
}

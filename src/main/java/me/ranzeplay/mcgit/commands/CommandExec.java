package me.ranzeplay.mcgit.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CommandExec implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (command.isRegistered()) {
            if (command.getName().equalsIgnoreCase("mcgit") || command.getAliases().contains(s)) {
                if (args.length > 0) {
                    switch (args[0].toLowerCase()) {
                        case "commit":
                            try {
                                Commit.Do(args, commandSender);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            break;
                        case "rollback":
                            break;
                    }
                }

                return true;
            }
        }

        return false;
    }
}

package me.ranzeplay.mcgit.commands;

import me.ranzeplay.mcgit.managers.GitManager;
import me.ranzeplay.mcgit.models.Commit;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class CommandCompleter implements TabCompleter {
    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String label, String[] args) {
        ArrayList<String> availableChoices = new ArrayList<>();

        ArrayList<String> s = new ArrayList<>();
        for (String arg : args) {
            if (!arg.isEmpty()) {
                s.add(arg);
            }
        }

        if (command.isRegistered()) {
            if (commandSender instanceof Player) {
                if (command.getName().equalsIgnoreCase("mcgit") || command.getAliases().contains(label)) {
                    if (s.size() == 0) {
                        availableChoices.add("gui");
                        availableChoices.add("commit");
                        availableChoices.add("view");
                        availableChoices.add("rollback");
                        availableChoices.add("delete");
                    } else if (s.size() == 1) {
                        if (s.get(0).equalsIgnoreCase("view")) {
                            availableChoices.add("commits");
                            availableChoices.add("commit");
                        } else if (s.get(0).equalsIgnoreCase("rollback") || s.get(0).equalsIgnoreCase("delete")) {
                            try {
                                ArrayList<Commit> commits = GitManager.commitsList();
                                for (Commit commit : commits) {
                                    availableChoices.add(commit.getCommitId().toString());
                                }
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
                    } else if (s.size() == 2) {
                        if (s.get(0).equalsIgnoreCase("view") && s.get(1).equalsIgnoreCase("commit")) {
                            try {
                                ArrayList<Commit> commits = GitManager.commitsList();
                                for (Commit commit : commits) {
                                    availableChoices.add(commit.getCommitId().toString());
                                }
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
                    } else if (s.size() == 3) {
                        if (s.get(0).equalsIgnoreCase("commit")) {
                            for (World world : Bukkit.getWorlds()) {
                                if (!(world.getName().endsWith("_nether") || world.getName().endsWith("_the_end"))) {
                                    availableChoices.add(world.getName());
                                }
                            }
                        }
                    }
                }
            }
        }

        return availableChoices;
    }
}

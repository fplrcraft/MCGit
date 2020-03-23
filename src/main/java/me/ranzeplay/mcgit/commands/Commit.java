package me.ranzeplay.mcgit.commands;

import me.ranzeplay.mcgit.Zip.ZipManager;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class Commit {
    public static void Do(String[] args, CommandSender sender) throws Exception {
        if (sender instanceof Player) {
            Player execPlayer = (Player) sender;
            switch (args.length) {
                case 2:
                    ZipManager.zipWorld(execPlayer.getWorld().toString(), false, false, UUID.randomUUID().toString().replace("-", ""));
                    break;
                case 3:
                    ZipManager.zipWorld(args[2], false, false, UUID.randomUUID().toString().replace("-", ""));
                    break;
                default:
                    sender.sendMessage("Usage: /mcgit commit <commitName> [worldName]");
                    break;
            }
        } else {
            if (args.length > 2) {
                ZipManager.zipWorld(args[2], false, false, UUID.randomUUID().toString().replace("-", ""));
            } else {
                sender.sendMessage("Usage: /mcgit commit <commitName> <worldName>");
            }
        }


    }
}

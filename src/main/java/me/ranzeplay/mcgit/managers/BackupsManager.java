package me.ranzeplay.mcgit.managers;

import me.ranzeplay.mcgit.Constants;
import me.ranzeplay.mcgit.Main;
import me.ranzeplay.mcgit.managers.zip.ZipManager;
import me.ranzeplay.mcgit.models.Commit;
import org.bukkit.command.CommandSender;

import java.io.IOException;

public class BackupsManager {
    public static void Execute(CommandSender sender, String commitId) throws IOException {
        Commit commit = new Commit(null, null, null);
        if (Main.Instance.getConfig().getBoolean("compressNetherWorldByDefault")) {
            ZipManager.replaceWorldFromBackup(commit.getWorld().getName().replaceAll("_nether", "") + "_nether", commit.getCommitId().toString().replace("-", ""));
        }
        if (Main.Instance.getConfig().getBoolean("compressTheEndByDefault")) {
            ZipManager.replaceWorldFromBackup(commit.getWorld().getName().replaceAll("_nether", "").replaceAll("_the_end", "") + "_the_end",
                    commit.getCommitId().toString().replace("-", ""));
        }

        ZipManager.replaceWorldFromBackup(commit.getWorld().getName().replaceAll("_the_end", ""), commitId.replace("-", ""));
    }

    public static void Schedule(String commitId) {
        Main.Instance.getConfig().set("nextRollback", commitId);
        Constants.IsScheduled = true;
        Main.Instance.saveConfig();
    }

    /**
     * To abort rollback operation if it was scheduled
     *
     * @return Has been cancelled or not scheduled
     */
    public static boolean Abort() {
        if (Constants.IsScheduled) {
            Constants.IsScheduled = false;
            return true;
        }

        return false;
    }
}

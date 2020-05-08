package me.ranzeplay.mcgit.managers;

import me.ranzeplay.mcgit.Constants;
import me.ranzeplay.mcgit.Main;
import me.ranzeplay.mcgit.managers.zip.ZipManager;
import me.ranzeplay.mcgit.models.Commit;

import java.io.File;

public class BackupsManager {
    /**
     * Will not check if the commitId is valid
     *
     * @param commitId Must be valid
     * @throws Exception When unzip operation fail
     */
    public static void Execute(String commitId) throws Exception {
        Commit commit = new Commit(null, null, null).getFromBukkitYmlFile(new File(Constants.ConfigDirectory + "/Commits/" + commitId +".yml"));
        if (Main.Instance.getConfig().getBoolean("compressNetherWorldByDefault")) {
            ZipManager.replaceWorldFromBackup(commit.getWorldName().replaceAll("_nether", "").replaceAll("_the_end", "") + "_nether",
                    commit.getCommitId().toString().replace("-", ""));
        }
        if (Main.Instance.getConfig().getBoolean("compressTheEndWorldByDefault")) {
            ZipManager.replaceWorldFromBackup(commit.getWorldName().replaceAll("_nether", "").replaceAll("_the_end", "") + "_the_end",
                    commit.getCommitId().toString().replace("-", ""));
        }

        ZipManager.replaceWorldFromBackup(commit.getWorldName().replaceAll("_nether", "").replaceAll("_the_end", ""), commitId.replace("-", ""));
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
            Main.Instance.getConfig().set("nextRollback", "unset");
            Main.Instance.saveConfig();

            Constants.IsScheduled = false;
            return true;
        }

        return false;
    }
}

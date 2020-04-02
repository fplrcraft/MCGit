package me.ranzeplay.mcgit.managers;

import me.ranzeplay.mcgit.Constants;
import me.ranzeplay.mcgit.models.Commit;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Objects;

public class GitManager {
    public static Commit makeCommit(String description, Player player, World world) throws IOException {
        Commit commit = new Commit(description, player, world);

        File newCommit = new File(Constants.CommitsDirectory + "\\" + commit.getCommitId().toString() + ".yml");
        newCommit.createNewFile();

        FileConfiguration filec = commit.saveToBukkitYmlFile(new YamlConfiguration());
        filec.save(newCommit);

        return commit;
    }

    public static ArrayList<Commit> commitsList() throws ParseException {
        ArrayList<Commit> list = new ArrayList<>();
        File files = Constants.CommitsDirectory;
        if (Objects.requireNonNull(files.listFiles()).length == 0) return list;
        for (File file : Objects.requireNonNull(Constants.CommitsDirectory.listFiles())) {
            list.add(new Commit(null, null, null).getFromBukkitYmlFile(file));
        }

        return list;
    }

    public static Commit getCommit(String commitId) throws ParseException {
        File commitFile = new File(Constants.CommitsDirectory + "\\" + commitId + ".yml");
        if (!commitFile.exists()) {
            return null;
        }

        return new Commit(null, null, null).getFromBukkitYmlFile(commitFile);
    }

    public static double GetCommitTotalSize(String commitId) {
        double totalSize = 0;

        File commitFile = new File(Constants.BackupDirectory + "\\" + commitId.replace("-", ""));
        for (File file : commitFile.listFiles()) {
            totalSize += file.length();
        }

        return totalSize;
    }
}

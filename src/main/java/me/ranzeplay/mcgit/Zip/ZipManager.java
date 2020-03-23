package me.ranzeplay.mcgit.Zip;

import me.ranzeplay.mcgit.Constants;
import me.ranzeplay.mcgit.Main;
import org.bukkit.Bukkit;
import org.bukkit.World;

import java.io.File;
import java.io.FileNotFoundException;

public class ZipManager {
    public static void zipWorld(String worldName, boolean zipNether, boolean zipTheEnd, String backupId) throws Exception {
        File destinationDirectory = new File(Constants.BackupFolder + "\\" + backupId);
        if (!destinationDirectory.exists()) destinationDirectory.mkdirs();

        File serverRootDirectory = (Main.Instance.getDataFolder().getParentFile().getAbsoluteFile()).getParentFile();
        File worldRootDirectory = new File(serverRootDirectory + "\\" + worldName);
        if (worldRootDirectory.exists()) {
            World world = Bukkit.getWorld(worldName);
            if (world != null) {
                String zipFilePath = serverRootDirectory.getAbsolutePath() + "\\" + (backupId + "-" + worldName + ".zip");
                ZipFiles.ZipProg(worldRootDirectory, zipFilePath);

                File sourceZip = new File(zipFilePath);
                if (!sourceZip.exists()) {
                    throw new FileNotFoundException("Zip file not found!");
                }

                sourceZip.renameTo(new File(destinationDirectory, sourceZip.getName()));
            } else {
                throw new Exception("World not found!");
            }
        } else {
            throw new Exception("World folder not found!");
        }
    }
}

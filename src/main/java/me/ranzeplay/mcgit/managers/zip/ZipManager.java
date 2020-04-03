package me.ranzeplay.mcgit.managers.zip;

import me.ranzeplay.mcgit.Constants;
import me.ranzeplay.mcgit.Main;
import org.bukkit.Bukkit;
import org.bukkit.World;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class ZipManager {
    public static void zipWorld(String worldName, boolean zipNether, boolean zipTheEnd, String backupId) throws Exception {
        File destinationDirectory = new File(Constants.BackupDirectory.getAbsolutePath() + "/" + backupId);
        System.out.println("Destination: " + destinationDirectory.getAbsolutePath());
        if (!destinationDirectory.exists()) destinationDirectory.mkdirs();

        File serverRootDirectory = (Main.Instance.getDataFolder().getParentFile().getAbsoluteFile()).getParentFile();
        System.out.println("Server root: " + serverRootDirectory.getAbsolutePath());
        File worldRootDirectory = new File(serverRootDirectory.getAbsolutePath() + "/" + worldName);
        System.out.println("World root: " + worldRootDirectory.getAbsolutePath());
        if (worldRootDirectory.exists()) {
            World world = Bukkit.getWorld(worldName);
            if (world != null) {
                String zipFilePath = serverRootDirectory.getAbsolutePath() + "/" + (backupId + "-" + worldName + ".zip");
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

    public static void unzipWorldFromBackup(String worldName, String backupId) throws IOException {
        File serverRootDirectory = (Main.Instance.getDataFolder().getParentFile().getAbsoluteFile()).getParentFile();
        File worldRootDirectory = new File(serverRootDirectory + "/" + worldName);

        // Delete world directory recursively
        deleteDirectory(worldRootDirectory.getAbsolutePath());

        UnzipFiles.UnzipToDirectory(new File(Constants.BackupDirectory.getAbsolutePath() + "/" + backupId + "/" + (backupId + "-" + worldName + ".zip")), new File(serverRootDirectory.getAbsolutePath() + "/" + worldName));
    }

    private static void deleteDirectory(String path) {
        File currentDir = new File(path);
        for (String s : currentDir.list()) {
            File recursiveFileCurrent = new File(currentDir + "/" + s);
            if (recursiveFileCurrent.isDirectory()) {
                deleteDirectory(recursiveFileCurrent.getAbsolutePath());
            }

            recursiveFileCurrent.delete();
        }

        currentDir.delete();
    }
}

package me.ranzeplay.mcgit.managers.config;

import me.ranzeplay.mcgit.Constants;
import me.ranzeplay.mcgit.Main;

import java.io.File;
import java.io.IOException;

public class ConfigManager {
    public static void CreateConfigurations() throws IOException {
        if (!Constants.ConfigDirectory.exists()) Constants.ConfigDirectory.mkdirs();

        File commitsFile = Constants.CommitsDirectory;
        if(!commitsFile.exists()) {
            Main.Instance.getServer().getLogger().warning("Commits configuration file is not found. Creating...");
            commitsFile.createNewFile();
        }
    }
}

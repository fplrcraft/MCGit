package me.ranzeplay.mcgit;

import java.io.File;
import java.text.SimpleDateFormat;

public class Constants {
    public static final File BackupDirectory = new File(Main.Instance.getDataFolder() + "/Backups");
    public static final File ConfigDirectory = new File(Main.Instance.getDataFolder() + "/Config");

    // Formatting
    public static final SimpleDateFormat DateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");

    // Git File Configurations
    public static final File CommitsDirectory = new File(Constants.ConfigDirectory + "/Commits");

    public static boolean IsBackingUp;
}

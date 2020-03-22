package me.ranzeplay.mcgit;

import me.ranzeplay.mcgit.commands.CommandCompleter;
import me.ranzeplay.mcgit.commands.CommandExec;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.Objects;

public final class Main extends JavaPlugin {

    public static Main Instance = null;

    @Override
    public void onEnable() {
        // Plugin startup logic
        Instance = this;
        this.initialPluginFiles();

        Objects.requireNonNull(Bukkit.getPluginCommand("mcgit")).setExecutor(new CommandExec());
        Objects.requireNonNull(Bukkit.getPluginCommand("mcgit")).setTabCompleter(new CommandCompleter());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    private void initialPluginFiles() {
        saveDefaultConfig();

        File path = getDataFolder();
        File backupFolder = new File(path + "\\Backups");
        if (!backupFolder.exists()) backupFolder.mkdirs();
    }
}

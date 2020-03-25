package me.ranzeplay.mcgit;

import me.ranzeplay.mcgit.commands.CommandCompleter;
import me.ranzeplay.mcgit.commands.CommandExec;
import me.ranzeplay.mcgit.managers.config.ConfigManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.util.Objects;

public final class Main extends JavaPlugin {

    public static Main Instance = null;

    @Override
    public void onEnable() {
        // Plugin startup logic
        Instance = this;
        try {
            this.initialPluginFiles();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Objects.requireNonNull(Bukkit.getPluginCommand("mcgit")).setExecutor(new CommandExec());
        Objects.requireNonNull(Bukkit.getPluginCommand("mcgit")).setTabCompleter(new CommandCompleter());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    private void initialPluginFiles() throws IOException {
        saveDefaultConfig();

        if(!Constants.BackupDirectory.exists()) Constants.BackupDirectory.mkdirs();
        if(!Constants.CommitsDirectory.exists()) Constants.CommitsDirectory.mkdirs();

        ConfigManager.CreateConfigurations();
    }
}

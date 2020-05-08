package me.ranzeplay.mcgit;

import me.ranzeplay.mcgit.commands.CommandCompleter;
import me.ranzeplay.mcgit.commands.CommandExec;
import me.ranzeplay.mcgit.gui.CommitsPanel;
import me.ranzeplay.mcgit.managers.BackupsManager;
import me.ranzeplay.mcgit.managers.config.ConfigManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.util.Objects;
import java.util.logging.Level;

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

        Bukkit.getPluginManager().registerEvents(new CommitsPanel(), this);

        // Do rollback operation if there's already scheduled a rollback
        String rollbackCommitId = getConfig().getString("nextRollback");
        if (!Objects.requireNonNull(rollbackCommitId).equalsIgnoreCase("unset")) {
            getServer().getLogger().log(Level.INFO, "Pending rollback found, executing... (" + rollbackCommitId + ")");
            try {
                BackupsManager.Execute(rollbackCommitId);
                getConfig().set("nextRollback", "unset");
                saveConfig();
            } catch (Exception e) {
                e.printStackTrace();
                getServer().getLogger().log(Level.WARNING, "Rollback operation failed! Please check server log");
            }
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    private void initialPluginFiles() throws IOException {
        saveDefaultConfig();

        if (!Constants.BackupsDirectory.exists()) Constants.BackupsDirectory.mkdirs();
        if (!Constants.CommitsDirectory.exists()) Constants.CommitsDirectory.mkdirs();

        ConfigManager.CreateConfigurations();
    }
}

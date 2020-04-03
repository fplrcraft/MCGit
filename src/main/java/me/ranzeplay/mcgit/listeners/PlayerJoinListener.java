package me.ranzeplay.mcgit.listeners;

import me.ranzeplay.mcgit.Constants;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent joinEvent) {
        if(Constants.IsBackingUp) {
            joinEvent.getPlayer().kickPlayer("Rollback operation in progress");
        }
    }
}

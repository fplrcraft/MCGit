package me.ranzeplay.mcgit.gui;

import me.ranzeplay.mcgit.commands.ViewCommand;
import me.ranzeplay.mcgit.managers.GitManager;
import me.ranzeplay.mcgit.models.Commit;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Objects;

public class CommitsPanel implements InventoryHolder, Listener {
    public Inventory getInventory() {
        ArrayList<Commit> commitsList = null;
        try {
            commitsList = GitManager.commitsList();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Inventory inventory = Bukkit.createInventory(null, 9 * 6, "All Commits");
        int i = 1;
        for (Commit commit : Objects.requireNonNull(commitsList)) {
            if (i >= 54) {
                break;
            }
            i++;

            ItemStack item = new ItemStack(Material.GREEN_WOOL, 1);
            ItemMeta meta = Objects.requireNonNull(item.getItemMeta()).clone();
            meta.setDisplayName(commit.getCommitId().toString());

            ArrayList<String> lore = new ArrayList<>();
            lore.add(ChatColor.GREEN + "Description: " + ChatColor.YELLOW + commit.getDescription());
            lore.add(ChatColor.GREEN + "Create Time: " + ChatColor.YELLOW + commit.getCreateTime());
            lore.add(ChatColor.GREEN + "Operator: " + ChatColor.YELLOW + commit.getPlayer().getName());
            lore.add(ChatColor.GREEN + "World Name: " + ChatColor.YELLOW + commit.getWorld());
            meta.setLore(lore);

            item.setItemMeta(meta);
            inventory.addItem(item);
        }

        return inventory;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) throws ParseException {
        if (Objects.requireNonNull(event.getClickedInventory()).getHolder() != this.getInventory().getHolder()) {
            return;
        }

        Player player = (Player) event.getWhoClicked();
        String commitId = Objects.requireNonNull(Objects.requireNonNull(event.getCurrentItem()).getItemMeta()).getDisplayName();
        if (event.getClick().isLeftClick()) {
            player.closeInventory();
            ViewCommand.ViewCommit(player, commitId);
        } else if(event.getClick().isRightClick()) {
            player.closeInventory();
            player.performCommand("mcgit rollback " + commitId);
        }

        event.setCancelled(true);
    }
}

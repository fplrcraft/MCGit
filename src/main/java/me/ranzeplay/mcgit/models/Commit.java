package me.ranzeplay.mcgit.models;

import me.ranzeplay.mcgit.Constants;
import me.ranzeplay.mcgit.Main;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.text.ParseException;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

public class Commit {
    private UUID commitId;
    private String description;
    private Date createTime;
    private UUID playerUUID;
    private String worldName;

    public Commit(String description, Player player, World world) {
        if (player == null) {
            this.playerUUID = UUID.randomUUID();
        } else {
            this.playerUUID = player.getUniqueId();
        }

        if (world == null) {
            worldName = "";
        } else {
            this.worldName = world.getName();
        }

        this.commitId = UUID.randomUUID();
        this.description = description;
        this.createTime = new Date();
    }

    public UUID getCommitId() {
        return commitId;
    }

    public String getDescription() {
        return description;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public Player getPlayer() {
        return Main.Instance.getServer().getOfflinePlayer(this.playerUUID).getPlayer();
    }

    public World getWorld() {
        return Main.Instance.getServer().getWorld(worldName);
    }


    public YamlConfiguration saveToBukkitYmlFile(YamlConfiguration yamlc) {
        yamlc.set("id", this.getCommitId().toString());
        yamlc.set("description", this.getDescription());
        yamlc.set("time", Constants.DateFormat.format(new Date()));
        yamlc.set("player", this.getPlayer().getUniqueId().toString());
        yamlc.set("world", this.getWorld().getName());

        return yamlc;
    }

    public Commit getFromBukkitYmlFile(File ymlFile) throws ParseException {
        YamlConfiguration filec = YamlConfiguration.loadConfiguration(ymlFile);
        this.commitId = UUID.fromString(Objects.requireNonNull(filec.getString("id")));
        this.description = filec.getString("description");
        this.createTime = Constants.DateFormat.parse(filec.getString("time"));
        this.playerUUID = UUID.fromString(Objects.requireNonNull(filec.getString("player")));
        this.worldName = filec.getString("world");

        return this;
    }
}

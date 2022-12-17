package de.jonas.githubworldsync;

import de.jonas.githubworldsync.commands.Synchronise;
import de.jonas.githubworldsync.utils.ConfigHandler;
import de.jonas.githubworldsync.utils.WorldSync;
import org.bukkit.plugin.java.JavaPlugin;

public final class GitHubWorldSync extends JavaPlugin {

    private static GitHubWorldSync instance;
    private static ConfigHandler configHandler;
    private static WorldSync handler;

    @Override
    public void onEnable() {
        instance = this;
        handler = new WorldSync();

        configHandler = new ConfigHandler();
        configHandler.setup();
        configHandler.load();

        getCommand("sync").setExecutor(new Synchronise());

        this.getLogger().info("World Synchronisation has been §aenabled§f!");
    }

    @Override
    public void onDisable() {
        this.getLogger().info("World Synchronisation has been §cdisabled§f!");
    }

    public static GitHubWorldSync getInstance() {
        return instance;
    }

    public static WorldSync getSyncHandler() {
        return handler;
    }
}

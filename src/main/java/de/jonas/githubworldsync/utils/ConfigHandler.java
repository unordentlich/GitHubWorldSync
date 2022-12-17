package de.jonas.githubworldsync.utils;

import de.jonas.githubworldsync.GitHubWorldSync;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class ConfigHandler {
    static File file = new File(GitHubWorldSync.getInstance().getDataFolder().getPath(), "config.yml");
    static YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

    public static void saveConfig() {
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void load() {
        try {
            config.load(file);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    public void updateCFG() {
        config = YamlConfiguration.loadConfiguration(file);
    }

    public void createIfNotExists() {
        GitHubWorldSync instance = GitHubWorldSync.getInstance();
        File file = new File(instance.getDataFolder().getPath(), "config.yml");
        if (!file.exists()) {
            file = new File(instance.getDataFolder().getPath(), "config.yml");
        }
        config = YamlConfiguration.loadConfiguration(file);
        try {
            setup();
            config.save(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        load();
    }

    public void setup() {
        load();

        config.addDefault("world", "BuildWorld");
        config.addDefault("github.username", "unordentlich");
        config.addDefault("github.auth", "ghp_PasteYourTokenHereToAuthenticateWithGitHubServices");
        config.addDefault("github.repo", "unordentlich/MyWorld");

        config.options().copyDefaults(true);
        saveConfig();
    }

    public YamlConfiguration config() {
        updateCFG();
        return config;
    }
}
package de.jonas.githubworldsync.utils;

import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;


public class WorldSync {
    private static long lastSynchronisation = 0;

    public static void lastSynchronisation() {
        WorldSync.lastSynchronisation = System.currentTimeMillis();
    }

    public static boolean isSynchronisationReady() {
        return System.currentTimeMillis() - lastSynchronisation >= 1800 * 1000;
    }

    public void uploadWorldFolder(Player p) throws IOException, GitAPIException {
        String worldFolderPath = Bukkit.getWorld(ConfigHandler.config.getString("world")).getWorldFolder().getPath();
        String repoUrl = "https://github.com/" + ConfigHandler.config.getString("github.repo");

        File localRepoDir = Files.createTempDirectory("repo").toFile();
        Git.cloneRepository()
                .setURI(repoUrl)
                .setDirectory(localRepoDir)
                .setCredentialsProvider(new UsernamePasswordCredentialsProvider(ConfigHandler.config.getString("github.username"), ConfigHandler.config.getString("github.auth")))
                .call();

        File worldFolder = new File(worldFolderPath);
        FileUtils.copyDirectory(worldFolder, localRepoDir);

        Git git = Git.open(localRepoDir);
        git.add().addFilepattern(".").call();
        git.commit().setMessage("🌍 Update · Requested by " + p.getName() + " (" + p.getUniqueId() + ")").call();

        git.push()
                .setCredentialsProvider(new UsernamePasswordCredentialsProvider(ConfigHandler.config.getString("github.username"), ConfigHandler.config.getString("github.auth")))
                .call();
    }
}

package de.jonas.githubworldsync.commands;

import de.jonas.githubworldsync.GitHubWorldSync;
import de.jonas.githubworldsync.utils.WorldSync;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class Synchronise implements CommandExecutor {
    /**
     * Executes the given command, returning its success.
     * <br>
     * If false is returned, then the "usage" plugin.yml entry for this command
     * (if defined) will be sent to the player.
     *
     * @param sender  Source of the command
     * @param command Command which was executed
     * @param label   Alias of the command which was used
     * @param args    Passed command arguments
     * @return true if a valid command, otherwise false
     */
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!sender.isOp() && !sender.hasPermission("worldSync.push"))
            return false;
        if (!WorldSync.isSynchronisationReady()) {
            sender.sendMessage("§6World§eSync §8· §7There were recently pushes to the repository. Please wait before trying again.");
            return false;
        }
        WorldSync.lastSynchronisation();

        Player p = (Player) sender;
        p.sendMessage("§6World§eSync §8· §7Starting synchronisation...");
        try {
            GitHubWorldSync.getSyncHandler().uploadWorldFolder(p);
            p.sendMessage("§6World§eSync §8· §7Synchronisation §acompleted§7!");
        } catch (IOException e) {
            p.sendMessage("§6World§eSync §8· §7Synchronisation §caborted§7!");
            throw new RuntimeException(e);
        } catch (GitAPIException e) {
            throw new RuntimeException(e);
        }
        return false;
    }
}

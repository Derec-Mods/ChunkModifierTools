package io.github.derec4.chunkModifierTools.commands;

import io.github.derec4.chunkModifierTools.SubCommand;
import org.bukkit.Chunk;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CoordinatesCommand implements SubCommand {

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Only players can use this command.");
            return true;
        }

        Chunk chunk = player.getLocation().getChunk();
        player.sendMessage("Chunk coordinates: X=" + chunk.getX() + ", Z=" + chunk.getZ());
        player.sendMessage("Chunk world" + chunk.getWorld().getName());
        return true;
    }
}

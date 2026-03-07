package io.github.derec4.chunkModifierTools;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class ChunkCommandManager implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 0) {
            sender.sendMessage("Usage: /chunk <forceload|inhabitedtime|coordinates>");
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "forceload": {
                break;
            }
            case "inhabitedtime": {
                break;
            }
            case "coordinates": {
                break;
            }
            default: {
                sender.sendMessage("Unknown subcommand. Usage: /chunk <forceload|inhabitedtime|coordinates>");
                break;
            }
        }

        return true;
    }
}


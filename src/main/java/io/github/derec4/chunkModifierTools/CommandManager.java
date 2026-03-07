package io.github.derec4.chunkModifierTools;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CommandManager implements TabExecutor {

    private static final List<String> SUBCOMMANDS = Arrays.asList("forceload", "inhabitedtime", "coordinates");

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

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 1) {
            return SUBCOMMANDS.stream()
                    .filter(sub -> sub.startsWith(args[0].toLowerCase()))
                    .collect(Collectors.toList());
        }
        return List.of();
    }
}


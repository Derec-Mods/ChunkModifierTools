package io.github.derec4.chunkModifierTools;

import io.github.derec4.chunkModifierTools.commands.CoordinatesCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CommandManager implements TabExecutor {

    private static final List<String> SUB_COMMANDS = List.of(
            "DataVersion",
            "InhabitedTime",
            "LastUpdate",
            "Status",
            "forceload",
            "xPos",
            "yPos",
            "zPos"
    );

    private final Map<String, SubCommand> subCommands = new HashMap<>();

    public CommandManager() {
        CoordinatesCommand coordinatesCommand = new CoordinatesCommand();
        subCommands.put("xPos", coordinatesCommand);
        subCommands.put("zPos", coordinatesCommand);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 0) {
            sender.sendMessage("Usage: /chunk <sub command>");
            return true;
        }

        switch (args[0]) {
            case "Status": {
                break;
            }
            case "DataVersion": {
                break;
            }
            case "LastUpdate": {
                break;
            }
            case "yPos": {
                break;
            }
            case "InhabitedTime": {
                break;
            }
            case "xPos": {
                return subCommands.get("xPos").execute(sender, args);
            }
            case "zPos": {
                return subCommands.get("zPos").execute(sender, args);
            }
            case "forceload": {
                break;
            }
            default: {
                sender.sendMessage("Unknown subcommand. Usage: /chunk <Status|DataVersion|LastUpdate|yPos|InhabitedTime|xPos|zPos|forceload>");
                break;
            }
        }

        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 1) {
            /**
             * this uses a lambda
             * stream converts a collection to a stream so u can chain operations
             * filter function, for each KEY, keep it only if it starts with what the player has typed so far
             * collect() converts stream back to List
             */
            return SUB_COMMANDS.stream()
                    .filter(sub -> sub.toLowerCase().startsWith(args[0].toLowerCase()))
                    .collect(Collectors.toList());
        }
        return List.of();
    }
}


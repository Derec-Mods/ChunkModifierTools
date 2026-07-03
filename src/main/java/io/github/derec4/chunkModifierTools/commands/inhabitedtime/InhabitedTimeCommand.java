package io.github.derec4.chunkModifierTools.commands.InhabitedTime;

import io.github.derec4.chunkModifierTools.SubCommand;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Stream;

public class InhabitedTimeCommand implements SubCommand {

    private static final List<String> ACTIONS = List.of("get", "set", "add", "subtract");

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Only players can use this command.");
            return true;
        }

        if (args.length < 2) {
            sendUsage(sender);
            return true;
        }

        Chunk center = player.getLocation().getChunk();
        String action = args[1].toLowerCase(Locale.ROOT);

        switch (action) {
            case "get" -> {
                sender.sendMessage("InhabitedTime: " + center.getInhabitedTime() + " ticks");
                return true;
            }
            case "set" -> {
                if (args.length < 3) {
                    sender.sendMessage("Usage: /chunk InhabitedTime set <ticks>");
                    return true;
                }
                long value = parseTicks(sender, args[2]);
                if (value < 0) {
                    return true;
                }
                center.setInhabitedTime(value);
                sender.sendMessage("Set InhabitedTime to " + value + " ticks in chunk " + center.getX() + ", " + center.getZ());
                return true;
            }
            case "add", "subtract" -> {
                if (args.length < 3) {
                    sender.sendMessage("Usage: /chunk InhabitedTime " + action + " <ticks> [radius] [profile] [seed]");
                    return true;
                }
                long amount = parseTicks(sender, args[2]);
                if (amount < 0) {
                    return true;
                }
                boolean subtract = action.equals("subtract");

                if (args.length < 4) {
                    applySingleChunk(sender, center, subtract, amount);
                    return true;
                }

                handleArea(sender, player.getWorld(), center, subtract, amount, args);
                return true;
            }
            default -> sendUsage(sender);
        }

        return true;
    }

    private void applySingleChunk(CommandSender sender, Chunk chunk, boolean subtract, long amount) {
        long delta = subtract ? -amount : amount;
        long updated = Math.max(0L, chunk.getInhabitedTime() + delta);
        chunk.setInhabitedTime(updated);
        sender.sendMessage("InhabitedTime is now " + updated + " ticks in chunk " + chunk.getX() + ", " + chunk.getZ());
    }

    private void handleArea(CommandSender sender, World world, Chunk center, boolean subtract, long centerAmount, String[] args) {
        int radius;
        try {
            radius = Integer.parseInt(args[3]);
        } catch (NumberFormatException ex) {
            sender.sendMessage("Radius must be a whole number of chunks.");
            return;
        }
        if (radius < 0) {
            sender.sendMessage("Radius must be 0 or greater.");
            return;
        }

        InhabitedTimeProfile profile = InhabitedTimeProfile.BELL;
        if (args.length >= 5) {
            try {
                profile = InhabitedTimeProfile.fromString(args[4]);
            } catch (IllegalArgumentException ex) {
                sender.sendMessage("Unknown profile. Options: flat, bell, linear, inverse, noise");
                return;
            }
        }

        long seed = defaultSeed(world, center);
        if (args.length >= 6) {
            try {
                seed = Long.parseLong(args[5]);
            } catch (NumberFormatException ex) {
                sender.sendMessage("Seed must be a whole number.");
                return;
            }
        }

        int centerX = center.getX();
        int centerZ = center.getZ();
        int modified = 0;
        long centerDelta = subtract ? -centerAmount : centerAmount;

        for (int dx = -radius; dx <= radius; dx++) {
            for (int dz = -radius; dz <= radius; dz++) {
                int chunkX = centerX + dx;
                int chunkZ = centerZ + dz;
                double weight = profile.weight(dx, dz, radius, seed, chunkX, chunkZ);
                if (weight <= 0.0) {
                    continue;
                }

                long delta = Math.round(centerDelta * weight);
                if (delta == 0L) {
                    continue;
                }

                Chunk chunk = world.getChunkAt(chunkX, chunkZ);
                chunk.setInhabitedTime(Math.max(0L, chunk.getInhabitedTime() + delta));
                modified++;
            }
        }

        int span = radius * 2 + 1;
        sender.sendMessage("Modified InhabitedTime in " + modified + " chunk(s) using profile "
                + profile.name().toLowerCase(Locale.ROOT) + " over a " + span + "x" + span
                + " area centered on " + centerX + ", " + centerZ + ".");
    }

    private long defaultSeed(World world, Chunk center) {
        return world.getSeed() ^ (center.getX() * 341873128712L) ^ (center.getZ() * 1323617872L);
    }

    private long parseTicks(CommandSender sender, String raw) {
        try {
            long value = Long.parseLong(raw);
            if (value < 0) {
                sender.sendMessage("Ticks must be zero or greater.");
                return -1L;
            }
            return value;
        } catch (NumberFormatException ex) {
            sender.sendMessage("Ticks must be a whole number.");
            return -1L;
        }
    }

    private void sendUsage(CommandSender sender) {
        sender.sendMessage("Usage: /chunk InhabitedTime <get|set|add|subtract>");
        sender.sendMessage("Add/subtract: /chunk InhabitedTime <add|subtract> <ticks> [radius] [profile] [seed]");
        sender.sendMessage("Profiles: flat, bell, linear, inverse, noise");
    }

    public static List<String> tabComplete(String[] args) {
        if (args.length == 2) {
            return filterPrefix(ACTIONS, args[1]);
        }
        if (args.length == 5 && (args[1].equalsIgnoreCase("add") || args[1].equalsIgnoreCase("subtract"))) {
            return filterPrefix(
                    Stream.of(InhabitedTimeProfile.values()).map(p -> p.name().toLowerCase(Locale.ROOT)).toList(),
                    args[4]
            );
        }
        return List.of();
    }

    private static List<String> filterPrefix(List<String> options, String prefix) {
        String lowerPrefix = prefix.toLowerCase(Locale.ROOT);
        List<String> matches = new ArrayList<>();
        for (String option : options) {
            if (option.startsWith(lowerPrefix)) {
                matches.add(option);
            }
        }
        return matches;
    }
}

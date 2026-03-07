package io.github.derec4.chunkModifierTools;

import org.bukkit.plugin.java.JavaPlugin;

public final class ChunkModifierTools extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        CommandManager chunkCommandManager = new CommandManager();
        getCommand("chunk").setExecutor(chunkCommandManager);
        getCommand("chunk").setTabCompleter(chunkCommandManager);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}

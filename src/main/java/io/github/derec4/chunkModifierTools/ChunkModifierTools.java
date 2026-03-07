package io.github.derec4.chunkModifierTools;

import org.bukkit.plugin.java.JavaPlugin;

public final class ChunkModifierTools extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        getCommand("chunk").setExecutor(new ChunkCommandManager());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}

package fr.zankia.svplayer;

import org.bukkit.command.CommandExecutor;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public class SVP extends JavaPlugin {

    @Override
    public void onEnable() {
        saveDefaultConfig();
        getServer().getPluginManager().registerEvents(new SVPListener(this), this);

        CommandExecutor executor = new SVPCommand(this);
        Objects.requireNonNull(getServer().getPluginCommand("infos")).setExecutor(executor);
        Objects.requireNonNull(getServer().getPluginCommand("svplayer")).setExecutor(executor);
        getLogger().info("Enabled");
    }

    @Override
    public void onDisable() {
        getLogger().info("Disabled");
    }


}

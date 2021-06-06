package fr.zankia.svplayer;

import org.bukkit.command.CommandExecutor;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.Objects;

public class SVP extends JavaPlugin {
    private ChatReplacer chatReplacer;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        getServer().getPluginManager().registerEvents(new SVPListener(this), this);

        initChatReplacer();

        CommandExecutor executor = new SVPCommand(this);
        Objects.requireNonNull(getServer().getPluginCommand("bvn")).setExecutor(executor);
        Objects.requireNonNull(getServer().getPluginCommand("svplayer")).setExecutor(executor);
        getLogger().info("Enabled");
    }

    @Override
    public void onDisable() {
        getLogger().info("Disabled");
    }


    public ChatReplacer getChatReplacer() {
        return chatReplacer;
    }

    public void initChatReplacer() {
        String[] replaceList = Objects.requireNonNull(getConfig().getString("welcomereplace")).split(", ");
        List<String> possibilities = getConfig().getStringList("welcome");
        chatReplacer = new ChatReplacer(replaceList, possibilities);
    }
}

package fr.zankia.svplayer;


import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;

public class SVPListener implements Listener {
    private final SVP plugin;

    public SVPListener(SVP plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onJoin(PlayerJoinEvent e) {
        if (e.getPlayer().hasPlayedBefore()) {
            return;
        }

        final FileConfiguration configs = plugin.getConfig();
        plugin.getChatReplacer().setNewPlayer(e.getPlayer().getName());

        plugin.getServer().getPluginManager().registerEvents(plugin.getChatReplacer(), plugin);

        Bukkit.getScheduler().runTaskLater(
                plugin,
                () -> AsyncPlayerChatEvent.getHandlerList().unregister(plugin.getChatReplacer()),
                configs.getLong("welcomecooldown") * 20L
        );
    }
}

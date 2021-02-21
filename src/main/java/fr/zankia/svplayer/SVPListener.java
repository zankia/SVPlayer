package fr.zankia.svplayer;


import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.List;

public class SVPListener implements Listener {
    private final SVP plugin;
    Listener chatListener;

    public SVPListener(SVP plugin) {
        this.plugin = plugin;
    }


    @EventHandler(priority = EventPriority.LOW)
    public void onJoin(PlayerJoinEvent e) {
        if (!e.getPlayer().hasPlayedBefore()) {
            final FileConfiguration configs = plugin.getConfig();
            final String newPlayer = e.getPlayer().getName();

            if (chatListener != null)
                AsyncPlayerChatEvent.getHandlerList().unregister(chatListener);

            chatListener = new Listener() {

                @EventHandler
                public void onChat(AsyncPlayerChatEvent e) {
                    String[] replaceList = configs.getString("welcomereplace").split(", ");
                    for (String rpl : replaceList)
                        if (e.getMessage().equalsIgnoreCase(rpl)) {
                            List<String> possibilities = configs.getStringList("welcome");
                            e.setMessage(possibilities.get(
                                    (int) (Math.random() * (possibilities.size()))).replace(
                                    "{player}", newPlayer));
                        }
                }
            };

            plugin.getServer().getPluginManager().registerEvents(chatListener, plugin);

            Bukkit.getScheduler().runTaskLater(
                    plugin,
                    () -> AsyncPlayerChatEvent.getHandlerList().unregister(chatListener),
                    configs.getLong("welcomecooldown") * 20L
            );


            final int id = configs.getInt("lastid") + 1;
            configs.set("lastid", id);
            plugin.saveConfig();
            EssPlayerConf userFile = new EssPlayerConf(e.getPlayer());
            userFile.setPosition(id);

            Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, () -> {
                for (Player p : Bukkit.getOnlinePlayers())
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&',
                            configs.getString("newbroadcast").replace(
                            "{num}", id + (id != 1 ? "ème" : "er")).replace(
                            "{player}", newPlayer)
                        )
                    );
            }, 5L);

        }

    }
}

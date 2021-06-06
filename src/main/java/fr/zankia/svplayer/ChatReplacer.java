package fr.zankia.svplayer;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class ChatReplacer implements Listener {
    private final String[] replaceList;
    private final List<String> possibilities;
    private String newPlayer;

    public ChatReplacer(String[] replaceList, List<String> possibilities) {
        this.replaceList = replaceList;
        this.possibilities = possibilities;
    }

    public void setNewPlayer(String newPlayer) {
        this.newPlayer = newPlayer;
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        if (Arrays.stream(replaceList).noneMatch(e.getMessage()::equalsIgnoreCase)) {
            return;
        }
        e.setMessage(getMessage());
    }

    public String getMessage() {
        try {
            Random random = SecureRandom.getInstanceStrong();
            return possibilities.get(random.nextInt(possibilities.size())).replace("{player}", newPlayer);
        } catch (NoSuchAlgorithmException e) {
            return "";
        }
    }
}

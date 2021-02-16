package fr.zankia.svplayer;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SVPCommand implements CommandExecutor {
    private final SVP plugin;
    public static final String NO_PERMISSION = "Erreur : Vous n'avez pas la permission pour cette commande.";

    public SVPCommand(SVP plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String name, String[] args) {
        switch (name.toLowerCase()) {

            case "infos":
                return infosCommand(sender, args);

            case "svp":
            case "svplayer":
                return svplayerCommand(sender, args);

            default:
                return false;
        }
    }

    private boolean infosCommand(CommandSender sender, String[] args) {
        if (!sender.hasPermission("svplayer.infos")) {
            sender.sendMessage(ChatColor.RED + NO_PERMISSION);
            return true;
        }
        if (args.length == 0) {
            return false;
        }
        sendInfos(sender, args[0], sender.hasPermission("svplayer.mod"));
        return true;
    }

    private boolean svplayerCommand(CommandSender sender, String[] args) {
        if (sender.hasPermission("svplayer.admin")) {
            if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
                plugin.reloadConfig();
                sender.sendMessage(ChatColor.RED + "SVPlayer : " + ChatColor.GREEN + "Reload done.");
            } else if (args.length == 1 && args[0].equalsIgnoreCase("update")) {
                int id = 0;
                OfflinePlayer[] players = Bukkit.getOfflinePlayers();
                quicksort(players, 0, players.length - 1);
                for (OfflinePlayer p : players) {
                    EssPlayerConf usrFile = new EssPlayerConf(p);
                    usrFile.setPosition(++id);
                }
                FileConfiguration configs = plugin.getConfig();
                configs.set("lastid", id);
                plugin.saveConfig();
                sender.sendMessage(ChatColor.RED + "SVPlayer : " + ChatColor.GREEN + "Update done.");
            } else
                return false;
        } else
            sender.sendMessage(ChatColor.RED + NO_PERMISSION);
        return true;
    }

    private void quicksort(OfflinePlayer[] players, int min, int max) {
        int i = min;
        int j = max;
        OfflinePlayer mid = players[i + (j - i) / 2];
        while (i <= j) {
            while (players[i].getFirstPlayed() < mid.getFirstPlayed())
                ++i;
            while (players[j].getFirstPlayed() > mid.getFirstPlayed())
                --j;

            if (i <= j) {
                OfflinePlayer temp = players[i];
                players[i] = players[j];
                players[j] = temp;
                ++i;
                --j;
            }
        }

        if (min < j)
            quicksort(players, min, j);
        if (i < max)
            quicksort(players, i, max);

    }

    private void sendInfos(CommandSender sender, String playerName, boolean isAdminPerformed) {
        Player player = Bukkit.getPlayer(playerName);
        OfflinePlayer target = player != null ? Bukkit.getOfflinePlayer(player.getUniqueId()) : Bukkit.getOfflinePlayer(playerName);

        if (!target.hasPlayedBefore()) {
            sender.sendMessage(ChatColor.RED + "Erreur : le joueur n'existe pas");
            return;
        }
        EssPlayerConf targetFile = new EssPlayerConf(target);
        targetFile.load();
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm dd/MM/yy");

        sender.sendMessage(ChatColor.DARK_GREEN + "####" + ChatColor.AQUA + " Infos sur "
                + ChatColor.DARK_AQUA + target.getName() + ChatColor.DARK_GREEN + " ####");


        if (isAdminPerformed) {
            String ip;
            if (player != null)
                ip = player.getAddress().getAddress().getHostAddress();
            else
                ip = targetFile.getString("ipAddress");
            String address = "NA";
            try {
                address = getAddress(ip);
            } catch (IOException e) {
                plugin.getLogger().severe(e.getMessage());
            }
            sender.sendMessage(ChatColor.GOLD + "Adresse : " + ChatColor.YELLOW + address);
            sender.sendMessage(ChatColor.GOLD + "Adresse IP : " + ChatColor.YELLOW + ip);

        }

        sender.sendMessage(ChatColor.GOLD + "Première connexion : "
                + ChatColor.YELLOW + dateFormat.format(new Date(target.getFirstPlayed())));
        sender.sendMessage(ChatColor.GOLD + "Dernière connexion : "
                + ChatColor.YELLOW + dateFormat.format(new Date(target.getLastSeen())));
        sender.sendMessage(ChatColor.AQUA + "Il est le "
                + ChatColor.DARK_AQUA + targetFile.getPosition()
                + ChatColor.AQUA + (targetFile.getPosition() != 1 ? "ème" : "er")
                + " joueur à avoir rejoint le serveur.");
    }

    private String getAddress(String ip) throws IOException {
        URL url = new URL("http://ip-api.com/json/" + ip);
        BufferedReader stream = new BufferedReader(new InputStreamReader(url.openStream(), StandardCharsets.UTF_8));
        String city, country, countryCode, regionName;
        city = country = countryCode = regionName = "";
        boolean found = false;
        String inputLine = stream.readLine();
        if (inputLine.contains("\"zip\":"))
            found = (getInputValue(inputLine, "zip") != "");
        if (inputLine.contains("\"city\":"))
            city = getInputValue(inputLine, "city");
        if (inputLine.contains("\"country\":"))
            country = getInputValue(inputLine, "country");
        if (inputLine.contains("\"countryCode\":"))
            countryCode = getInputValue(inputLine, "countryCode");
        if (inputLine.contains("\"regionName\":"))
            regionName = getInputValue(inputLine, "regionName");
        stream.close();
        return (found ? (city + ", " + regionName + ", " + countryCode) : country);
    }

    private String getInputValue(String inputLine, String search) {
        String result = inputLine.substring(inputLine.indexOf(search));
        result = result.substring(result.indexOf(":\"") + 2);
        return result.substring(0, result.indexOf("\""));
    }

}

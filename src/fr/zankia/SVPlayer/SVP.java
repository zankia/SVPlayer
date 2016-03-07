package fr.zankia.SVPlayer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import me.confuser.banmanager.BmAPI;
import me.confuser.banmanager.data.PlayerBanRecord;
import me.confuser.banmanager.internal.ormlite.dao.CloseableIterator;

public class SVP extends JavaPlugin {
	
	public static final String NO_PERMISSION = "Erreur : Vous n'avez pas la permission pour cette commande.";
	
	
	
	@Override
	public void onEnable() {
		saveDefaultConfig();
		getServer().getPluginManager().registerEvents(new SVPListener(this), this);
		getLogger().info("Enabled");
	}

	@Override
	public void onDisable() {
		getLogger().info("Disabled");
	}



	@Override
	public boolean onCommand(CommandSender sender, Command command, String name, String[] args) {
		switch(name.toLowerCase()) {
		
		  case "infos": {
			if(sender.hasPermission("svplayer.infos")) {
				if(args.length == 2 && args[0].equalsIgnoreCase("admin")) {
					if(sender.hasPermission("svplayer.mod"))
						sendInfos(sender, args[1], true);
					else
						sender.sendMessage(ChatColor.RED + NO_PERMISSION);
				} else if(args.length == 1)
					sendInfos(sender, args[0], false);
				else
					return false;
				return true;
			} else
				sender.sendMessage(ChatColor.RED + NO_PERMISSION);
			return true;
		  }

		  case "svp":
		  case "svplayer": {
			return svplayerCommand(sender, args);
		  }
		}
		return false;
	}

	private boolean svplayerCommand(CommandSender sender, String[] args) {
		if(sender.hasPermission("svplayer.admin")) {
			if(args.length == 1 && args[0].equalsIgnoreCase("reload")) {
				this.reloadConfig();
				sender.sendMessage(ChatColor.RED + "SVPlayer : " + ChatColor.GREEN + "Reload done.");
			} else if (args.length == 1 && args[0].equalsIgnoreCase("update")) {
				int id = 0;
				OfflinePlayer[] players = Bukkit.getOfflinePlayers();
				quicksort(players, 0, players.length -1);
				for(OfflinePlayer p : players) {
					EssPlayerConf usrFile = new EssPlayerConf(p);
					usrFile.setPosition(++id);
				}
				FileConfiguration configs = getConfig();
				configs.set("lastid", id);
				saveConfig();
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
		OfflinePlayer mid = players[i+(j-i)/2];
		while(i <= j) {
			while(players[i].getFirstPlayed() < mid.getFirstPlayed())
				++i;
			while(players[j].getFirstPlayed() > mid.getFirstPlayed())
				--j;
			
			if(i <= j ) {
				OfflinePlayer temp = players[i];
				players[i] = players[j];
				players[j] = temp;
				++i;
				--j;
			}
		}

		if(min < j)
			quicksort(players, min, j);
		if(i < max)
			quicksort(players, i, max);
		
	}

	@SuppressWarnings("deprecation")
	private void sendInfos(CommandSender sender, String playerName, boolean isAdminPerformed) {
		Player player = Bukkit.getPlayer(playerName);
		OfflinePlayer target;
		if(player != null) {
			target = Bukkit.getOfflinePlayer(player.getUniqueId());
		} else
			target = Bukkit.getOfflinePlayer(playerName);
		
		if(target.hasPlayedBefore()) {
			EssPlayerConf targetFile = new EssPlayerConf(target);
			targetFile.load();
			SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm dd/MM/yy");
			
			sender.sendMessage(ChatColor.DARK_GREEN + "####" + ChatColor.AQUA + " Infos sur "
					+ ChatColor.DARK_AQUA + target.getName() + ChatColor.DARK_GREEN + " ####");


			try {
				Class.forName( "me.confuser.banmanager.BmAPI" );
				sender.sendMessage(ChatColor.GOLD + "Joueur muté ? "
							+ (BmAPI.isMuted(playerName) ? (ChatColor.GREEN + "Oui") : (ChatColor.RED + "Non"))
							+ ChatColor.GOLD + "\nJoueur banni ? "
							+ (BmAPI.isBanned(playerName) ? (ChatColor.GREEN + "Oui") : (ChatColor.RED +"Non")));
	
				if(isAdminPerformed) {
					int CountBan = 0;
					try {
						CloseableIterator<PlayerBanRecord> iter = BmAPI.getBanRecords(BmAPI.getPlayer(playerName));
						for(; iter.hasNext() ; iter.next())
							++CountBan;
					} catch (SQLException e) {
						e.printStackTrace();
					}
					
					sender.sendMessage(ChatColor.GOLD + "Nombre de bans : "
							+ ChatColor.YELLOW + CountBan);
				}
			} catch( ClassNotFoundException e ) {
				getLogger().log(Level.WARNING, "BanManager est introuvable! Pour activer des données"
						+ "supplementaires, veuillez installer BanManager");
			}
				
			if(isAdminPerformed) {
				String ip;
				if(player != null)
					ip = player.getAddress().getAddress().getHostAddress();
				else
					ip = targetFile.getString("ipAddress");
				String address = "NA";
				try {
					address = getAddress(ip);
				} catch (IOException e) {
					e.printStackTrace();
				}
				sender.sendMessage(ChatColor.GOLD + "Adresse : "
						+ ChatColor.YELLOW + address
						+ ChatColor.GOLD + "\nAdresse IP : "
						+ ChatColor.YELLOW + ip);
				
			}

			sender.sendMessage(ChatColor.GOLD + "Première connexion : "
						+ ChatColor.YELLOW + dateFormat.format(new Date(target.getFirstPlayed()))
					+ ChatColor.GOLD + "\nDernière connexion : "
						+ ChatColor.YELLOW + dateFormat.format(new Date(target.getLastPlayed()))
					+ ChatColor.AQUA + "\nIl est le "
						+ ChatColor.DARK_AQUA + targetFile.getPosition()
						+ ChatColor.AQUA + (targetFile.getPosition() != 1 ? "ème" : "er")
						+ " joueur à avoir rejoint le serveur.");
		} else
			sender.sendMessage(ChatColor.RED + "Erreur : le joueur n'existe pas");
	}

	private String getAddress(String ip) throws IOException {
		URL url = new URL("http://ip-api.com/json/" + ip);
		BufferedReader stream = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));
		String city, country, countryCode, regionName;
		city = country = countryCode = regionName = "";
		boolean found = false;
		String inputLine = stream.readLine();
		if(inputLine.contains("\"zip\":"))
			found = (getInputValue(inputLine, "zip") != "");
		if(inputLine.contains("\"city\":"))
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
		String result = inputLine.substring(inputLine.indexOf(search), inputLine.length());
		result = result.substring(result.indexOf(":\"") + 2, result.length() );
		return result.substring(0, result.indexOf("\""));
	}

}

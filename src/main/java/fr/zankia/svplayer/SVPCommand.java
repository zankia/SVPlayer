package fr.zankia.svplayer;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SVPCommand implements CommandExecutor {
    public static final String NO_NEW_PLAYER = "Erreur: il n'y a pas de nouveau joueur à accueillir";
    private final SVP plugin;
    public static final String NO_PERMISSION = "Erreur : Vous n'avez pas la permission pour cette commande.";

    public SVPCommand(SVP plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String name, String[] args) {
        switch (name.toLowerCase()) {

            case "bvn":
                return bvnCommand(sender);

            case "svp":
            case "svplayer":
                return svplayerCommand(sender, args);

            default:
                return false;
        }
    }

    private boolean bvnCommand(CommandSender sender) {
        if (!sender.hasPermission("svplayer.bvn")) {
            sender.sendMessage(ChatColor.RED + NO_PERMISSION);
        } else if (plugin.getChatReplacer().getNewPlayer() == null) {
            sender.sendMessage(ChatColor.RED + NO_NEW_PLAYER);
        } else {
            ((Player) sender).chat(plugin.getChatReplacer().getMessage());
        }
        return true;
    }

    private boolean svplayerCommand(CommandSender sender, String[] args) {
        if (sender.hasPermission("svplayer.admin")) {
            if (args.length != 1 || !args[0].equalsIgnoreCase("reload")) {
                return false;
            }
            plugin.reloadConfig();
            plugin.initChatReplacer();
            sender.sendMessage(ChatColor.RED + "SVPlayer : " + ChatColor.GREEN + "Reload done.");
        } else
            sender.sendMessage(ChatColor.RED + NO_PERMISSION);
        return true;
    }

}

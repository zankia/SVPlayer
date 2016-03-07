package fr.zankia.SVPlayer;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import com.earth2me.essentials.EssentialsConf;

public class EssPlayerConf extends EssentialsConf {
	
	final private static String PATH = "position";
	
	
	public EssPlayerConf(File configFile) {
		super(configFile);
	}

	public EssPlayerConf(OfflinePlayer p) {
		this(new File(Bukkit.getPluginManager().getPlugin("Essentials").getDataFolder(),
				"userdata/" + p.getUniqueId().toString() + ".yml"));
	}
	
	public int getPosition() {
		return getInt(PATH);
	}

	public void setPosition(int pos) {
		if(this.getInt(PATH) == 0) {
			createSection(PATH);
		}
		set(PATH, pos);
		save();
	}
	
	

}

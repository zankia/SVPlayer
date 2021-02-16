package fr.zankia.svplayer;

import com.earth2me.essentials.EssentialsConf;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.io.File;

public class EssPlayerConf extends EssentialsConf {

    private static final String PATH = "position";


    public EssPlayerConf(File configFile) {
        super(configFile);
    }

    public EssPlayerConf(OfflinePlayer p) {
        this(new File(Bukkit.getPluginManager().getPlugin("Essentials").getDataFolder(),
                "userdata/" + p.getUniqueId().toString() + ".yml"));
        this.load();
    }

    public int getPosition() {
        return getInt(PATH);
    }

    public void setPosition(int pos) {
        if (isSet(PATH)) {
            createSection(PATH);
        }
        set(PATH, pos);
        save();
    }


}

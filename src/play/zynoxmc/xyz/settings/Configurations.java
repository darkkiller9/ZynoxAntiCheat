package play.zynoxmc.xyz.settings;

import java.io.File;
import java.io.IOException;
import java.util.Set;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

public class Configurations {
	
	private File file;
    private FileConfiguration config;
 
    public Configurations(Plugin plugin, String fileName) {
    	
        if (!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdir();
        }
 
        file = new File(plugin.getDataFolder(), fileName+".yml");
 
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
 
        config = YamlConfiguration.loadConfiguration(file);
    }
 
    public void set(String path, Object value) {
        config.set(path, value);
    }
 
    public Object get(String path) {
        return config.get(path);
    }
 
    public boolean contains(String path) {
        return config.contains(path);
    }
 
    public ConfigurationSection createSection(String path) {
        ConfigurationSection section = config.createSection(path);
        save();
        return section;
    }
 
    public Set<String> getKeys(String path) {
        return config.getConfigurationSection(path).getKeys(false);
    }
 
    public void save() {
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
 


}
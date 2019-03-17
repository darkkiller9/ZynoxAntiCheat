package play.zynoxmc.xyz;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;


import play.zynoxmc.xyz.settings.Configurations;
import play.zynoxmc.xyz.utils.Colors;
import play.zynoxmc.xyz.check.events.Join;
import play.zynoxmc.xyz.check.events.movement.*;
import play.zynoxmc.xyz.check.events.movement.advanced.*;
import play.zynoxmc.xyz.check.events.combat.*;



public class ZAC extends JavaPlugin implements Listener {
	
	
	
	private static ZAC instance;
	private static Configurations players;
	private static Configurations detections;
	public static FileConfiguration config;


	public static File conf;
	
	
	@Override
	public void onEnable(){
		
		CommandSender sender = Bukkit.getConsoleSender();
        //Send Message
        sender.sendMessage(Colors.format("&7&m-------------&8&l[&4&lZAC&8&l]&f&m-------------"));
        sender.sendMessage("");
        sender.sendMessage(Colors.format("       &bPlugin made by &e&oJudam || darkkiller9"));
        sender.sendMessage(Colors.format("       &b&oVersion: &e" + getDescription().getVersion()));
        sender.sendMessage("");
        sender.sendMessage(Colors.format("&7&m------------------------------------"));
        
        
        
        
        
        Bukkit.getServer().getPluginManager().registerEvents(new Join(), this);
		Bukkit.getServer().getPluginManager().registerEvents(new Speed(), this);
		Bukkit.getServer().getPluginManager().registerEvents(new SpeedB(), this);
		Bukkit.getServer().getPluginManager().registerEvents(new SpeedC(), this);
		Bukkit.getServer().getPluginManager().registerEvents(new KillAura(), this);
		Bukkit.getServer().getPluginManager().registerEvents(new Fly(), this);
		Bukkit.getServer().getPluginManager().registerEvents(new FlyB(), this);
		Bukkit.getServer().getPluginManager().registerEvents(new FlyC(), this);
		Bukkit.getServer().getPluginManager().registerEvents(new Glide(), this);
		Bukkit.getServer().getPluginManager().registerEvents(new WaterWalk(), this);
		Bukkit.getServer().getPluginManager().registerEvents(new NoFall(), this);
		Bukkit.getServer().getPluginManager().registerEvents(new FastBow(), this);
		
		
		
		
		
		
		
		
	
		
		instance = this;
		config = getConfig();
		config.options().copyDefaults(true);
		conf = new  File(getDataFolder(), "config.yml");
		players = new Configurations(this, "players");
		detections = new Configurations(this, "detections");
		Bukkit.getServer().getPluginManager().registerEvents(this, this);
		saveConfig();
		saveDefaultConfig();
		register();
		
		
		
		
	
		
	}
	public static ZAC getInstance(){
		
    	return instance;
	}
	public void register(){
		
	}
	public static Configurations getPlayers() {
		return players;
	}
	public static Configurations getDetections() {
		return detections;
	}
}
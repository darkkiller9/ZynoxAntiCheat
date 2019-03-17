package play.zynoxmc.xyz.check.events.combat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.util.Vector;

import play.zynoxmc.xyz.ZAC;



public class KillAura implements Listener{
	
	
	 public static ArrayList<Player> checked = new ArrayList<>();
	 public static HashMap<Player, NPC> check = new HashMap<>();
	 public static HashMap<Player, PacketReader> packets = new HashMap<>();
	 
	 @EventHandler
	    public void onJoin(PlayerJoinEvent e){
	    	Player p = e.getPlayer();
	    	PacketReader reader = new PacketReader(p);
	    	reader.inject();
	    	packets.put(p, reader);
	    	
	    }
	 @EventHandler
	    public void onQuit(PlayerQuitEvent e){
	    	Player p = e.getPlayer();
	    	if(packets.containsKey(p)){
	    		PacketReader reader = packets.get(p);
	    		reader.uninject();
	    		packets.remove(p);	
	    	}
	    	
	    	
	    }
	    
	    @EventHandler
	    public void onDamage(EntityDamageByEntityEvent e){
	    	if(e.getDamager() instanceof Player){
	    		Player p = (Player) e.getDamager();
	    		if(!(checked.contains(p))){
	    			checked.add(p);
	    			performCheck(p);
	    			
	    			Bukkit.getScheduler().runTaskLaterAsynchronously(ZAC.getInstance(), new Runnable(){
	    				
	    				@Override
	    				public void run(){
	    					checked.remove(p);
	    				}
	    			}, 20*5);
	    		}
	    	}
	    }
	    public static void performCheck(Player p){
	    	spawn(p);
	    	Bukkit.getScheduler().runTaskLaterAsynchronously(ZAC.getInstance(), new Runnable(){
	    		
	    		@Override
	    		public void run(){
	    			despawn(p);
	    		}
	    	}, 15);
	    }
	    public static void spawn(Player p){
	    	NPC npc = new NPC(getRandomName(), getBlockBehind(p));
	    	npc.spawn(p);
	    	check.put(p, npc);
	    	
	    	
	    	
	    	
	    	
	    }
	    
	    public static Location getBlockBehind(Player p){
	    	Location loc = p.getLocation();
	    	Vector vec = loc.getDirection().multiply(-0.3D);
	    	vec.setY(0);
	    	return loc.add(vec);
	    }
	    public static String getRandomName(){
	    	String st = "";
	    	Random r = new Random();
	    	int zufall = r.nextInt(1);
	    	switch(zufall) {
	    	
	    	case 0:
	    		st = "NPC";
	        	break;
	    	case 1:
	    		st = "NPC";
	    		break;
	    	}
	    	return st;
	    }
	    public static void despawn(Player p){
	    	if(check.containsKey(p)){
	    		NPC npc = check.get(p);
	    		npc.destroy(p);
	    		check.remove(p);
	    	}
	    }
}
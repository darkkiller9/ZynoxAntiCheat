package play.zynoxmc.xyz.check.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;


import play.zynoxmc.xyz.ZAC;
import play.zynoxmc.xyz.utils.Messages;

public class Join implements Listener {
	
	@EventHandler
	public void onJoin(PlayerJoinEvent e){
	Player p = e.getPlayer();
	String uuid = p.getUniqueId().toString();
	if(!p.hasPlayedBefore()){
		System.out.println(Messages.PREFIX + " Player " + p.getName() + " joined for the first time!");
		if(!ZAC.getPlayers().contains(p.getName())){
			ZAC.getPlayers().set("Player." + p.getName() + ".UUID", uuid);
			ZAC.getPlayers().save();
		}
	}
	System.out.println(Messages.PREFIX + " Player " + p.getName() + " has joined the game.");
	ZAC.getPlayers().set("Player." + p.getName() + ".UUID", uuid);
	ZAC.getPlayers().save();
	}
}
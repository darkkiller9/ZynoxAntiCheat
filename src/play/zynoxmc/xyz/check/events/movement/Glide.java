package play.zynoxmc.xyz.check.events.movement;


import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import play.zynoxmc.xyz.ZAC;
import play.zynoxmc.xyz.check.CheckType;
import play.zynoxmc.xyz.check.Detections;
import play.zynoxmc.xyz.utils.Colors;
import play.zynoxmc.xyz.utils.Messages;




public class Glide implements Listener {
	

	@EventHandler
	public void onMove(PlayerMoveEvent e){
		if((e.getTo().getY() - e.getFrom().getY() == -0.125D) 
			&& e.getTo().clone().subtract(0.0D, 1.0D, 0.0D).getBlock().getType().equals(Material.AIR)){
			e.setCancelled(true);
			e.getPlayer().teleport(e.getPlayer());
			ZAC.getDetections().set("Player." + e.getPlayer().getName() + ".CheckType", CheckType.GLIDE.toString());
			ZAC.getDetections().set("Player." + e.getPlayer().getName() + ".DetectionType", Detections.HIGH.toString());
			ZAC.getDetections().save();
			for(Player staff : Bukkit.getOnlinePlayers()){
				if(staff.hasPermission("zac.notify")){
					staff.sendMessage(Colors.format(Messages.PREFIX + "Player &c&l" + e.getPlayer().getName() + "&e is sending too many packets &aCheckType: &c&l" + CheckType.GLIDE.toString() + " &e, &aDetection: &c&l" + Detections.HIGH.toString() + "&e."));
				}
			}
			
		}
	}
}

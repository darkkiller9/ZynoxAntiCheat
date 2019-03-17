package play.zynoxmc.xyz.check.events.movement;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;


import play.zynoxmc.xyz.ZAC;
import play.zynoxmc.xyz.check.CheckType;
import play.zynoxmc.xyz.settings.CheckDoubles;
import play.zynoxmc.xyz.utils.Colors;
import play.zynoxmc.xyz.utils.Messages;
import play.zynoxmc.xyz.check.Detections;

public class WaterWalk implements Listener
{
	
	@EventHandler
	public void onMove(PlayerMoveEvent e)
	{
		Player p = e.getPlayer();
		Location to = e.getTo();
		Location from = e.getFrom();
		Vector vec = to.toVector();
		double i = vec.distance(from.toVector());
		if(p.getGameMode().equals(GameMode.CREATIVE))
		{
			return;
		}
		if(p.getEntityId()==
				100){
			
			return;
		}
		if(p.getVehicle() !=null){
			return;
		}
		if(p.getAllowFlight() == true){
			return;
		}
		if((i > CheckDoubles.MIN_WATER_WALK) && (i < 
				CheckDoubles.MAX_WATER_WALK)){
			if(p.getLocation().getBlock().getRelative(BlockFace.DOWN).getType() == 
					Material.WATER){
				return;
			}
			if(p.getLocation().getBlock().getRelative(BlockFace.DOWN).isLiquid())
			{
				p.setHealth(0);
				p.kickPlayer(Colors.format(Messages.PREFIX + "&eUnfair advantage."));
				ZAC.getDetections().set("Player." + e.getPlayer().getName() + ".CheckType", CheckType.WATER_WALK.toString());
				ZAC.getDetections().set("Player." + e.getPlayer().getName() + ".DetectionType", Detections.HIGH.toString());
				ZAC.getDetections().save();
				for(Player staff : Bukkit.getOnlinePlayers()){
					if(staff.hasPermission("zac.notify")){
						staff.sendMessage(Colors.format(Messages.PREFIX + "Player &c&l" + p.getPlayer().getName() + "&e is sending too many packets &aCheckType: &c&l" + CheckType.WATER_WALK.toString() + " &e, &aDetection: &c&l" + Detections.HIGH.toString() + "&e."));
					}
				}
				
			}
		}
	}

}

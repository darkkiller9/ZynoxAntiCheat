package play.zynoxmc.xyz.check.events.movement.advanced;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import play.zynoxmc.xyz.ZAC;
import play.zynoxmc.xyz.check.CheckType;
import play.zynoxmc.xyz.settings.CheckDoubles;
import play.zynoxmc.xyz.utils.Colors;
import play.zynoxmc.xyz.utils.Messages;
import play.zynoxmc.xyz.check.Detections;


public class FlyB implements Listener {
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onMove(PlayerMoveEvent e)
	{
		Player p = e.getPlayer();
		Location to = e.getTo();
		Location from = e.getFrom();
		Vector vec = to.toVector();
		double i = vec.distance(from.toVector());
		if(p.getLocation().getBlock().getRelative(BlockFace.DOWN).getType().equals(Material.SPONGE)){
			return;
		}
		if(p.getGameMode().equals(GameMode.CREATIVE))
		{
			return;
		}
		if(p.getActivePotionEffects().equals(PotionEffectType.SPEED))
		{
			return;
		}
		if(p.getEntityId()==100)
		{
			return;
		}
		if(p.getVehicle() !=null)
		{
			return;
		}
		if(p.getAllowFlight() == true)
		{
			return;
		}
		if((p.getFallDistance() == 0.0F) && 
				(p.getLocation().getBlock().getRelative(BlockFace.UP).getType().equals(Material.AIR)))
		{
			if(i > CheckDoubles.MAX_FLIGHT_B)
			{
				if(p.isOnGround())
				{
					return;
				}
				e.setCancelled(true);
				p.teleport(e.getFrom());
				ZAC.getDetections().set("Player." + p.getName() + ".CheckType", CheckType.FLY_B.toString());
				ZAC.getDetections().set("Player." + p.getName() + ".DetectionType", Detections.HIGH.toString());
				ZAC.getDetections().save();
				for(Player staff : Bukkit.getOnlinePlayers()){
					if(staff.hasPermission("zac.notify")){
						staff.sendMessage(Colors.format(Messages.PREFIX + "Player &c&l" + p.getPlayer().getName() + "&e is sending too many packets &aCheckType: &c&l" + CheckType.FLY_B.toString() + " &e, &aDetection: &c&l" + Detections.HIGH.toString() + "&e."));
					}
				}
				
			}
		}
	}
}
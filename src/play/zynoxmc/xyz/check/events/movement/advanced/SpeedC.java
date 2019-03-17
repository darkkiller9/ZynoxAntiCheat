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

public class SpeedC implements Listener{
	
	@EventHandler
	public void onMove(PlayerMoveEvent e){
		Player p = e.getPlayer();
		Location to = e.getTo();
		Location From = e.getFrom();
		
		Vector vec = to.toVector().setY(0.0D);
		double i = vec.distance(From.toVector().setY(0.0D));
		if(p.getLocation().getBlock().getRelative(BlockFace.DOWN).getType().equals(Material.SPONGE)){
			return;
		}
		if(p.getGameMode().equals(GameMode.CREATIVE)){
			return;
		}
		if(p.getEntityId() == 100){
			return;
		}
		if(p.getVehicle() !=null){
			return;
		}
		if(p.getActivePotionEffects().equals(PotionEffectType.SPEED))
		{
			return;
			
		}
		if(p.getAllowFlight() == true){
			return;
		}
		if(From.getY() < to.getY()){
			return;
		}
		
		if(i >= CheckDoubles.MAX_SPEED_C){
			e.setCancelled(true);
			p.teleport(e.getFrom());
			p.kickPlayer(Colors.format(Messages.PREFIX + "&eUnfair advantage."));
			ZAC.getDetections().set("Player." + p.getName() + ".CheckType", CheckType.SPEED_C.toString());
			ZAC.getDetections().set("Player." + p.getName() + ".DetectionType", Detections.NORMAL.toString());
			ZAC.getDetections().save();
			for(Player staff : Bukkit.getOnlinePlayers()){
				if(staff.hasPermission("zac.notify")){
					staff.sendMessage(Colors.format(Messages.PREFIX + "Player &c&l" + p.getPlayer().getName() + "&e is sending too many packets &aCheckType: &c&l" + CheckType.SPEED_C.toString() + " &e, &aDetection: &c&l" + Detections.HIGH.toString() + "&e."));
				}
			}
			
			
		}
	}
}
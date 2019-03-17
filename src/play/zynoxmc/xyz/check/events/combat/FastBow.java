package play.zynoxmc.xyz.check.events.combat;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;



import play.zynoxmc.xyz.ZAC;
import play.zynoxmc.xyz.check.CheckType;
import play.zynoxmc.xyz.check.Detections;
import play.zynoxmc.xyz.utils.Colors;
import play.zynoxmc.xyz.utils.Messages;

public class FastBow implements Listener{
	
	static HashMap<Player, Long> lastbow = new HashMap<>();
	
	@EventHandler
	public void onShot(EntityShootBowEvent e){
		
	
		if(!(e.getEntity() instanceof Player)){
			return;
		}
		
		Player p = (Player) e.getEntity();
		if(!lastbow.containsKey(p)){
			lastbow.put(p, 0L);
		}
		if(e.getForce() !=1.0D){
			return;
		}
		if(lastbow.get(p) == 0L){
			lastbow.put(p, System.currentTimeMillis());
			return;
		}
		if(System.currentTimeMillis() - lastbow.get(p) < 500L){
			e.getProjectile().remove();
			e.setCancelled(true);
			ZAC.getDetections().set("Player." + p.getName() + ".CheckType", CheckType.FAST_BOW.toString());
			ZAC.getDetections().set("Player." + p.getName() + ".DetectionType", Detections.HIGH.toString());
			ZAC.getDetections().save();
			for(Player staff : Bukkit.getOnlinePlayers()){
				if(staff.hasPermission("zac.notify")){
					staff.sendMessage(Colors.format(Messages.PREFIX + "Player &c&l" + p.getPlayer().getName() + "&e is sending too many packets &aCheckType: &c&l" + CheckType.FAST_BOW.toString() + " &e, &aDetection: &c&l" + Detections.HIGH.toString() + "&e."));
				}
			}
			
		}
	}
	
}
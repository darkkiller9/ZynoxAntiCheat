package play.zynoxmc.xyz.check.events.combat;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import net.minecraft.server.v1_8_R3.Packet;


import play.zynoxmc.xyz.ZAC;
import play.zynoxmc.xyz.check.CheckType;
import play.zynoxmc.xyz.check.Detections;
import play.zynoxmc.xyz.utils.Colors;
import play.zynoxmc.xyz.utils.Messages;

public class PacketReader {
	 
    Player player;
    Channel channel;
   
    public PacketReader(Player player) {
            this.player = player;
    }
   
    public void inject(){
            CraftPlayer cPlayer = (CraftPlayer)this.player;
            channel = cPlayer.getHandle().playerConnection.networkManager.channel;
            channel.pipeline().addAfter("decoder", "PacketInjector", new MessageToMessageDecoder<Packet<?>>() {@Override protected void decode(ChannelHandlerContext arg0, Packet<?> packet,List<Object> arg2) throws Exception {arg2.add(packet);readPacket(packet);}});
    }
   
    public void uninject(){
            if(channel.pipeline().get("PacketInjector") != null){
                    channel.pipeline().remove("PacketInjector");
            }
    }

    public static ArrayList<Player> dedected = new ArrayList<>();
   
    public void readPacket(Packet<?> packet){
    	
        if(packet.getClass().getSimpleName().equalsIgnoreCase("PacketPlayInUseEntity")){
                int id = (Integer)getValue(packet, "a");
                if(!(dedected.contains(player))){
                	if(KillAura.check.containsKey(player)){
                		NPC npc = KillAura.check.get(player);
                		if(id == npc.getEntityID()){
                			if(getValue(packet, "action").toString().equalsIgnoreCase("ATTACK")){
                				npc.animation(player, 1);
                				dedected.add(player);
                				Bukkit.getScheduler().runTaskLater(ZAC.getInstance(), new Runnable(){
                					
                					@Override
                					public void run(){
                						ZAC.getDetections().set("Player." + player.getName() + ".CheckType", CheckType.KILL_AURA.toString());
                						ZAC.getDetections().set("Player." + player.getName() + ".DetectionType", Detections.HIGH.toString());
                						ZAC.getDetections().save();
                						for(Player staff : Bukkit.getOnlinePlayers()){
                							if(staff.hasPermission("zac.notify")){
                								staff.sendMessage(Colors.format(Messages.PREFIX + "Player &c&l" + player.getPlayer().getName() + "&e is sending too many packets &aCheckType: &c&l" + CheckType.KILL_AURA.toString() + " &e, &aDetection: &c&l" + Detections.HIGH.toString() + "&e."));
                							}
                						}
                						dedected.remove(player);
                					
                					
                				
                					}
                				},10);
                			}
                		}
                	}
                }
        }
               
    }
   

    public void setValue(Object obj,String name,Object value){
            try{
            Field field = obj.getClass().getDeclaredField(name);
            field.setAccessible(true);
            field.set(obj, value);
            }catch(Exception e){}
    }
   
    public Object getValue(Object obj,String name){
            try{
            Field field = obj.getClass().getDeclaredField(name);
            field.setAccessible(true);
            return field.get(obj);
            }catch(Exception e){}
            return null;
    }
   
}

package play.zynoxmc.xyz.check.events.combat;

import java.util.List;
import java.util.UUID;
 
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.util.CraftChatMessage;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
 
import com.mojang.authlib.GameProfile;
import net.minecraft.server.v1_8_R3.BlockPosition;
import net.minecraft.server.v1_8_R3.DataWatcher;
import net.minecraft.server.v1_8_R3.MathHelper;
import net.minecraft.server.v1_8_R3.PacketPlayOutAnimation;
import net.minecraft.server.v1_8_R3.PacketPlayOutBed;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntity.PacketPlayOutEntityLook;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityEquipment;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityHeadRotation;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityStatus;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityTeleport;
import net.minecraft.server.v1_8_R3.PacketPlayOutNamedEntitySpawn;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerInfo;
import net.minecraft.server.v1_8_R3.WorldSettings.EnumGamemode;
import play.zynoxmc.xyz.ZAC;
 
public class NPC extends Reflections {
 
   
    private int entityID;
    private Location location;
    private GameProfile gameprofile;
   
   
    public NPC(String name,Location location){
        entityID = (int)Math.ceil(Math.random() * 1000) + 2000;
        gameprofile = new GameProfile(UUID.randomUUID(), name);
        this.location = location.clone();
    }
 
    @SuppressWarnings("deprecation")
    public static String getUUID(String name) {
        return Bukkit.getOfflinePlayer(name).getUniqueId().toString().replace("-", "");
    }
   
   
    public void animation(Player p,int animation){
        PacketPlayOutAnimation packet = new PacketPlayOutAnimation();
        setValue(packet, "a", entityID);
        setValue(packet, "b", (byte)animation);
        sendPacket(packet, p);
    }
   
    public void status(Player p,int status){
        PacketPlayOutEntityStatus packet = new PacketPlayOutEntityStatus();
        setValue(packet, "a", entityID);
        setValue(packet, "b", (byte)status);
        sendPacket(packet, p);
    }
   
    public void equip(Player p,int slot,ItemStack itemstack){
        PacketPlayOutEntityEquipment packet = new PacketPlayOutEntityEquipment();
        setValue(packet, "a", entityID);
        setValue(packet, "b", slot);
        setValue(packet, "c", itemstack);
        sendPacket(packet, p);
    }
   
    @SuppressWarnings("deprecation")
    public void sleep(Player p,boolean state){
        if(state){
            Location bedLocation = new Location(location.getWorld(), 1, 1, 1);
            PacketPlayOutBed packet = new PacketPlayOutBed();
            setValue(packet, "a", entityID);
            setValue(packet, "b", new BlockPosition(bedLocation.getX(),bedLocation.getY(),bedLocation.getZ()));
           
            for(Player pl : Bukkit.getOnlinePlayers()){
                pl.sendBlockChange(bedLocation, Material.BED_BLOCK, (byte)0);
            }
 
            sendPacket(packet, p);
            teleport(p, location.clone().add(0,0.3,0));
        }else{
            animation(p, 2);
            teleport(p, location.clone().subtract(0,0.3,0));
        }
    }
   
    public void spawn(final Player p){
        PacketPlayOutNamedEntitySpawn packet = new PacketPlayOutNamedEntitySpawn();
       
        setValue(packet, "a", entityID);
        setValue(packet, "b", gameprofile.getId());
        setValue(packet, "c", getFixLocation(location.getX()));
        setValue(packet, "d", getFixLocation(location.getY()));
        setValue(packet, "e", getFixLocation(location.getZ()));
        setValue(packet, "f", getFixRotation(location.getYaw()));
        setValue(packet, "g", getFixRotation(location.getPitch()));
        setValue(packet, "h", 0);
        DataWatcher w = new DataWatcher(null);
        w.a(6,(float)20);
        addToTablist(p);
        w.a(10,(byte)127);
        setValue(packet, "i", w);
        sendPacket(packet, p);
        headRotation(p, location.getYaw(), location.getPitch());
        Bukkit.getScheduler().runTaskLater(ZAC.getInstance(), new Runnable() {
           
            @Override
            public void run() {
                rmvFromTablist(p);
            }
        }, 30);
    }
   
    public void teleport(Player p, Location location){
        PacketPlayOutEntityTeleport packet = new PacketPlayOutEntityTeleport();
        setValue(packet, "a", entityID);
        setValue(packet, "b", getFixLocation(location.getX()));
        setValue(packet, "c", getFixLocation(location.getY()));
        setValue(packet, "d", getFixLocation(location.getZ()));
        setValue(packet, "e", getFixRotation(location.getYaw()));
        setValue(packet, "f", getFixRotation(location.getPitch()));
 
        sendPacket(packet, p);
        headRotation(p, location.getYaw(), location.getPitch());
        this.location = location.clone();
    }
   
    public void headRotation(Player p,float yaw,float pitch){
        PacketPlayOutEntityLook packet = new PacketPlayOutEntityLook(entityID, getFixRotation(yaw),getFixRotation(pitch) , true);
        PacketPlayOutEntityHeadRotation packetHead = new PacketPlayOutEntityHeadRotation();
        setValue(packetHead, "a", entityID);
        setValue(packetHead, "b", getFixRotation(yaw));
       
 
        sendPacket(packet, p);
        sendPacket(packetHead, p);
    }
   
    public void destroy(Player p){
        PacketPlayOutEntityDestroy packet = new PacketPlayOutEntityDestroy(new int[] {entityID});
        rmvFromTablist(p);
        sendPacket(packet, p);
    }
    public void addToTablist(Player p){
        PacketPlayOutPlayerInfo packet = new PacketPlayOutPlayerInfo();
        PacketPlayOutPlayerInfo.PlayerInfoData data = packet.new PlayerInfoData(gameprofile, 1, EnumGamemode.NOT_SET, CraftChatMessage.fromString(gameprofile.getName())[0]);
        @SuppressWarnings("unchecked")
        List<PacketPlayOutPlayerInfo.PlayerInfoData> players = (List<PacketPlayOutPlayerInfo.PlayerInfoData>) getValue(packet, "b");
        players.add(data);
       
        setValue(packet, "a", PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER);
        setValue(packet, "b", players);
        sendPacket(packet, p);
    }
   
    public void rmvFromTablist(Player p){
        PacketPlayOutPlayerInfo packet = new PacketPlayOutPlayerInfo();
        PacketPlayOutPlayerInfo.PlayerInfoData data = packet.new PlayerInfoData(gameprofile, 1, EnumGamemode.NOT_SET, CraftChatMessage.fromString(gameprofile.getName())[0]);
        @SuppressWarnings("unchecked")
        List<PacketPlayOutPlayerInfo.PlayerInfoData> players = (List<PacketPlayOutPlayerInfo.PlayerInfoData>) getValue(packet, "b");
        players.add(data);
       
        setValue(packet, "a", PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER);
        setValue(packet, "b", players);
       
        sendPacket(packet, p);
    }
   
    public int getFixLocation(double pos){
        return (int)MathHelper.floor(pos * 32.0D);
    }
   
    public int getEntityID() {
        return entityID;
    }
   
    public byte getFixRotation(float yawpitch){
        return (byte) ((int) (yawpitch * 256.0F / 360.0F));
    }
}
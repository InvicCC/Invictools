package me.invic.invictools.util.Leaderboards;

import net.minecraft.server.level.WorldServer;
import net.minecraft.world.entity.decoration.EntityArmorStand;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_19_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_19_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_19_R1.entity.CraftArmorStand;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class statsHolo
{
    private Location loc;
    private Player p;
    private HashMap<String,Integer> titles = new HashMap<>();

    statsHolo(Location s, Player pl,String header)
    {
        titles.put(header,-1);
        loc = s;
        p = pl;
    }

    public void create()
    {
        /*
        WorldServer s = ((CraftWorld)p.getWorld()).getHandle();
        EntityArmorStand stand = new EntityArmorStand(s,loc.getX(),loc.getY(),loc.getZ());

        stand.setLocation(x, y, z, 0, 0);
        stand.setCustomName(name);
        stand.setCustomNameVisible(true);
        stand.setGravity(true);
        stand.setSmall(true);
        stand.setInvisible(true);

        PacketPlayOutSpawnEntityLiving packet = new PacketPlayOutSpawnEntityLiving(stand);
        ((CraftPlayer)player).getHandle().playerConnection.sendPacket(packet);

         */
    }

    public void add(String title, int statistic)
    {
        titles.put(title,statistic);
    }

}

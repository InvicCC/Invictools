package me.invic.invictools.Leaderboards;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import me.invic.invictools.commands.OldCommands;
import me.invic.invictools.cosmetics.statisticRequirments;
import me.invic.invictools.gamemodes.bf.bedfightStatistics;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class statsHolo
{
    private Location loc;
    private Player p;
    private HashMap<String,Integer> titles = new HashMap<>();
    private HashMap<Integer,String> order = new HashMap<>(); // loooooool
    private String header;
    private List<Entity> stands = new ArrayList<>();

    statsHolo(Location s, Player pl,String h)
    {
        header = h;
        loc = s;
        p = pl;
    }

    public List<Entity> getStands()
    {
        return stands;
    }

    public void destroy()
    {
        for (Entity ar:stands)
        {
            ar.teleport(ar.getLocation().add(0,100,0));
            ar.remove();
        }
    }

    public void add(String title, int statistic)
    {
        titles.put(title,statistic);
        order.put(order.size(),title);
    }

    private static final DecimalFormat df = new DecimalFormat("0.00");
    public void build()
    {
        Location temp = loc.clone();
        for (int i = 0; i<order.size();i++)
        {
            String title = order.get(i);
            ArmorStand as = (ArmorStand) temp.getWorld().spawnEntity(temp.subtract(0,.3,0), EntityType.ARMOR_STAND);
            as.setVisible(false);
            as.setGravity(false);
            if(title.equalsIgnoreCase("Points:"))
                as.setCustomName(ChatColor.AQUA+ title +" "+ new bedfightStatistics().presColor(titles.get(title))+new bedfightStatistics().presColor(titles.get(title))+titles.get(title)+"山");
            else if(title.equalsIgnoreCase("Stars:"))
                as.setCustomName(ChatColor.AQUA+ title +" "+ new leaderboard().presColor(titles.get(title))+titles.get(title)+new leaderboard().presColor2(titles.get(title))+"✰");
            else if(title.equalsIgnoreCase("bwfkdr"))
            {
                File Folder = new File(OldCommands.Invictools.getDataFolder(), "PlayerData");
                File pFile = new File(Folder, p.getUniqueId() + ".yml");
                YamlConfiguration config = YamlConfiguration.loadConfiguration(pFile);
                Double fkdr = config.getDouble("FinalKills")/ config.getDouble("FinalDeaths");
                as.setCustomName(ChatColor.AQUA+ "Final k/d:" +" "+ ChatColor.WHITE+ df.format(fkdr));
            }
            else if(title.equalsIgnoreCase("bwwl"))
            {
                Double wl = Double.parseDouble(String.valueOf(statisticRequirments.getStatistic("bw_wins",p))) / Double.parseDouble(String.valueOf(statisticRequirments.getStatistic("bw_loses",p)));
                as.setCustomName(ChatColor.AQUA+ "w/l:" +" "+ ChatColor.WHITE+df.format(wl));
            }
            else if(title.equalsIgnoreCase("bffkdr"))
            {
                File Folder = new File(OldCommands.Invictools.getDataFolder(), "Bedfight");
                File file = new File(Folder,"bedfightstats.yml");
                FileConfiguration stats = YamlConfiguration.loadConfiguration(file);
                Double fkdr = stats.getDouble("data."+p.getUniqueId()+".FinalKills")/ stats.getDouble("data."+p.getUniqueId()+".FinalDeaths");
                as.setCustomName(ChatColor.AQUA+ "Final k/d:" +" "+ ChatColor.WHITE+ df.format(fkdr));
            }
            else if(title.equalsIgnoreCase("bfwl"))
            {
                File Folder = new File(OldCommands.Invictools.getDataFolder(), "Bedfight");
                File file = new File(Folder,"bedfightstats.yml");
                FileConfiguration stats = YamlConfiguration.loadConfiguration(file);
                Double wl = stats.getDouble("data."+p.getUniqueId()+".Wins")/ stats.getDouble("data."+p.getUniqueId()+".Losses");
                as.setCustomName(ChatColor.AQUA+ "w/l:" +" "+ ChatColor.WHITE+ df.format(wl));
            }
           else
                as.setCustomName(ChatColor.AQUA+ title +" "+ ChatColor.WHITE+titles.get(title));
            as.setCustomNameVisible(true);
            as.setMetadata("holo", new FixedMetadataValue(OldCommands.Invictools, true));
            stands.add(as);
          //  i += .3;
        }
        ArmorStand headerStand = (ArmorStand) temp.getWorld().spawnEntity(loc, EntityType.ARMOR_STAND);
        headerStand.setVisible(false);
        headerStand.setGravity(false);
        headerStand.setCustomName(header);
        headerStand.setCustomNameVisible(true);
        headerStand.setMetadata("holo", new FixedMetadataValue(OldCommands.Invictools, true));
        stands.add(headerStand);
        for (Player otherp:Bukkit.getOnlinePlayers())
        {
            if(!otherp.getName().equals(p.getName()))
                sendPacket(otherp,stands);
        }
    }

    public static void sendPacket(Player p, List<Entity> entities)
    {
        ProtocolManager manager = ProtocolLibrary.getProtocolManager();
        PacketContainer packet1 = manager.createPacket(PacketType.Play.Server.ENTITY_DESTROY);

        List<Integer> entityIDList = new ArrayList<>();
        for (Entity en:entities)
        {
            entityIDList.add(en.getEntityId());
        }

        packet1.getIntLists().write(0, entityIDList);

        try
        {
            manager.sendServerPacket(p, packet1);
         //   p.sendMessage("Destroyed a hologram for you "+entityIDList.size());
        }
        catch (InvocationTargetException e)
        {
            e.printStackTrace();
        }
    }

}

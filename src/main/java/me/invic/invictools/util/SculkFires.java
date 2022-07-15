package me.invic.invictools.util;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;
import org.screamingsandals.bedwars.api.events.BedwarsGameEndingEvent;
import org.screamingsandals.bedwars.api.events.BedwarsGameStartedEvent;
import org.screamingsandals.bedwars.api.events.BedwarsPlayerBreakBlock;
import org.screamingsandals.bedwars.api.game.Game;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class SculkFires implements Listener
{
    static String map = "MegaShire";

    @EventHandler
    public void bedDetect(BedwarsPlayerBreakBlock e)
    {
        if(!e.getGame().getGameWorld().equals(Bukkit.getWorld(map)))
            return;

        if (e.getBlock().getType().equals(Material.BLUE_BED)
                || e.getBlock().getType().equals(Material.GRAY_BED)
                || e.getBlock().getType().equals(Material.RED_BED)
                || e.getBlock().getType().equals(Material.LIGHT_BLUE_BED)
                || e.getBlock().getType().equals(Material.WHITE_BED)
                || e.getBlock().getType().equals(Material.YELLOW_BED)
                || e.getBlock().getType().equals(Material.PINK_BED)
                || e.getBlock().getType().equals(Material.LIME_BED)
                || e.getBlock().getType().equals(Material.ORANGE_BED))
        {
            String[] s = e.getBlock().getType().toString().split("_");
            Material block = e.getBlock().getType();
            Location loc = e.getBlock().getLocation();
            new BukkitRunnable()
            {
                @Override
                public void run()
                {
                    if(loc.getBlock().getType()!=block)
                    {
                        placeBedrock(s[0], false, Material.DEEPSLATE_BRICKS);
                    }
                }
            }.runTaskLater(Objects.requireNonNull(Bukkit.getServer().getPluginManager().getPlugin("Invictools")), 1L);
        }
    }

    @EventHandler
    public void init(BedwarsGameStartedEvent e)
    {
        if(!e.getGame().getGameWorld().equals(Bukkit.getWorld(map)))
            return;

        List<String> teams = new ArrayList<>();
        teams.add("GRAY");
        teams.add("BLUE");
        teams.add("RED");
        teams.add("WHITE");
        teams.add("YELLOW");
        teams.add("PINK");
        teams.add("ORANGE");
        teams.add("LIME");

        for (Player p:e.getGame().getConnectedPlayers())
        {
            teams.remove(e.getGame().getTeamOfPlayer(p).getName().toUpperCase(Locale.ROOT));
        }

        for (String s:teams)
        {
            placeBedrock(s, false, Material.DEEPSLATE_BRICKS);
        }
    }

    @EventHandler
    public void reset(BedwarsGameEndingEvent e)
    {
        if(!e.getGame().getGameWorld().equals(Bukkit.getWorld(map)))
            return;

        resetFires();
    }

    public static void resetFires()
    {
        List<String> teams = new ArrayList<>();
        teams.add("GRAY");
        teams.add("BLUE");
        teams.add("RED");
        teams.add("WHITE");
        teams.add("YELLOW");
        teams.add("PINK");
        teams.add("ORANGE");
        teams.add("LIME");

        for (String s:teams)
        {
            placeBedrock(s, false, Material.SOUL_CAMPFIRE);
        }
    }

    static void placeBedrock(String color, boolean strike, Material material)
    {
        World w = Bukkit.getWorld(map);
        int y = 76;
        int y2 = 79;
        Location loc;
        Location loc2;
        switch (color)
        {
            case "LIGHT":
            case "BLUE":
                loc = new Location(Bukkit.getWorld(map), 0.5,y,118.5);
                loc2 = new Location(Bukkit.getWorld(map), 0.5,y2,118.5);
                break;
            case "GRAY":
                loc = new Location(Bukkit.getWorld(map), -79.5,y,77.5);
                loc2 = new Location(Bukkit.getWorld(map), -79.5,y2,77.5);
                break;
            case "RED":
                loc = new Location(Bukkit.getWorld(map), -117.5,y,.5);
                loc2 = new Location(Bukkit.getWorld(map), -117.5,y2,.5);
                break;
            case "WHITE":
                loc = new Location(Bukkit.getWorld(map), -76.5,y,-79.5);
                loc2 = new Location(Bukkit.getWorld(map), -76.5,y2,-79.5);
                break;
            case "YELLOW":
                loc = new Location(Bukkit.getWorld(map), 80.5,y,-76.5);
                loc2 = new Location(Bukkit.getWorld(map), 80.5,y2,-76.5);
                break;
            case "PINK":
                loc = new Location(Bukkit.getWorld(map), .5,y,-117.5);
                loc2 = new Location(Bukkit.getWorld(map), .5,y2,-117.5);
                break;
            case "LIME":
                loc = new Location(Bukkit.getWorld(map), 77.5,y,80.5);
                loc2 = new Location(Bukkit.getWorld(map), 77.5,y2,80.5);
                break;
            case "ORANGE":
                loc = new Location(Bukkit.getWorld(map), 118.5,y,.5);
                loc2 = new Location(Bukkit.getWorld(map), 118.5,y2,.5);
                break;
            default:
                loc = new Location(Bukkit.getWorld(map), 0,y,0);
                loc2 = new Location(Bukkit.getWorld(map), 0,y2,0);
        }
        loc.getBlock().setType(material);
        loc2.getBlock().setType(material);

        if(strike)
            loc.getWorld().strikeLightningEffect(loc);
    }
}

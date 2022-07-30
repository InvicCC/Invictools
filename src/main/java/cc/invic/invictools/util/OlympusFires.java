package cc.invic.invictools.util;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;
import org.screamingsandals.bedwars.api.events.BedwarsGameEndingEvent;
import org.screamingsandals.bedwars.api.events.BedwarsGameStartedEvent;
import org.screamingsandals.bedwars.api.events.BedwarsPlayerBreakBlock;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class OlympusFires implements Listener
{
    @EventHandler
    public void bedDetect(BedwarsPlayerBreakBlock e)
    {
        if (!e.getGame().getGameWorld().equals(Bukkit.getWorld("map2")))
            return;

        if (e.getBlock().getType().equals(Material.BLUE_BED)
                || e.getBlock().getType().equals(Material.GRAY_BED)
                || e.getBlock().getType().equals(Material.LIGHT_GRAY_BED)
                || e.getBlock().getType().equals(Material.RED_BED)
                || e.getBlock().getType().equals(Material.LIGHT_BLUE_BED)
                || e.getBlock().getType().equals(Material.WHITE_BED)
                || e.getBlock().getType().equals(Material.YELLOW_BED)
                || e.getBlock().getType().equals(Material.PINK_BED)
                || e.getBlock().getType().equals(Material.LIME_BED)
                || e.getBlock().getType().equals(Material.ORANGE_BED))
        {
            Material block = e.getBlock().getType();
            Location loc = e.getBlock().getLocation();
            String[] s = e.getBlock().getType().toString().split("_");
            new BukkitRunnable()
            {
                @Override
                public void run()
                {
                    if (loc.getBlock().getType() != block)
                    {
                        placeBedrock(s[0], true, Material.COAL_BLOCK);
                    }
                }
            }.runTaskLater(Objects.requireNonNull(Bukkit.getServer().getPluginManager().getPlugin("Invictools")), 1L);
        }
    }

    @EventHandler
    public void init(BedwarsGameStartedEvent e)
    {
        if (!e.getGame().getGameWorld().equals(Bukkit.getWorld("map2")))
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

        for (Player p : e.getGame().getConnectedPlayers())
        {
            teams.remove(e.getGame().getTeamOfPlayer(p).getName().toUpperCase(Locale.ROOT));
        }

        for (String s : teams)
        {
            placeBedrock(s, false, Material.COAL_BLOCK);
        }
    }

    @EventHandler
    public void reset(BedwarsGameEndingEvent e)
    {
        if (!e.getGame().getGameWorld().equals(Bukkit.getWorld("map2")))
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

        for (String s : teams)
        {
            placeBedrock(s, false, Material.CAMPFIRE);
        }
    }

    static void placeBedrock(String color, boolean strike, Material material)
    {
        int y = 89;
        Location loc;
        switch (color)
        {
            case "LIGHT":
            case "BLUE":
                loc = new Location(Bukkit.getWorld("map2"), -35, y, -107);
                break;
            case "GRAY":
                loc = new Location(Bukkit.getWorld("map2"), -106, y, -36);
                break;
            case "RED":
                loc = new Location(Bukkit.getWorld("map2"), -107, y, 36);
                break;
            case "WHITE":
                loc = new Location(Bukkit.getWorld("map2"), -36, y, 107);
                break;
            case "YELLOW":
                loc = new Location(Bukkit.getWorld("map2"), 37, y, 107);
                break;
            case "PINK":
                loc = new Location(Bukkit.getWorld("map2"), 108, y, 36);
                break;
            case "LIME":
                loc = new Location(Bukkit.getWorld("map2"), 37, y, -107);
                break;
            case "ORANGE":
                loc = new Location(Bukkit.getWorld("map2"), 108, y, -36);
                break;
            default:
                loc = new Location(Bukkit.getWorld("map2"), -34, y, -106);
        }

        if (strike)
            loc.getWorld().strikeLightningEffect(loc);

        loc.getBlock().setType(material);
        // game.getRegion().addBuiltDuringGame(loc);

        //   Location loc2 = loc.clone().add(0,1,0);
        //   loc2.getBlock().setType(Material.COAL_BLOCK);
        //  game.getRegion().addBuiltDuringGame(loc2);
    }
}

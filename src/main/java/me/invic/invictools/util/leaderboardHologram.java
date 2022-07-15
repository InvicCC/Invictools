package me.invic.invictools.util;

import me.invic.invictools.Commands;
import me.invic.invictools.cosmetics.projtrail.ProjTrailHandler;
import me.invic.invictools.gamemodifiers.WardenSpawner;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.*;

public class leaderboardHologram
{
    FileConfiguration config = Commands.Invictools.getConfig();
    Location loc = new WardenSpawner().locationFromConfig(config.getString("Leaderboard.Holo"));
    public static List<ArmorStand> Holos = new ArrayList<>();

    public void createLeaderboard()
    {
        destroyHolos();
        Location as = loc.clone().subtract(0, 1.5, 0);
        List<String> leaderboard = new ArrayList<>();
        leaderboard.add(ChatColor.WHITE + " " + ChatColor.BOLD + modifySort(me.invic.invictools.util.leaderboard.Sort) + ChatColor.AQUA + " " + ChatColor.BOLD + "Leaderboard ");
        leaderboard.addAll(me.invic.invictools.util.leaderboard.formattedLeaderboard);
        leaderboard.add(ChatColor.AQUA + " " + ChatColor.BOLD + "Click to Toggle ");
        Collections.reverse(leaderboard);
        createStand(ChatColor.WHITE + " /it leaderboard position <name> ", as.clone().add(-2, .3, 2));
        createStand(ChatColor.WHITE + " to check other players ", as.clone().add(-2, 0, 2));
        for (int i = 0; i < leaderboard.size(); i++)
        {
            createStand(leaderboard.get(i), as.add(0, .3, 0));
        }
    }

    public String modifySort(String s)
    {
        s = s.toUpperCase(Locale.ROOT);

        switch (s)
        {
            case "DESTROYEDBEDS":
                return "BED BREAK";
            case "LOSES":
                return "LOSSES";
            case "WL":
                return "WIN / LOSS";
            case "KDR":
                return "KILL / DEATH";
            case "FKDR":
                return "FINAL KILL / DEATH";
            case "FINALS":
                return "FINAL KILL";
            default:
                return s;
        }
    }

    private void createStand(String title, Location loc)
    {
        ArmorStand as = (ArmorStand) loc.getWorld().spawnEntity(loc, EntityType.ARMOR_STAND);
        as.setGravity(false);
        as.setVisible(false);
        as.setCustomName(title);
        as.setCustomNameVisible(true);
        as.setMetadata("holo", new FixedMetadataValue(Objects.requireNonNull(Bukkit.getServer().getPluginManager().getPlugin("Invictools")), true));
        Holos.add(as);
    }

    public void destroyHolos()
    {
        Holos.forEach(Entity::remove);
    }
}

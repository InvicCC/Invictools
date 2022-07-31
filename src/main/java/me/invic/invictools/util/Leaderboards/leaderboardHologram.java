package me.invic.invictools.util.Leaderboards;

import me.invic.invictools.commands.OldCommands;
import me.invic.invictools.gamemodifiers.WardenSpawner;
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
    FileConfiguration config = OldCommands.Invictools.getConfig();
    Location loc = new WardenSpawner().locationFromConfig(config.getString("Leaderboard.Holo"));
    public static List<ArmorStand> Holos = new ArrayList<>();

    public void createLeaderboard()
    {
        destroyHolos();
        Location as = loc.clone().subtract(0, 1.5, 0);
        List<String> leaderboard = new ArrayList<>();
        leaderboard.add(ChatColor.WHITE + " " + ChatColor.BOLD + modifySort(me.invic.invictools.util.Leaderboards.leaderboard.Sort) + ChatColor.AQUA + " " + ChatColor.BOLD + "Bedwars Leaderboard ");
        leaderboard.addAll(me.invic.invictools.util.Leaderboards.leaderboard.formattedLeaderboard);
        leaderboard.add(ChatColor.AQUA + " " + ChatColor.BOLD + "Click to Toggle ");
        Collections.reverse(leaderboard);
        createStand(ChatColor.WHITE + " /lb bw <name> ", as.clone().add(-2, .3, 2));
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
        as.setMetadata("holo", new FixedMetadataValue(OldCommands.Invictools, true));
        Holos.add(as);
    }

    public void destroyHolos()
    {
        Holos.forEach(Entity::remove);
    }
}

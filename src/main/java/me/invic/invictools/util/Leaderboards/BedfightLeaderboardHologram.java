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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class BedfightLeaderboardHologram
{
    FileConfiguration config = OldCommands.Invictools.getConfig();
    Location loc = new WardenSpawner().locationFromConfig(config.getString("BedfightLeaderboard.Holo"));
    public static List<ArmorStand> Holos = new ArrayList<>();

    public void createBFLeaderboard()
    {
        destroyHolos();
        Location as = loc.clone().subtract(0, 1.5, 0);
        List<String> leaderboard = new ArrayList<>();
        leaderboard.add(ChatColor.WHITE + " " + ChatColor.BOLD + modifySort(BedfightLeaderboard.Sort) + ChatColor.AQUA + " " + ChatColor.BOLD + "Bedfight Leaderboard ");
        leaderboard.addAll(BedfightLeaderboard.formattedLeaderboard);
        leaderboard.add(ChatColor.AQUA + " " + ChatColor.BOLD + "Click to Toggle ");
        Collections.reverse(leaderboard);
        createStand(ChatColor.WHITE + " /lb bf <name> ", as.clone().add(-2, .3, -2));
        createStand(ChatColor.WHITE + " to check other players ", as.clone().add(-2, 0, -2));
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
            case "BEDBREAKS":
                return "BED BREAK";
            case "WL":
                return "WIN / LOSS";
            case "KDR":
                return "KILL / DEATH";
            case "FKDR":
                return "FINAL KILL / DEATH";
            case "FINALKILLS":
                return "FINAL KILL";
            case "FINALDEATHS":
                return "FINAL DEATH";
            case "NORMALKILLS":
                return "KILL";
            case "NORMALDEATHS":
                return "DEATH";
            case "SCORE":
                return "POINTS";
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

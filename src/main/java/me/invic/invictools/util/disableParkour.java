package me.invic.invictools.util;

import me.invic.invictools.commands.Commands;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;

public class disableParkour
{
    public static boolean parkour = true;
    World world = Bukkit.getWorld("bwlobby");
    FileConfiguration fileConfiguration = Commands.Invictools.getConfig();
    List<String> fences = fileConfiguration.getStringList("fenceloc");

    public disableParkour()
    {
        if (parkour)
        {
            for (String fence : fences)
            {
                String[] coords = fence.split("-");
                world.getBlockAt(new Location(world, Integer.parseInt(coords[0]), Integer.parseInt(coords[1]), Integer.parseInt(coords[2]))).setType(Material.BEDROCK);
            }
            parkour = false;
        }
        else
        {
            for (String fence : fences)
            {
                String[] coords = fence.split("-");
                world.getBlockAt(new Location(world, Integer.parseInt(coords[0]), Integer.parseInt(coords[1]), Integer.parseInt(coords[2]))).setType(Material.AIR);
            }
            parkour = true;
        }
    }
}

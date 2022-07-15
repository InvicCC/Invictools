package me.invic.invictools.util;

import me.invic.invictools.Commands;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.screamingsandals.bedwars.api.BedwarsAPI;

import java.io.File;
import java.util.List;
import java.util.Objects;

public class WorldBorder
{
    Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("Invictools");
    public WorldBorder(int InitialSize, int reductionSize, int reductionTime, int center, World world, boolean useConfig)
    {
        FileConfiguration fileConfiguration = Bukkit.getPluginManager().getPlugin("Invictools").getConfig();
        List<String> blackListedWorlds = fileConfiguration.getStringList("blacklistedWorlds");
        if(blackListedWorlds.contains(world.getName()))
        {
            Commands.MasterPlayer.sendMessage(ChatColor.RED + "World Border cannot be activated in a blacklisted world");
            return;
        }

        FileConfiguration config = new LobbyLogic().getMapConfiguration(BedwarsAPI.getInstance().getGameOfPlayer(Commands.MasterPlayer) + ".yml");
        if(config.getString("WorldBorder") != null && useConfig)
        {
            String[] vars = config.getString("WorldBorder").split(";");
            world.getWorldBorder().setCenter(new Location(world,Double.parseDouble(vars[3]),0,Double.parseDouble(vars[3])));
            world.getWorldBorder().setSize(Double.parseDouble(vars[0]));
            world.getWorldBorder().setSize(Double.parseDouble(vars[1]), (long) Double.parseDouble(vars[2]));
        }
        else
        {
            world.getWorldBorder().setCenter(new Location(world,center,0,center));
            world.getWorldBorder().setSize(InitialSize);
            world.getWorldBorder().setSize(reductionSize, reductionTime);
        }


        new BukkitRunnable()
        {
            @Override
            public void run()
            {
                if(world.getPlayers().size() == 0)
                {
                    world.getWorldBorder().setSize(5000);
                    this.cancel();
                }
            }
        }.runTaskTimer(Objects.requireNonNull(Bukkit.getServer().getPluginManager().getPlugin("Invictools")), 20L, 20L);
    }
}

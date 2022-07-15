package me.invic.invictools.gamemodifiers;

import me.invic.invictools.Commands;
import me.invic.invictools.Invictools;
import me.invic.invictools.util.LobbyLogic;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.data.type.SculkShrieker;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.screamingsandals.bedwars.api.BedwarsAPI;

public class WardenSpawner
{
    public void setShrieker(Location loc)
    {
        if(loc.getBlock().getType().equals(Material.AIR))
        {
            loc.getBlock().setType(Material.SCULK_SHRIEKER);
            SculkShrieker sculk = (SculkShrieker) loc.getBlock().getBlockData();
            sculk.setCanSummon(true);
            loc.getBlock().setBlockData(sculk);
        }
    }

    public void setAir(Location loc)
    {
        if(loc.getBlock().getType().equals(Material.SCULK_SHRIEKER))
        {
            loc.getBlock().setType(Material.AIR);
            loc.getBlock().setBlockData(loc.getBlock().getBlockData());
        }
    }

    public void shriekerFromPlayer(Player player, boolean toggle)
    {
        final FileConfiguration pluginConfig = new LobbyLogic().getMapConfiguration(BedwarsAPI.getInstance().getGameOfPlayer(player).getName());
        for (String s:pluginConfig.getStringList("Shrieker"))
        {
            if(!toggle)
                setShrieker(locationFromConfig(s));
            else
                setAir(locationFromConfig(s));
        }
    }

    public Location locationFromConfig(String s)
    {
        String[] array = s.split(";");
        return new Location(Bukkit.getWorld(array[0]), Double.parseDouble(array[1]), Double.parseDouble(array[2]), Double.parseDouble(array[3]),Float.parseFloat(array[4]),Float.parseFloat(array[5]));
    }

    public void ShriekerFromWorld(World world, boolean toggle)
    {
        final FileConfiguration pluginConfig = new LobbyLogic().getMapConfigurationFromWorld(world.getName());
        for (String s:pluginConfig.getStringList("Shrieker"))
        {
            if(!toggle)
                setShrieker(locationFromConfig(s));
            else
                setAir(locationFromConfig(s));
        }
    }

}

package me.invic.invictools.util.physics;

import me.invic.invictools.gamemodifiers.LuckyBlocks.LuckyBlockSpawner;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;

public class grabSandstone
{
    public grabSandstone(Player player)
    {
        Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("Invictools");
        final FileConfiguration Config = plugin.getConfig();
        Location loc = new Location(player.getWorld(),Config.getDouble("twinkle."+player.getWorld().getName()+".x"),Config.getDouble("twinkle."+player.getWorld().getName()+".y"),Config.getDouble("twinkle."+player.getWorld().getName()+".z"));

        List<Block> sandstone = getNearbyBlocks(loc,24,"sandstone",false);
        for (Block block : sandstone)
        {
            new doLighting(block, player);
        }
    }

    public static List<Block> getNearbyBlocks(Location location, int radius, String blockType, boolean checktagged) {
        List<Block> blocks = new ArrayList<Block>();
        for(int x = location.getBlockX() - radius; x <= location.getBlockX() + radius; x++) {
            for(int y = location.getBlockY() - radius; y <= location.getBlockY() + radius; y++) {
                for(int z = location.getBlockZ() - radius; z <= location.getBlockZ() + radius; z++) {
                    if(location.getWorld().getBlockAt(x, y, z).getType().name().equalsIgnoreCase(blockType))
                        if(!checktagged || !taggedBlocks.contains(location.getBlock()))
                            blocks.add(location.getWorld().getBlockAt(x, y, z));
                }
            }
        }
        return blocks;
    }

    public static List<Block> taggedBlocks = new ArrayList<>();
}

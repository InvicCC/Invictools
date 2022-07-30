package cc.invic.invictools.util.physics;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.block.data.Lightable;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.Objects;
import java.util.Random;

public class doLighting
{
    public doLighting(Block sandstone, Player player)
    {
        String worldname = player.getWorld().getName();
        List<Block> nearbyLamps = grabSandstone.getNearbyBlocks(sandstone.getLocation(), 3, "redstone_lamp", false);

        Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("Invictools");
        final FileConfiguration Config = plugin.getConfig();

        Random rand = new Random();
        int delay = rand.nextInt(Config.getInt("twinkle." + worldname + ".delay"));

        new BukkitRunnable()
        {
            @Override
            public void run()
            {
                if (!player.getWorld().getName().equalsIgnoreCase(worldname))
                {
                    unlightall(nearbyLamps);
                    this.cancel();
                }

                for (Block block : nearbyLamps)
                {
                    Lightable lightable = (Lightable) block.getBlockData();
                    if (lightable.isLit())
                    {
                        lightable.setLit(false);
                        block.setBlockData(lightable);
                    }
                    else if (!lightable.isLit())
                    {
                        lightable.setLit(true);
                        block.setBlockData(lightable);
                    }
                }
            }
        }.runTaskTimer(Objects.requireNonNull(Bukkit.getServer().getPluginManager().getPlugin("Invictools")), delay + 20, delay + 20);
    }

    void unlightall(List<Block> lamps)
    {
        for (Block block : lamps)
        {
            Lightable lightable = (Lightable) block.getBlockData();
            lightable.setLit(false);
            block.setBlockData(lightable);
        }
    }
}

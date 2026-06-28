package me.invic.invictools.gamemodifiers;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Objects;

public class tnt
{
    int intervalTicks;
    int fuseTicks;

    public tnt(int interval, String worldName, int Fuse, Player player)
    {
        intervalTicks = interval * 20;
        fuseTicks = Fuse * 20;

        new BukkitRunnable()
        {
            @Override
            public void run()
            {
                if (!player.getWorld().getName().equalsIgnoreCase(worldName))
                    this.cancel();

                if (player.getWorld().getName().equalsIgnoreCase(worldName) && player.getGameMode() != GameMode.SPECTATOR)
                {
                    Location loc = player.getLocation();
                    TNTPrimed tntEntity = (TNTPrimed) loc.getWorld().spawnEntity(loc, EntityType.TNT);
                    tntEntity.setFuseTicks(fuseTicks);
                }
            }
        }.runTaskTimer(Objects.requireNonNull(Bukkit.getServer().getPluginManager().getPlugin("Invictools")), intervalTicks, intervalTicks);
    }
}
package me.invic.invictools.gamemodifiers;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Objects;

public class tnt
{
    String command;

    int intervalSeconds;
    int fuseSeconds;

    public tnt(int interval, String worldName, int Fuse, Player player)
    {
        intervalSeconds = interval * 20;
        fuseSeconds = Fuse * 10; // I know these are different but somehow they both correspond to seconds

        new BukkitRunnable()
        {
            @Override
            public void run()
            {
                if(!player.getWorld().getName().equalsIgnoreCase(worldName))
                    this.cancel();

                    Location loc = player.getLocation();
                    command = "execute at " + player.getName() + " run summon minecraft:tnt " + loc.getX() + " " + loc.getY() + " " + loc.getZ() + " {Fuse:" + fuseSeconds + "}";

                    if (player.getWorld().getName().equalsIgnoreCase(worldName) && player.getGameMode() != GameMode.SPECTATOR)
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
            }
        }.runTaskTimer(Objects.requireNonNull(Bukkit.getServer().getPluginManager().getPlugin("Invictools")), intervalSeconds, intervalSeconds);
    }
}
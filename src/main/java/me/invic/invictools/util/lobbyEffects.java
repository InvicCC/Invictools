package me.invic.invictools.util;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Locale;
import java.util.Objects;
import java.util.Random;

public class lobbyEffects
{
    public static void effectHandler(String effect, Location loc)
    {
        switch(effect.toLowerCase(Locale.ROOT))
        {
            case "boom":
                boom(loc);
                break;
            case "strike":
                strike(loc);
                break;
            default:
        }
    }

    private static void boom(Location loc)
    {
        new BukkitRunnable()
        {
            int i = 0;
            @Override
            public void run()
            {
                if(i >= 15)
                    this.cancel();

                Random rand = new Random();
                int cblocX = rand.nextInt(16)-8;
                int cblocZ = rand.nextInt(16)-8;
                Location temploc = new Location(loc.getWorld(),loc.getX()+cblocX,loc.getY(),loc.getZ()+cblocZ);
                TNTPrimed tnt = (TNTPrimed) loc.getWorld().spawnEntity(temploc, EntityType.PRIMED_TNT);
                TNTPrimed tnt2 = (TNTPrimed) loc.getWorld().spawnEntity(temploc, EntityType.PRIMED_TNT);
                tnt.setFuseTicks(5);
                tnt2.setFuseTicks(10);

                i++;
            }
        }.runTaskTimer(Objects.requireNonNull(Bukkit.getServer().getPluginManager().getPlugin("Invictools")),  0, 1);
    }

    private static void strike(Location loc)
    {
        loc.getWorld().strikeLightningEffect(loc);
        /*
        new BukkitRunnable()
        {
            int i = 0;
            @Override
            public void run()
            {
                if(i >= 2)
                    this.cancel();

                Random rand = new Random();
                int cblocX = rand.nextInt(16)-8;
                int cblocZ = rand.nextInt(16)-8;
                Location temploc = new Location(loc.getWorld(),loc.getX()+cblocX,loc.getY(),loc.getZ()+cblocZ);
                temploc.getWorld().strikeLightningEffect(temploc);

                i++;
            }
        }.runTaskTimer(Objects.requireNonNull(Bukkit.getServer().getPluginManager().getPlugin("Invictools")),  0, 1);

         */
    }
}

package me.invic.invictools.cosmetics.bedbreaks;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;

import java.util.Timer;
import java.util.TimerTask;

public class RankedBedBreak
{
    public RankedBedBreak(Location loc)
    {
        final int[] i = {0};
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask()
        {
            public void run()
            {
                if (i[0] == 0)
                {
                    loc.getWorld().spawnParticle(Particle.HEART, loc, 150, 4, 2, 4);
                    loc.getWorld().spawnParticle(Particle.VILLAGER_HAPPY, loc, 150, 5, 4, 5);
                    ;
                    loc.getWorld().spawnParticle(Particle.NOTE, loc, 250, 7, 3, 7);
                    ;
                    loc.getWorld().playSound(loc, Sound.BLOCK_CONDUIT_DEACTIVATE, 2.0F, 1.0F);
                }
                else if (i[0] == 1 || i[0] == 2)
                {
                    loc.getWorld().spawnParticle(Particle.HEART, loc, 100, 5, 3, 3);
                    loc.getWorld().spawnParticle(Particle.VILLAGER_HAPPY, loc, 100, 6, 4, 6);
                    ;
                    loc.getWorld().spawnParticle(Particle.NOTE, loc, 150, 7, 4, 5);
                    ;
                    loc.getWorld().playSound(loc, Sound.BLOCK_CONDUIT_DEACTIVATE, 2.0F, 1.0F);
                }
                else timer.cancel();
                i[0]++;
            }
        }, 10, 750);
    }
}

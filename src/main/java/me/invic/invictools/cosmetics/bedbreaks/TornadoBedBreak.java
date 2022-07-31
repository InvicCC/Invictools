package me.invic.invictools.cosmetics.bedbreaks;

import org.bukkit.Particle;
import org.bukkit.Sound;

import org.bukkit.Location;

import java.util.Timer;
import java.util.TimerTask;

public class TornadoBedBreak
{
    public TornadoBedBreak(Location loc)
    {
        Timer timer = new Timer();

        int max_height = 20;
        double max_radius = 13;
        int lines = 2;
        double height_increasement = 0.5;
        double radius_increasement = max_radius / max_height;

        int volume = 2;
        int pitch = 2;

        loc.getWorld().playSound(loc, Sound.ENTITY_WARDEN_SONIC_BOOM, volume, pitch); // scuffed way to make it loud
        /*
        loc.getWorld().playSound(loc,Sound.ENTITY_ENDER_EYE_DEATH,volume,pitch);
        loc.getWorld().playSound(loc,Sound.ENTITY_ENDER_EYE_DEATH,volume,pitch);
        loc.getWorld().playSound(loc,Sound.ENTITY_ENDER_EYE_DEATH,volume,pitch);
        loc.getWorld().playSound(loc,Sound.ENTITY_ENDER_EYE_DEATH,volume,pitch);
        loc.getWorld().playSound(loc,Sound.ENTITY_ENDER_EYE_DEATH,volume,pitch);
        loc.getWorld().playSound(loc,Sound.ENTITY_ENDER_EYE_DEATH,volume,pitch);
        loc.getWorld().playSound(loc,Sound.ENTITY_ENDER_EYE_DEATH,volume,pitch);
        loc.getWorld().playSound(loc,Sound.ENTITY_ENDER_EYE_DEATH,volume,pitch);

         */

        timer.scheduleAtFixedRate(new TimerTask()
        {
            int angle = 0;
            int cancel = 0;

            public void run()
            {
                for (int l = 0; l < lines; l++)
                {
                    for (double y = 0; y < max_height; y += height_increasement)
                    {
                        double radius = y * radius_increasement;
                        double x = Math.cos(Math.toRadians(360 / lines * l + y * 25 - angle)) * radius;
                        double z = Math.sin(Math.toRadians(360 / lines * l + y * 25 - angle)) * radius;
                        loc.getWorld().spawnParticle(Particle.CRIT_MAGIC, loc.clone().add(x, y, z), 5, .05, .05, .05);
                        loc.getWorld().spawnParticle(Particle.CRIT, loc.clone().add(x, y, z), 5, .05, .05, .05);
                    }
                    angle++;
                }
                //     if(angle==45)//18
                // timer.cancel();

                cancel++;
                if (cancel == 18 * 2 * 2) // 2 times old length
                    timer.cancel();
            }
        }, 0, 111);
    }
}

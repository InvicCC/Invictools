package cc.invic.invictools.cosmetics.bedbreaks;

import cc.invic.invictools.cosmetics.projtrail.ProjTrailHandler;
import cc.invic.invictools.impl.Commands;
import cc.invic.invictools.util.Laser;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Guardian;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.Random;

public class GuardianBedBreak
{
    public GuardianBedBreak(Location loc)
    {
        ArmorStand as2 = (ArmorStand) loc.getWorld().spawnEntity(loc.clone().add(0, 6, 0), EntityType.ARMOR_STAND);
        as2.setVisible(false);
        as2.setGravity(false);

        createLaser(loc.clone().add(10, 0, -10), as2, loc, 40);
        createLaser(loc.clone().add(10, 0, 10), as2, loc, 40);
        createLaser(loc.clone().add(-10, 0, -10), as2, loc, 40);
        createLaser(loc.clone().add(-10, 0, 10), as2, loc, 40);
        loc.getWorld().playSound(loc, Sound.ENTITY_GUARDIAN_AMBIENT, 1, 3);
        new BukkitRunnable()
        {
            int i = 0;

            @Override
            public void run()
            {
                if (i == 20)
                    this.cancel();

                loc.getWorld().playSound(loc, Sound.ENTITY_GUARDIAN_AMBIENT_LAND, 1, i ^ 2 + 2);

                i++;
            }
        }.runTaskTimer(Commands.Invictools, 0L, 1L);
    }

    private void createLaser(Location start, ArmorStand end, Location bed, int ticks)
    {
        ArmorStand as1 = (ArmorStand) start.getWorld().spawnEntity(start, EntityType.ARMOR_STAND);
        as1.setVisible(false);
        as1.setGravity(false);

        try
        {
            Laser laser = new Laser.GuardianLaser(bed, start, (ticks / 30), 50);
            laser.start(Commands.Invictools);
            laser.moveEnd(end.getLocation(), 20, null);
        }
        catch (ReflectiveOperationException e)
        {
            e.printStackTrace();
        }

        new BukkitRunnable()
        {
            final Random rand = new Random();

            @Override
            public void run()
            {
                end.getWorld().spawnParticle(Particle.EXPLOSION_LARGE, end.getLocation(), 1);
                end.getWorld().playSound(end.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 1, 1);
                end.getWorld().playSound(end.getLocation(), Sound.ENTITY_ELDER_GUARDIAN_DEATH, 1, 1);
                Guardian g = (Guardian) end.getWorld().spawnEntity(end.getLocation(), EntityType.GUARDIAN);
                Guardian g2 = (Guardian) end.getWorld().spawnEntity(end.getLocation(), EntityType.GUARDIAN);
                new ProjTrailHandler().bubble(g);
                new ProjTrailHandler().bubble(g2);
                //     g.setAware(false);
                //   g2.setAware(false);
                g.setCollidable(false);
                g2.setCollidable(false);
                g.setVelocity(new Vector(2 * (rand.nextDouble() - .5), 1.2, 2 * (rand.nextDouble() - .5)));
                g2.setVelocity(new Vector(2 * (rand.nextDouble() - .5), 1.2, 2 * (rand.nextDouble() - .5)));
                as1.remove();
                end.remove();
                new BukkitRunnable()
                {
                    @Override
                    public void run()
                    {

                        g2.damage(g2.getHealth() * 2);
                        g.damage(g.getHealth() * 2);

                        if (!g.isDead())
                            g.remove();

                        if (!g2.isDead())
                            g2.remove();
                    }
                }.runTaskLater(Commands.Invictools, 35L);

            }
        }.runTaskLater(Commands.Invictools, 22L);
    }
}
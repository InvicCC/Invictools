package me.invic.invictools.cosmetics.bedbreaks;

import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.Objects;

public class EndermanBedBreak
{
    public EndermanBedBreak(Location loc, String bed, boolean real)
    {
      //  ArmorStand as1 = (ArmorStand) loc.getWorld().spawnEntity(loc, EntityType.ARMOR_STAND);
      //  as1.setVisible(false);
        Enderman e = (Enderman) loc.getWorld().spawnEntity(loc, EntityType.ENDERMAN);
        e.getWorld().spawnParticle(Particle.PORTAL,e.getLocation(),30,.5,4,.5);
        e.getWorld().playSound(e.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT,5,1);
      //  e.setAI(false);
      //  as1.setPassenger(e);
        if(real)
            e.setCarriedBlock(Bukkit.createBlockData(Material.getMaterial(bed)));
        else
            e.setCarriedBlock(Bukkit.createBlockData(Material.RED_BED));
        e.setVelocity(new Vector(0,1.2,0));

        new BukkitRunnable()
        {
            @Override
            public void run()
            {
             //   e.getWorld().spawnParticle(Particle.PORTAL,e.getLocation(),1,0,0,0);
                e.getWorld().playSound(e.getLocation(), Sound.ENTITY_ENDERMAN_AMBIENT,3,1);

                if(e.isDead())
                    this.cancel();
            }
        }.runTaskTimer(Objects.requireNonNull(Bukkit.getServer().getPluginManager().getPlugin("Invictools")), 0L, 3L);

        BukkitRunnable runnable = new BukkitRunnable()
        {
            @Override
            public void run()
            {
                e.getWorld().spawnParticle(Particle.PORTAL,e.getLocation(),30,.5,4,.5);
                e.getWorld().playSound(e.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT,5,1);
                e.remove();
            }
        };
        runnable.runTaskLater(Objects.requireNonNull(Bukkit.getServer().getPluginManager().getPlugin("Invictools")), 20);
    }
}

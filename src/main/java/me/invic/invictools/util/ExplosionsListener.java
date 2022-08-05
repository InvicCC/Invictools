package me.invic.invictools.util;

import me.invic.invictools.commands.OldCommands;
import me.invic.invictools.gamemodifiers.perGameJumpingHandler;
import me.invic.invictools.gamemodifiers.perGameJumpingListener;
import me.invic.invictools.items.createItems;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.screamingsandals.bedwars.api.BedwarsAPI;

public class ExplosionsListener implements Listener
{
    @EventHandler
    public void boom(EntityDamageByEntityEvent e)
    {
        perGameJumpingHandler jumpInfo = perGameJumpingListener.jumpInfo.getOrDefault(BedwarsAPI.getInstance().getGameOfPlayer((Player) e.getEntity()),new perGameJumpingHandler());
        double xzmultiplier = jumpInfo.getX();
        double ymultiplier = jumpInfo.getY();
        double range = jumpInfo.getRange();
        boolean op = jumpInfo.getOP();

        //Fireball Types modification. not implemented yet so all should be x1
        for (String meta: createItems.getFireballTypes().keySet())
        {
            if(e.getDamager().hasMetadata(meta))
            {
                xzmultiplier *= createItems.getFireballTypes().get(meta);
                ymultiplier *= createItems.getFireballTypes().get(meta);
                if(createItems.getFireballTypes().get(meta) >1)
                    op = true;
            }
        }

        if (e.getCause().equals(EntityDamageEvent.DamageCause.ENTITY_EXPLOSION) && (e.getDamager().getName().equalsIgnoreCase("fireball") || e.getDamager().getName().equalsIgnoreCase("primed tnt")))
        {
            String cause = e.getDamager().getName();
            e.setDamage(2);

            // Distance Calculations
            Location Affected = e.getEntity().getLocation();
            Location ExplosionSource = e.getDamager().getLocation();
            final double[] x = {Affected.getX() - ExplosionSource.getX()};
            final double[] z = {Affected.getZ() - ExplosionSource.getZ()};
            double distance = Affected.distance(ExplosionSource);
            if (distance <= range)
            {
                double multiplier = ((range / distance) + 1) * xzmultiplier;
                if (multiplier < 1)
                    multiplier = 1;
                double Ysubtracter = 1 + ((range / distance) * ymultiplier);
                if (Ysubtracter < 1)
                    Ysubtracter = 1;

                if (!op)
                    if (Ysubtracter > 3.0)
                        Ysubtracter = 3.0;

                double finalMultiplier = multiplier;
                double finalYsubtracter1 = Ysubtracter;

                //Damage Calculations
                if ((e.getDamager().hasMetadata("sender") || cause.equalsIgnoreCase("primed tnt")) && e.getEntity().getType().equals(EntityType.PLAYER))
                {
                    Player p = (Player) e.getEntity();

                    if(!p.isInvulnerable() && !p.getWorld().getName().equals("bwlobby"))
                    {
                        double damage = (3.0 - distance);

                        if (damage < 0)
                            damage = .5;

                        if(damage > p.getHealth())
                            damage = p.getHealth();


                        p.setHealth(p.getHealth() - damage);
                    }
                }

//                BukkitRunnable runnable = new BukkitRunnable()
//                {
//                    @Override
//                    public void run()
//                    {

                        // Minimum Catches
                        if (x[0] < -.4)
                            x[0] = finalMultiplier - x[0];
                        else if (x[0] > .4)
                            x[0] = (finalMultiplier + x[0]) * -1;

                        if (z[0] < -.4)
                            z[0] = finalMultiplier - z[0];
                        else if (z[0] > .4)
                            z[0] = (finalMultiplier + z[0]) * -1;

                        x[0] = x[0] * -1;
                        z[0] = z[0] * -1;

                        //Velocity
                        if (e.getEntity().getWorld().getName().equals("bwlobby"))
                            e.getEntity().setVelocity(new Vector(x[0] / 3, finalYsubtracter1 / 1.5, z[0] / 3));
                        else if(!cause.equalsIgnoreCase("fireball"))
                            e.getEntity().setVelocity(new Vector(x[0] / 5, finalYsubtracter1 / 2, z[0] / 5));
                        else
                            e.getEntity().setVelocity(new Vector(x[0] / 4, finalYsubtracter1 / 2, z[0] / 4));
//                    }
//                };
//                runnable.runTaskLater(OldCommands.Invictools, 1);
            }
        }
    }
}

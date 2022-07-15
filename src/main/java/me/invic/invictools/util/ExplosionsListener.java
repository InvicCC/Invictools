package me.invic.invictools.util;

import com.comphenix.protocol.events.PacketListener;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.io.File;
import java.util.List;
import java.util.Objects;

public class ExplosionsListener implements Listener
{
    static Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("Invictools");
    final static FileConfiguration pluginConfig = plugin.getConfig();
    public static double range = pluginConfig.getDouble("jump.fireball.range"); // 4
    public static double xzmultiplier = pluginConfig.getDouble("jump.fireball.xzmultiplier"); // .4
    public static double ymultiplier = pluginConfig.getDouble("jump.fireball.ymultiplier"); // .6
    public static boolean op = pluginConfig.getBoolean("jump.fireball.op"); // false

    @EventHandler
    public void boom(EntityDamageByEntityEvent e)
    {
        if (e.getCause().equals(EntityDamageEvent.DamageCause.ENTITY_EXPLOSION) && e.getDamager().getName().equalsIgnoreCase("fireball"))
        {
            e.setDamage(2);
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

                if (!op) // allows fireballs to become really op but otherwise fixes close range height issues
                    if (Ysubtracter > 2.5)
                        Ysubtracter = 2.5;

                double finalMultiplier = multiplier;
                double finalYsubtracter1 = Ysubtracter;

                if (e.getDamager().hasMetadata("sender") && e.getEntity().getType().equals(EntityType.PLAYER))
                {
                    Player p = (Player) e.getEntity();

                    double damage = (4.0 - distance);
                    if (damage < 0)
                        damage = .5;

                    p.setHealth(p.getHealth() - damage);
                    Bukkit.getPluginManager().callEvent(new EntityDamageEvent(p, EntityDamageEvent.DamageCause.ENTITY_EXPLOSION, damage));
                }

                BukkitRunnable runnable = new BukkitRunnable()
                {
                    @Override
                    public void run()
                    {
                        // System.out.println("orioginal x " + (x[0]));
                        // System.out.println("roginal z " + (z[0]));

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

                        if (e.getEntity().getWorld().getName().equals("bwlobby"))
                            e.getEntity().setVelocity(new Vector(x[0] / 3, finalYsubtracter1 / 1.5, z[0] / 3));
                        else
                            e.getEntity().setVelocity(new Vector(x[0] / 5, finalYsubtracter1 / 2, z[0] / 5));
                    }
                };
                runnable.runTaskLater(Objects.requireNonNull(Bukkit.getServer().getPluginManager().getPlugin("Invictools")), 1);
            }
        }
        else if (e.getCause().equals(EntityDamageEvent.DamageCause.ENTITY_EXPLOSION) && e.getDamager().getName().equalsIgnoreCase("primed tnt"))
        {
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

                if (!op) // allows fireballs to become really op but otherwise fixes close range height issues
                    if (Ysubtracter > 3.5)
                        Ysubtracter = 3.5;

                double finalMultiplier = multiplier;
                double finalYsubtracter1 = Ysubtracter;
                BukkitRunnable runnable = new BukkitRunnable()
                {
                    @Override
                    public void run()
                    {
                        //      System.out.println("orioginal x " + (x[0]));
                        //     System.out.println("roginal z " + (z[0]));

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

                        if (e.getEntity().getWorld().getName().equals("bwlobby"))
                            e.getEntity().setVelocity(new Vector(x[0] / 3, finalYsubtracter1 / 2, z[0] / 3));
                        else
                            e.getEntity().setVelocity(new Vector(x[0] / 5, finalYsubtracter1 / 2, z[0] / 5));
                    }
                };
                runnable.runTaskLater(Objects.requireNonNull(Bukkit.getServer().getPluginManager().getPlugin("Invictools")), 1);
            }
        }
    }
}

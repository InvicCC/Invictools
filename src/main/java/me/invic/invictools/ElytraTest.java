package me.invic.invictools;

import me.invic.invictools.commands.OldCommands;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPotionEffectEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.HashMap;

public class ElytraTest implements Listener
{
    static HashMap<Player, Double> speedHolder = new HashMap<>();
    public static double ylevel = 15;
    public static double forgiveness = 20;
    public static double start = 250;
    public static int logic = 1;

    @EventHandler
    public void movespeed(PlayerMoveEvent e)
    {
        if (!e.getPlayer().getWorld().getName().equalsIgnoreCase("civservermap"))
            return;

        if(logic == 1)
        {
            if (e.getPlayer().isGliding())
            {
                //Double speed = 20*(new Location(e.getPlayer().getWorld(),e.getPlayer().getLocation().getX(),0,e.getPlayer().getLocation().getZ())).distance(speedHolder.getOrDefault(e.getPlayer(), new Location(e.getPlayer().getWorld(),0,0,0)));
                Double speed = Math.abs(speedHolder.getOrDefault(e.getPlayer(), 0.0) - (e.getPlayer().getLocation().getY() * 20));
                //speedHolder.put(e.getPlayer(), new Location(e.getPlayer().getWorld(),e.getPlayer().getLocation().getX(),0,e.getPlayer().getLocation().getZ()));
                if (e.getPlayer().getLocation().getY() >= start - forgiveness)
                {
                    e.getPlayer().sendMessage(speed + " Safety Net");
                } else
                {
                    e.getPlayer().sendMessage(String.valueOf(speed));
                }

                if (speed <= ylevel && e.getPlayer().getLocation().getY() < start - forgiveness)
                {
                    // ItemStack item = e.getPlayer().getInventory().getItem(38);
                    new BukkitRunnable()
                    {
                        @Override
                        public void run()
                        {
                            e.getPlayer().getInventory().setItem(38, new ItemStack(Material.AIR));
                            // e.getPlayer().getInventory().setItem(0,item);
                            e.getPlayer().addPotionEffect(PotionEffectType.SLOW_FALLING.createEffect(10000, 0));
                        }
                    }.runTaskLater(OldCommands.Invictools, 45L);
                }
                speedHolder.put(e.getPlayer(), e.getPlayer().getLocation().getY() * 20);
            }
        }

        if (!e.getPlayer().getLocation().subtract(0, 1, 0).getBlock().getType().equals(Material.AIR))
        {
            e.getPlayer().removePotionEffect(PotionEffectType.SLOW_FALLING);
        }
    }

    @EventHandler
    public void effectgained(EntityPotionEffectEvent e)
    {
        if(e.getNewEffect() == null)
            return;

        if(e.getNewEffect().getType().equals(PotionEffectType.SLOW_FALLING) && e.getEntity() instanceof Player online)
        {
            new BukkitRunnable()
            {
                @Override
                public void run()
                {
                    Vector velocity = online.getVelocity().clone();
                    velocity.setX(online.getLocation().getDirection().getX() * 0.6);
                    velocity.setZ(online.getLocation().getDirection().getZ() * 0.6);

                    double verticalVelocity = -0.3;

                    if (online.getLocation().getPitch() > 10)
                        verticalVelocity = -((online.getLocation().getPitch() / 10) / 10);

                    velocity.setY(verticalVelocity);
                    online.setVelocity(velocity);

                    if(!online.hasPotionEffect(PotionEffectType.SLOW_FALLING))
                        this.cancel();

                }
            }.runTaskTimer(OldCommands.Invictools, 0L, 1L);
        }
    }
}

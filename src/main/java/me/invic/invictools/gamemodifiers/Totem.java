package me.invic.invictools.gamemodifiers;

import me.invic.invictools.Commands;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Objects;

public class Totem implements Listener
{
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerDamage(EntityDamageEvent e)
    {
        if (e.getEntity() instanceof Player)
        {
            Player player = (Player) e.getEntity();

            if (Commands.InfiniteTotems.get(player) == null)
                return;

            if (e.isCancelled())
                return;

            if (Commands.InfiniteTotems.get(player))
            {
                double Health = player.getHealth();
                double Damage = e.getFinalDamage();

                if (Damage >= Health)
                {
                    if (player.getInventory().getItem(40) != null)
                    {
                        ItemStack offhand = player.getInventory().getItem(40);
                        BukkitRunnable runnable = new BukkitRunnable()
                        {
                            @Override
                            public void run()
                            {
                                player.getInventory().setItem(40, offhand);
                            }
                        };
                        runnable.runTaskLater(Objects.requireNonNull(Bukkit.getServer().getPluginManager().getPlugin("Invictools")), 1L);
                    }
                    else
                    {
                        BukkitRunnable runnable = new BukkitRunnable()
                        {
                            @Override
                            public void run()
                            {
                                player.updateInventory();
                            }
                        };
                        runnable.runTaskLater(Objects.requireNonNull(Bukkit.getServer().getPluginManager().getPlugin("Invictools")), 1L);
                    }
                    player.getInventory().setItem(40, new ItemStack(Material.TOTEM_OF_UNDYING));
                }
            }
        }
    }
}

package me.invic.invictools.gamemodifiers.PotionEffects;

import me.invic.invictools.commands.Commands;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.screamingsandals.bedwars.api.events.BedwarsPlayerKilledEvent;

import java.util.Map;
import java.util.Objects;

public class KillEffectListener implements Listener
{
    @EventHandler
    public void deathEvent(BedwarsPlayerKilledEvent e)
    {
        Player killer = e.getKiller();
        for (int i = 0; i < Commands.killEffects.size(); i++)
        {
            if (Commands.killEffects.get(killer) != null)
            {
                String potionName = Commands.killEffects.get(killer);
                String[] split = potionName.split("-");
                killer.addPotionEffect(new PotionEffect(PotionEffectType.getByName(split[0]), Integer.parseInt(split[2]), Integer.parseInt(split[1]) - 1, false, true));
            }
        }

        if (Commands.killItems.containsKey(killer))
        {
            killer.playSound(killer.getLocation(), Sound.ENTITY_CHICKEN_EGG, 1, 1);
            final Map<Integer, ItemStack> map = killer.getInventory().addItem(Commands.killItems.get(killer));
            for (final ItemStack item : map.values())
            {
                killer.getWorld().dropItemNaturally(killer.getLocation(), item);
            }
        }

        if (Commands.deathItems.containsKey(e.getPlayer()))
        {
            BukkitRunnable runnable = new BukkitRunnable()
            {
                @Override
                public void run()
                {
                    e.getPlayer().getInventory().addItem(Commands.deathItems.get(e.getPlayer()));
                }
            };
            runnable.runTaskLater(Objects.requireNonNull(Bukkit.getServer().getPluginManager().getPlugin("Invictools")), 20 * 6);
        }
    }
}

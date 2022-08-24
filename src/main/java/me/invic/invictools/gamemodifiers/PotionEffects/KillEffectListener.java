package me.invic.invictools.gamemodifiers.PotionEffects;

import me.invic.invictools.commands.OldCommands;
import me.invic.invictools.gamemodifiers.LuckyBlocks.Blocks.goodBlocks;
import me.invic.invictools.items.ModBow;
import me.invic.invictools.util.disableStats;
import org.bukkit.Bukkit;
import org.bukkit.Material;
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
import java.util.Random;

public class KillEffectListener implements Listener
{
    @EventHandler
    public void deathEvent(BedwarsPlayerKilledEvent e)
    {
        int delay = 5;
        boolean isBedfight = false;
        if(disableStats.getGameType(e.getGame()).equalsIgnoreCase("bedfight"))
        {
            delay = 3;
            isBedfight = true;
        }

        Player killer = e.getKiller();
        for (int i = 0; i < OldCommands.killEffects.size(); i++)
        {
            if (OldCommands.killEffects.get(killer) != null)
            {
                String potionName = OldCommands.killEffects.get(killer);
                String[] split = potionName.split("-");
                killer.addPotionEffect(new PotionEffect(PotionEffectType.getByName(split[0]), Integer.parseInt(split[2]), Integer.parseInt(split[1]) - 1, false, true));
            }
        }

        if (OldCommands.killItems.containsKey(killer))
        {
            killer.playSound(killer.getLocation(), Sound.ENTITY_CHICKEN_EGG, 1, 1);
            final Map<Integer, ItemStack> map = killer.getInventory().addItem(OldCommands.killItems.get(killer));
            for (final ItemStack item : map.values())
            {
                killer.getWorld().dropItemNaturally(killer.getLocation(), item);
                if(isBedfight && (item.getType().equals(Material.BOW) || item.getType().equals(Material.CROSSBOW)))
                {
                    ItemStack arrow = new ItemStack(Material.ARROW);
                    arrow.setAmount(new Random().nextInt(2) + 3);
                    killer.getWorld().dropItemNaturally(killer.getLocation(), arrow);
                }
            }
        }

        if (OldCommands.deathItems.containsKey(e.getPlayer()))
        {
            boolean finalIsBedfight = isBedfight;
            ItemStack arrow = new ItemStack(Material.ARROW);
            arrow.setAmount(new Random().nextInt(4) + 2);
            if(isBedfight && new ModBow().searchInventory(e.getKiller(),"BOW").getType().equals(Material.BOW))
            {
                killer.getWorld().dropItemNaturally(killer.getLocation(), arrow);
            }
            BukkitRunnable runnable = new BukkitRunnable()
            {
                @Override
                public void run()
                {
                    e.getPlayer().getInventory().addItem(OldCommands.deathItems.get(e.getPlayer()));
                    if(finalIsBedfight && (OldCommands.deathItems.get(e.getPlayer()).getType().equals(Material.BOW) || OldCommands.deathItems.get(e.getPlayer()).getType().equals(Material.CROSSBOW)))
                    {
                        if(e.getPlayer().getInventory().getItem(9) == null)
                            e.getPlayer().getInventory().setItem(9,arrow);
                        else
                            e.getPlayer().getInventory().addItem(arrow);
                    }
                }
            };
            runnable.runTaskLater(Objects.requireNonNull(Bukkit.getServer().getPluginManager().getPlugin("Invictools")), 20 * delay+1);
        }
    }
}

package me.invic.invictools.gamemodifiers.PotionEffects;

import me.invic.invictools.commands.OldCommands;
import me.invic.invictools.gamemodifiers.LuckyBlocks.Blocks.goodBlocks;
import me.invic.invictools.gamemodifiers.gainItems;
import me.invic.invictools.items.ModBow;
import me.invic.invictools.util.disableStats;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.screamingsandals.bedwars.api.events.BedwarsGameEndEvent;
import org.screamingsandals.bedwars.api.events.BedwarsPlayerKilledEvent;
import org.screamingsandals.bedwars.api.events.BedwarsPlayerLeaveEvent;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.stream.Collectors;

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
        if(gainItems.activeGains.containsKey(killer))
        {
            if(gainItems.activeGains.get(killer).checkGame(e.getGame()) && !gainItems.activeGains.get(killer).getKillEffects().isEmpty())
            {
                for (String potionName:gainItems.activeGains.get(killer).getKillEffects())
                {
                    String[] split = potionName.split("-");
                    killer.addPotionEffect(new PotionEffect(PotionEffectType.getByName(split[0]), Integer.parseInt(split[2]), Integer.parseInt(split[1]) - 1, false, true));
                }
            }
            else if(!gainItems.activeGains.get(killer).checkGame(e.getGame()))
                gainItems.activeGains.get(killer).destroy(killer);

            if(gainItems.activeGains.get(killer).checkGame(e.getGame()) && !gainItems.activeGains.get(killer).getKillItems().isEmpty())
            {
                for (ItemStack item:gainItems.activeGains.get(killer).getKillItems())
                {
                    killer.playSound(killer.getLocation(), Sound.ENTITY_CHICKEN_EGG, 1, 1);
                    final Map<Integer, ItemStack> map = killer.getInventory().addItem(item);
                    killer.getWorld().dropItemNaturally(killer.getLocation(), map.get(0));
                    if(isBedfight)
                        arrowCheck(item,killer);
                }
            }
            else if(!gainItems.activeGains.get(killer).checkGame(e.getGame()))
                gainItems.activeGains.get(killer).destroy(killer);
        }

        boolean finalIsBedfight = isBedfight;
        BukkitRunnable runnable = new BukkitRunnable()
        {
            @Override
            public void run()
            {
                if (gainItems.activeGains.containsKey(e.getPlayer()))
                {
                    if (gainItems.activeGains.get(e.getPlayer()).checkGame(e.getGame()) && !gainItems.activeGains.get(e.getPlayer()).getDeathItems().isEmpty())
                    {
                        for (ItemStack item : gainItems.activeGains.get(e.getPlayer()).getDeathItems())
                        {
                            final Map<Integer, ItemStack> map = e.getPlayer().getInventory().addItem(item);
                            e.getPlayer().getWorld().dropItemNaturally(e.getPlayer().getLocation(), map.get(0));
                            if (finalIsBedfight)
                                arrowCheck(item, e.getPlayer());
                        }
                    }
                    else if(!gainItems.activeGains.get(killer).checkGame(e.getGame()))
                        gainItems.activeGains.get(killer).destroy(killer);
                }
            }
        };
        runnable.runTaskLater(OldCommands.Invictools, 20 * delay+1);
    }

    void arrowCheck(ItemStack item,Player p)
    {
        if((item.getType().equals(Material.BOW) || item.getType().equals(Material.CROSSBOW)))
        {
            ItemStack arrow = new ItemStack(Material.ARROW);
            arrow.setAmount(new Random().nextInt(4) + 2);
            p.getWorld().dropItemNaturally(p.getLocation(), arrow);
        }
    }

    void destruct(Player p)
    {
        if(gainItems.activeGains.containsKey(p))
            gainItems.activeGains.get(p).destroy(p);
    }

    @EventHandler
    public void bwend(BedwarsGameEndEvent e)
    {
        for (Player p:e.getGame().getConnectedPlayers())
        {
            destruct(p);
        }
    }

    @EventHandler
    public void bwleave(BedwarsPlayerLeaveEvent e)
    {
        destruct(e.getPlayer());
    }

    @EventHandler
    public void quit(PlayerQuitEvent e)
    {
        destruct(e.getPlayer());
    }
}

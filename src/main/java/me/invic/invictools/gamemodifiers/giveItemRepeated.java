package me.invic.invictools.gamemodifiers;

import me.invic.invictools.items.createItems;
import me.invic.invictools.gamemodifiers.LuckyBlocks.createLuckyBlocks;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Map;
import java.util.Objects;

public class giveItemRepeated // specific item handled in commands.java
{
    public giveItemRepeated(int delay, ItemStack item, Player player, String type) // delay in seconds, type random / normal, give paper for random item, give sea lantern for random lucky block
    {
        String worldname = player.getWorld().getName();

        new BukkitRunnable()
        {
            @Override
            public void run()
            {
                if (!repeatedCancel)
                {
                    // System.out.println(player.getWorld().getName() + " "+worldname);
                    if (player.getWorld().getName().equalsIgnoreCase(worldname))
                    {
                        if (type.equalsIgnoreCase("normal"))
                        {
                            final Map<Integer, ItemStack> map = player.getInventory().addItem(item);
                            player.playSound(player.getLocation(), Sound.ENTITY_CHICKEN_EGG, 1, 1);
                            for (final ItemStack item : map.values())
                            {
                                player.getWorld().dropItemNaturally(player.getLocation(), item);
                            }
                        }
                        else if (type.equalsIgnoreCase("random"))
                        {
                            if (item.getType().equals(Material.SEA_LANTERN))
                            {
                                final ItemStack random = new createLuckyBlocks().getRandomBlockWeighted();
                                random.setAmount(item.getAmount());
                                final Map<Integer, ItemStack> map = player.getInventory().addItem(random);
                                player.playSound(player.getLocation(), Sound.ENTITY_CHICKEN_EGG, 1, 1);
                                for (final ItemStack item : map.values())
                                {
                                    player.getWorld().dropItemNaturally(player.getLocation(), item);
                                }
                            }
                            else if (item.getType().equals(Material.PAPER))
                            {
                                final ItemStack random = new createItems().getRandomItem();
                                random.setAmount(item.getAmount());
                                final Map<Integer, ItemStack> map = player.getInventory().addItem(random);
                                player.playSound(player.getLocation(), Sound.ENTITY_CHICKEN_EGG, 1, 1);
                                for (final ItemStack item : map.values())
                                {
                                    player.getWorld().dropItemNaturally(player.getLocation(), item);
                                }
                            }
                        }
                    }
                    else
                        this.cancel();
                }
                else
                    this.cancel();
            }
        }.runTaskTimer(Objects.requireNonNull(Bukkit.getServer().getPluginManager().getPlugin("Invictools")), delay * 20L, delay * 20L);
    }

    public static boolean repeatedCancel;
}
package me.invic.invictools.gamemodifiers.LuckyBlocks;

import me.invic.invictools.items.LootBoc;
import me.invic.invictools.gamemodifiers.LuckyBlocks.Blocks.OPBlocks;
import me.invic.invictools.gamemodifiers.LuckyBlocks.Blocks.badBlocks;
import me.invic.invictools.gamemodifiers.LuckyBlocks.Blocks.goodBlocks;
import me.invic.invictools.util.deathListener;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.screamingsandals.bedwars.api.BedwarsAPI;
import org.screamingsandals.bedwars.api.game.ItemSpawnerType;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class luckyBlockBreakDetection implements Listener // can add blocks to lucky block arrays manually elsewhere too
{
    BedwarsAPI api = BedwarsAPI.getInstance();

    @EventHandler
    public void BlockBreakDetection(BlockBreakEvent e)
    {
        if (e.getBlock().getType() == Material.SEA_LANTERN)
        {
            Random rand = new Random();
            int percent = rand.nextInt(101);
            if (badBlocks.contains(e.getBlock())) // bad blocks
            {
                if (percent > 0 && percent <= 2)
                    new OPBlocks(e.getBlock().getLocation(), e.getPlayer());
                else if (percent > 6 && percent <= 21)
                    new goodBlocks(e.getBlock().getLocation(), e.getPlayer());
                else
                    new badBlocks(e.getBlock().getLocation(), e.getPlayer());
            }
            else if (goodBlocks.contains(e.getBlock())) // normal blocks
            {
                if (percent > 75 && percent <= 81)
                    new OPBlocks(e.getBlock().getLocation(), e.getPlayer());
                else if (percent > 0 && percent <= 75)
                    new goodBlocks(e.getBlock().getLocation(), e.getPlayer());
                else
                    new badBlocks(e.getBlock().getLocation(), e.getPlayer());
            }
            else if (OPBlocks.contains(e.getBlock())) // op blocks
            {
                if (percent > 0 && percent <= 80)
                    new OPBlocks(e.getBlock().getLocation(), e.getPlayer());
                else if (percent > 80 && percent <= 95)
                    new goodBlocks(e.getBlock().getLocation(), e.getPlayer());
                else
                    new badBlocks(e.getBlock().getLocation(), e.getPlayer());
            }

            if (OPBlocks.contains(e.getBlock()) || goodBlocks.contains(e.getBlock()) || badBlocks.contains(e.getBlock()))
            {
                e.setCancelled(true);
                BukkitRunnable runnable = new BukkitRunnable()
                {
                    @Override
                    public void run()
                    {
                        e.getBlock().setType(Material.AIR);
                    }
                };
                runnable.runTaskLater(Objects.requireNonNull(Bukkit.getServer().getPluginManager().getPlugin("Invictools")), 1L);
            }
        }
        else if (deathListener.coalBlocks.contains(e.getBlock()))
        {
            e.setCancelled(true);
            Location loc = e.getBlock().getLocation();
            Random rand = new Random();
            int amount = rand.nextInt(5);
            if (rand.nextInt(10) == 0)
            {
                String command = "execute at " + e.getPlayer().getName() + " run summon minecraft:tnt " + loc.getX() + " " + loc.getY() + " " + loc.getZ() + " {Fuse:" + 1 + "}";
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
            }
            else
            {
                ItemSpawnerType type;
                switch (rand.nextInt(4))
                {
                    case 0:
                        type = api.getItemSpawnerTypeByName("emerald");
                        break;
                    case 1:
                        type = api.getItemSpawnerTypeByName("diamond");
                        break;
                    case 2:
                        type = api.getItemSpawnerTypeByName("gold");
                        break;
                    case 3:
                        type = api.getItemSpawnerTypeByName("iron");
                        break;
                    default:
                        type = api.getItemSpawnerTypeByName("iron");
                }

                e.getPlayer().playSound(loc, Sound.ENTITY_CHICKEN_EGG, 1, 1);

                ItemStack stack = type.getStack();
                stack.setAmount(amount);

                if (stack.getType() != Material.AIR)
                {
                    //  System.out.println(stack.getType());
                    loc.getWorld().dropItemNaturally(loc, stack);
                }
                else
                {
                    String command = "execute at " + e.getPlayer().getName() + " run summon minecraft:tnt " + loc.getX() + " " + loc.getY() + " " + loc.getZ() + " {Fuse:" + 1 + "}";
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
                }
            }

            BukkitRunnable runnable = new BukkitRunnable()
            {
                @Override
                public void run()
                {
                    e.getBlock().setType(Material.AIR);
                    deathListener.coalBlocks.remove(e.getBlock());
                }
            };
            runnable.runTaskLater(Objects.requireNonNull(Bukkit.getServer().getPluginManager().getPlugin("Invictools")), 1L);
        }
    }

    @EventHandler
    public void BlockPlaceDetection(BlockPlaceEvent e)
    {
        ItemStack block = e.getItemInHand();
        if (e.getBlock().getType() == Material.SEA_LANTERN)
        {
            List<String> lore = Objects.requireNonNull(block.getItemMeta()).getLore();

            assert lore != null;
            if (lore.get(0).equalsIgnoreCase("§7Unlucky I"))
                badBlocks.add(e.getBlock());
            else if (lore.get(0).equalsIgnoreCase("§7lucky I"))
                goodBlocks.add(e.getBlock());
            else if (lore.get(0).equalsIgnoreCase("§7lucky II"))
                OPBlocks.add(e.getBlock());
        }
        else if (e.getBlock().getType().equals(Material.CHEST))
        {
            List<String> lore = Objects.requireNonNull(block.getItemMeta()).getLore();
            if (lore.get(0).equalsIgnoreCase("§7Contains loot!"))
            {
                LootBoc.fillBoc(e.getBlock().getLocation());
            }
        }
    }

    public static List<Block> badBlocks = new ArrayList<>();

    public static List<Block> goodBlocks = new ArrayList<>();

    public static List<Block> OPBlocks = new ArrayList<>();
}

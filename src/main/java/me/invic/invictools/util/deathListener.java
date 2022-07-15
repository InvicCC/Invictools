package me.invic.invictools.util;

import me.invic.invictools.Commands;
import me.invic.invictools.Invictools;
import me.invic.invictools.gamemodifiers.Haunt;
import me.invic.invictools.gamemodifiers.LuckyBlocks.createLuckyBlocks;
import me.invic.invictools.gamemodifiers.giveItemRepeated;
import me.invic.invictools.items.ItemListener;
import me.invic.invictools.util.OlympusFires;
import me.invic.invictools.util.npc.BlazeNpc;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.screamingsandals.bedwars.api.events.BedwarsGameEndEvent;
import org.screamingsandals.bedwars.api.events.BedwarsGameEndingEvent;
import org.screamingsandals.bedwars.api.events.BedwarsPlayerKilledEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class deathListener implements Listener
{

    @EventHandler
    public void deathEvent(BedwarsPlayerKilledEvent e)
    {
        if (Commands.LuckyBlocksEnabled)
        {
            Player player = e.getPlayer();
            Location loc = player.getLocation();

            ItemStack lb = new createLuckyBlocks().getRandomBlockWeighted();
            player.playSound(loc, Sound.ENTITY_CHICKEN_EGG, 1, 1);
            player.getWorld().dropItemNaturally(loc, lb);
        }

        if (Commands.Hauntable.containsKey(e.getPlayer()))
        {
            BukkitRunnable runnable = new BukkitRunnable()
            {
                @Override
                public void run()
                {
                    if (e.getPlayer().getGameMode().equals(GameMode.SPECTATOR))
                        new Haunt(e.getPlayer(), Commands.HauntConfig);
                }
            };
            runnable.runTaskLater(Objects.requireNonNull(Bukkit.getServer().getPluginManager().getPlugin("Invictools")), 10L * 20L);
        }
    }

    @EventHandler
    public void coalgrabber(EntityChangeBlockEvent e)
    {
        BukkitRunnable runnable = new BukkitRunnable()
        {
            @Override
            public void run()
            {
                if (e.getBlock().getType().equals(Material.DIRT))
                {
                    coalBlocks.add(e.getBlock());
                }

                if (e.getBlock().getType().equals(Material.ANVIL))
                {
                    addedNormalBlocks.add(e.getBlock());
                }
            }
        };
        runnable.runTaskLater(Objects.requireNonNull(Bukkit.getServer().getPluginManager().getPlugin("Invictools")), 10L);
    }

    @EventHandler
    public void destroyAddedBlocks(BedwarsGameEndingEvent e)
    {
        clearEverything(e.getGame().getGameWorld());
    }

    public static void clearEverything(World world)
    {
        for (Block block : coalBlocks)
        {
            block.setType(Material.AIR);
        }
        coalBlocks.clear();

        for (Block block : addedNormalBlocks)
        {
            block.setType(Material.AIR);
        }
        addedNormalBlocks.clear();

        ItemListener.Falling.clear();
        Commands.killEffects.clear();
        OlympusFires.resetFires();
        //   SculkFires.resetFires(); hanlded in class
        Commands.killItems.clear();
        Commands.Hauntable.clear();
        Commands.ProximityElytra.clear();
        Commands.noShop.clear();
        giveItemRepeated.repeatedCancel = false;

        for (Entity e : world.getEntitiesByClass(Warden.class))
        {
            e.remove();
        }
        for (Entity e : world.getEntitiesByClass(Allay.class))
        {
            e.remove();
        }
        for (Entity e : world.getEntitiesByClass(ArmorStand.class))
        {
            // if(!e.hasMetadata("holo"))
            e.remove();
        }
        for (Entity e : world.getEntitiesByClass(EnderDragon.class))
        {
            e.remove();
        }
        for (Entity e : world.getEntitiesByClass(SkeletonHorse.class))
        {
            e.remove();
        }
        for (Entity e : Bukkit.getWorld("bwlobby").getEntitiesByClass(Illusioner.class))
        {
            e.remove();
        }
        for (Entity e : Bukkit.getWorld("bwlobby").getEntitiesByClass(ArmorStand.class))
        {
            e.remove();
        }


        new BlazeNpc().spawnNPC("npc", true);
        new BlazeNpc().spawnNPC("npc2", false);
        new leaderboard().loadLeaderboard("Star");
        new leaderboardHologram().createLeaderboard();

        BukkitRunnable runnable = new BukkitRunnable()
        {
            @Override
            public void run()
            {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "nte reload");
            }
        };
        runnable.runTaskLater(Objects.requireNonNull(Bukkit.getServer().getPluginManager().getPlugin("Invictools")), 1L);
    }

    @EventHandler
    public void CancelFallDamage(EntityDamageEvent e)
    {
        if (e.getCause() == EntityDamageEvent.DamageCause.FALL)
            if (ItemListener.Falling.contains(e.getEntity()))
                e.setCancelled(true);
    }

    public static List<Block> coalBlocks = new ArrayList<>();
    public static List<Block> addedNormalBlocks = new ArrayList<>();
}

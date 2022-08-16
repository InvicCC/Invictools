package me.invic.invictools.cosmetics.bedbreaks;

import me.invic.invictools.commands.OldCommands;
import me.invic.invictools.econ.givePoints;
import me.invic.invictools.gamemodifiers.LuckyBlocks.createLuckyBlocks;
import me.invic.invictools.gamemodifiers.gamemodeData;
import me.invic.invictools.util.disableStats;
import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.screamingsandals.bedwars.api.BedwarsAPI;
import org.screamingsandals.bedwars.api.events.BedwarsTargetBlockDestroyedEvent;

import java.io.File;
import java.util.Objects;

public class BedBreaks implements Listener
{
    @EventHandler
    public void onDestroy(BedwarsTargetBlockDestroyedEvent e)
    {
        String bed = e.getTeam().getTargetBlock().getBlock().getType().toString();

        Player player = e.getPlayer();
        Location loc = e.getTeam().getTargetBlock();

        if ((!OldCommands.StatsTrack || !disableStats.shouldTrack(e.getPlayer()) || disableStats.singleDisable.contains(e.getGame())) && !disableStats.getGameType(e.getGame()).equalsIgnoreCase("bedfight"))
            new givePoints(e.getPlayer(),"BedwarsBedBreak");

        BukkitRunnable runnable = new BukkitRunnable()
        {
            @Override
            public void run()
            {
                if (loc.getBlock().getType() == Material.AIR)
                {
                    if (!new gamemodeData().getLuckyBlockMode(e.getGame()).equals("none"))
                    {
                        new BukkitRunnable()
                        {
                            int i = 0;

                            @Override
                            public void run()
                            {
                                ItemStack lb = new createLuckyBlocks().getRandomBlockWeighted();
                                player.playSound(loc, Sound.ENTITY_CHICKEN_EGG, 1, 1);
                                player.getWorld().dropItemNaturally(loc, lb);

                                i++;

                                if (i >= 2)
                                    this.cancel();
                            }
                        }.runTaskTimer(Objects.requireNonNull(Bukkit.getServer().getPluginManager().getPlugin("Invictools")), 0, 1L);
                    }
                    handle(loc, player, bed, true, "ingame");
                }
                this.cancel();
            }
        };
        runnable.runTaskLater(Objects.requireNonNull(Bukkit.getServer().getPluginManager().getPlugin("Invictools")), 1);
    }

    public void handle(Location loc, Player player, String bed, boolean real, String override)
    {
        Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("Invictools");
        File Folder = new File(plugin.getDataFolder(), "PlayerData");
        File pFile = new File(Folder, player.getUniqueId() + ".yml");
        final FileConfiguration playerData = YamlConfiguration.loadConfiguration(pFile);
        String bedBreak = playerData.getString("BedBreak","Fireworks");
        if (!override.equals("ingame"))
            bedBreak = override;

        switch (bedBreak)
        {
            case "Lightning":
                new LightningBedBreak(loc);
                break;
            case "Fireworks":
                new FireworkBedBreak(loc, player);
                break;
            case "Ranked":
                new RankedBedBreak(loc);
                break;
            case "Tornado":
                new TornadoBedBreak(loc);
                break;
            case "Enderman":
                new EndermanBedBreak(loc, bed, real);
                break;
            case "Holo":
                new HoloBedBreak(loc, player, bed, real);
                break;
            case "Guardian":
                new GuardianBedBreak(loc);
                break;
            default:
                new FireworkBedBreak(loc, player);
        }
    }

}


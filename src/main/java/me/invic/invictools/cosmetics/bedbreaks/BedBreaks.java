package me.invic.invictools.cosmetics.bedbreaks;

import me.invic.invictools.Commands;
import me.invic.invictools.gamemodifiers.LuckyBlocks.createLuckyBlocks;
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

import java.io.File;
import java.util.Objects;

public class BedBreaks implements Listener
{
    @EventHandler
    public void onDestroy(BlockBreakEvent e)
    {
        String bed = e.getBlock().getType().toString();

        if(e.getBlock().getType().equals(Material.BLUE_BED)
                || e.getBlock().getType().equals(Material.GRAY_BED)
                || e.getBlock().getType().equals(Material.RED_BED)
                || e.getBlock().getType().equals(Material.LIGHT_BLUE_BED)
                || e.getBlock().getType().equals(Material.WHITE_BED)
                || e.getBlock().getType().equals(Material.YELLOW_BED)
                || e.getBlock().getType().equals(Material.PINK_BED)
                || e.getBlock().getType().equals(Material.LIME_BED)
                || e.getBlock().getType().equals(Material.ORANGE_BED))
        {
            Player player = e.getPlayer();
            Location loc = e.getBlock().getLocation();

            BukkitRunnable runnable = new BukkitRunnable()
            {
                @Override
                public void run()
                {
                    if (loc.getBlock().getType() == Material.AIR)
                    {
                        if (Commands.LuckyBlocksEnabled)
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
                        handle(loc,player,bed,true,"ingame");
                    }
                    this.cancel();
                }
            };
            runnable.runTaskLater(Objects.requireNonNull(Bukkit.getServer().getPluginManager().getPlugin("Invictools")),  1);
        }
    }

    public void handle(Location loc, Player player,String bed,boolean real,String override)
    {
                    Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("Invictools");
                    //  File pFile = new File(plugin.getDataFolder().getPath() + "\\PlayerData" + File.separator + player.getUniqueId() + ".yml");
                    File Folder = new File(plugin.getDataFolder(), "PlayerData");
                    File pFile = new File(Folder, player.getUniqueId() + ".yml");
                    final FileConfiguration playerData = YamlConfiguration.loadConfiguration(pFile);
                    String bedBreak = playerData.getString("BedBreak");
                    if(!override.equals("ingame"))
                        bedBreak = override;

                    switch (bedBreak)
                    {
                        case "Lightning":
                            new LightningBedBreak(loc);
                            break;
                        case "Fireworks":
                            new FireworkBedBreak(loc,player);
                            break;
                        case "Ranked":
                            new RankedBedBreak(loc);
                            break;
                        case "Tornado":
                            new TornadoBedBreak(loc);
                            break;
                        case "Enderman":
                            new EndermanBedBreak(loc,bed,real);
                            break;
                        case "Holo":
                            new HoloBedBreak(loc, player, bed, real);
                            break;
                        case "Guardian":
                            new GuardianBedBreak(loc);
                            break;
                        default:
                            new FireworkBedBreak(loc,player);
                    }
    }

}


package me.invic.invictools.gamemodifiers;

import me.invic.invictools.util.disableStats;
import org.bukkit.*;
import org.bukkit.block.BlockFace;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.screamingsandals.bedwars.api.BedwarsAPI;

import java.util.Objects;


public class AlwaysBridge
{
    BedwarsAPI api = BedwarsAPI.getInstance();
    Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("Invictools");
    final FileConfiguration cfg = plugin.getConfig();
    int InventoryCheckSize = cfg.getInt("ScaffoldInventoryCheckSize");

    public AlwaysBridge(Player player)
    {
        World GameWorld = player.getWorld();
        new BukkitRunnable()
        {
            @Override
            public void run()
            {
                if (player.getWorld() != GameWorld)
                    this.cancel();

                for (int size = 0; size < InventoryCheckSize; size++)
                {
                    if (player.getInventory().getItem(size) != null)
                    {
                        ItemStack item = player.getInventory().getItem(size);
                        if (isItemWool(item))
                        {
                            if (player.getGameMode() != GameMode.SURVIVAL)
                                return;

                            if (player.isSneaking())
                                return;

                            if (api.isPlayerPlayingAnyGame(player))
                            {
                                if (!api.getGameOfPlayer(player).isLocationInArena(player.getLocation().getBlock().getRelative(BlockFace.DOWN).getLocation()))
                                    return;
                            }

                            if (player.getLocation().getBlock().getRelative(BlockFace.DOWN).getType().equals(Material.AIR))
                            {
                                player.getLocation().getBlock().getRelative(BlockFace.DOWN).setType(player.getInventory().getItem(size).getType());
                                if(!disableStats.getGameType(api.getGameOfPlayer(player)).equalsIgnoreCase("bedfight"))
                                    player.getInventory().getItem(size).setAmount(player.getInventory().getItem(size).getAmount() - 1);
                                if (api.isPlayerPlayingAnyGame(player))
                                    api.getGameOfPlayer(player).getRegion().addBuiltDuringGame(player.getLocation().getBlock().getRelative(BlockFace.DOWN).getLocation());
                            }

                            if (isMaterialWool(player.getLocation().getBlock().getRelative(BlockFace.DOWN).getType()))
                            {
                                Location loc2 = SecondBlockLocation(player, player.getLocation().getBlock().getRelative(BlockFace.DOWN).getLocation());
                                if (loc2.getBlock().getType().equals(Material.AIR))
                                {
                                    if(player.getInventory().getItem(size) != null)
                                        loc2.getBlock().setType(player.getInventory().getItem(size).getType());
                                    if(!disableStats.getGameType(api.getGameOfPlayer(player)).equalsIgnoreCase("bedfight"))
                                        player.getInventory().getItem(size).setAmount(player.getInventory().getItem(size).getAmount() - 1);
                                    BukkitRunnable runnable = new BukkitRunnable()
                                    {
                                        @Override
                                        public void run()
                                        {
                                            if (api.isPlayerPlayingAnyGame(player))
                                                api.getGameOfPlayer(player).getRegion().addBuiltDuringGame(loc2);
                                        }
                                    };
                                    runnable.runTaskLater(Objects.requireNonNull(Bukkit.getServer().getPluginManager().getPlugin("Invictools")), 1);
                                }
                            }
                        }
                    }
                }
            }
        }.runTaskTimer(Objects.requireNonNull(Bukkit.getServer().getPluginManager().getPlugin("Invictools")), 1, 1); // repeat every second so you can have a floating timer with named invis armor stand and just add variable that checks if its at delay to do item spawn
    }


    public boolean isMaterialWool(Material item)
    {
        return item.equals(Material.BLUE_WOOL) ||
                item.equals(Material.GRAY_WOOL) ||
                item.equals(Material.RED_WOOL) ||
                item.equals(Material.WHITE_WOOL) ||
                item.equals(Material.YELLOW_WOOL) ||
                item.equals(Material.PINK_WOOL) ||
                item.equals(Material.ORANGE_WOOL) ||
                item.equals(Material.LIME_WOOL) ||
                item.equals(Material.LIGHT_GRAY_WOOL) ||
                item.equals(Material.LIGHT_BLUE_WOOL);
    }

    public boolean isItemWool(ItemStack item)
    {
        return isMaterialWool(item.getType());
    }

    Location SecondBlockLocation(Player player, Location loca)
    {
        BlockFace direction = yawToFace(player.getLocation().getYaw(), false);
        switch (direction)
        {
            case NORTH:
                loca.add(0, 0, -1);
                break;
            case EAST:
                loca.add(+1, 0, 0);
                break;
            case SOUTH:
                loca.add(0, 0, +1);
                break;
            case WEST:
                loca.add(-1, 0, 0);
                break;
        }
        return loca;
    }

    public BlockFace yawToFace(float yaw, boolean useSubCardinalDirections)
    {
        if (useSubCardinalDirections)
            return radial[Math.round(yaw / 45f) & 0x7].getOppositeFace();

        return axis[Math.round(yaw / 90f) & 0x3].getOppositeFace();
    }

    private static final BlockFace[] axis = {BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST};
    private static final BlockFace[] radial = {BlockFace.NORTH, BlockFace.NORTH_EAST, BlockFace.EAST, BlockFace.SOUTH_EAST, BlockFace.SOUTH, BlockFace.SOUTH_WEST, BlockFace.WEST, BlockFace.NORTH_WEST};
}

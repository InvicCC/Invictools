package me.invic.invictools.gamemodes;

import me.invic.invictools.Commands;
import me.invic.invictools.items.ModBow;
import me.invic.invictools.util.LobbyLogic;
import me.invic.invictools.util.disableStats;
import me.invic.invictools.util.fixes.LobbyInventoryFix;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.screamingsandals.bedwars.api.BedwarsAPI;
import org.screamingsandals.bedwars.api.RunningTeam;
import org.screamingsandals.bedwars.api.events.BedwarsGameStartEvent;
import org.screamingsandals.bedwars.api.events.BedwarsPlayerKilledEvent;
import org.screamingsandals.bedwars.api.events.BedwarsTargetBlockDestroyedEvent;
import org.screamingsandals.bedwars.api.game.Game;
import org.screamingsandals.bedwars.api.game.GameStatus;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class bedfight implements Listener //map file optional bedfight.layers, optional bedfight.loadout. bedfioght folder holds loadout.yml
{
    String bedfight = "bedfight";
    static String loadout = Commands.Invictools.getConfig().getString("defaultBedfightLoadout", "default");
    BedwarsAPI api = BedwarsAPI.getInstance();

    @EventHandler
    public void init(BedwarsGameStartEvent e) // creates defence
    {
        if (!disableStats.getGameType(e.getGame()).equalsIgnoreCase(bedfight))
            return;

        System.out.println(loadout);
        //      if(new LobbyLogic().getMapConfiguration(e.getGame().getName()).getString("Bedfight.loadout") != null)
        //       loadout = new LobbyLogic().getMapConfiguration(e.getGame().getName()).getString(loadout);

        new BukkitRunnable()
        {
            @Override
            public void run()
            {
                for (Player p : e.getGame().getConnectedPlayers())
                {
                    loadBedfightInventory(loadout, p, false);
                }
            }
        }.runTaskLater(Commands.Invictools, 20L);

        String[] layers = new LobbyLogic().getMapConfiguration(e.getGame().getName()).getString("Bedfight.layers", "ENDSTONE;PLANKS;WOOL").split(";");
        for (int i = 0; i < layers.length; i++)
        {
            for (RunningTeam team : e.getGame().getRunningTeams())
            {
                buildDefence(team.getTargetBlock(), layers[i], i, team.getColor().name(), e.getGame().getName());
            }
        }

        for (Player p : e.getGame().getConnectedPlayers())
        {
            p.sendTitle(ChatColor.translateAlternateColorCodes('&', "&bSTARTING IN 10 SECONDS"), ChatColor.translateAlternateColorCodes('&', "&r&fOrganize your inventory!"), 40, 5 * 20, 30);
        }

        new BukkitRunnable()
        {
            @Override
            public void run()
            {
                for (Player p : e.getGame().getConnectedPlayers())
                {
                    saveBedfightInventory(loadout, p, false);
                }
            }
        }.runTaskLater(Commands.Invictools, 20 * 10);
    }

    @EventHandler
    public void killed(BedwarsPlayerKilledEvent e) // final kill, wins,  kill, death, loss stats. give items after respawn
    {
        if (!disableStats.getGameType(e.getGame()).equalsIgnoreCase(bedfight))
            return;

        new BukkitRunnable()
        {
            @Override
            public void run()
            {
                loadBedfightInventory(loadout, e.getPlayer(), false);
            }
        }.runTaskLater(Commands.Invictools, 20 * 4 + 5);
    }

    @EventHandler
    public void bedBreak(BedwarsTargetBlockDestroyedEvent e) //bedbreak stats
    {
        if (!disableStats.getGameType(e.getGame()).equalsIgnoreCase(bedfight))
            return;
    }

    public void buildDefence(Location loc, String layer, int spacing, String color, String game) // spacing is for iterator, delays loop for aesthetic and spaces next layer
    {
        if (layer.equalsIgnoreCase("WOOL")
                || layer.equalsIgnoreCase("STAINED_GLASS")
                || layer.equalsIgnoreCase("STAINED_GLASS_PANES")
                || layer.equalsIgnoreCase("CONCRETE")
                || layer.equalsIgnoreCase("TERRACOTTA")
                || layer.equalsIgnoreCase("CONCRETE_POWDER"))
            layer = color + "_" + layer;

        Block block = loc.getBlock();
        List<BlockFace> d = new ArrayList<>();
        d.add(BlockFace.EAST); // +
        d.add(BlockFace.WEST); // -
        d.add(BlockFace.NORTH); // -
        d.add(BlockFace.SOUTH); // +
        d.add(BlockFace.UP);
        d.remove(findBedFace(loc));
        //    Location loc2 = findBed(loc.clone());
        String finalLayer = layer;
        new BukkitRunnable()
        {
            @Override
            public void run() // 4 9 15  (*2)
            {
                for (BlockFace face : d) // concurrent modification exception here
                {
                    Location cardinal = block.getRelative(face).getLocation().add(directionAddition(face).multiply(spacing));
                    if (cardinal.getBlock().getType().equals(Material.AIR))
                    {
                        if (api.getGameByName(game).getStatus().equals(GameStatus.RUNNING))
                        {
                            cardinal.getBlock().setType(Material.valueOf(finalLayer));
                            if (face.getOppositeFace().equals(findBedFace(loc)) && spacing > 0) // true if opposite other half of bed
                            {
                                for (int i = 0; i < spacing; i++) // for every gap
                                {
                                    List<BlockFace> directions = new ArrayList<>(d); // block face list of left and right from opposite bed face
                                    directions.remove(face);
                                    directions.remove(BlockFace.UP);
                                    Location temp = cardinal.clone().subtract(face.getOppositeFace().getDirection()); // subtracts 1 towards other bed half
                                    cardinal.clone().subtract(temp.clone().subtract(directions.get(0).getDirection().multiply(spacing))).getBlock().setType(Material.valueOf(finalLayer));
                                    cardinal.clone().subtract(temp.clone().subtract(directions.get(1).getDirection().multiply(spacing))).getBlock().setType(Material.valueOf(finalLayer));
                                    new ModBow().addLater(cardinal.clone().subtract(temp.clone().subtract(directions.get(0).getDirection().multiply(spacing))).getBlock(), api.getGameByName(game));
                                    new ModBow().addLater(cardinal.clone().subtract(temp.clone().subtract(directions.get(1).getDirection().multiply(spacing))).getBlock(), api.getGameByName(game));
                                    // clones temp and grabs location one to the right and one to the left of temp
                                }
                            }
                            new ModBow().addLater(cardinal.getBlock(), api.getGameByName(game));
                        }
                        block.getWorld().playSound(block.getLocation(), Sound.BLOCK_AMETHYST_BLOCK_PLACE, 3, 1);
                    }
                }
            }
        }.runTaskLater(Commands.Invictools, spacing * 20L);
    }

    private Vector directionAddition(BlockFace face)
    {
        if (face.equals(BlockFace.EAST))
            return new Vector(1, 0, 0);
        else if (face.equals(BlockFace.WEST))
            return new Vector(-1, 0, 0);
        else if (face.equals(BlockFace.NORTH))
            return new Vector(0, 0, -1);
        else if (face.equals(BlockFace.SOUTH))
            return new Vector(0, 0, 1);
        else if (face.equals(BlockFace.UP))
            return new Vector(0, 1, 0);
        else return new Vector(0, 5, 0);
    }

    private Location findBed(Location loc)
    {
        Block block = loc.getBlock();
        if (isAnyBed(block.getRelative(BlockFace.EAST)))
            return block.getRelative(BlockFace.EAST).getLocation();
        else if (isAnyBed(block.getRelative(BlockFace.WEST)))
            return block.getRelative(BlockFace.WEST).getLocation();
        else if (isAnyBed(block.getRelative(BlockFace.NORTH)))
            return block.getRelative(BlockFace.NORTH).getLocation();
        else if (isAnyBed(block.getRelative(BlockFace.SOUTH)))
            return block.getRelative(BlockFace.SOUTH).getLocation();
        else return null;
    }

    private BlockFace findBedFace(Location loc)
    {
        Block block = loc.getBlock();
        if (isAnyBed(block.getRelative(BlockFace.EAST)))
            return BlockFace.EAST;
        else if (isAnyBed(block.getRelative(BlockFace.WEST)))
            return BlockFace.WEST;
        else if (isAnyBed(block.getRelative(BlockFace.NORTH)))
            return BlockFace.NORTH;
        else if (isAnyBed(block.getRelative(BlockFace.SOUTH)))
            return BlockFace.SOUTH;
        else return null;
    }

    public boolean isAnyBlock(ItemStack item, String type) // wont work for any items with underscores past coloration
    {
        String[] s = item.getType().toString().split("_");

        if (s.length >= 2)
            if (s[1].equalsIgnoreCase(type.toUpperCase(Locale.ROOT)))
                return true;

        if (s.length >= 3)
            if (s[2].equalsIgnoreCase(type.toUpperCase(Locale.ROOT)))// handles LIGHT_ colors
                return true;

        return false;
    }


    public boolean isAnyBed(Block block)
    {
        String[] s = block.getType().toString().split("_");

        if (s.length == 2)
            if (s[1].equalsIgnoreCase("BED"))
                return true;

        if (s.length == 3)
            if (s[2].equalsIgnoreCase("BED"))// handles LIGHT_ colors
                return true;

        return false;
    }

    public void swapLoadout(String s)
    {
        loadout = s;
    }

    public void loadBedfightInventory(String loadout, Player p, boolean forceReset)
    {
        int items = 0;
        int indendedItems = 0;
        File Folder = new File(Commands.Invictools.getDataFolder(), "PlayerData");
        File pFile = new File(Folder, p.getUniqueId() + ".yml");
        FileConfiguration balls = YamlConfiguration.loadConfiguration(pFile);

        File bffolder = new File(Commands.Invictools.getDataFolder(), "Bedfight");
        File bfFile = new File(bffolder, loadout + ".yml");
        FileConfiguration bf = YamlConfiguration.loadConfiguration(bfFile);

        String pass = balls.getString(loadout, "pass");

        System.out.println("cleared");
        //  new LobbyInventoryFix().clearMainInventory(p);
        for (int i = 0; i <= 35; i++)
        {
            ItemStack item = p.getInventory().getItem(i);
            if (item != null)
                item.setType(Material.AIR);
        }

        if (!pass.equalsIgnoreCase("pass") && !forceReset)
        {
            System.out.println("giving saved");
            @SuppressWarnings("unchecked")
            List<ItemStack> inventoryList = (List<ItemStack>) balls.get(loadout);
            for (int a = 0; a <= 10; a++)
            {
                int i;
                if (a != 10)
                    i = a;
                else
                    i = 40;

                ItemStack item = inventoryList.get(a);
                if (item.getType() != Material.STRUCTURE_VOID)
                {
                    items++;
                    if (api.isPlayerPlayingAnyGame(p))
                    {
                        if (item.getType().equals(Material.WHITE_WOOL))
                        {
                            item.setType(Material.valueOf(api.getGameOfPlayer(p).getTeamOfPlayer(p).getColor().toString() + "_WOOL"));
                            p.getInventory().setItem(i, item);
                        }
                        else
                            p.getInventory().setItem(i, item);
                    }
                    else
                        p.getInventory().setItem(i, item);
                }
            }
        }

        if (pass.equalsIgnoreCase("pass"))
        {
            @SuppressWarnings("unchecked")
            List<ItemStack> uneditedLoadout = (List<ItemStack>) bf.get(loadout);
            for (int a = 0; a <= 10; a++)
            {
                int i;
                if (a != 10)
                    i = a;
                else
                    i = 40;

                ItemStack item = uneditedLoadout.get(a);
                if (item.getType() != Material.STRUCTURE_VOID)
                {
                    indendedItems++;
                    if (pass.equalsIgnoreCase("pass") || forceReset)
                    {
                        if (api.isPlayerPlayingAnyGame(p))
                        {
                            if (item.getType().equals(Material.WHITE_WOOL))
                            {
                                item.setType(Material.valueOf(api.getGameOfPlayer(p).getTeamOfPlayer(p).getColor().toString() + "_WOOL"));
                                p.getInventory().setItem(i, item);
                            }
                            else
                                p.getInventory().setItem(i, item);
                        }
                        else
                            p.getInventory().setItem(i, item);
                    }
                }
            }
        }
/*
        if(items != indendedItems && !forceReset && !pass.equalsIgnoreCase("pass"))
        {
            new LobbyInventoryFix().clearMainInventory(p);
            System.out.println("giving default");
            loadBedfightInventory(loadout, p, true);
        }
 */
        destroyWood(p);
    }

    private void destroyWood(Player p)
    {
        if (p.getInventory().getItem(8) != null)
        {
            if (p.getInventory().getItem(8).getType().equals(Material.WOODEN_AXE))
                p.getInventory().setItem(8, new ItemStack(Material.AIR));
        }

        if (p.getInventory().getItem(7) != null)
        {
            if (p.getInventory().getItem(7).getType().equals(Material.WOODEN_PICKAXE))
                p.getInventory().setItem(7, new ItemStack(Material.AIR));
        }
    }

    public void saveBedfightInventory(String loadout, Player p, boolean bedfight)
    {
        File Folder = new File(Commands.Invictools.getDataFolder(), "PlayerData");
        File pFile = new File(Folder, p.getUniqueId() + ".yml");

        File bffolder = new File(Commands.Invictools.getDataFolder(), "Bedfight");
        File bfFile = new File(bffolder, loadout + ".yml");

        FileConfiguration balls;
        if (bedfight)
            balls = YamlConfiguration.loadConfiguration(bfFile);
        else
            balls = YamlConfiguration.loadConfiguration(pFile);

        List<ItemStack> inventoryList = new ArrayList<>();
        for (int a = 0; a <= 10; a++)
        {
            int i;
            if (a != 10)
                i = a;
            else
                i = 40;

            ItemStack item = p.getInventory().getItem(i);
            if (item != null)
            {
                if (isAnyBlock(item, "WOOL"))
                {
                    item.setType(Material.WHITE_WOOL);
                    inventoryList.add(item);
                }
                else
                    inventoryList.add(item);
            }
            else
            {
                ItemStack voidItem = new ItemStack(Material.STRUCTURE_VOID);
                ItemMeta meta = voidItem.getItemMeta();
                meta.setDisplayName(String.valueOf(i));
                voidItem.setItemMeta(meta);
                inventoryList.add(voidItem);
            }
        }
        balls.set(loadout, inventoryList);

        try
        {
            if (!bedfight)
                balls.save(pFile);
            else
                balls.save(bfFile);
        }
        catch (IOException ioException)
        {
            ioException.printStackTrace();
        }
    }
}

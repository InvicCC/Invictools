package cc.invic.invictools.gamemodes;

import cc.invic.invictools.impl.Commands;
import cc.invic.invictools.items.ModBow;
import cc.invic.invictools.util.LobbyLogic;
import cc.invic.invictools.util.cageHandler;
import cc.invic.invictools.util.disableStats;
import com.viaversion.viaversion.api.Via;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.screamingsandals.bedwars.api.BedwarsAPI;
import org.screamingsandals.bedwars.api.RunningTeam;
import org.screamingsandals.bedwars.api.events.BedwarsGameStartEvent;
import org.screamingsandals.bedwars.api.events.BedwarsPlayerKilledEvent;
import org.screamingsandals.bedwars.api.game.Game;
import org.screamingsandals.bedwars.api.game.GameStatus;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class bedfight implements Listener //map file optional bedfight.layers, optional bedfight.loadout. bedfioght folder holds loadout.yml
{
    String bedfight = "bedfight";
    static String loadout = Commands.Invictools.getConfig().getString("defaultBedfightLoadout", "default");
    BedwarsAPI api = BedwarsAPI.getInstance();
    HashMap<Player,String> spawnPoints = new HashMap<>();
    static HashMap<Game,String> customLoadout = new HashMap<>(); // for gamemode selector

    @EventHandler
    public void init(BedwarsGameStartEvent e) // creates defence
    {
        if (!disableStats.getGameType(e.getGame()).equalsIgnoreCase(bedfight))
            return;

        new BukkitRunnable() //  1 tick delay to try to fix no team select not giving items or building defence
        {
            @Override
            public void run()
            {
                if(customLoadout.get(e.getGame()) == null)
                    loadout = new LobbyLogic().getMapConfiguration(e.getGame().getName()).getString("Bedfight.loadout","default");
                else
                {
                    loadout = customLoadout.get(e.getGame());
                    customLoadout.remove(e.getGame());
                }

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
                }.runTaskLater(Commands.Invictools, 30L);

                String[] layers = new LobbyLogic().getMapConfiguration(e.getGame().getName()).getString("Bedfight.layers", "END_STONE;MANGROVE_PLANKS;STAINED_GLASS").split(";");
                for (int i = 0; i < layers.length; i++)
                {
                    for (RunningTeam team : e.getGame().getRunningTeams())
                    {
                        buildDefence(team.getTargetBlock(), layers[i], i, team.getColor().name(), e.getGame().getName());
                        buildDefence(findBed(team.getTargetBlock()) , layers[i], i, team.getColor().name(), e.getGame().getName());
                    }
                }

                HashMap<Player,Location> cager = new HashMap<>();

                for (RunningTeam team:e.getGame().getRunningTeams())
                {
                    List<Player> players = team.getConnectedPlayers();
                    Collections.shuffle(players);
                    cager.put(players.get(0),team.getTeamSpawn().clone().add(0,8,0));
                    new cageHandler().buildCage(players.get(0),team.getTeamSpawn().clone().add(0,8,0));
                }

                for (Player p : e.getGame().getConnectedPlayers())
                {
                    new BukkitRunnable()
                    {
                        @Override
                        public void run()
                        {
                            if(p.getGameMode().equals(GameMode.SURVIVAL))
                            {
                                p.setInvulnerable(true);
                                spawnPoints.put(p,p.getWorld().getName()+";"+p.getLocation().getX()+";"+p.getLocation().getY()+";"+p.getLocation().getZ()+";"+p.getLocation().getYaw()+";"+p.getLocation().getPitch());
                                p.teleport(e.getGame().getTeamOfPlayer(p).getTeamSpawn().clone().add(0,8,0));
                            }
                        }
                    }.runTaskLater(Commands.Invictools, 10L);
                    p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.BOLD +" "+ChatColor.RED + "Sort your hotbar before the game starts!"));
                    p.sendTitle(ChatColor.translateAlternateColorCodes('&', "&eStarting in 10 Seconds"), ChatColor.translateAlternateColorCodes('&', "&r&fSneak to start early!"), 40, 5 * 20, 30);
                }

                new BukkitRunnable()
                {
                    int i = 0;
                    @Override
                    public void run()
                    {
                        i++;

                        int required = e.getGame().getConnectedPlayers().size();
                        int sneaking = 0;
                        for (Player p : e.getGame().getConnectedPlayers())
                        {
                            if(p.isSneaking())
                                sneaking++;
                        }

                        if(sneaking==required || i == 11)
                        {
                            if(e.getGame().getStatus().equals(GameStatus.RUNNING))
                            {
                                for (Player p : e.getGame().getConnectedPlayers())
                                {
                                    if(spawnPoints.get(p) != null)
                                    {
                                        new BukkitRunnable()
                                        {
                                            @Override
                                            public void run()
                                            {
                                                p.setInvulnerable(false);
                                            }
                                        }.runTaskLater(Commands.Invictools, 50L);

                                        spawnPoints.remove(p);
                                        saveBedfightInventory(loadout, p, false);
                                        p.playSound(p.getLocation(),Sound.ENTITY_ENDER_DRAGON_GROWL,1,1);
                                        p.sendTitle(ChatColor.translateAlternateColorCodes('&', "&eGame Started!"), ChatColor.translateAlternateColorCodes('&', "&fGood Luck"), 20, 50, 15);
                                    }
                                    else if(spawnPoints.get(p) == null)
                                        p.damage(p.getHealth());
                                }

                                for (Player p:cager.keySet())
                                {
                                    new cageHandler().destroyCage(p,cager.get(p));
                                }
                            }
                            else
                            {
                                for (Player p:cager.keySet())
                                {
                                    new cageHandler().destroyCage(p,cager.get(p));
                                    spawnPoints.remove(p);
                                }
                            }
                            this.cancel();
                        }

                        for (Player p : e.getGame().getConnectedPlayers())
                        {
                            p.playSound(p.getLocation(),Sound.BLOCK_NOTE_BLOCK_HAT,1,1);
                        }
                    }
                }.runTaskTimer(Commands.Invictools, 0L, 20L);
            }
        }.runTaskLater(Commands.Invictools, 1L);
    }

    @EventHandler
    public void drops(PlayerDropItemEvent e)
    {
        if(spawnPoints.containsKey(e.getPlayer()))
            e.setCancelled(true);
    }

    @EventHandler
    public void place(BlockPlaceEvent e)
    {
        if(spawnPoints.containsKey(e.getPlayer()))
            e.setCancelled(true);

        if(BedwarsAPI.getInstance().isPlayerPlayingAnyGame(e.getPlayer()))
        {
            if(disableStats.getGameType(BedwarsAPI.getInstance().getGameOfPlayer(e.getPlayer())).equals(bedfight))
            {
                if (isAnyBlock(new ItemStack(e.getBlock().getType()), "WOOL") && !spawnPoints.containsKey(e.getPlayer()))
                {
                    ItemStack item = new ModBow().searchInventory(e.getPlayer(), BedwarsAPI.getInstance().getGameOfPlayer(e.getPlayer()).getTeamOfPlayer(e.getPlayer()).getColor() + "_WOOL");
                    if(item.getAmount() < 64)
                        item.setAmount(item.getAmount()+1);
                }
            }
        }
    }

    @EventHandler
    public void drops(EntityPickupItemEvent e)
    {
        if(e.getEntity() instanceof Player)
        {
            Player p = (Player) e.getEntity();
            if(spawnPoints.containsKey(p))
                e.setCancelled(true);
        }
    }

    @EventHandler
    public void killed(BedwarsPlayerKilledEvent e) throws IOException
    {
        if (!disableStats.getGameType(e.getGame()).equalsIgnoreCase(bedfight))
            return;

        new BukkitRunnable()
        {
            @Override
            public void run()
            {
                if(e.getGame().getStatus().equals(GameStatus.RUNNING))
                    loadBedfightInventory(loadout, e.getPlayer(), false);
            }
        }.runTaskLater(Commands.Invictools, 20 * 3+1);

        if(e.getKiller() != null)
        {
            e.getKiller().getInventory().addItem(new ItemStack(Material.GOLDEN_APPLE));
        }
    }

    public void buildDefence(Location loc, String layer, int spacing, String color, String game) // spacing is for iterator, delays loop for aesthetic and spaces next layer
    {
        if (layer.equalsIgnoreCase("WOOL")
                || layer.equalsIgnoreCase("STAINED_GLASS")
                || layer.equalsIgnoreCase("STAINED_GLASS_PANE")
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
        String finalLayer = layer;
        new BukkitRunnable()
        {
            @Override
            public void run() // 4 9 15  (*2)
            {
                for (BlockFace face : d)
                {
                    Location cardinal = block.getRelative(face).getLocation().add(directionAddition(face).multiply(spacing));

                        if (api.getGameByName(game).getStatus().equals(GameStatus.RUNNING))
                        {
                            if (cardinal.getBlock().getType().equals(Material.AIR))
                            {
                                cardinal.getBlock().setType(Material.valueOf(finalLayer));
                                new ModBow().addLater(cardinal.getBlock(), api.getGameByName(game));
                            }

                            if (face.getOppositeFace().equals(findBedFace(loc)) && spacing > 0) // true if opposite other half of bed
                            {
                                System.out.println("entering for " + finalLayer);
                                List<BlockFace> directions = new ArrayList<>(d); // block face list of left and right from opposite bed face
                                directions.remove(face);
                                directions.remove(BlockFace.UP);
                                for (int i = 0; i < spacing+1; i++) // for one up
                                {
                                    // for sides
                                    placeDiagonal(cardinal, face, directions.get(0), spacing, finalLayer, game);
                                    placeDiagonal(cardinal, face, directions.get(1), spacing, finalLayer, game);

                                    // for one up
                                    if(i>0)
                                    {
                                        Location upover = cardinal.clone().add(0, i, 0).subtract(directionAddition(face).multiply(i));
                                        placeDiagonal(upover, face, directions.get(1), spacing-i+1, finalLayer, game);
                                        placeDiagonal(upover, face, directions.get(0), spacing-i+1, finalLayer, game);
                                        upover.getBlock().setType(Material.valueOf(finalLayer));
                                        new ModBow().addLater(upover.getBlock(), api.getGameByName(game));
                                    }
                                }
                            }
                        }
                        block.getWorld().playSound(block.getLocation(), Sound.BLOCK_AMETHYST_BLOCK_PLACE, 3, 1);
                    }
            }
        }.runTaskLater(Commands.Invictools, spacing * 15L + new Random().nextInt(10));
    }

    private void placeDiagonal(Location cardinal, BlockFace face, BlockFace direction, int spacing, String blocktype, String game)
    {
        if(spacing<=0) return;

        Location temp = cardinal.clone().add(face.getOppositeFace().getDirection()); // subtracts 1 towards other bed half
        Location placement = temp.clone().add(direction.getDirection()/*.multiply(spacing)*/);

        if (placement.getBlock().getType().equals(Material.AIR))
        {
            new BukkitRunnable()
            {
                @Override
                public void run()
                {
                    placement.getBlock().setType(Material.valueOf(blocktype));
                    new ModBow().addLater(placement.getBlock(), api.getGameByName(game));
                    placement.getWorld().playSound(placement, Sound.BLOCK_AMETHYST_BLOCK_PLACE, 3, 1);
                }
            }.runTaskLater(Commands.Invictools, new Random().nextInt(25));
        }

        placeDiagonal(placement,face,direction,spacing-1,blocktype,game);
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

        // handles LIGHT_ colors
        if (s.length >= 3)
            return s[2].equalsIgnoreCase(type.toUpperCase(Locale.ROOT));

        return false;
    }


    public boolean isAnyBed(Block block)
    {
        String[] s = block.getType().toString().split("_");

        if (s.length == 2)
            if (s[1].equalsIgnoreCase("BED"))
                return true;

        // handles LIGHT_ colors
        if (s.length == 3)
            return s[2].equalsIgnoreCase("BED");

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

        //  new LobbyInventoryFix().clearMainInventory(p);
        for (int i = 0; i <= 35; i++)
        {
            ItemStack item = p.getInventory().getItem(i);
            if (item != null)
                item.setType(Material.AIR);
        }

        if (!pass.equalsIgnoreCase("pass") && !forceReset)
        {
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
                            if(Via.getAPI().getPlayerVersion(p) == 47 && i == 40)
                                p.getInventory().addItem(item);
                            else
                                p.getInventory().setItem(i, item);
                        }
                        else
                            p.getInventory().setItem(i, item);
                    }
                    else
                    {
                        if(Via.getAPI().getPlayerVersion(p) == 47 && i == 40)
                            p.getInventory().addItem(item);
                        else
                            p.getInventory().setItem(i, item);
                    }
                }
            }
        }

        if (pass.equalsIgnoreCase("pass") || forceReset)
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
                        if (item.getType().equals(Material.WHITE_WOOL))
                        {
                            ItemStack newitem = new ItemStack(Material.valueOf(api.getGameOfPlayer(p).getTeamOfPlayer(p).getColor().toString() + "_WOOL"));
                            newitem.setAmount(item.getAmount());
                            p.getInventory().setItem(i, newitem);
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
                ItemStack cloned = item.clone();
                if (isAnyBlock(cloned, "WOOL"))
                    cloned.setType(Material.WHITE_WOOL);
                inventoryList.add(cloned);
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

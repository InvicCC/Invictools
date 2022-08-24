package me.invic.invictools.util.ingame;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.BlockPosition;
import me.invic.invictools.commands.OldCommands;
import me.invic.invictools.gamemodes.bf.bedfight;
import me.invic.invictools.gamemodifiers.WardenSpawner;
import me.invic.invictools.util.fixes.ChangeTeamSize;
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.screamingsandals.bedwars.api.BedwarsAPI;
import org.screamingsandals.bedwars.api.events.BedwarsGameEndEvent;
import org.screamingsandals.bedwars.api.events.BedwarsGameStartEvent;
import org.screamingsandals.bedwars.api.game.Game;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class blockDecay implements Listener
{
    static List<Location> decaying = new ArrayList<>();
    static HashMap<Game,List<Location>> spawnPoints = new HashMap<>();
    static HashMap<Game,Integer> maxY = new HashMap<>();
    public static List<Game> decayGamemode = new ArrayList<>();

    public blockDecay()
    {

    }

    public blockDecay(Location loc, Game game, int seconds)
    {
        /*
        if(new bedfight().isAnyBlock(new ItemStack(loc.getBlock().getType()),"WOOL"))
        {
            loc.getBlock().setType(Material.LIGHT_GRAY_WOOL);
        }
         */
        decaying.add(loc);
        ProtocolManager manager = ProtocolLibrary.getProtocolManager();
        PacketContainer packet1 = manager.createPacket(PacketType.Play.Server.BLOCK_BREAK_ANIMATION);
        packet1.getBlockPositionModifier().write(0, new BlockPosition((int)loc.getX(), (int)loc.getY(), (int)loc.getZ())); //position
        packet1.getIntegers().write(0, getBlockEntityId(loc)); //entityid

        PacketContainer clear = manager.createPacket(PacketType.Play.Server.BLOCK_BREAK_ANIMATION);
        clear.getBlockPositionModifier().write(0, new BlockPosition((int)loc.getX(), (int)loc.getY(), (int)loc.getZ())); //position
        clear.getIntegers().write(0, getBlockEntityId(loc)); //entityid
        packet1.getIntegers().write(1, 10); // stage

        new BukkitRunnable()
        {
            int i = 1;
            @Override
            public void run()
            {
                packet1.getIntegers().write(1, i); // stage
               // ((CraftPlayer)Bukkit.getPlayer("Invictable")).getHandle().playerConnection.sendPacket(packet1);
                game.getConnectedPlayers().forEach(player ->
                {
                    try
                    {
                        manager.sendServerPacket(player,clear);
                        manager.sendServerPacket(player,packet1);
                    } catch (InvocationTargetException e)
                    {
                        e.printStackTrace();
                    }
                });

                i++;

                if(i==11 || !decaying.contains(loc))
                {
                    if(decaying.contains(loc))
                    {
                        loc.getWorld().playEffect(loc, Effect.STEP_SOUND, loc.getBlock().getType());
                        loc.getBlock().setType(Material.AIR);
                    }

                    decaying.remove(loc);
                    game.getConnectedPlayers().forEach(player ->
                    {
                        try
                        {
                            manager.sendServerPacket(player,clear);
                        } catch (InvocationTargetException e)
                        {
                            e.printStackTrace();
                        }
                    });

                    this.cancel();
                }
            }
        }.runTaskTimer(OldCommands.Invictools, 5L, ((seconds* 20L)/10)+1);
    }

    private static int getBlockEntityId(Location block) {
        return   (((int)block.getX() & 0xFFF) << 20)
                | (((int)block.getZ() & 0xFFF) << 8)
                | ((int)block.getY() & 0xFF);
    }

    @EventHandler
    public void destructionCheck(BlockBreakEvent e)
    {
        decaying.remove(e.getBlock().getLocation());
    }

    @EventHandler
    public void placement(BlockPlaceEvent e)
    {
        if(BedwarsAPI.getInstance().isPlayerPlayingAnyGame(e.getPlayer()))
        {
            if(e.getBlock().getLocation().getY() == maxY.get(BedwarsAPI.getInstance().getGameOfPlayer(e.getPlayer()))-2)
            {
                new blockDecay(e.getBlock().getLocation(), BedwarsAPI.getInstance().getGameOfPlayer(e.getPlayer()),2);
            }
            else if(e.getBlock().getLocation().getY() == maxY.get(BedwarsAPI.getInstance().getGameOfPlayer(e.getPlayer()))-1)
            {
                new blockDecay(e.getBlock().getLocation(), BedwarsAPI.getInstance().getGameOfPlayer(e.getPlayer()),1);
            }
            else if(e.getBlock().getLocation().getY() == maxY.get(BedwarsAPI.getInstance().getGameOfPlayer(e.getPlayer())))
            {
                new blockDecay(e.getBlock().getLocation(), BedwarsAPI.getInstance().getGameOfPlayer(e.getPlayer()),0);
            }
            else if(closeToSpawn(2.0,BedwarsAPI.getInstance().getGameOfPlayer(e.getPlayer()),e.getBlock().getLocation()))
            {
                new blockDecay(e.getBlock().getLocation(), BedwarsAPI.getInstance().getGameOfPlayer(e.getPlayer()),4);
            }
            else if(decayGamemode.contains(BedwarsAPI.getInstance().getGameOfPlayer(e.getPlayer())))
            {
                new blockDecay(e.getBlock().getLocation(), BedwarsAPI.getInstance().getGameOfPlayer(e.getPlayer()), (int) e.getBlock().getType().getHardness()+15);
            }
        }
    }

    static final Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("BedWars");
    static final File Folder = new File(plugin.getDataFolder(), "arenas");

    public static int arenaMaxY(String gameName)
    {
        final File pFile = new File(Folder, ChangeTeamSize.ConfigConversion(gameName) + ".yml");
        final FileConfiguration data = YamlConfiguration.loadConfiguration(pFile);
        String[] pos1 = data.getString("pos1").split(";");
        String[] pos2 = data.getString("pos2").split(";");
        if(Double.parseDouble(pos1[1])>=Double.parseDouble(pos2[1]))
        {
            return (int) Double.parseDouble(pos1[1]);
        }
        else
        {
            return (int) Double.parseDouble(pos2[1]);
        }
    }

    @EventHandler
    public void setDecayLocations(BedwarsGameStartEvent e)
    {
        maxY.put(e.getGame(),arenaMaxY(e.getGame().getName()));
        List<Location> teamSpawns = new ArrayList<>();
        e.getGame().getAvailableTeams().forEach(team -> teamSpawns.add(team.getTeamSpawn()));
        spawnPoints.put(e.getGame(),teamSpawns);
    }

    @EventHandler
    public void remove(BedwarsGameEndEvent e)
    {
        maxY.remove(e.getGame());
        spawnPoints.remove(e.getGame());
        decayGamemode.remove(e.getGame());
    }

    private boolean closeToSpawn(Double distance, Game game, Location loc)
    {
        for (Location sp:spawnPoints.get(game))
        {
            if(sp.distance(loc)<= distance)
            {
                return true;
            }
        }
        return false;
    }
}

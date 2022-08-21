package me.invic.invictools.util;

import me.invic.invictools.commands.OldCommands;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;
import org.screamingsandals.bedwars.api.BedwarsAPI;
import org.screamingsandals.bedwars.api.events.BedwarsGameEndEvent;
import org.screamingsandals.bedwars.api.events.BedwarsGameStartedEvent;
import org.screamingsandals.bedwars.api.events.BedwarsSavePlayerStatisticEvent;
import org.screamingsandals.bedwars.api.game.Game;
import org.screamingsandals.bedwars.api.game.GameStatus;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class disableStats implements Listener
{
    public static HashMap<String, Integer> startingSize = new HashMap<>();
    public static HashMap<String, String> recentGame = new HashMap<>(); //name, game
    public static List<Game> singleDisable = new ArrayList<>();

    @EventHandler
    public void saveEvent(BedwarsSavePlayerStatisticEvent e)
    {
        if(e.getPlayerStatistic().getName() == null)
            return;

        if(!recentGame.containsKey((e.getPlayerStatistic().getName())))
        {
            e.setCancelled(true);
         //   System.out.println("missing "+Bukkit.getPlayer(e.getPlayerStatistic().getName()).getName());
        }
        else if (!shouldTrack(Bukkit.getPlayer(e.getPlayerStatistic().getName()))
                || !OldCommands.StatsTrack
                || singleDisable.contains(BedwarsAPI.getInstance().getGameOfPlayer(Bukkit.getPlayer(recentGame.get(e.getPlayerStatistic().getName())))))
        {
            e.setCancelled(true);
        }

        if(singleDisable.contains(BedwarsAPI.getInstance().getGameOfPlayer(Bukkit.getPlayer(recentGame.get(e.getPlayerStatistic().getName())))))
        {
            new BukkitRunnable()
            {
                @Override
                public void run()
                {
                    if(BedwarsAPI.getInstance().getGameOfPlayer(Bukkit.getPlayer(recentGame.get(e.getPlayerStatistic().getName()))).getStatus().equals(GameStatus.WAITING))
                        singleDisable.remove(BedwarsAPI.getInstance().getGameOfPlayer(Bukkit.getPlayer(recentGame.get(e.getPlayerStatistic().getName()))));
                }
            }.runTaskLater(OldCommands.Invictools, 50L);
        }
    }

    @EventHandler
    public void gameStart(BedwarsGameStartedEvent e)
    {
        startingSize.put(e.getGame().getName(), e.getGame().getConnectedPlayers().size());

        for (Player p : e.getGame().getConnectedPlayers())
        {
            recentGame.put(p.getName(), e.getGame().getName());
        }

        if (!OldCommands.StatsTrack || singleDisable.contains(e.getGame()))
        {
            for (Player p : e.getGame().getConnectedPlayers())
            {
                new BukkitRunnable()
                {
                    @Override
                    public void run()
                    {
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&b&l(!) &r&fStats will not track this match"));
                        p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
                    }
                }.runTaskLater(OldCommands.Invictools, 25L);
            }
        }
        else if(!shouldTrack(e.getGame().getConnectedPlayers().get(0)) && !getGameType(e.getGame()).equalsIgnoreCase("bedfight"))
        {
            for (Player p : e.getGame().getConnectedPlayers())
            {
                new BukkitRunnable()
                {
                    @Override
                    public void run()
                    {
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&b&l(!) &r&fStats will not track this match"));
                        p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
                    }
                }.runTaskLater(OldCommands.Invictools, 25L);
            }
        }
    }

    public static boolean shouldTrack(Player p) // tracks if game has at least 4 players and isnt a bedfight game
    {
        return startingSize.get(recentGame.get(p.getName())) >= 4 && !getGameType(BedwarsAPI.getInstance().getGameByName(recentGame.get(p.getName()))).equalsIgnoreCase("bedfight");
    }

    final static File Folder = new File(OldCommands.Invictools.getDataFolder(), "Maps");

    public static String getGameType(Game game)
    {
        File pFile = new File(Folder, game.getName() + ".yml");
        final FileConfiguration mapData = YamlConfiguration.loadConfiguration(pFile);
        return mapData.getString("GameType", "normal");
    }
}

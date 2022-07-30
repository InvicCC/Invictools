package cc.invic.invictools.util;

import cc.invic.invictools.impl.Commands;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.screamingsandals.bedwars.api.BedwarsAPI;
import org.screamingsandals.bedwars.api.events.BedwarsGameStartedEvent;
import org.screamingsandals.bedwars.api.events.BedwarsSavePlayerStatisticEvent;
import org.screamingsandals.bedwars.api.game.Game;

import java.io.File;
import java.util.HashMap;

public class disableStats implements Listener
{
    public static HashMap<String, Integer> startingSize = new HashMap<>();
    public static HashMap<String, String> recentGame = new HashMap<>(); //name, game

    @EventHandler
    public void saveEvent(BedwarsSavePlayerStatisticEvent e)
    {
        if(!recentGame.containsKey((e.getPlayerStatistic().getName())))
        {
            e.setCancelled(true);
         //   System.out.println("missing "+Bukkit.getPlayer(e.getPlayerStatistic().getName()).getName());
        }
        else if (!shouldTrack(Bukkit.getPlayer(e.getPlayerStatistic().getName())) || !Commands.StatsTrack)
        {
            e.setCancelled(true);
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

        if (!Commands.StatsTrack)
        {
            for (Player p : e.getGame().getConnectedPlayers())
            {
                p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&b&l(!) &r&fStats will not track this match"));
                p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 3, 90);
            }
        }
        else if(!shouldTrack(e.getGame().getConnectedPlayers().get(0)) && !getGameType(e.getGame()).equalsIgnoreCase("bedfight"))
        {
            for (Player p : e.getGame().getConnectedPlayers())
            {
                p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&b&l(!) &r&fStats will not track this match"));
                p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 3, 90);
            }
        }
    }

    /*
        @EventHandler
        public void gameEnd(BedwarsGameEndEvent e)
        {
            startingSize.remove(e.getGame().getName());
        }
     */
    public static boolean shouldTrack(Player p) // tracks if game has at least 4 players and isnt a bedfight game
    {
        return startingSize.get(recentGame.get(p.getName())) >= 4 && !getGameType(BedwarsAPI.getInstance().getGameByName(recentGame.get(p.getName()))).equalsIgnoreCase("bedfight");
    }

    final static File Folder = new File(Commands.Invictools.getDataFolder(), "Maps");

    public static String getGameType(Game game)
    {
        File pFile = new File(Folder, game.getName() + ".yml");
        final FileConfiguration mapData = YamlConfiguration.loadConfiguration(pFile);
        return mapData.getString("GameType", "normal");
    }
}

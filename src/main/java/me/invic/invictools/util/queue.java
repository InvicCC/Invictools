package me.invic.invictools.util;

import me.invic.invictools.commands.Commands;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;
import org.screamingsandals.bedwars.api.BedwarsAPI;
import org.screamingsandals.bedwars.api.events.BedwarsGameChangedStatusEvent;
import org.screamingsandals.bedwars.api.events.BedwarsGameStartEvent;
import org.screamingsandals.bedwars.api.events.BedwarsPlayerJoinEvent;
import org.screamingsandals.bedwars.api.events.BedwarsPlayerLeaveEvent;
import org.screamingsandals.bedwars.api.game.Game;
import org.screamingsandals.bedwars.api.game.GameStatus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class queue implements Listener
{
    public static Game activeBedwarsGame;
    public static Game activeBedfightGame;

    BedwarsAPI api = BedwarsAPI.getInstance();

    private List<Game> randomBlacklist()
    {
        List<Game> temp = new ArrayList<>();
        temp.add(api.getGameByName("Multiverse"));
        temp.add(api.getGameByName("Skylantis"));
        temp.add(api.getGameByName("stream"));
        temp.add(api.getGameByName("OasisScenario"));
        temp.add(api.getGameByName("test"));
        temp.add(api.getGameByName("SnowThree"));
        temp.add(api.getGameByName("MarsFours"));
        return temp;
    }

    public Game getRandomGame(String GameType)
    {
        List<Game> games = api.getGames();
        Collections.shuffle(games);
        for (Game game:games)
        {
            if(disableStats.getGameType(game).equalsIgnoreCase(GameType) && game != activeBedfightGame && game != activeBedwarsGame && game.getStatus().equals(GameStatus.WAITING) && !randomBlacklist().contains(game))
            {
                return game;
            }
        }
        return api.getFirstWaitingGame();
    }

    @EventHandler
    public void status(BedwarsGameChangedStatusEvent e)
    {
        new BukkitRunnable()
        {
            @Override
            public void run()
            {
                if(e.getGame().equals(activeBedfightGame) && e.getGame().getStatus().equals(GameStatus.RUNNING))
                {
                    activeBedfightGame = getRandomGame("Bedfight");
                }
                else if(e.getGame().equals(activeBedwarsGame) && e.getGame().getStatus().equals(GameStatus.RUNNING))
                {
                    activeBedwarsGame = getRandomGame("Normal");
                }
            }
        }.runTaskLater(Commands.Invictools, 1L);
    }

    @EventHandler
    public void bwstart(BedwarsPlayerJoinEvent e) // getgame.getconnected seems to grab the number before the player who invokes this actually joins, so -1
    {
        if(e.getGame().equals(activeBedfightGame) && e.getGame().getConnectedPlayers().size() == e.getGame().getMaxPlayers()-1 || e.getGame().equals(activeBedfightGame) && e.getGame().getStatus().equals(GameStatus.RUNNING))
        {
            activeBedfightGame = getRandomGame("Bedfight");
        }
        else if(e.getGame().equals(activeBedwarsGame) && e.getGame().getConnectedPlayers().size() == e.getGame().getMaxPlayers()-1|| e.getGame().equals(activeBedwarsGame) && e.getGame().getStatus().equals(GameStatus.RUNNING))
        {
            activeBedwarsGame = getRandomGame("Normal");
        }
    }

    @EventHandler
    public void bwleave(BedwarsPlayerLeaveEvent e) // getgame.getconnected seems to grab the number before the player who invokes this actually joins, so 1 for empty.
    {
        System.out.println(e.getGame().getConnectedPlayers().size()+" leave connected");
        if(e.getGame().equals(activeBedfightGame) && e.getGame().getConnectedPlayers().size() == 1 && !safeSizeChange.beingModified.contains(e.getGame()))
        {
            activeBedfightGame = getRandomGame("Bedfight");
        }
        else if(e.getGame().equals(activeBedwarsGame) && e.getGame().getConnectedPlayers().size() == 1&& !safeSizeChange.beingModified.contains(e.getGame()))
        {
            activeBedwarsGame = getRandomGame("Normal");
        }
    }

}

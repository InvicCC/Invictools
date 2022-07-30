package cc.invic.invictools.util;

import cc.invic.invictools.impl.Commands;
import cc.invic.invictools.util.fixes.ChangeTeamSize;
import cc.invic.invictools.util.fixes.LobbyInventoryFix;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;
import org.screamingsandals.bedwars.api.BedwarsAPI;
import org.screamingsandals.bedwars.api.events.BedwarsGameEndEvent;
import org.screamingsandals.bedwars.api.game.Game;
import org.screamingsandals.bedwars.api.game.GameStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class safeSizeChange implements Listener
{
    static HashMap<Game, Integer> returnSize = new HashMap<>();
    static List<Game> beingModified = new ArrayList<>();

    BedwarsAPI api = BedwarsAPI.getInstance();

    public boolean safeSizeEdit(String modify, CommandSender p, int size)
    {
        // if(!isAnyGameRunning())

            if (!(size > 0 && size < 11))
            {
                p.sendMessage(ChatColor.RED + "Invalid Team Size");
                return false;
            }

            if(BedwarsAPI.getInstance().isGameWithNameExists(modify))
                leaveRejoinGame(BedwarsAPI.getInstance().getGameByName(modify));
            else
                p.sendMessage(ChatColor.RED+"invalid game");

            return true;
    }

    public void leaveRejoinGame(Game game)
    {
        if(!game.getStatus().equals(GameStatus.WAITING))
            return;

        List<Player> players = new ArrayList<>();
        for (Player player : game.getConnectedPlayers())
        {
            players.add(player);
            game.leaveFromGame(player);
        }

        beingModified.add(game);
        returnSize.put(game, ChangeTeamSize.getTeamSize(game));

        new BukkitRunnable()
        {
            @Override
            public void run()
            {
                new BukkitRunnable()
                {
                    @Override
                    public void run()
                    {
                        for (Player player : players)
                        {
                            new LobbyInventoryFix().saveInventory(player);
                            if (!api.isPlayerPlayingAnyGame(player))
                                game.joinToGame(player);
                        }
                        beingModified.remove(game);
                    }
                }.runTaskLater(Commands.Invictools, 10L);
            }
        }.runTaskLater(Commands.Invictools, 10L);
    }

    public boolean isAnyGameRunning()
    {
        for (Game game : api.getGames())
        {
            if (!game.getStatus().equals(GameStatus.WAITING) && !game.getStatus().equals(GameStatus.DISABLED))
            {
                return true;
            }
        }
        return false;
    }

    @EventHandler
    public void bwend(BedwarsGameEndEvent e)
    {
        if (returnSize.containsKey(e.getGame()))
        {
            ChangeTeamSize.ChangeSingleArenaTeamSize(e.getGame().getName(), returnSize.get(e.getGame()));
            // Bukkit.dispatchCommand(Bukkit.getConsoleSender(),"bw singlereload "+ChangeTeamSize.ConfigConversion(e.getGame().getName())+".yml");
            returnSize.remove(e.getGame());
        }
    }
}

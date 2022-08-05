package me.invic.invictools.util;

import me.invic.invictools.commands.OldCommands;
import me.invic.invictools.util.fixes.ChangeTeamSize;
import me.invic.invictools.util.fixes.LobbyInventoryFix;
import me.invic.invictools.util.fixes.SafeOpCommand;
import org.bukkit.Bukkit;
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

            if(BedwarsAPI.getInstance().isGameWithNameExists(modify) && !modify.equalsIgnoreCase("Multiverse"))
            {
                p.sendMessage(ChatColor.AQUA+"executing...");
                System.out.println("executing leave rejoin");
                leaveRejoinGame(BedwarsAPI.getInstance().getGameByName(modify),size);
            }
            else
                p.sendMessage(ChatColor.RED+"invalid game");

            return true;
    }

    public void leaveRejoinGame(Game game,int size)
    {
        if(!game.getStatus().equals(GameStatus.WAITING))
            return;

        List<Player> players = new ArrayList<>();
        for (Player player : game.getConnectedPlayers())
        {
            players.add(player);
            Bukkit.dispatchCommand(player,"spawn");
            //game.leaveFromGame(player);
        }

        beingModified.add(game);
        returnSize.put(game, ChangeTeamSize.getTeamSize(game));

        new BukkitRunnable()
        {
            @Override
            public void run()
            {
                if(size != 0)
                    ChangeTeamSize.ChangeSingleArenaTeamSize(game.getName(),size);

              //  Bukkit.dispatchCommand(Bukkit.getConsoleSender(),"bw singlereload "+ChangeTeamSize.ConfigConversion(game.getName())+".yml");

                new BukkitRunnable()
                {
                    @Override
                    public void run()
                    {
                        for (Player player : players)
                        {
                            new LobbyInventoryFix().saveInventory(player);
                            if (!api.isPlayerPlayingAnyGame(player))
                            {
                                new SafeOpCommand(player,"queue selector "+game.getName());
                            }
                        }
                        beingModified.remove(game);
                    }
                }.runTaskLater(OldCommands.Invictools, 10L);
            }
        }.runTaskLater(OldCommands.Invictools, 10L);

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
            System.out.println("game end singlereload");
            e.getGame().stop();
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(),"bw singlereload "+ChangeTeamSize.ConfigConversion(e.getGame().getName())+".yml");
            returnSize.remove(e.getGame());
        }
    }
}

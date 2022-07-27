package me.invic.invictools.util;

import me.invic.invictools.commands.Commands;
import me.invic.invictools.commands.joinCommands;
import me.invic.invictools.util.fixes.ChangeTeamSize;
import me.invic.invictools.util.fixes.LobbyInventoryFix;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;
import org.screamingsandals.bedwars.api.BedwarsAPI;
import org.screamingsandals.bedwars.api.events.BedwarsGameEndEvent;
import org.screamingsandals.bedwars.api.events.BedwarsGameEndingEvent;
import org.screamingsandals.bedwars.api.game.Game;
import org.screamingsandals.bedwars.api.game.GameStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class safeSizeChange implements Listener
{
    static HashMap<Game,Integer> returnSize = new HashMap<>();
    static List<Game> beingModified = new ArrayList<>();

    BedwarsAPI api = BedwarsAPI.getInstance();
    public boolean safeSizeEdit(String modify, CommandSender p, int size)
    {
       // if(!isAnyGameRunning())
        {
            if(!(size>0 && size<11))
            {
                p.sendMessage(ChatColor.RED +"Invalid Team Size");
                return false;
            }

            List<Player> inLobby = api.getGameByName(modify).getConnectedPlayers();

            try
            {
                beingModified.add(api.getGameByName(modify));

                for (Player player:inLobby)
                {
                    api.getGameByName(modify).leaveFromGame(player);
                }

                returnSize.put(api.getGameByName(modify),ChangeTeamSize.getTeamSize(api.getGameByName(modify)));
                ChangeTeamSize.ChangeSingleArenaTeamSize(modify,size);
              //  Bukkit.dispatchCommand(Bukkit.getConsoleSender(),"bw singlereload "+ChangeTeamSize.ConfigConversion(modify)+".yml");
/*
                if(p instanceof Player)
                {
                    new joinCommands().safeInventorySave();
                    if(!api.isPlayerPlayingAnyGame((Player) p))
                        api.getGameByName(modify).joinToGame((Player) p);
                }

 */

                new BukkitRunnable()
                {
                    @Override
                    public void run()
                    {
                        for (Player player:inLobby)
                        {
                            new LobbyInventoryFix().saveInventory(player);
                            if(!api.isPlayerPlayingAnyGame(player))
                                api.getGameByName(modify).joinToGame(player);
                        }
                        beingModified.remove(api.getGameByName(modify));
                    }
                }.runTaskLater(Commands.Invictools, 10L);

            }
            catch (Throwable e)
            {
                p.sendMessage(ChatColor.RED +"Unknown Error");
                return false;
            }
            return true;
        }
    }

    public boolean isAnyGameRunning()
    {
        for (Game game:api.getGames())
        {
            if(!game.getStatus().equals(GameStatus.WAITING) && !game.getStatus().equals(GameStatus.DISABLED))
            {
                return true;
            }
        }
        return false;
    }

    @EventHandler
    public void bwend(BedwarsGameEndEvent e)
    {
        if(returnSize.containsKey(e.getGame()))
        {
            ChangeTeamSize.ChangeSingleArenaTeamSize(e.getGame().getName(),returnSize.get(e.getGame()));
           // Bukkit.dispatchCommand(Bukkit.getConsoleSender(),"bw singlereload "+ChangeTeamSize.ConfigConversion(e.getGame().getName())+".yml");
            returnSize.remove(e.getGame());
        }
    }
}

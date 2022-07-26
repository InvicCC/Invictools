package me.invic.invictools.util;

import me.invic.invictools.commands.Commands;
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
import org.screamingsandals.bedwars.api.events.BedwarsGameEndingEvent;
import org.screamingsandals.bedwars.api.game.Game;
import org.screamingsandals.bedwars.api.game.GameStatus;

import java.util.HashMap;
import java.util.List;

public class safeSizeChange implements Listener
{
    HashMap<Game,Integer> returnSize = new HashMap<>();

    BedwarsAPI api = BedwarsAPI.getInstance();
    public boolean safeSizeEdit(String modify, CommandSender p, int size)
    {
        if(!isAnyGameRunning())
        {
            if(!(size>0 && size<11))
            {
                p.sendMessage(ChatColor.RED +"Invalid Team Size");
                return false;
            }

            List<Player> inLobby = api.getGameByName(modify).getConnectedPlayers();

            try
            {
                for (Player player:inLobby)
                {
                    api.getGameByName(modify).leaveFromGame(player);
                }

                ChangeTeamSize.ChangeSingleArenaTeamSize(modify,size);
                returnSize.put(api.getGameByName(modify),ChangeTeamSize.getTeamSize(api.getGameByName(modify)));
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(),"bw reload");
                for (Player player:inLobby)
                {
                    new LobbyInventoryFix().saveInventory(player);
                    api.getGameByName(modify).joinToGame(player);
                }
/*
                new BukkitRunnable()
                {
                    @Override
                    public void run()
                    {

                    }
                }.runTaskLater(Commands.Invictools, 20L);

 */
            }
            catch (Throwable e)
            {
                p.sendMessage(ChatColor.RED +"Unknown Error");
                return false;
            }
            return true;
        }
        else
        {
            p.sendMessage(ChatColor.RED +"Cannot do this while a game is running");
            return false;
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
    public void bwend(BedwarsGameEndingEvent e)
    {
        if(returnSize.containsKey(e.getGame()))
        {
            ChangeTeamSize.ChangeSingleArenaTeamSize(e.getGame().getName(),returnSize.get(e.getGame()));
            returnSize.remove(e.getGame());
        }
    }
}

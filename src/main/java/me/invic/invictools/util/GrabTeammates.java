package me.invic.invictools.util;

import me.invic.invictools.commands.Commands;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.screamingsandals.bedwars.api.BedwarsAPI;
import org.screamingsandals.bedwars.api.game.Game;

import java.util.ArrayList;
import java.util.List;

public class GrabTeammates
{
    public GrabTeammates(Player p)
    {
        for (Player player : Bukkit.getOnlinePlayers())
        {
            List<Player> team = getTeammates(player);
            /*
            List<Entity> nearby = player.getNearbyEntities(15,15,15);
            String teammate = DamageTeammates.getFirstHuman(nearby);
            if(teammate == null)
            {
                if(Bukkit.getOnlinePlayers().size() > 8)
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "broadcast Warning: at least 1 player's teammate did not get registered");
                else
                    System.out.println("broadcast Warning: at least 1 player's teammate did not get registered");
            }
            else
            {
                Player team = Bukkit.getPlayer(teammate);
                Commands.teammates.put(player,team);
            }
            */
            if (team.get(0) != null)
                Commands.teammates.put(player, team.get(0));
        }
        p.sendMessage(ChatColor.AQUA + "Teammates assigned");
    }

    public static boolean isTeammate(Player p1, Player p2)
    {
        BedwarsAPI api = BedwarsAPI.getInstance();

        if (!api.isPlayerPlayingAnyGame(p1) || !api.isPlayerPlayingAnyGame(p2))
            return false;

        if (p1.equals(p2))
            return false;

        return api.getGameOfPlayer(p1).getTeamOfPlayer(p2) == api.getGameOfPlayer(p1).getTeamOfPlayer(p1);
    }

    public static int getTeamSize(Player p)
    {
        BedwarsAPI api = BedwarsAPI.getInstance();

        if (!api.isPlayerPlayingAnyGame(p))
            return 0;

        return api.getGameOfPlayer(p).getTeamOfPlayer(p).getConnectedPlayers().size();
    }

    public static List<Player> getTeammates(Player p1)
    {
        BedwarsAPI api = BedwarsAPI.getInstance();
        List<Player> l = new ArrayList<>();

        if (!api.isPlayerPlayingAnyGame(p1))
            return l;

        Game game = api.getGameOfPlayer(p1);

        for (Player p2 : Bukkit.getOnlinePlayers())
        {
            if (api.isPlayerPlayingAnyGame(p2))
            {
                if (api.getGameOfPlayer(p2).equals(game))
                {
                    if (api.getGameOfPlayer(p1).getTeamOfPlayer(p2) == api.getGameOfPlayer(p1).getTeamOfPlayer(p1) && !p1.equals(p2))
                        l.add(p2);
                }
            }
        }
        /*
System.out.println("grabteammates");
        for (Player p:l
             )
        {
            System.out.println(p);
        }

         */
        return l;
    }
}

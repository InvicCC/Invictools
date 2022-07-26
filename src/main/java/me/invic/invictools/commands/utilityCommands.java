package me.invic.invictools.commands;

import me.invic.invictools.util.safeSizeChange;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.screamingsandals.bedwars.api.BedwarsAPI;
import org.screamingsandals.bedwars.api.game.Game;

import java.util.ArrayList;
import java.util.List;

public class utilityCommands implements CommandExecutor, TabExecutor // end game by disabling stats then .leave every player. debug. test safesizeedit.
{
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args)
    {
        List<String> tabComplete = new ArrayList<>();

        if(sender instanceof Player)
        {
            if(!sender.hasPermission("invic.invictools"))
            {
                sender.sendMessage(Commands.permissionsError);
                return tabComplete;
            }
        }

        if(args.length == 1)
        {
            tabComplete.add("test");
            tabComplete.add("debug");
            tabComplete.add("endgame");
        }
        else if(args.length==2 && args[0].equalsIgnoreCase("test"))
        {
            tabComplete.add("changeTeamSize game size");
        }
        else if(args.length==2 && args[0].equalsIgnoreCase("endgame"))
        {
            tabComplete.add("game");
        }

        return tabComplete;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if(args[0].equalsIgnoreCase("test") && args[1].equalsIgnoreCase("changeteamsize"))
        {
            new safeSizeChange().safeSizeEdit(args[2],sender,Integer.parseInt(args[3]));
        }
        else if(args[0].equalsIgnoreCase("debug"))
        {
            Commands.debug(sender);
        }
        else if(args.length == 1 && args[0].equalsIgnoreCase("endgame"))
        {
            Player p = (Player) sender;
            endgameNoStats(BedwarsAPI.getInstance().getGameOfPlayer(p));
        }
        else if(args.length >= 2 && args[0].equalsIgnoreCase("endgame"))
        {
            endgameNoStats(BedwarsAPI.getInstance().getGameByName(args[1]));
        }
        return true;
    }

    public void endgameNoStats(Game game)
    {
        List<Player> players = game.getConnectedPlayers();

        if(Commands.StatsTrack)
            Commands.StatsTrack = false;

        for (Player p:players)
        {
            game.leaveFromGame(p);
        }

        new BukkitRunnable()
        {
            @Override
            public void run()
            {
                Commands.StatsTrack = true;
            }
        }.runTaskLater(Commands.Invictools, 20L);
    }
}

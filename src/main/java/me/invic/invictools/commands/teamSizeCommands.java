package me.invic.invictools.commands;

import com.mojang.authlib.minecraft.TelemetrySession;
import me.invic.invictools.util.fixes.ChangeTeamSize;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.screamingsandals.bedwars.api.BedwarsAPI;
import org.screamingsandals.bedwars.api.RunningTeam;
import org.screamingsandals.bedwars.api.Team;
import org.screamingsandals.bedwars.api.game.Game;

import java.util.ArrayList;
import java.util.List;

public class teamSizeCommands implements CommandExecutor, TabExecutor
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
            //tabComplete.add("EveryArena");
            tabComplete.add("EveryGameType");
            tabComplete.add("SingleArena");
            tabComplete.add("print");
        }
        else if(args.length == 2 && args[0].equalsIgnoreCase("SingleArena"))
        {
            tabComplete.add("EveryTeam");
            tabComplete.add("SingleTeam");
        }
        else if(args.length == 2 && args[0].equalsIgnoreCase("EveryGameType"))
        {
            tabComplete.add("normal");
            tabComplete.add("mega");
            tabComplete.add("fours");
            tabComplete.add("threes");
            tabComplete.add("bedfight");
        }
        else if(args.length == 2 && args[0].equalsIgnoreCase("print"))
        {
            tabComplete.add("all");
            tabComplete.add("GameType");
        }
        else if(args.length == 3 && args[0].equalsIgnoreCase("SingleArena"))
        {
            for (Game game:BedwarsAPI.getInstance().getGames())
            {
                tabComplete.add(game.getName());
            }
        }
        else if(args.length == 3 && args[0].equalsIgnoreCase("print") && args[1].equalsIgnoreCase("GameType"))
        {
            tabComplete.add("normal");
            tabComplete.add("mega");
            tabComplete.add("fours");
            tabComplete.add("threes");
            tabComplete.add("bedfight");
        }
        else if((args.length == 3 && args[0].equalsIgnoreCase("EveryGameType"))
                ||(args.length == 2 && args[0].equalsIgnoreCase("EveryArena"))
                ||(args.length == 4 && args[0].equalsIgnoreCase("SingleArena")))
        {
            tabComplete.add("1");
            tabComplete.add("2");
            tabComplete.add("3");
            tabComplete.add("4");
        }
        else if(args.length == 5 && args[1].equalsIgnoreCase("SingleTeam"))
        {
            for (Team team:BedwarsAPI.getInstance().getGameByName(args[2]).getAvailableTeams())
            {
                tabComplete.add(team.getName());
            }
        }

        return tabComplete;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if(sender instanceof Player)
        {
            if(!sender.hasPermission("invic.invictools"))
            {
                sender.sendMessage(Commands.permissionsError);
                return true;
            }
        }

        if (args[0].equalsIgnoreCase("EveryArena"))
        {
            ChangeTeamSize.ChangeEveryArenaTeamSize(Integer.parseInt(args[1]));
        }
        else if(args[0].equalsIgnoreCase("print"))
        {
            if (args[1].equalsIgnoreCase("GameType"))
            {
                ChangeTeamSize.printTeamSizesGameType(sender,args[2]);
            }
            else
            {
                ChangeTeamSize.printTeamSizes(sender);
            }
        }
        else if (args[0].equalsIgnoreCase("EveryGameType"))
        {
            ChangeTeamSize.ChangeEveryOfGameType(Integer.parseInt(args[2]),args[1]);
        }
        else if (args[0].equalsIgnoreCase("SingleArena"))
        {
            if (args[1].equalsIgnoreCase("EveryTeam"))
            {
                ChangeTeamSize.ChangeSingleArenaTeamSize(args[2], Integer.parseInt(args[3]));
            }
            else if (args[1].equalsIgnoreCase("SingleTeam"))
            {
                ChangeTeamSize.ChangeSingleTeamSize(args[2], Integer.parseInt(args[3]), args[4]);
            }
        }

        return true;
    }
}

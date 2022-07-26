package me.invic.invictools.commands;

import com.mojang.authlib.minecraft.TelemetrySession;
import me.invic.invictools.util.fixes.ChangeTeamSize;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

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
            tabComplete.add("EveryArena");
            tabComplete.add("EveryGameType");
            tabComplete.add("SingleArena");
            tabComplete.add("print");
        }
        else if(args.length == 2 && args[1].equalsIgnoreCase("SingleArena"))
        {
            tabComplete.add("EveryTeam");
            tabComplete.add("SingleTeam");
        }
        else if(args.length == 2 && args[1].equalsIgnoreCase("print"))
        {
            tabComplete.add("all");
            tabComplete.add("GameType");
        }
        else if(args.length == 3 && args[1].equalsIgnoreCase("print") && args[2].equalsIgnoreCase("GameType"))
        {
            tabComplete.add("normal");
            tabComplete.add("mega");
            tabComplete.add("fours");
            tabComplete.add("threes");
            tabComplete.add("bedfight");
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
            ChangeTeamSize.ChangeEveryOfGameType(Integer.parseInt(args[1]),args[2]);
        }
        else if (args[0].equalsIgnoreCase("SingleArena"))
        {
            if (args[1].equalsIgnoreCase("EveryTeam"))
            {
                ChangeTeamSize.ChangeSingleArenaTeamSize(args[2], Integer.parseInt(args[3]));
            }
            else if (args[1].equalsIgnoreCase("SingleTeam"))
            {
                ChangeTeamSize.ChangeSingleTeamSize(args[2], Integer.parseInt(args[3]), args[5]);
            }
        }

        return true;
    }
}

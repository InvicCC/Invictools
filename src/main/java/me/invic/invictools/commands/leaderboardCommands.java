package me.invic.invictools.commands;

import me.invic.invictools.Leaderboards.BedfightLeaderboard;
import me.invic.invictools.Leaderboards.BedfightLeaderboardHologram;
import me.invic.invictools.Leaderboards.leaderboard;
import me.invic.invictools.Leaderboards.leaderboardHologram;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class leaderboardCommands implements TabExecutor, CommandExecutor
{
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args)
    {
        List<String> tabComplete = new ArrayList<>();
        if(sender instanceof Player)
        {
            if(!sender.hasPermission("invic.invictools"))
            {
                return tabComplete;
            }
        }

        if (args.length == 1)
        {
            tabComplete.add("bw");
            tabComplete.add("bf");

            if(sender.hasPermission("invic.invictools"))
                tabComplete.add("edit");
        }
        else if (args.length == 2 && !args[0].equalsIgnoreCase("edit"))
        {
            if(sender.hasPermission("invic.invictools"))
                tabComplete.add("reload");

            for (Player p:Bukkit.getOnlinePlayers())
            {
                tabComplete.add(p.getName());
            }
        }
        else if(args.length == 2 && args[0].equalsIgnoreCase("edit"))
        {
            tabComplete.add("min");
            tabComplete.add("size");
        }
        else if (args.length == 3 && args[0].equalsIgnoreCase("bw") && args[1].equalsIgnoreCase("reload"))
        {
            tabComplete.add("destroyedBeds");
            tabComplete.add("star");
            tabComplete.add("score");
            tabComplete.add("loses");
            tabComplete.add("deaths");
            tabComplete.add("wins");
            tabComplete.add("kills");
            tabComplete.add("finals");

            tabComplete.add("fkdr");
            tabComplete.add("kdr");
            tabComplete.add("wl");
        }
        else if (args.length == 3 && args[0].equalsIgnoreCase("bf") && args[1].equalsIgnoreCase("reload"))
        {
            tabComplete.add("BedBreaks");
            tabComplete.add("Losses");
            tabComplete.add("Wins");
            tabComplete.add("NormalDeaths");
            tabComplete.add("NormalKills");
            tabComplete.add("FinalDeaths");
            tabComplete.add("FinalKills");

            tabComplete.add("fkdr");
            tabComplete.add("kdr");
            tabComplete.add("wl");
            tabComplete.add("Score");
        }
        return tabComplete;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        boolean perms = false;
        if(sender instanceof Player)
        {
            if(sender.hasPermission("invic.invictools"))
            {
                perms = true;
            }
        }
        else
        {
            perms = true;
        }

        if (args.length == 1 && args[0].equalsIgnoreCase("bw"))
        {
            new leaderboard().printFormattedLeaderboard((Player) sender);
        }
        else if (args.length == 1 && args[0].equalsIgnoreCase("bf"))
        {
            new BedfightLeaderboard().printBFFormattedLeaderboard((Player) sender);
        }
        else if (args.length == 2 && args[0].equalsIgnoreCase("bw") && args[1].equalsIgnoreCase("reload") && perms)
        {
            new leaderboard().loadLeaderboard("Star");
            new leaderboardHologram().createLeaderboard();
        }
        else if (args.length == 2 && args[0].equalsIgnoreCase("bf") && args[1].equalsIgnoreCase("reload") && perms)
        {
            new BedfightLeaderboard().loadBFLeaderboard("Score");
            new BedfightLeaderboardHologram().createBFLeaderboard();
        }
        else if (args.length == 2 && args[0].equalsIgnoreCase("bw"))
        {
            new leaderboard().givePosition(Bukkit.getOfflinePlayer(args[1]), (Player) sender);
        }
        else if (args.length == 2 && args[0].equalsIgnoreCase("bf"))
        {
            new BedfightLeaderboard().giveBFPosition(Bukkit.getOfflinePlayer(args[1]), (Player) sender);
        }
        else if(args.length == 3 && args[1].equalsIgnoreCase("min") && perms)
        {
            leaderboard.gamesBeforeLeaderboard = Integer.parseInt(args[2]);
        }
        else if(args.length == 3 && args[1].equalsIgnoreCase("size") && perms)
        {
            leaderboard.lbsize = Integer.parseInt(args[2]);
        }
        else if (args.length == 3 && args[0].equalsIgnoreCase("bw") && args[1].equalsIgnoreCase("reload") && perms)
        {
            new leaderboard().loadLeaderboard(args[2]);
            new leaderboardHologram().createLeaderboard();
        }
        else if (args.length == 3 && args[0].equalsIgnoreCase("bf") && args[1].equalsIgnoreCase("reload") && perms)
        {
             new BedfightLeaderboard().loadBFLeaderboard(args[2]);
             new BedfightLeaderboardHologram().createBFLeaderboard();
        }

        return true;
    }
}

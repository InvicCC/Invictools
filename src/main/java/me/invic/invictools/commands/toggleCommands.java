package me.invic.invictools.commands;

import me.invic.invictools.cosmetics.VictoryDances.VictoryDancePreview;
import me.invic.invictools.util.disableParkour;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

import static me.invic.invictools.commands.Commands.*;

public class toggleCommands implements TabExecutor, CommandExecutor
{
    public static boolean isHosting = false;

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

        if (args.length == 1)
        {
            tabComplete.add("toys");
            tabComplete.add("victory");
            tabComplete.add("hosting");
            tabComplete.add("stats");
            tabComplete.add("portal");
            tabComplete.add("parkour");
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

        if (args.length == 1 && args[0].equalsIgnoreCase("toys"))
        {
            if (FireStickEnabled)
            {
                FireStickEnabled = false;
                sender.sendMessage(ChatColor.AQUA + "Lobby toys are now disabled. ");
            }
            else
            {
                FireStickEnabled = true;
                sender.sendMessage(ChatColor.AQUA + "Lobby toys are now enabled. ");
            }
        }
        else if (args.length == 1 && args[0].equalsIgnoreCase("victory"))
        {
            if (VictoryDancePreview.VictoryPreviewEnabled)
            {
                VictoryDancePreview.VictoryPreviewEnabled = false;
                sender.sendMessage(ChatColor.AQUA + "Victory Dance previews are now disabled. ");
            }
            else
            {
                VictoryDancePreview.VictoryPreviewEnabled = true;
                sender.sendMessage(ChatColor.AQUA + "Victory Dance previews are now enabled. ");
            }
        }
        else if (args.length == 1 && args[0].equalsIgnoreCase("hosting"))
        {
            if (isHosting)
            {
                isHosting = false;
                sender.sendMessage(ChatColor.AQUA + "Games are no longer being hosted");
            }
            else
            {
                isHosting = true;
                sender.sendMessage(ChatColor.AQUA + "Games are now being hosted");
            }
        }
        else if (args.length == 1 && args[0].equalsIgnoreCase("stats"))
        {
            if (StatsTrack)
            {
                StatsTrack = false;
                sender.sendMessage(ChatColor.AQUA + "Stats should no longer track");
            }
            else
            {
                StatsTrack = true;
                sender.sendMessage(ChatColor.AQUA + "Stats should now track");
            }
        }
        else if (args.length == 1 && args[0].equalsIgnoreCase("portal"))
        {
            if (worldswap)
            {
                worldswap = false;
                sender.sendMessage(ChatColor.AQUA + "World portals disabled");
            }
            else
            {
                worldswap = true;
                sender.sendMessage(ChatColor.AQUA + "World Portals enabled");
            }
        }
        else if (args.length == 1 && args[0].equalsIgnoreCase("parkour"))
        {
            new disableParkour();
            sender.sendMessage(ChatColor.AQUA + "Parkour blocked: " + disableParkour.parkour);
        }

        return true;
    }
}

package me.invic.invictools.commands;

import me.invic.invictools.Invictools;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class econCommands implements CommandExecutor, TabExecutor
{
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        boolean perms = false;
        if (sender instanceof Player)
        {
            if (sender.hasPermission("invic.invictools"))
            {
                perms = true;
            }
        } else
        {
            perms = true;
        }

        if (args.length == 0 && sender instanceof Player)
        {
            sender.sendMessage(ChatColor.AQUA+"You have " +ChatColor.WHITE+ (int)Invictools.econ.getBalance((Player)sender)+ChatColor.AQUA+" Invictacoins");
        }
        else if(args.length == 1)
        {
            if(Bukkit.getOfflinePlayer(args[0]) != null)
                sender.sendMessage(ChatColor.WHITE+ args[0]+ChatColor.AQUA+" has "+ChatColor.WHITE+ (int)Invictools.econ.getBalance(Bukkit.getOfflinePlayer(args[0]))+ChatColor.AQUA+" Invictacoins");
            else
                sender.sendMessage(ChatColor.RED+"Invalid Player");
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args)
    {
        boolean perms = false;
        if (sender instanceof Player)
        {
            if (sender.hasPermission("invic.invictools"))
            {
                perms = true;
            }
        } else
        {
            perms = true;
        }

        List<String> tabComplete = new ArrayList<>();
        if (args.length == 1)
        {
            for (Player p:Bukkit.getOnlinePlayers())
            {
                tabComplete.add(p.getName());
            }
        }

        return tabComplete;

    }
}

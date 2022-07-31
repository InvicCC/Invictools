package me.invic.invictools.impl;

import me.invic.invictools.commandManagerLib.SubCommand;
import me.invic.invictools.gamemodes.bedfight;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class RbfcCommand implements SubCommand
{
    @Override
    public String getName()
    {
        return "rbfc";
    }

    @Override
    public String getDescription()
    {
        return null;
    }

    @Override
    public String getSyntax()
    {
        return "/it rbfc";
    }

    @Override
    public String getPermission()
    {
        return "invic.all";
    }

    @Override
    public List<String> getTabCompletion(int index, String[] args)
    {
        return null;
    }

    @Override
    public void perform(CommandSender sender, String[] args)
    {
        new bedfight().loadBedfightInventory(args[2], Bukkit.getPlayer(args[1]), true);
        sender.sendMessage(ChatColor.AQUA + "reset bedfight inventory " + args[2] +" for " + args[1]);
    }
}
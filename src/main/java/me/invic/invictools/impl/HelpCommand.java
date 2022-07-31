package me.invic.invictools.impl;

import me.invic.invictools.Invictools;
import me.invic.invictools.commandManagerLib.SubCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.List;

public class HelpCommand implements SubCommand
{
    @Override
    public String getName()
    {
        return "help";
    }

    @Override
    public String getDescription()
    {
        return "Shows help for this plugin";
    }

    @Override
    public String getSyntax()
    {
        return "/it help";
    }

    @Override
    public String getPermission()
    {
        return "invic.help";
    }

    @Override
    public List<String> getTabCompletion(int index, String[] args)
    {
        return null;
    }

    @Override
    public void perform(CommandSender sender, String[] args)
    {
        sender.sendMessage(ChatColor.AQUA + "Invictools version " + Invictools.getInstance().getDescription().getVersion());

        for (SubCommand subCommand : Invictools.getMainCommand().getSubCommands())
        {
            if (sender.hasPermission(subCommand.getPermission()))
                sender.sendMessage(ChatColor.AQUA + subCommand.getSyntax() + " - " + subCommand.getDescription() + " (" + subCommand.getPermission() + ")");
        }
    }
}
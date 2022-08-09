package me.invic.invictools.commands.impl.IT;

import me.invic.invictools.commands.commandManagerLib.SubCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.List;

import static me.invic.invictools.commands.OldCommands.StatsTrack;

public class StatsCommand implements SubCommand
{
    @Override
    public String getName()
    {
        return "stats";
    }

    @Override
    public String getDescription()
    {
        return "Disables and enables stats";
    }

    @Override
    public String getSyntax()
    {
        return "/it stats";
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
}
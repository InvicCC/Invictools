package me.invic.invictools.impl.IT;

import me.invic.invictools.commandManagerLib.SubCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.List;

import static me.invic.invictools.commands.OldCommands.worldswap;

public class PortalsCommand implements SubCommand
{
    @Override
    public String getName()
    {
        return "portals";
    }

    @Override
    public String getDescription()
    {
        return "Disables and enables portals";
    }

    @Override
    public String getSyntax()
    {
        return "/it portals";
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
        if (worldswap)
        {
            worldswap = false;
            sender.sendMessage(ChatColor.AQUA + "Portals Disabled");
        }
        else
        {
            worldswap = true;
            sender.sendMessage(ChatColor.AQUA + "Portals Enabled");
        }
    }
}
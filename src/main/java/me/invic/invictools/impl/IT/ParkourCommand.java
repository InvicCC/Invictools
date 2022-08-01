package me.invic.invictools.impl.IT;

import me.invic.invictools.commandManagerLib.SubCommand;
import me.invic.invictools.gamemodifiers.Manhunt.ManhuntMain;
import me.invic.invictools.util.disableParkour;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.List;

public class ParkourCommand implements SubCommand
{
    @Override
    public String getName()
    {
        return "Parkour";
    }

    @Override
    public String getDescription()
    {
        return null;
    }

    @Override
    public String getSyntax()
    {
        return "/it Parkour";
    }

    @Override
    public String getPermission()
    {
        return "invic.invictools";
    }

    @Override
    public List<String> getTabCompletion(int index, String[] args)
    {
        return null;
    }

    @Override
    public void perform(CommandSender sender, String[] args)
    {
        new disableParkour();
        sender.sendMessage(ChatColor.AQUA + "Toggling Parkour");
    }
}
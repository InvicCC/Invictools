package me.invic.invictools.impl.IT;

import me.invic.invictools.commandManagerLib.SubCommand;
import me.invic.invictools.gamemodifiers.Manhunt.ManhuntMain;
import me.invic.invictools.util.GrabTeammates;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class GrabteammatesCommand implements SubCommand
{
    @Override
    public String getName()
    {
        return "grabteammates";
    }

    @Override
    public String getDescription()
    {
        return null;
    }

    @Override
    public String getSyntax()
    {
        return "/it grabteammates";
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
        new GrabTeammates((Player) sender);
        //   sender.sendMessage(ChatColor.AQUA + "Grabbing teammates...");
    }
}
package me.invic.invictools.impl;

import me.invic.invictools.commandManagerLib.SubCommand;
import me.invic.invictools.items.FireStick;
import me.invic.invictools.items.dareListener;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

import static me.invic.invictools.commands.OldCommands.FireStickEnabled;

public class LobbydareCommand implements SubCommand
{
    @Override
    public String getName()
    {
        return "lobbydare";
    }

    @Override
    public String getDescription()
    {
        return "Spawns a dare";
    }

    @Override
    public String getSyntax()
    {
        return "/it lobbydare";
    }

    @Override
    public String getPermission()
    {
        return "invic.firestick";
    }

    @Override
    public List<String> getTabCompletion(int index, String[] args)
    {
        return null;
    }

    @Override
    public void perform(CommandSender sender, String[] args)
    {
        if (FireStickEnabled)
        {
            Player p = (Player) sender;
            if (Bukkit.getPlayer(sender.getName()).getWorld().getName().equals("bwlobby"))
                new dareListener().handleItem(p.getLocation(), p, true, true);
        }
        else
        {
            sender.sendMessage(ChatColor.GOLD + "Lobby Dare " + ChatColor.RED + "is currently disabled.");
        }
    }
}
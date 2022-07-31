package me.invic.invictools.impl;

import me.invic.invictools.Invictools;
import me.invic.invictools.commandManagerLib.SubCommand;
import me.invic.invictools.items.FireStick;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class FirestickCommand implements SubCommand
{
    @Override
    public String getName()
    {
        return "firestick";
    }

    @Override
    public String getDescription()
    {
        return "Shoots a fireball";
    }

    @Override
    public String getSyntax()
    {
        return "/it firestick";
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
        if (Bukkit.getPlayer(sender.getName()).getWorld().getName().equals("bwlobby"))
        {
            new FireStick((Player) sender);
        }
    }
}
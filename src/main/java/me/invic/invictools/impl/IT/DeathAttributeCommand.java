package me.invic.invictools.impl.IT;

import me.invic.invictools.commandManagerLib.SubCommand;
import me.invic.invictools.gamemodifiers.AbtributesOnDeath;
import me.invic.invictools.gamemodifiers.Manhunt.ManhuntMain;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

import static org.bukkit.Bukkit.getPlayer;

public class DeathAttributeCommand implements SubCommand
{
    @Override
    public String getName()
    {
        return "DeathAttribute";
    }

    @Override
    public String getDescription()
    {
        return null;
    }

    @Override
    public String getSyntax()
    {
        return "/it DeathAttribute";
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
        if (args[1].equalsIgnoreCase("all")) // it DeathAttribute all Attribute interval basevalue
        {
            new AbtributesOnDeath().AbtributesOnDeathAll((Player) sender, args[2], Double.parseDouble(args[3]), Double.parseDouble(args[4]));
        }
        else if (args[1].equalsIgnoreCase("single")) // it DeathAttribute single Attribute interval basevalue player
        {
            new AbtributesOnDeath().AttributesOnDeathSingular(getPlayer(args[5]), args[2], Double.parseDouble(args[3]), Double.parseDouble(args[4]));
        }
    }
}
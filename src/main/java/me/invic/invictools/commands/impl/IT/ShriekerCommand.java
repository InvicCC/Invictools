package me.invic.invictools.commands.impl.IT;

import me.invic.invictools.commands.commandManagerLib.SubCommand;
import me.invic.invictools.gamemodifiers.WardenSpawner;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import java.util.List;

public class ShriekerCommand implements SubCommand
{
    @Override
    public String getName()
    {
        return "shrieker";
    }

    @Override
    public String getDescription()
    {
        return null;
    }

    @Override
    public String getSyntax()
    {
        return "/it shrieker";
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
        if (args.length >= 3)
        {
            if (args[2].equalsIgnoreCase("player") || args[2].equalsIgnoreCase("p"))
            {
                new WardenSpawner().ShriekerFromWorld(Bukkit.getPlayer(args[3]).getWorld(), Boolean.parseBoolean(args[1]));
            }
            else
            {
                new WardenSpawner().ShriekerFromWorld(Bukkit.getWorld(args[3]), Boolean.parseBoolean(args[1]));
            }

            // sender.sendMessage(ChatColor.AQUA + "Default world border closing on 0,0...");
        }
    }
}
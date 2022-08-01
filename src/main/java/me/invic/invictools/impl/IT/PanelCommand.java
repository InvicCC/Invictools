package me.invic.invictools.impl.IT;

import me.invic.invictools.commandManagerLib.SubCommand;
import me.invic.invictools.util.fixes.TeamSelection;
import me.invic.invictools.util.panels;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.screamingsandals.bedwars.api.BedwarsAPI;

import java.util.List;

public class PanelCommand implements SubCommand
{
    @Override
    public String getName()
    {
        return "panel";
    }

    @Override
    public String getDescription()
    {
        return null;
    }

    @Override
    public String getSyntax()
    {
        return "/it panel";
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
        if (args.length == 2)
        {
            if (!args[1].equals("reload"))
                new panels().openInventory(args[1], (Player) sender);
            else
            {
                new panels().loadPanels();
                sender.sendMessage(ChatColor.AQUA + "Panels reloaded");
            }
        }
        else if (args.length == 3)
        {
            new panels().openInventory(args[1], Bukkit.getPlayer(args[2]));
        }
    }
}
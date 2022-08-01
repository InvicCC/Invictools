package me.invic.invictools.impl.IT;

import me.invic.invictools.commandManagerLib.SubCommand;
import me.invic.invictools.gamemodes.bedfight;
import me.invic.invictools.util.panels;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class CreatebfconfigCommand implements SubCommand
{
    @Override
    public String getName()
    {
        return "createbfconfig";
    }

    @Override
    public String getDescription()
    {
        return null;
    }

    @Override
    public String getSyntax()
    {
        return "/it createbfconfig";
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
        new bedfight().saveBedfightInventory(args[1], (Player) sender, true);
        sender.sendMessage(ChatColor.AQUA + "Saved your inventory as bedfight loadout" + args[1]);
    }
}
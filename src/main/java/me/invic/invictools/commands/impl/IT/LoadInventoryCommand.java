package me.invic.invictools.commands.impl.IT;

import me.invic.invictools.commands.commandManagerLib.SubCommand;
import me.invic.invictools.util.fixes.LobbyInventoryFix;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.List;

public class LoadInventoryCommand implements SubCommand
{
    @Override
    public String getName()
    {
        return "loadInventory";
    }

    @Override
    public String getDescription()
    {
        return null;
    }

    @Override
    public String getSyntax()
    {
        return "/it loadInventory";
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
        new LobbyInventoryFix().loadInventory(Bukkit.getPlayer(args[1]), Bukkit.getPlayer(args[2]));
        sender.sendMessage(ChatColor.YELLOW + " " + Bukkit.getPlayer(args[1]).getName() + ChatColor.AQUA + "'s Inventory has been loaded");
    }
}
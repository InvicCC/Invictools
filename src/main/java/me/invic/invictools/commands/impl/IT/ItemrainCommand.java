package me.invic.invictools.commands.impl.IT;

import me.invic.invictools.commands.commandManagerLib.SubCommand;
import me.invic.invictools.gamemodifiers.ItemRain;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class ItemrainCommand implements SubCommand
{
    @Override
    public String getName()
    {
        return "itemrain";
    }

    @Override
    public String getDescription()
    {
        return null;
    }

    @Override
    public String getSyntax()
    {
        return "/it itemrain";
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
        if (args[1].equalsIgnoreCase("loottable"))
        {
            ItemRain.ItemRainLootTable(args[2], Integer.parseInt(args[3]), (Player) sender);
        }
        else if (args[1].equalsIgnoreCase("config"))
        {
            ItemRain.ItemRainConfig(args[2], Integer.parseInt(args[3]), (Player) sender);
        }
        ItemRain.IsEnabled = true;
        sender.sendMessage(ChatColor.AQUA + "Raining items in the sky using LootTable " + ChatColor.YELLOW + args[2]);
    }
}
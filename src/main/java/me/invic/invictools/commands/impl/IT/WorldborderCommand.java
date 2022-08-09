package me.invic.invictools.commands.impl.IT;

import me.invic.invictools.commands.commandManagerLib.SubCommand;
import me.invic.invictools.util.WorldBorder;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class WorldborderCommand implements SubCommand
{
    @Override
    public String getName()
    {
        return "worldborder";
    }

    @Override
    public String getDescription()
    {
        return null;
    }

    @Override
    public String getSyntax()
    {
        return "/it worldborder";
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
        if (args.length == 1)
        {
            new WorldBorder(250, 50, 240, 0, ((Player) sender).getWorld(), true, (Player) sender);
            sender.sendMessage(ChatColor.AQUA + "world border closing");
        }
        if (args.length > 1)
        {
            new WorldBorder(Integer.parseInt(args[1]), Integer.parseInt(args[2]), Integer.parseInt(args[3]), Integer.parseInt(args[4]), ((Player) sender).getWorld(), false, (Player) sender);
            sender.sendMessage(ChatColor.AQUA + "World border closing on x/z" + Integer.parseInt(args[4]));
        }
    }
}
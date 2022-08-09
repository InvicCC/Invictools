package me.invic.invictools.commands.impl.IT;

import me.invic.invictools.commands.commandManagerLib.SubCommand;
import me.invic.invictools.cosmetics.NormalKillHandler;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class NormalKillCommand implements SubCommand
{
    @Override
    public String getName()
    {
        return "NormalKill";
    }

    @Override
    public String getDescription()
    {
        return null;
    }

    @Override
    public String getSyntax()
    {
        return "/it NormalKill";
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
        if (args.length > 2)
        {
            Player player = Bukkit.getPlayer(args[2]);
            assert player != null;
            new NormalKillHandler().configHandler(player, args[1], true);
        }
        else if (args.length == 2)
        {
            Player player = (Player) sender;
            new NormalKillHandler().configHandler(player, args[1], false);
        }
    }
}
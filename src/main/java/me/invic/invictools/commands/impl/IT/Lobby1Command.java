package me.invic.invictools.commands.impl.IT;

import me.invic.invictools.commands.commandManagerLib.SubCommand;
import me.invic.invictools.cosmetics.Lobby1Handler;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class Lobby1Command implements SubCommand
{
    @Override
    public String getName()
    {
        return "lobby1";
    }

    @Override
    public String getDescription()
    {
        return null;
    }

    @Override
    public String getSyntax()
    {
        return "/it lobby1";
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
            new Lobby1Handler().configHandler(player, args[1], true);
        }
        else if (args.length == 2)
        {
            Player player = (Player) sender;
            new Lobby1Handler().configHandler(player, args[1], false);
        }
    }
}
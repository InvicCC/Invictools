package me.invic.invictools.impl.IT;

import me.invic.invictools.commandManagerLib.SubCommand;
import me.invic.invictools.cosmetics.bedbreaks.BedBreakConfig;
import me.invic.invictools.items.FireStick;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class BedBreakCommand implements SubCommand
{
    @Override
    public String getName()
    {
        return "BedBreak";
    }

    @Override
    public String getDescription()
    {
        return null;
    }

    @Override
    public String getSyntax()
    {
        return "/it BedBreak";
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
            new BedBreakConfig(player, args[1], true);
        }
        else if (args.length == 2)
        {
            Player player = (Player) sender;
            new BedBreakConfig(player, args[1], false);
        }
    }
}
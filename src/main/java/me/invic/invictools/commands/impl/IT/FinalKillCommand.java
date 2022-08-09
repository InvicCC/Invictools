package me.invic.invictools.commands.impl.IT;

import me.invic.invictools.commands.commandManagerLib.SubCommand;
import me.invic.invictools.cosmetics.finalkills.FinalKillConfig;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class FinalKillCommand implements SubCommand
{
    @Override
    public String getName()
    {
        return "FinalKill";
    }

    @Override
    public String getDescription()
    {
        return null;
    }

    @Override
    public String getSyntax()
    {
        return "/it FinalKill";
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
            new FinalKillConfig(player, args[1], true);
        }
        else if (args.length == 2)
        {
            Player player = (Player) sender;
            new FinalKillConfig(player, args[1], false);
        }
    }
}
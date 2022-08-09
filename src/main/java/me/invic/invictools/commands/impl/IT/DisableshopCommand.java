package me.invic.invictools.commands.impl.IT;

import me.invic.invictools.commands.commandManagerLib.SubCommand;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

import static me.invic.invictools.commands.OldCommands.noShop;

public class DisableshopCommand implements SubCommand
{
    @Override
    public String getName()
    {
        return "disableshop";
    }

    @Override
    public String getDescription()
    {
        return null;
    }

    @Override
    public String getSyntax()
    {
        return "/it disableshop";
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
        Player p = (Player) sender;
        if (args[1].equalsIgnoreCase("single"))
        {
            noShop.put(Bukkit.getPlayer(args[2]), true);
        }
        else if (args[1].equalsIgnoreCase("all"))
        {
            for (Player o : Bukkit.getOnlinePlayers())
            {
                if (o.getWorld().equals(p.getWorld()) && o.getGameMode().equals(GameMode.SURVIVAL))
                {
                    noShop.put(o, true);
                }
            }
        }
    }
}
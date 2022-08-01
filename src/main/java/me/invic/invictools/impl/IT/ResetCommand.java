package me.invic.invictools.impl.IT;

import me.invic.invictools.commandManagerLib.SubCommand;
import me.invic.invictools.gamemodifiers.Manhunt.ManhuntMain;
import me.invic.invictools.util.deathListener;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class ResetCommand implements SubCommand
{
    @Override
    public String getName()
    {
        return "reset";
    }

    @Override
    public String getDescription()
    {
        return null;
    }

    @Override
    public String getSyntax()
    {
        return "/it reset";
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
        if (sender instanceof Player)
        {
            Player p = (Player) sender;
            deathListener.clearEverything(p.getWorld());
        }
        else
        {
            deathListener.clearEverything(Bukkit.getWorld("bwlobby"));
        }

        sender.sendMessage(ChatColor.AQUA + "Scenarios mostly reset.");
    }
}
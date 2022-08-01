package me.invic.invictools.impl.IT;

import me.invic.invictools.commandManagerLib.SubCommand;
import me.invic.invictools.gamemodifiers.Manhunt.ManhuntMain;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.List;

public class ForceManhuntTeamCommand implements SubCommand
{
    @Override
    public String getName()
    {
        return "forceManhuntTeam";
    }

    @Override
    public String getDescription()
    {
        return null;
    }

    @Override
    public String getSyntax()
    {
        return "/it forceManhuntTeam";
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
        ManhuntMain.ManhuntTeam.put(Bukkit.getPlayer(args[1]), "Hunted");
        sender.sendMessage(ChatColor.YELLOW + " " + Bukkit.getPlayer(args[1]).getName() + ChatColor.AQUA + " will be hunted.");
    }
}
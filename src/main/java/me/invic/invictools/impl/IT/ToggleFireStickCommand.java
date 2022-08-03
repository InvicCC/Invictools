package me.invic.invictools.impl.IT;

import me.invic.invictools.commandManagerLib.SubCommand;
import me.invic.invictools.commands.OldCommands;
import me.invic.invictools.gamemodifiers.Manhunt.ManhuntMain;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.List;

import static me.invic.invictools.commands.OldCommands.FireStickEnabled;

public class ToggleFireStickCommand implements SubCommand
{
    @Override
    public String getName()
    {
        return "ToggleFireStick";
    }

    @Override
    public String getDescription()
    {
        return null;
    }

    @Override
    public String getSyntax()
    {
        return "/it ToggleFireStick";
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
        FireStickEnabled = !FireStickEnabled;

        sender.sendMessage(ChatColor.AQUA + "Firestick Enabled is now " + ChatColor.YELLOW + FireStickEnabled);
    }
}
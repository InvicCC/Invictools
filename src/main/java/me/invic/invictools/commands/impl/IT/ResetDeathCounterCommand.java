package me.invic.invictools.commands.impl.IT;

import me.invic.invictools.commands.commandManagerLib.SubCommand;
import me.invic.invictools.util.DeathCounter;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.List;

public class ResetDeathCounterCommand implements SubCommand
{
    @Override
    public String getName()
    {
        return "ResetDeathCounter";
    }

    @Override
    public String getDescription()
    {
        return null;
    }

    @Override
    public String getSyntax()
    {
        return "/it ResetDeathCounter";
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
        DeathCounter.resetCounter();
        sender.sendMessage(ChatColor.AQUA + "Death counter reset");
    }
}
package me.invic.invictools.commands.impl.IT;

import me.invic.invictools.commands.commandManagerLib.SubCommand;
import me.invic.invictools.gamemodifiers.AbtributesOnDeath;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.List;

public class ResetAttributesCommand implements SubCommand
{
    @Override
    public String getName()
    {
        return "ResetAttributes";
    }

    @Override
    public String getDescription()
    {
        return null;
    }

    @Override
    public String getSyntax()
    {
        return "/it ResetAttributes";
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
        AbtributesOnDeath.resetAttributes();
        sender.sendMessage(ChatColor.AQUA + "All players active attributes modifiers have been reset");
    }
}
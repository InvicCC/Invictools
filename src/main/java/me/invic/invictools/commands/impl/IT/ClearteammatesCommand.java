package me.invic.invictools.commands.impl.IT;

import me.invic.invictools.commands.commandManagerLib.SubCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.List;

import static me.invic.invictools.commands.OldCommands.teammates;

public class ClearteammatesCommand implements SubCommand
{
    @Override
    public String getName()
    {
        return "clearteammates";
    }

    @Override
    public String getDescription()
    {
        return null;
    }

    @Override
    public String getSyntax()
    {
        return "/it clearteammates";
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
        teammates.clear();
        sender.sendMessage(ChatColor.AQUA + "Teammates cleared...");
    }
}
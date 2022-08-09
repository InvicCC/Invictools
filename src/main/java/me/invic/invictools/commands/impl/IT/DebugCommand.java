package me.invic.invictools.commands.impl.IT;

import me.invic.invictools.commands.commandManagerLib.SubCommand;
import org.bukkit.command.CommandSender;

import java.util.List;

import static me.invic.invictools.commands.OldCommands.debug;

public class DebugCommand implements SubCommand
{
    @Override
    public String getName()
    {
        return "debug";
    }

    @Override
    public String getDescription()
    {
        return null;
    }

    @Override
    public String getSyntax()
    {
        return "/it debug";
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
        debug(sender);
    }
}
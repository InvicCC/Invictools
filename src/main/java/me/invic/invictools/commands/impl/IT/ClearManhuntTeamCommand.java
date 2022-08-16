package me.invic.invictools.commands.impl.IT;

import me.invic.invictools.commands.commandManagerLib.SubCommand;
import me.invic.invictools.gamemodes.Manhunt.ManhuntMain;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.List;

public class ClearManhuntTeamCommand implements SubCommand
{
    @Override
    public String getName()
    {
        return "clearManhuntTeam";
    }

    @Override
    public String getDescription()
    {
        return null;
    }

    @Override
    public String getSyntax()
    {
        return "/it clearManhuntTeam";
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
        ManhuntMain.ManhuntTeam.clear();
        ManhuntMain.HuntedItems.clear();
        ManhuntMain.HuntedEffects.clear();

        ManhuntMain.HunterItems.clear();
        ManhuntMain.HunterEffects.clear();
        sender.sendMessage(ChatColor.AQUA + "Manhunt team pre-assignments and data has been cleared");
    }
}
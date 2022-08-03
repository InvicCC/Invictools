package me.invic.invictools.impl.IT;

import me.invic.invictools.commandManagerLib.SubCommand;
import me.invic.invictools.cosmetics.VictoryDances.VictoryDancePreview;
import me.invic.invictools.gamemodifiers.Manhunt.ManhuntMain;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.List;

public class ToggleVictoryCommand implements SubCommand
{
    @Override
    public String getName()
    {
        return "ToggleVictory";
    }

    @Override
    public String getDescription()
    {
        return null;
    }

    @Override
    public String getSyntax()
    {
        return "/it ToggleVictory";
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
        VictoryDancePreview.VictoryPreviewEnabled = !VictoryDancePreview.VictoryPreviewEnabled;

        sender.sendMessage(ChatColor.AQUA + "Victory Dance Preview Enabled is now " + ChatColor.YELLOW + VictoryDancePreview.VictoryPreviewEnabled);
    }
}
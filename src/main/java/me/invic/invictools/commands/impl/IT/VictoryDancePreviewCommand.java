package me.invic.invictools.commands.impl.IT;

import me.invic.invictools.commands.commandManagerLib.SubCommand;
import me.invic.invictools.cosmetics.VictoryDances.VictoryDancePreview;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class VictoryDancePreviewCommand implements SubCommand
{
    @Override
    public String getName()
    {
        return "VictoryDancePreview";
    }

    @Override
    public String getDescription()
    {
        return "Previews Victory Dances";
    }

    @Override
    public String getSyntax()
    {
        return "/it VictoryDancePreview";
    }

    @Override
    public String getPermission()
    {
        return "invic.all";
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
        if (p.getWorld().getName().equalsIgnoreCase("bwlobby"))
        {
            new VictoryDancePreview().handle(args[1], p);
        }
    }
}
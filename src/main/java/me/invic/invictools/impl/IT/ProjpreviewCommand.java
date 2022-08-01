package me.invic.invictools.impl.IT;

import me.invic.invictools.commandManagerLib.SubCommand;
import me.invic.invictools.cosmetics.projtrail.ProjTrailPreview;
import me.invic.invictools.items.dareListener;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

import static me.invic.invictools.commands.OldCommands.FireStickEnabled;

public class ProjpreviewCommand implements SubCommand
{
    @Override
    public String getName()
    {
        return "projpreview";
    }

    @Override
    public String getDescription()
    {
        return "Previews projectile trails";
    }

    @Override
    public String getSyntax()
    {
        return "/it projpreview";
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
            new ProjTrailPreview().handle(args[1], p);
        }
    }
}
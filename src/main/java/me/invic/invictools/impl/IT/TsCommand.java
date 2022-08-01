package me.invic.invictools.impl.IT;

import me.invic.invictools.commandManagerLib.SubCommand;
import me.invic.invictools.cosmetics.projtrail.ProjTrailConfig;
import me.invic.invictools.util.fixes.TeamSelection;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.screamingsandals.bedwars.api.BedwarsAPI;

import java.util.List;

public class TsCommand implements SubCommand
{
    @Override
    public String getName()
    {
        return "ts";
    }

    @Override
    public String getDescription()
    {
        return null;
    }

    @Override
    public String getSyntax()
    {
        return "/it ts";
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
        new TeamSelection().openInventory((Player) sender, BedwarsAPI.getInstance().getGameOfPlayer((Player) sender), true);
        sender.sendMessage("Opened team selection");
    }
}
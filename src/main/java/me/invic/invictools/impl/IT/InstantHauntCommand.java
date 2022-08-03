package me.invic.invictools.impl.IT;

import me.invic.invictools.commandManagerLib.SubCommand;
import me.invic.invictools.gamemodifiers.Haunt;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.screamingsandals.bedwars.api.BedwarsAPI;

import java.util.List;
import java.util.Objects;

import static me.invic.invictools.commands.OldCommands.Hauntable;

public class InstantHauntCommand implements SubCommand
{
    @Override
    public String getName()
    {
        return "InstantHaunt";
    }

    @Override
    public String getDescription()
    {
        return null;
    }

    @Override
    public String getSyntax()
    {
        return "/it InstantHaunt";
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
        Player player = Bukkit.getPlayer(args[1]);
        player.sendMessage(ChatColor.AQUA + "You can now haunt living players.");
        new Haunt(player, args[2]);
    }
}
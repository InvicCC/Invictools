package me.invic.invictools.impl.IT;

import me.invic.invictools.commandManagerLib.SubCommand;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

import java.util.List;

public class RanksCommand implements SubCommand
{
    @Override
    public String getName()
    {
        return "ranks";
    }

    @Override
    public String getDescription()
    {
        return "Shows obtainable ranks";
    }

    @Override
    public String getSyntax()
    {
        return "/it ranks";
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
        Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("Invictools");
        final FileConfiguration Config = plugin.getConfig();
        List<String> messages = Config.getStringList("RankMessages");
        for (String message : messages)
        {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
        }
    }
}
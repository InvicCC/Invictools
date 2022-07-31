package me.invic.invictools.impl;

import me.invic.invictools.commandManagerLib.SubCommand;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class TrackerCommand implements SubCommand
{
    @Override
    public String getName()
    {
        return "tracker";
    }

    @Override
    public String getDescription()
    {
        return null;
    }

    @Override
    public String getSyntax()
    {
        return "/it tracker";
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
        Player player = (Player) sender;
        Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("Invictools");
        //   File pFile = new File(plugin.getDataFolder().getPath() + "\\PlayerData" + File.separator + player.getUniqueId() + ".yml");
        File Folder = new File(plugin.getDataFolder(), "PlayerData");
        File pFile = new File(Folder, player.getUniqueId() + ".yml");
        final FileConfiguration playerData = YamlConfiguration.loadConfiguration(pFile);

        int tracker = playerData.getInt("tracker");
        tracker++;
        playerData.set("tracker", tracker);
        try
        {
            playerData.save(pFile);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
package me.invic.invictools.util.physics;

import me.invic.invictools.commands.Commands;
import me.invic.invictools.commands.toggleCommands;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;

public class MasterPlayerJoin implements Listener
{
    @EventHandler
    public void WorldSwitch(PlayerChangedWorldEvent e) // update to dynamically grab valid worlds
    {
        probe();

        if (Commands.MasterPlayer instanceof Player && e.getPlayer().getName().equalsIgnoreCase(Commands.MasterPlayer.getName()))
        {
            if (e.getPlayer().getWorld().getName().equalsIgnoreCase("Tree"))
                new grabSandstone(e.getPlayer());
        }
    }

    @EventHandler
    public void Join(PlayerJoinEvent e)
    {
        /*
        Plugin plugin = Commands.Invictools;
        final FileConfiguration config = plugin.getConfig();
        String masterplayer = config.getString("masterplayer","Invictable");
        Commands.MasterPlayer = Bukkit.getPlayer(masterplayer);
         */
        probe();
    }

    @EventHandler
    public void leave(PlayerQuitEvent e)
    {
        probe();
    }

    public void probe()
    {
        try
        {
            Commands.MasterPlayer.sendMessage(" ");
        }
        catch (NullPointerException e)
        {
            if(!toggleCommands.isHosting && Bukkit.getOnlinePlayers().size() > 0)
            {
                Player next = Bukkit.getOnlinePlayers().iterator().next();
                FileConfiguration config = Commands.Invictools.getConfig();
                Commands.MasterPlayer = next;
                config.set("masterplayer", next);
                Commands.Invictools.saveConfig();
            }
        }
    }
}

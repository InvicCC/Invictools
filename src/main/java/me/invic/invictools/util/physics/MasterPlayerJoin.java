package me.invic.invictools.util.physics;

import me.invic.invictools.commands.Commands;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;

public class MasterPlayerJoin implements Listener
{
    @EventHandler
    public void WorldSwitch(PlayerChangedWorldEvent e) // update to dynamically grab valid worlds
    {
        Plugin plugin = Commands.Invictools;
        final FileConfiguration config = plugin.getConfig();
        String masterplayer = config.getString("masterplayer");
        Commands.MasterPlayer = Bukkit.getPlayer(masterplayer);

        if (e.getPlayer().getName().equalsIgnoreCase(masterplayer))
        {
            if (e.getPlayer().getWorld().getName().equalsIgnoreCase("Tree"))
                new grabSandstone(e.getPlayer());
            /*
            else if(e.getPlayer().getWorld().getName().equalsIgnoreCase("bwlobby"))
                new grabSandstone(e.getPlayer());

             */
        }
    }

    @EventHandler
    public void Join(PlayerJoinEvent e)
    {
        Plugin plugin = Commands.Invictools;
        final FileConfiguration config = plugin.getConfig();
        String masterplayer = config.getString("masterplayer");
        Commands.MasterPlayer = Bukkit.getPlayer(masterplayer);
    }
}

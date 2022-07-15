package me.invic.invictools.cosmetics;

import me.invic.invictools.Commands;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class LobbyListener implements Listener
{
    @EventHandler
    public void world(PlayerChangedWorldEvent e)
    {
        if (e.getPlayer().getWorld().getName().equalsIgnoreCase("bwlobby") && !e.getPlayer().getWorld().equals(e.getFrom()))
            Lobby1Handler.FullHandle(e.getPlayer());
    }

    @EventHandler
    public void join(PlayerJoinEvent e) // waiting for spawn teleport if in world
    {
        new BukkitRunnable()
        {
            @Override
            public void run()
            {
                if (e.getPlayer().getWorld().getName().equalsIgnoreCase("bwlobby"))
                    Lobby1Handler.FullHandle(e.getPlayer());
            }
        }.runTaskLater(Commands.Invictools, 20L);
    }
}

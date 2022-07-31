package me.invic.invictools.util.fixes;

import me.invic.invictools.commands.OldCommands;
import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

public class lobbyDestroyFix implements Listener
{
    @EventHandler
    public void fixDestroy(BlockBreakEvent e)
    {
        if (!e.getPlayer().equals(OldCommands.MasterPlayer) && e.getPlayer().getWorld().getName().equals("bwlobby"))
            e.setCancelled(true);

        if (e.getPlayer().equals(OldCommands.MasterPlayer) && !e.getPlayer().getGameMode().equals(GameMode.CREATIVE) && e.getPlayer().getWorld().getName().equals("bwlobby"))
            e.setCancelled(true);
    }

    @EventHandler
    public void fixPlace(BlockPlaceEvent e)
    {
        if (!e.getPlayer().equals(OldCommands.MasterPlayer) && e.getPlayer().getWorld().getName().equals("bwlobby"))
            e.setCancelled(true);

        if (e.getPlayer().equals(OldCommands.MasterPlayer) && !e.getPlayer().getGameMode().equals(GameMode.CREATIVE) && e.getPlayer().getWorld().getName().equals("bwlobby"))
            e.setCancelled(true);
    }
}

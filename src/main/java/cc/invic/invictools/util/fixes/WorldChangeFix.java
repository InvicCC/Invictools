package cc.invic.invictools.util.fixes;

import cc.invic.invictools.impl.Commands;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPortalEvent;

public class WorldChangeFix implements Listener
{
    @EventHandler
    public void WorldChangeFix(PlayerPortalEvent e)
    {
        if (!Commands.worldswap)
            e.setCancelled(true);
    }
}

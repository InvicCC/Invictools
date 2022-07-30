package cc.invic.invictools.util.physics;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPhysicsEvent;

public class CancelLampUpdates implements Listener
{
    @EventHandler
    public void CancelLamp(BlockPhysicsEvent e)
    {
        if (e.getBlock().getType().equals(Material.REDSTONE_LAMP) && e.getBlock().getWorld().getName().equalsIgnoreCase("tree") || e.getBlock().getWorld().getName().equalsIgnoreCase("bwlobby"))
            e.setCancelled(true);
    }
}

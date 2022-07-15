package me.invic.invictools.util.physics;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFormEvent;

public class CancelConcreteChange implements Listener
{
    @EventHandler()
    public void OnConcreteChangeEvent(BlockFormEvent e)
    {
        if (e.getBlock().getWorld().getName().equalsIgnoreCase("ocean"))
            if (e.getBlock().getType() == Material.CYAN_CONCRETE_POWDER)
                e.setCancelled(true);
    }
}

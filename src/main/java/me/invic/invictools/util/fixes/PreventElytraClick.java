package me.invic.invictools.util.fixes;

import me.invic.invictools.Commands;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class PreventElytraClick implements Listener
{
    @EventHandler
    final void ClickListener(InventoryClickEvent e)
    {
        Player p = (Player) e.getWhoClicked();
        if (Commands.ProximityElytra.get(p) != null)
            if (Commands.ProximityElytra.get(p))
                if (e.getSlot() == 38)
                    e.setCancelled(true);
    }
}

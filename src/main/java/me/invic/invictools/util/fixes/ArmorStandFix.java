package me.invic.invictools.util.fixes;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;

public class ArmorStandFix implements Listener
{
    @EventHandler
    public void ArmorStandFix(PlayerArmorStandManipulateEvent e)
    {
        e.setCancelled(true);
    }
}

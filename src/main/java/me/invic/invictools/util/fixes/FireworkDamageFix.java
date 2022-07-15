package me.invic.invictools.util.fixes;

import org.bukkit.entity.Firework;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class FireworkDamageFix implements Listener
{
    // fixes fireworks dealing damage on bedbreak
    @EventHandler
    public void fireworkDamage(EntityDamageByEntityEvent e)
    {
        if (e.getDamager() instanceof Firework) {
            Firework fw = (Firework) e.getDamager();
            if (fw.hasMetadata("nodamage")) {
                e.setCancelled(true);
            }
        }
    }
}

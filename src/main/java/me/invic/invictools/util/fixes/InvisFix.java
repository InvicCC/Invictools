package me.invic.invictools.util.fixes;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.potion.PotionEffectType;

public class InvisFix implements Listener
{
    @EventHandler
    public void InvisWatcher(EntityDamageEvent e)
    {
        if(e.getEntity() instanceof Player)
        {
            if(!e.getCause().equals(EntityDamageEvent.DamageCause.DROWNING)
            && !e.getCause().equals(EntityDamageEvent.DamageCause.FALL)
            && !e.getCause().equals(EntityDamageEvent.DamageCause.FIRE)
            && !e.getCause().equals(EntityDamageEvent.DamageCause.FIRE_TICK)
            && !e.getCause().equals(EntityDamageEvent.DamageCause.SUFFOCATION)
            && !e.getCause().equals(EntityDamageEvent.DamageCause.FLY_INTO_WALL)
            && !e.getCause().equals(EntityDamageEvent.DamageCause.POISON)
            && !e.getCause().equals(EntityDamageEvent.DamageCause.WITHER))
            {
                Player p = (Player) e.getEntity();
                if(p.hasPotionEffect(PotionEffectType.INVISIBILITY))
                    p.removePotionEffect(PotionEffectType.INVISIBILITY);
            }
        }
    }
}

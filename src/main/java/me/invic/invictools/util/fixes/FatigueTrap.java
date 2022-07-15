package me.invic.invictools.util.fixes;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPotionEffectEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class FatigueTrap implements Listener
{
    @EventHandler
    public void FatigueTrap(EntityPotionEffectEvent e)
    {
        if (e.getEntity().getType() != EntityType.PLAYER)
            return;

        try
        {
            if (e.getNewEffect().getType().equals(PotionEffectType.BLINDNESS))
            {
                Player player = (Player) e.getEntity();
                player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 20 * 7, 0, false, true));
            }
        }
        catch (NullPointerException ignored)
        {
        }
    }
}

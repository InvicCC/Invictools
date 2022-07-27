package me.invic.invictools.util.fixes;

import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.api.ViaAPI;
import me.invic.invictools.commands.Commands;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPotionEffectEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import static me.invic.invictools.items.ItemListener.Falling;

public class Protocol47Fix implements Listener
{
    @EventHandler
    public void FatigueTrap(EntityPotionEffectEvent e)
    {
        if (e.getEntity().getType() != EntityType.PLAYER)
            return;

        if(e.getNewEffect() ==null)
            return;

        if(e.getNewEffect().getType().equals(PotionEffectType.SLOW_FALLING) && Via.getAPI().getPlayerVersion(e.getEntity()) == 47 && false)
        {
            Falling.add(e.getEntity());
            new BukkitRunnable()
            {
                @Override
                public void run()
                {
                    Falling.remove(e.getEntity());
                }
            }.runTaskLater(Commands.Invictools, e.getNewEffect().getDuration()* 20L);
        }
    }

  //  ViaAPI api = Via.getPlatform().getApi();
    @EventHandler
    public void joinevent(PlayerJoinEvent e)
    {

        new BukkitRunnable()
        {
            @Override
            public void run()
            {
                Player p = e.getPlayer();
                //if(api.getPlayerVersion(e.getPlayer()) == 47)
                {
                //    p.sendTitle(ChatColor.translateAlternateColorCodes('&', "&c" + "1.8 detected"), ChatColor.translateAlternateColorCodes('&', "&r&fUse 1.19 or some features will not work"), 10, 5 * 20, 15);
           //         p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1F);
                }
            }
        }.runTaskLater(Commands.Invictools, 100L);
    }
}

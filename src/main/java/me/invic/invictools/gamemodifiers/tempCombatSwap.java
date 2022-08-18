package me.invic.invictools.gamemodifiers;

import me.invic.invictools.util.fixes.SafeOpCommand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.screamingsandals.bedwars.api.events.BedwarsPlayerLeaveEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class tempCombatSwap implements Listener
{
    static List<Player> swapped = new ArrayList<>();

    public static void swap(Player p)
    {
        new SafeOpCommand(p,"ocm toggle");
        swapped.add(p);
    }

    @EventHandler
    public void preventDamage(EntityDamageByEntityEvent e)
    {
        if(e.getDamager() instanceof Player p && swapped.contains(p) && p.getAttackCooldown() != 1.0)
        {
            e.setDamage(0.5);
        }
    }

    @EventHandler
    public void bwleave(BedwarsPlayerLeaveEvent e)
    {
        swapped.remove(e.getPlayer());
    }

    @EventHandler
    public void disconnect(PlayerQuitEvent e)
    {
        swapped.remove(e.getPlayer());
    }
}

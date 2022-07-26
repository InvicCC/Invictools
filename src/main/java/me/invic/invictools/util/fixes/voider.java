package me.invic.invictools.util.fixes;

import me.invic.invictools.util.LobbyLogic;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.screamingsandals.bedwars.api.events.BedwarsGameStartEvent;
import org.screamingsandals.bedwars.api.events.BedwarsPlayerLeaveEvent;

import java.util.HashMap;

public class voider implements Listener
{
    HashMap<Player,Integer> voidLevel = new HashMap<>();
    public static boolean shouldVoidCheck = true;

    @EventHandler
    public void bwstart(BedwarsGameStartEvent e)
    {
        if(!shouldVoidCheck) // disablable for performence in case of 100 player events etc
            return;

        FileConfiguration config = new LobbyLogic().getMapConfiguration(e.getGame().getName());
        for (Player p: e.getGame().getConnectedPlayers())
        {
            voidLevel.put(p,config.getInt("Void",0));
        }

    }

    @EventHandler
    public void bwend(BedwarsPlayerLeaveEvent e)
    {
        voidLevel.remove(e.getPlayer());
    }

    @EventHandler
    public void VoidWatcher(PlayerMoveEvent e)
    {
        if(e.getPlayer().getWorld().getName().equalsIgnoreCase("bwlobby") && e.getPlayer().getLocation().getY() <=5)
        {
            Bukkit.dispatchCommand(e.getPlayer(), "spawn");
        }
        else if(voidLevel.get(e.getPlayer()) != null)
        {
            if (e.getPlayer().getLocation().getY() < voidLevel.get(e.getPlayer()))
            {
                e.getPlayer().damage(e.getPlayer().getHealth() + 9999999);
               // new EntityDamageEvent(e.getPlayer(), EntityDamageEvent.DamageCause.VOID, e.getPlayer().getHealth() + 9999999);
            }
        }
    }
}

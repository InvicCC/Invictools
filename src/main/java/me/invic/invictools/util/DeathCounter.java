package me.invic.invictools.util;

import me.invic.invictools.commands.OldCommands;
import me.invic.invictools.gamemodifiers.AbtributesOnDeath;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.screamingsandals.bedwars.api.events.BedwarsGameEndingEvent;
import org.screamingsandals.bedwars.api.events.BedwarsPlayerKilledEvent;

import java.util.HashMap;
import java.util.List;

public class DeathCounter implements Listener
{
    public static HashMap<Player, Integer> deaths = new HashMap<>();

    public static void InitializeDeathCounter(List<Player> players)
    {
        if (deaths.containsKey(players.get(0)))
            OldCommands.MasterPlayer.sendMessage(ChatColor.RED + "Death counter is being reinitialized");

        for (Player player : players)
        {
            if (!deaths.containsKey(player))
                deaths.put(player, 0);
        }
    }

    public static void resetCounter()
    {
        deaths.clear();
    }

    @EventHandler
    private void DeathWatcher(BedwarsPlayerKilledEvent e)
    {
        if (deaths.containsKey(e.getPlayer()))
            deaths.put(e.getPlayer(), deaths.get(e.getPlayer()) + 1);
    }

    @EventHandler
    private void ResetOnGameEnd(BedwarsGameEndingEvent e)
    {
       // resetCounter();
        AbtributesOnDeath.resetAttributes();
    }
}

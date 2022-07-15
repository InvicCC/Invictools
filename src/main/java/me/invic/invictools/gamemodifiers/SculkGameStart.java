package me.invic.invictools.gamemodifiers;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.screamingsandals.bedwars.api.events.BedwarsGameEndingEvent;
import org.screamingsandals.bedwars.api.events.BedwarsGameStartedEvent;

public class SculkGameStart implements Listener
{
    @EventHandler
    public void gameStart(BedwarsGameStartedEvent e)
    {
        if (e.getGame().getName().equalsIgnoreCase("Sculk"))
            new WardenSpawner().shriekerFromPlayer(e.getGame().getConnectedPlayers().get(0), false);
    }

    @EventHandler
    public void gameEnd(BedwarsGameEndingEvent e)
    {
        if (e.getGame().getName().equalsIgnoreCase("Sculk"))
            new WardenSpawner().shriekerFromPlayer(e.getGame().getConnectedPlayers().get(0), true);
    }
}

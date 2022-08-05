package me.invic.invictools.gamemodifiers;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.screamingsandals.bedwars.api.events.BedwarsGameEndEvent;
import org.screamingsandals.bedwars.api.events.BedwarsPlayerJoinEvent;
import org.screamingsandals.bedwars.api.game.Game;

import java.util.HashMap;

public class perGameJumpingListener implements Listener
{
    public static HashMap<Game,perGameJumpingHandler> jumpInfo = new HashMap<>();

    @EventHandler
    public void playerJoin(BedwarsPlayerJoinEvent e)
    {
        if(!jumpInfo.containsKey(e.getGame()))
            jumpInfo.put(e.getGame(),new perGameJumpingHandler());
    }

    @EventHandler
    public void gameEnd(BedwarsGameEndEvent e)
    {
        jumpInfo.remove(e.getGame());
    }
}

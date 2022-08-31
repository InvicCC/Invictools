package me.invic.invictools.gamemodifiers;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.screamingsandals.bedwars.api.BedwarsAPI;
import org.screamingsandals.bedwars.api.events.BedwarsGameChangedStatusEvent;
import org.screamingsandals.bedwars.api.events.BedwarsGameEndEvent;
import org.screamingsandals.bedwars.api.game.Game;
import org.screamingsandals.bedwars.api.game.GameStatus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class infestation implements Listener
{
    static List<Game> activeInfestation = new ArrayList<>();

    public static void addGame(Game game)
    {
        activeInfestation.add(game);
    }

    public static void removeGame(Game game)
    {
        activeInfestation.remove(game);
    }

    @EventHandler
    public void damage(EntityDamageByEntityEvent e)
    {
        if(e.getDamager() instanceof Player p && e.getEntity() instanceof Player spawnP && BedwarsAPI.getInstance().isPlayerPlayingAnyGame(p))
        {
            List<EntityType> mobs = new ArrayList<>();
            for (EntityType type:EntityType.values())
            {
                if(type.isSpawnable())
                    mobs.add(type);
            }
            Collections.shuffle(mobs);
            spawnP.getWorld().spawnEntity(spawnP.getLocation(), mobs.get(0));
        }
    }

    @EventHandler
    public void gameEnd(BedwarsGameEndEvent e)
    {
        removeGame(e.getGame());
    }

    @EventHandler
    public void statusChange(BedwarsGameChangedStatusEvent e)
    {
        System.out.println(e.getGame().getStatus());
        if(e.getGame().getStatus().equals(GameStatus.RUNNING))
        {
            removeGame(e.getGame());
        }
    }
}

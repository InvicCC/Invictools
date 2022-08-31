package me.invic.invictools.gamemodifiers;

import me.invic.invictools.commands.OldCommands;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;
import org.screamingsandals.bedwars.api.RunningTeam;
import org.screamingsandals.bedwars.api.events.BedwarsGameEndEvent;
import org.screamingsandals.bedwars.api.events.BedwarsGameStartEvent;
import org.screamingsandals.bedwars.api.events.BedwarsGameTickEvent;
import org.screamingsandals.bedwars.api.game.Game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class albi implements Listener
{
    static HashMap<Game, List<Entity>> activeDragons = new HashMap<>();
    static List<String> dragonNames = new ArrayList<>();

    public albi()
    {
        dragonNames.add(ChatColor.translateAlternateColorCodes('&',"&dAlbi"));
        dragonNames.add(ChatColor.translateAlternateColorCodes('&',"&dBrother of Albi"));
        dragonNames.add(ChatColor.translateAlternateColorCodes('&',"&dSister of Albi"));
        dragonNames.add(ChatColor.translateAlternateColorCodes('&',"&dAlibi's Cousin"));
        dragonNames.add(ChatColor.translateAlternateColorCodes('&',"&dAlibi's Father"));
        dragonNames.add(ChatColor.translateAlternateColorCodes('&',"&dMother of Albi"));
        dragonNames.add(ChatColor.translateAlternateColorCodes('&',"&dAlbi the second"));
        dragonNames.add(ChatColor.translateAlternateColorCodes('&',"&dAlbi the 19th"));
        dragonNames.add(ChatColor.translateAlternateColorCodes('&',"&dAlbi the 21st"));
        dragonNames.add(ChatColor.translateAlternateColorCodes('&',"&dAlbitha"));
        dragonNames.add(ChatColor.translateAlternateColorCodes('&',"&dAlbert"));
        dragonNames.add(ChatColor.translateAlternateColorCodes('&',"&dAlbison"));
        dragonNames.add(ChatColor.translateAlternateColorCodes('&',"&dAlberta"));
        dragonNames.add(ChatColor.translateAlternateColorCodes('&',"&dAlbeee"));
        dragonNames.add(ChatColor.translateAlternateColorCodes('&',"&dAlbi demon mode"));
    }

    public static void spawnDragon(Game game)
    {
        if(dragonNames.isEmpty())
            new albi();

        Collections.shuffle(dragonNames);

        EnderDragon dragon = (EnderDragon) game.getGameWorld().spawnEntity(game.getLobbySpawn(), EntityType.ENDER_DRAGON);
        dragon.setCustomName(dragonNames.get(0));
        dragonNames.remove(dragonNames.get(0));
        dragon.setAware(true);
        dragon.setPhase(EnderDragon.Phase.SEARCH_FOR_BREATH_ATTACK_TARGET);

        List<Entity> ds = activeDragons.getOrDefault(game,new ArrayList<>());
        ds.add(dragon);
        activeDragons.put(game,ds);
    }

    public static void spawnDragon(Game game, Player p)
    {
        if(dragonNames.isEmpty())
            new albi();

        Collections.shuffle(dragonNames);

        EnderDragon dragon = (EnderDragon) game.getGameWorld().spawnEntity(game.getLobbySpawn(), EntityType.ENDER_DRAGON);
        dragon.setCustomName(dragonNames.get(0));
        dragonNames.remove(dragonNames.get(0));
        dragon.setAware(true);
        dragon.attack(p);
        dragon.setPhase(EnderDragon.Phase.STRAFING);
        dragon.attack(p);

        List<Entity> ds = activeDragons.getOrDefault(game,new ArrayList<>());
        ds.add(dragon);
        activeDragons.put(game,ds);
    }
    static List<Game> gameDesignation = new ArrayList<>();
    @EventHandler
    public void end(BedwarsGameEndEvent e)
    {
        gameDesignation.remove(e.getGame());
        if(activeDragons.containsKey(e.getGame()))
        {
            for (Entity dragon:activeDragons.get(e.getGame()))
            {
                if(!dragon.isDead())
                    dragon.remove();
            }
            activeDragons.remove(e.getGame());
        }
    }

    @EventHandler
    public void midRoundDragons(BedwarsGameStartEvent e)
    {
        gameDesignation.add(e.getGame());
        new BukkitRunnable()
        {
            @Override
            public void run()
            {
                if(gameDesignation.contains(e.getGame()))
                {
                    for (Player p:e.getGame().getConnectedPlayers())
                    {
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&b&l(!) &r&fDragons spawn in &b2&f minutes!"));
                        p.playSound(p.getLocation(), Sound.ENTITY_ENDER_DRAGON_FLAP, 1, 1);
                    }

                    new BukkitRunnable()
                    {
                        @Override
                        public void run()
                        {
                            if(gameDesignation.contains(e.getGame()))
                            {
                                for (RunningTeam team : e.getGame().getRunningTeams())
                                {
                                    spawnDragon(e.getGame());
                                }
                            }
                        }
                    }.runTaskLater(OldCommands.Invictools, 120*20);
                }
            }
        }.runTaskLater(OldCommands.Invictools, 1800*20);
    }
}

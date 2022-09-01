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
import org.bukkit.event.entity.EnderDragonChangePhaseEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.screamingsandals.bedwars.api.BedwarsAPI;
import org.screamingsandals.bedwars.api.RunningTeam;
import org.screamingsandals.bedwars.api.events.BedwarsGameEndEvent;
import org.screamingsandals.bedwars.api.events.BedwarsGameStartEvent;
import org.screamingsandals.bedwars.api.events.BedwarsGameTickEvent;
import org.screamingsandals.bedwars.api.game.Game;

import java.util.*;

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

        EnderDragon dragon = (EnderDragon) game.getGameWorld().spawnEntity(game.getLobbySpawn().add(new Random().nextInt(50)-25,new Random().nextInt(10)-20,new Random().nextInt(30)-15), EntityType.ENDER_DRAGON);
        dragon.setCustomName(dragonNames.get(0));
        dragonNames.remove(dragonNames.get(0));
        dragon.setAware(true);
        dragon.setPhase(EnderDragon.Phase.STRAFING);
        dragon.setTarget(game.getConnectedPlayers().get(new Random().nextInt(game.getConnectedPlayers().size()-1)));

        List<Entity> ds = activeDragons.getOrDefault(game,new ArrayList<>());
        ds.add(dragon);
        activeDragons.put(game,ds);
    }

    public static void spawnDragon(Game game, Player p)
    {
        if(dragonNames.isEmpty())
            new albi();

        Collections.shuffle(dragonNames);
        EnderDragon dragon = (EnderDragon) game.getGameWorld().spawnEntity(game.getLobbySpawn().add(new Random().nextInt(30)-15,new Random().nextInt(10)-20,new Random().nextInt(30)-15), EntityType.ENDER_DRAGON);
        dragon.setCustomName(dragonNames.get(0));
        dragonNames.remove(dragonNames.get(0));
        dragon.setAware(true);
        dragon.setPhase(EnderDragon.Phase.STRAFING);
        dragon.setTarget(p);

        List<Entity> ds = activeDragons.getOrDefault(game,new ArrayList<>());
        ds.add(dragon);
        activeDragons.put(game,ds);
    }
    static List<Game> gameDesignation = new ArrayList<>();

    @EventHandler
    public void damage(EntityDamageEvent e)
    {
        if(e.getCause().equals(EntityDamageEvent.DamageCause.DRAGON_BREATH))
        {
            System.out.println("reduction");
            e.setDamage(1.0);
        }
    }

    @EventHandler
    public void target(EnderDragonChangePhaseEvent e)
    {
        Game currentGame = null;
        for (Game game:gameDesignation)
        {
            if(game.getGameWorld().equals(e.getEntity().getWorld()))
            {
                currentGame = game;
            }
        }

        if(currentGame == null)
            return;

        if((e.getNewPhase().equals(EnderDragon.Phase.FLY_TO_PORTAL)/* && new Random().nextInt(5) >2) || e.getNewPhase().equals(EnderDragon.Phase.HOVER)*/))
        {
            if (new Random().nextInt(3)!=1)
            {
                e.getEntity().setTarget(currentGame.getConnectedPlayers().get(new Random().nextInt(currentGame.getConnectedPlayers().size() - 1)));
                e.setNewPhase(EnderDragon.Phase.CHARGE_PLAYER);
                e.getEntity().setTarget(currentGame.getConnectedPlayers().get(new Random().nextInt(currentGame.getConnectedPlayers().size() - 1)));
            }
            else
            {
                e.setNewPhase(EnderDragon.Phase.STRAFING);
            }
        }
        /*
        else
        {
            System.out.println("temp hover");
            e.setNewPhase(EnderDragon.Phase.ROAR_BEFORE_ATTACK);
            e.setNewPhase(EnderDragon.Phase.HOVER);
            new BukkitRunnable()
            {
                @Override
                public void run()
                {
                    if(!e.getEntity().isDead())
                        e.setNewPhase(EnderDragon.Phase.STRAFING);
                }
            }.runTaskLater(OldCommands.Invictools, 20*5L);
        }

         */
    }

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

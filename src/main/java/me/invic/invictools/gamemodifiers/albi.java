package me.invic.invictools.gamemodifiers;

import me.invic.invictools.commands.OldCommands;
import me.invic.invictools.cosmetics.projtrail.ProjTrailHandler;
import me.invic.invictools.util.disableStats;
import me.invic.invictools.util.ingame.blockDecay;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EnderDragonChangePhaseEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
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
        dragonNames.clear();
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
        if(dragonNames.isEmpty() || dragonNames.get(0) == null)
            new albi();

        int y;

        if(disableStats.getGameType(game).equalsIgnoreCase("bedfight"))
            y=20;
        else
            y=50;

        Collections.shuffle(dragonNames);
        EnderDragon dragon = (EnderDragon) game.getGameWorld().spawnEntity(game.getLobbySpawn().clone().add(new Random().nextInt(50)-25,-25,new Random().nextInt(50)-25), EntityType.ENDER_DRAGON);
        dragon.setCustomName(dragonNames.get(0));
        dragonNames.remove(dragonNames.get(0));
        dragon.setAware(true);
        dragon.setTarget(game.getConnectedPlayers().get(new Random().nextInt(game.getConnectedPlayers().size()-1)));
        dragon.setPhase(EnderDragon.Phase.CHARGE_PLAYER);

        List<Entity> ds = activeDragons.getOrDefault(game,new ArrayList<>());
        ds.add(dragon);
        activeDragons.put(game,ds);
        new ProjTrailHandler().Lava(dragon);
    }

    public static void spawnDragon(Game game, Player p)
    {
        if(dragonNames.isEmpty() || dragonNames.get(0) == null)
            new albi();

        int y;

        if(disableStats.getGameType(game).equalsIgnoreCase("bedfight"))
            y=20;
        else
            y=50;

        Collections.shuffle(dragonNames);
        EnderDragon dragon = (EnderDragon) game.getGameWorld().spawnEntity(game.getLobbySpawn().clone().add(new Random().nextInt(50)-25,-25,new Random().nextInt(50)-25), EntityType.ENDER_DRAGON);
        dragonNames.remove(dragonNames.get(0));
        dragon.setAware(true);
        dragon.setTarget(p);
        dragon.setPhase(EnderDragon.Phase.CHARGE_PLAYER);


        List<Entity> ds = activeDragons.getOrDefault(game,new ArrayList<>());
        ds.add(dragon);
        activeDragons.put(game,ds);
        new ProjTrailHandler().team(dragon,p);
    }
    static List<Game> gameDesignation = new ArrayList<>();

    @EventHandler
    public void damage(EntityDamageByEntityEvent e)
    {
       // System.out.println(e.getDamager().getName());
        if(e.getDamager().getType().equals(EntityType.ENDER_DRAGON))
        {
            e.setDamage(0.0);
        }
        else if(e.getDamager().getType().equals(EntityType.AREA_EFFECT_CLOUD))
        {
            //e.setDamage(2.0)
            new BukkitRunnable()
            {
                @Override
                public void run()
                {
                    if(!e.getDamager().isDead())
                        e.getDamager().remove();
                }
            }.runTaskLater(OldCommands.Invictools, 20L);
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

        if((disableStats.getGameType(currentGame).equalsIgnoreCase("bedfight") && e.getEntity().getLocation().distance(new Location(e.getEntity().getWorld(),0,100,0))<150)
        || (!disableStats.getGameType(currentGame).equalsIgnoreCase("bedfight") && e.getEntity().getLocation().getY() >= blockDecay.maxY.get(currentGame)))
        {
            Location loc = currentGame.getLobbySpawn().clone();
            loc.setY(blockDecay.maxY.get(currentGame)-25);
            e.getEntity().teleport(loc);
        }

        if(e.getNewPhase().equals(EnderDragon.Phase.FLY_TO_PORTAL))
        {
            if (new Random().nextInt(20)==1)
            {
                int wait = new Random().nextInt(3)+2;
                e.setNewPhase(EnderDragon.Phase.HOVER);
                new BukkitRunnable()
                {
                    @Override
                    public void run()
                    {
                        if(!e.getEntity().isDead())
                            e.getEntity().setPhase(EnderDragon.Phase.SEARCH_FOR_BREATH_ATTACK_TARGET);
                    }
                }.runTaskLater(OldCommands.Invictools, wait*20);
            }
            else
            {
                if(new Random().nextInt(3)!=1)
                {
                    if(new Random().nextInt(3)==1)
                        e.setNewPhase(EnderDragon.Phase.STRAFING);
                    else
                        e.setNewPhase(EnderDragon.Phase.CIRCLING);
                }
                else
                {
                    e.getEntity().setTarget(currentGame.getConnectedPlayers().get(new Random().nextInt(currentGame.getConnectedPlayers().size())-1));
                    e.getEntity().setPhase(EnderDragon.Phase.CHARGE_PLAYER);
                }
            }
        }

        if(e.getNewPhase().equals(EnderDragon.Phase.LAND_ON_PORTAL))
        {
            e.setNewPhase(EnderDragon.Phase.CIRCLING);
        }

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
                                    spawnDragon(e.getGame(),team.getConnectedPlayers().get(0));
                                }
                            }
                        }
                    }.runTaskLater(OldCommands.Invictools, 120*20);
                }
            }
        }.runTaskLater(OldCommands.Invictools, 1800*20);
    }
}

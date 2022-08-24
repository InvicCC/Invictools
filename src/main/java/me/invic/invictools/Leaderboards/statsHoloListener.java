package me.invic.invictools.Leaderboards;

import me.invic.invictools.commands.OldCommands;
import me.invic.invictools.commands.toggleCommands;
import me.invic.invictools.cosmetics.statisticRequirments;
import me.invic.invictools.gamemodifiers.WardenSpawner;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.screamingsandals.bedwars.api.events.BedwarsPlayerJoinEvent;

import java.util.HashMap;

public class statsHoloListener implements Listener
{
    public static HashMap<Player,statsHolo> activeBedfightHolos = new HashMap<>();
    public static HashMap<Player,statsHolo> activeBedwarsHolos = new HashMap<>();
    final static Location bedfightLocation = new WardenSpawner().locationFromConfig(OldCommands.Invictools.getConfig().getString("Holo.Bedfight","bwlobby;225;129;225;0;0"));
    final static Location bedwarsLocation = new WardenSpawner().locationFromConfig(OldCommands.Invictools.getConfig().getString("Holo.Bedwars","bwlobby;275;129;225;0;0"));
    final static String bedfightHeader = ChatColor.translateAlternateColorCodes('&',"&b&lYour &f&lBEDFIGHT &b&lStatistics");
    final static String bedwarsHeader = ChatColor.translateAlternateColorCodes('&',"&b&lYour &f&lBEDWARS &b&lStatistics");

    @EventHandler
    public void join(PlayerJoinEvent e)
    {
        new BukkitRunnable()
        {
            @Override
            public void run()
            {
                for (Player p:activeBedwarsHolos.keySet())
                {
                 //   e.getPlayer().sendMessage("destroying active");
                    statsHolo.sendPacket(e.getPlayer(),activeBedwarsHolos.get(p).getStands());
                    statsHolo.sendPacket(e.getPlayer(),activeBedfightHolos.get(p).getStands());
                }

            //    e.getPlayer().sendMessage("building new");
                buildGeneric(e.getPlayer());
            }
        }.runTaskLater(OldCommands.Invictools, 40L);
    }

    /*
    @EventHandler
    public void bwleave(BedwarsPlayerLeaveEvent e)
    {
        new BukkitRunnable()
        {
            @Override
            public void run()
            {
                for (Player p:activeBedwarsHolos.keySet())
                {
                //    e.getPlayer().sendMessage("destroying active");
                    statsHolo.sendPacket(e.getPlayer(),activeBedwarsHolos.get(p).getStands());
                    statsHolo.sendPacket(e.getPlayer(),activeBedfightHolos.get(p).getStands());
                }

         //   e.getPlayer().sendMessage("building new");
            buildGeneric(e.getPlayer());
            }
        }.runTaskLater(OldCommands.Invictools, 40L);
    }

     */

    @EventHandler
    public void swapworld(PlayerChangedWorldEvent e)
    {
        new BukkitRunnable()
        {
            @Override
            public void run()
            {
                for (Player p:activeBedwarsHolos.keySet())
                {
                    //    e.getPlayer().sendMessage("destroying active");
                    statsHolo.sendPacket(e.getPlayer(),activeBedwarsHolos.get(p).getStands());
                    statsHolo.sendPacket(e.getPlayer(),activeBedfightHolos.get(p).getStands());
                }

                //   e.getPlayer().sendMessage("building new");
                buildGeneric(e.getPlayer());
            }
        }.runTaskLater(OldCommands.Invictools, 40L);
    }

    @EventHandler
    public void quit(PlayerQuitEvent e)
    {
        if(activeBedfightHolos.containsKey(e.getPlayer()))
        {
            activeBedfightHolos.get(e.getPlayer()).destroy();
            activeBedfightHolos.remove(e.getPlayer());
        }
        if(activeBedwarsHolos.containsKey(e.getPlayer()))
        {
            activeBedwarsHolos.get(e.getPlayer()).destroy();
            activeBedwarsHolos.remove(e.getPlayer());
        }
    }

    @EventHandler
    public void bwjoin(BedwarsPlayerJoinEvent e)
    {
        if(activeBedfightHolos.containsKey(e.getPlayer()))
        {
            activeBedfightHolos.get(e.getPlayer()).destroy();
            activeBedfightHolos.remove(e.getPlayer());
        }
        if(activeBedwarsHolos.containsKey(e.getPlayer()))
        {
            activeBedwarsHolos.get(e.getPlayer()).destroy();
            activeBedwarsHolos.remove(e.getPlayer());
        }
    }
    
    public void buildGeneric(Player p)
    {
        if(toggleCommands.holoLimit && activeBedfightHolos.size() >= 25)
        {
            return;
        }

        statsHolo bedfight = new statsHolo(bedfightLocation,p,bedfightHeader);
        bedfight.add("Points:", statisticRequirments.getStatistic("bf_Points",p));
        bedfight.add("Wins:", statisticRequirments.getStatistic("bf_Wins",p));
        bedfight.add("Losses:", statisticRequirments.getStatistic("bf_Losses",p));
        bedfight.add("bfwl", 1);
        bedfight.add("Kills:", statisticRequirments.getStatistic("bf_NormalKills",p));
        bedfight.add("Bed Breaks:", statisticRequirments.getStatistic("bf_BedBreaks",p));
        bedfight.add("Best Winstreak:", statisticRequirments.getStatistic("bf_BestWinStreak",p));
        bedfight.add("Current Winstreak:", statisticRequirments.getStatistic("bf_WinStreak",p));
        bedfight.add("Games Played:", statisticRequirments.getStatistic("bf_Games",p));
        bedfight.build();
        activeBedfightHolos.put(p,bedfight);

        statsHolo bedwars = new statsHolo(bedwarsLocation,p,bedwarsHeader);
        bedwars.add("Stars:",new leaderboard().starLevel(statisticRequirments.getStatistic("bw_score",p)));
        bedwars.add("Wins:",statisticRequirments.getStatistic("bw_wins",p));
        bedwars.add("Kills:",statisticRequirments.getStatistic("bw_kills",p));
        bedwars.add("Bed Breaks:",statisticRequirments.getStatistic("bw_destroyedBeds",p));
        bedwars.add("Losses:",statisticRequirments.getStatistic("bw_loses",p));
        //bedwars.add("Deaths:",statisticRequirments.getStatistic("bw_deaths",p));
        bedwars.add("bwfkdr",1);
        bedwars.add("bwwl",1);
        bedwars.add("Games Played:",statisticRequirments.getStatistic("bw_games",p));
        bedwars.build();
        activeBedwarsHolos.put(p,bedwars);
    }
}

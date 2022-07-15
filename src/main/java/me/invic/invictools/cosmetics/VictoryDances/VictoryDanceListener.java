package me.invic.invictools.cosmetics.VictoryDances;

import me.invic.invictools.Commands;
import me.invic.invictools.cosmetics.Lobby1Handler;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.screamingsandals.bedwars.api.RunningTeam;
import org.screamingsandals.bedwars.api.events.BedwarsPlayerKilledEvent;
import org.spigotmc.event.entity.EntityDismountEvent;

public class VictoryDanceListener implements Listener
{
    @EventHandler
    public void gameEndDetector(BedwarsPlayerKilledEvent e)
    {
        RunningTeam livingTeam = null;
        int AliveTeams = e.getGame().getRunningTeams().size();
        for (RunningTeam team:e.getGame().getRunningTeams())
        {
            if(team.isDead())
                AliveTeams--;
            else
                livingTeam = team;
        }

        if(AliveTeams == 1)
        {
            for (Player p: livingTeam.getConnectedPlayers())
            {
                new VictoryDanceHandler().grabEffect(p);
                new BukkitRunnable()
                {
                    @Override
                    public void run()
                    {
                        new Lobby1Handler().FireFeet(p);
                    }
                }.runTaskLater(Commands.Invictools, (VictoryDanceHandler.effectDuration*20)+60);
            }
        }
    }

    @EventHandler
    public void dismount(EntityDismountEvent e)
    {
        if(VictoryDanceHandler.isVictoryDancing.get(e.getEntity().getName()) != null)
            e.setCancelled(true);
    }

    @EventHandler
    public void bankItemFix(PlayerDropItemEvent e)
    {
        if(VictoryDanceHandler.isVictoryDancing.get(e.getPlayer().getName()) != null)
            e.setCancelled(true);
    }
}

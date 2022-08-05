package me.invic.invictools.util.fixes;

import me.invic.invictools.commands.OldCommands;
import me.invic.invictools.cosmetics.VictoryDances.VictoryDanceHandler;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.screamingsandals.bedwars.api.BedwarsAPI;
import org.screamingsandals.bedwars.api.game.GameStatus;

public class stuckOnDeathFix implements Listener
{
    BedwarsAPI api = BedwarsAPI.getInstance();

    @EventHandler
    public void damageEvent(PlayerDeathEvent e)
    {
        /*
        if(api.isPlayerPlayingAnyGame(e.getEntity().getKiller()) && api.isPlayerPlayingAnyGame(e.getEntity()))
        {
            if(api.getGameOfPlayer(e.getEntity()).equals(api.getGameOfPlayer(e.getEntity().getKiller())))
            {
                if (!api.getGameOfPlayer(e.getEntity()).getTeamOfPlayer(e.getEntity().getKiller()).isTargetBlockExists())
                {

                }
            }
        }

         */
        if(api.isPlayerPlayingAnyGame(e.getEntity()))
        {
            if(api.getGameOfPlayer(e.getEntity()).getStatus().equals(GameStatus.RUNNING))
            {
                new BukkitRunnable()
                {
                    @Override
                    public void run()
                    {
                        if(e.getEntity().getKiller() != null
                                && api.getGameOfPlayer(e.getEntity()).getTeamOfPlayer(e.getEntity().getKiller()) == null
                                && !VictoryDanceHandler.isVictoryDancing.containsKey(e.getEntity().getName())
                                && !e.getEntity().getWorld().getName().equalsIgnoreCase("bwlobby")
                                && !e.getEntity().equals(e.getEntity().getKiller()))
                        {
                            Bukkit.dispatchCommand(Bukkit.getConsoleSender(),"minecraft:kill "+e.getEntity().getName());
                            System.out.println("killed potentially glitched player");
                        }
                    }
                }.runTaskLater(OldCommands.Invictools, 20*6L);
            }
        }
    }
}

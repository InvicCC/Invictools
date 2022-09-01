package me.invic.invictools.gamemodifiers;

import me.invic.invictools.commands.OldCommands;
import org.bukkit.GameMode;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.screamingsandals.bedwars.api.game.Game;
import org.screamingsandals.bedwars.api.game.GameStatus;

public class mobEveryMinute
{
    public mobEveryMinute(EntityType entity, Game game, int seconds)
    {
        final int[] i = {0};
        new BukkitRunnable()
        {
            @Override
            public void run()
            {
                if(game.getStatus() != GameStatus.RUNNING)
                    this.cancel();

                if(i[0] == seconds)
                {
                    for (Player p:game.getConnectedPlayers())
                    {
                        if(p.getGameMode().equals(GameMode.SURVIVAL))
                        {

                            if(entity.toString().equalsIgnoreCase(EntityType.ENDER_DRAGON.toString()))
                            {
                                EnderDragon e = (EnderDragon)p.getWorld().spawnEntity(p.getLocation(),entity);
                                e.setAware(true);
                                e.setPhase(EnderDragon.Phase.STRAFING);
                                e.setTarget(p);
                            }
                            else
                                p.getWorld().spawnEntity(p.getLocation(),entity);
                        }
                    }
                    i[0] = 0;
                }

                i[0]++;
            }
        }.runTaskTimer(OldCommands.Invictools, 20L, 20L);
    }
}

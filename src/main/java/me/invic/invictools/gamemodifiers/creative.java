package me.invic.invictools.gamemodifiers;

import me.invic.invictools.commands.OldCommands;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.screamingsandals.bedwars.api.BedwarsAPI;
import org.screamingsandals.bedwars.api.game.Game;

import java.util.Objects;

public class creative
{

    public creative(double intervalBetween, double timein, Player p, Game game)
    {
        new BukkitRunnable()
        {
            @Override
            public void run()
            {
                if (BedwarsAPI.getInstance().isPlayerPlayingAnyGame(p) && BedwarsAPI.getInstance().getGameOfPlayer(p).equals(game))
                {
                    if (p.getGameMode() != GameMode.SPECTATOR)
                    {
                        p.setGameMode(GameMode.CREATIVE);
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "tell " + p + " You now temporarily have creative mode!");
                        p.getWorld().playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 2, 1);

                        BukkitRunnable runnable = new BukkitRunnable()
                        {
                            @Override
                            public void run()
                            {
                                p.setGameMode(GameMode.SURVIVAL);
                            }
                        };
                        runnable.runTaskLater(OldCommands.Invictools, (long) timein * 20);
                    }
                }
                else
                    this.cancel();
            }
        }.runTaskTimer(OldCommands.Invictools, (long) intervalBetween * 20, (long) intervalBetween * 20);
    }
}
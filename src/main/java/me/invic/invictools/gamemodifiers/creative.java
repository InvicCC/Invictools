package me.invic.invictools.gamemodifiers;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Objects;

public class creative
{

    public creative(double intervalBetween, double timein, String player)
    {
        Player p = Bukkit.getPlayer(player);
        String worldName = p.getWorld().getName();

        new BukkitRunnable()
        {
            @Override
            public void run()
            {

                if (p.getWorld().getName().equals(worldName))
                {
                    if (p.getGameMode()!= GameMode.SPECTATOR)
                    {
                        p.setGameMode(GameMode.CREATIVE);
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "tell " + player + " You now temporarily have creative mode!");
                        p.getWorld().playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 2, 1);

                        BukkitRunnable runnable = new BukkitRunnable()
                        {
                            @Override
                            public void run()
                            {
                                p.setGameMode(GameMode.SURVIVAL);
                            }
                        };
                        runnable.runTaskLater(Objects.requireNonNull(Bukkit.getServer().getPluginManager().getPlugin("Invictools")), (long) timein * 20);
                    }
                }
                else
                    this.cancel();
            }
        }.runTaskTimer(Objects.requireNonNull(Bukkit.getServer().getPluginManager().getPlugin("Invictools")), (long) intervalBetween * 20, (long) intervalBetween * 20);
    }
}
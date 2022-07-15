package me.invic.invictools.util.fixes;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.Objects;

public class FixSpectatoring implements Listener
{
    @EventHandler
    public void deathcheck(PlayerDeathEvent e)
    {
        Player p = e.getEntity().getPlayer();
        assert p != null;
        String world = p.getWorld().getName();

        Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("Invictools");
        FileConfiguration Config = plugin.getConfig();
        List<String> blacklisted = Config.getStringList("blacklistedWorlds");

        if(!blacklisted.contains(world))
        {
            new BukkitRunnable()
            {
                int i = 0;
                @Override
                public void run()
                {
                    if(p.getGameMode() != GameMode.SPECTATOR)
                        p.setGameMode(GameMode.SPECTATOR);

                    if(i >= 4*20 || p.getGameMode() == GameMode.SPECTATOR)
                        this.cancel();

                    i++;
                }
            }.runTaskTimer(Objects.requireNonNull(Bukkit.getServer().getPluginManager().getPlugin("Invictools")),  0, 1);
        }
    }
}

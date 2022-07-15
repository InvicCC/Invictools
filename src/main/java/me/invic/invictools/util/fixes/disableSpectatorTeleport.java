package me.invic.invictools.util.fixes;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;
import java.util.Objects;

// stolen from https://www.spigotmc.org/resources/minimalist-no-spectator-teleport.52318/
// sorry

public class disableSpectatorTeleport implements Listener
{
    FileConfiguration fileConfiguration = Objects.requireNonNull(Bukkit.getServer().getPluginManager().getPlugin("Invictools")).getConfig();
    List<String> worlds = fileConfiguration.getStringList("blacklistedWorlds");

    @EventHandler
    final void onPlayerTeleportEvent(PlayerTeleportEvent event)
    {
        if (event.isCancelled())
            return;

        if (event.getPlayer().hasPermission("nospectp.allowteleport") || event.getPlayer().hasPermission("Permissions.*"))
            return;
/*
        if (!worlds.contains(event.getPlayer().getWorld().getName()) && event.getCause().equals(PlayerTeleportEvent.TeleportCause.SPECTATE))
            event.setCancelled(true);

        if (event.isCancelled())
            return;
 */
        if (!event.getTo().getWorld().equals(event.getFrom().getWorld()) && event.getCause().equals(PlayerTeleportEvent.TeleportCause.SPECTATE))
        {
            event.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 240, 0, false, false));
            event.setCancelled(true);
        }
    }
}

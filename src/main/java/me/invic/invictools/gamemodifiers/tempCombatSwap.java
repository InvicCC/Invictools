package me.invic.invictools.gamemodifiers;

import me.invic.invictools.util.fixes.SafeOpCommand;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.bedwars.api.BedwarsAPI;
import org.screamingsandals.bedwars.api.events.BedwarsGameEndEvent;
import org.screamingsandals.bedwars.api.events.BedwarsPlayerLeaveEvent;
import org.screamingsandals.bedwars.api.game.Game;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class tempCombatSwap implements Listener
{
    static List<Player> swapped = new ArrayList<>();
    public static Game swappedGame = null;

    public static void swap(Player p)
    {
        new SafeOpCommand(p,"ocm toggle");
        swapped.add(p);
    }

    @EventHandler
    public void preventDamage(EntityDamageByEntityEvent e)
    {
        if(e.getDamager() instanceof Player p && swapped.contains(p) && p.getAttackCooldown() != 1.0)
        {
            e.setDamage(0.5);
        }
    }

    @EventHandler
    public void bwleave(BedwarsPlayerLeaveEvent e)
    {
        swapped.remove(e.getPlayer());
    }

    @EventHandler
    public void bwend(BedwarsGameEndEvent e)
    {
        if(swappedGame.equals(e.getGame()))
        {
            quickCombatConfig();
            swappedGame = null;
        }
    }

    @EventHandler
    public void disconnect(PlayerQuitEvent e)
    {
        swapped.remove(e.getPlayer());
    }

    static Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("OldCombatMechanics");
    public static void timedCombatConfig()
    {
        File pFile = new File(plugin.getDataFolder(), "config.yml");
        final FileConfiguration data = YamlConfiguration.loadConfiguration(pFile);
        data.set("disable-attack-cooldown.disable-attack-cooldown", 4);
        data.set("disable-sword-sweep.enabled", false);
        data.set("disable-attack-sounds.enabled", false);
        data.set("old-tool-damage.enabled", false);
        data.set("old-player-knockback.enabled", false);
        data.set("old-critical-hits.enabled", false);
        data.set("old-critical-hits.allow-sprinting", false);

        try
        {
            data.save(pFile);
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(),"ocm reload");
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public static void quickCombatConfig()
    {
        File pFile = new File(plugin.getDataFolder(), "config.yml");
        final FileConfiguration data = YamlConfiguration.loadConfiguration(pFile);
        data.set("disable-attack-cooldown.disable-attack-cooldown", 30);
        data.set("disable-sword-sweep.enabled", true);
        data.set("disable-attack-sounds.enabled", true);
        data.set("old-tool-damage.enabled", true);
        data.set("old-player-knockback.enabled", true);
        data.set("old-critical-hits.enabled", true);
        data.set("old-critical-hits.allow-sprinting", true);

        try
        {
            data.save(pFile);
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(),"ocm reload");
        } catch (IOException e)
        {
            e.printStackTrace();
        }

    }
}

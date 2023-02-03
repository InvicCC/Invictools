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

// Invictable's combat swapper using Old Combat Mechanics 1/9/2023

public class tempCombatSwap implements Listener
{
    // How this works for me is when the bedwars game starts, I call timedCombatConfig() to switch the server completely then i call swap() for every player.
    // swap() sets the attack bar for individual players and handles the attack bar damage in preventDamage, timedCombatConfig() does kb, sounds, etc.
    // When you need to swap back just called quickCombatConfig()
    // When the game ends you need to remove players from being part of the swapped list or they wont be able to deal damage.
    // To access this code from other files you can do tempCombatSwap.(function) so tempCombatSwap.swap(player) etc.

    static List<Player> swapped = new ArrayList<>();
    public static Game swappedGame = null; // bedwars game, you dont need this.

    public static void swap(Player p) //call this and give it the instance of the player you're swapping combat for
    {
        new SafeOpCommand(p,"ocm toggle"); // youll need to replace this with function that ops the player, executes "ocm toggle", then deops the player
        swapped.add(p);
    }

    public static void unswap(Player p)
    {
        swapped.remove(p);
    }

    @EventHandler
    public void preventDamage(EntityDamageByEntityEvent e) // this prevents full damage if the player is swapped
    {
        if(e.getDamager() instanceof Player p && swapped.contains(p) && p.getAttackCooldown() <= .9)
        {
            e.setDamage(0.5);
        }
    }

    // You'll need to detect when the player leaves the game where their combat is swapped and run unswap(player).
    // I do this with the below code but you dont have these events because you dont have the bedwars plugin.

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

    @EventHandler // removes players when they quit to avoid errors
    public void disconnect(PlayerQuitEvent e)
    {
        swapped.remove(e.getPlayer());
    }

    static Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("OldCombatMechanics");
    public static void timedCombatConfig() // call to change to 1.9 style
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

    public static void quickCombatConfig() // call to change to 1.8 style
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

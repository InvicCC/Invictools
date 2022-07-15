package me.invic.invictools.gamemodifiers;

import me.invic.invictools.Commands;
import me.invic.invictools.util.DeathCounter;
import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class AbtributesOnDeath implements Listener
{
    public void AbtributesOnDeathAll(Player player, String attribute, double attributeInterval, double baseValue)
    {
        List<Player> players = getPlayersInSameWorld(player);
        DeathCounter.InitializeDeathCounter(players);

        for (Player p:players)
        {
            handleAbtribute(p, attribute, attributeInterval, baseValue);
        }
        ActiveAttributes.put(attribute, Commands.MasterPlayer.getAttribute(Attribute.valueOf(attribute)).getValue());
    }

    public void AttributesOnDeathSingular(Player player, String attribute, double attributeInterval, double baseValue)
    {
        handleAbtribute(player, attribute, attributeInterval, baseValue);
        ActiveAttributes.put(attribute, Commands.MasterPlayer.getAttribute(Attribute.valueOf(attribute)).getValue());
    }

    private void handleAbtribute(Player player, String attribute, double interval, double baseValue)
    {
        AttributeInstance a = player.getAttribute(Attribute.valueOf(attribute));
        a.setBaseValue(baseValue);
        if(attribute.equalsIgnoreCase("GENERIC_MAX_HEALTH"))
            player.setHealth(baseValue);

        new BukkitRunnable()
        {
            int deaths = DeathCounter.deaths.get(player);
            @Override
            public void run()
            {
                if(DeathCounter.deaths.get(player) == null)
                    this.cancel();

                if(DeathCounter.deaths.get(player)>deaths)
                {
                    a.setBaseValue(a.getValue() + interval);
                    deaths = DeathCounter.deaths.get(player);
                }
            }
        }.runTaskTimer(Objects.requireNonNull(Bukkit.getServer().getPluginManager().getPlugin("Invictools")), 20L, 20L);
    }

    public static List<Player> getPlayersInSameWorld(Player player)
    {
        List<Player> playersInWorld = new ArrayList<>();
        for (Player p : Bukkit.getOnlinePlayers())
        {
            if (p.getWorld().equals(player.getWorld()))
                playersInWorld.add(p);
        }
        return playersInWorld;
    }

    public static void resetAttributes()
    {
        AbtributesOnDeath.ActiveAttributes.forEach((a, value) ->
        {
            for (Player p: Bukkit.getOnlinePlayers())
            {
                if(!Attribute.valueOf(a).equals(Attribute.GENERIC_MOVEMENT_SPEED))
                    p.getAttribute(Attribute.valueOf(a)).setBaseValue(p.getAttribute(Attribute.valueOf(a)).getDefaultValue());
                else
                    p.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(.1);
            }
        });
        AbtributesOnDeath.ActiveAttributes.clear();
    }

    public static void resetSingularPlayerAttributes(Player p)
    {
        for (Attribute a : Attribute.values())
        {
            try
            {
                if(!a.equals(Attribute.GENERIC_MOVEMENT_SPEED))
                    p.getAttribute(a).setBaseValue(p.getAttribute(a).getDefaultValue());
                else
                    p.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(.1);
            }
            catch(NullPointerException ex)
            {
                continue;
            }
        }
    }

    @EventHandler
    public void ResetOnServerjoin(PlayerJoinEvent e)
    {
        resetSingularPlayerAttributes(e.getPlayer());
    }

    public static HashMap<String, Double> ActiveAttributes = new HashMap<>();
}

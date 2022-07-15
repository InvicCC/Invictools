package me.invic.invictools.util.fixes;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashSet;
import java.util.Objects;
import java.util.List;

public class voider
{
    double dynamicy;

    HashSet<String> worlds = new HashSet<>();

    public voider(List<String> worlds ,double sety )
    {
        this.worlds.addAll(worlds);
        this.worlds.add("bwlobby");

        Bukkit.getScheduler().scheduleSyncRepeatingTask(Objects.requireNonNull(Bukkit.getServer().getPluginManager().getPlugin("Invictools")), () ->
        {
            for (Player player : Bukkit.getOnlinePlayers())
            {
                Location loc = player.getLocation();
                dynamicy = loc.getY();
                if (worlds.contains(player.getLocation().getWorld().getName()) && dynamicy <= sety && player.getHealth() > 1)
                {
                    for (PotionEffect effect:player.getActivePotionEffects())
                    {
                        player.removePotionEffect(effect.getType());
                    }
                }
                else if(player.getLocation().getWorld().getName().equals("bwlobby") && dynamicy <= 5)
                {
                    Bukkit.dispatchCommand(player,"spawn");
                }
            }
        }, 1, 20);
    }
}

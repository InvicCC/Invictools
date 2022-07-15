package me.invic.invictools.gamemodifiers.PotionEffects;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.screamingsandals.bedwars.api.BedwarsAPI;
import org.screamingsandals.bedwars.api.Team;

import java.util.Objects;
import java.util.Random;

public class EffectSometimes
{
    Random rand = new Random();

    public EffectSometimes(Boolean all, Player player, String Effect, int delayrandom, int min, int effecttime, int effectlevel)
    {
        final World[] gameworld = {player.getWorld()};

        delayrandom = delayrandom * 20;
        min = min * 20;
        effecttime = effecttime * 20;

        int finalEffecttime = effecttime;
        new BukkitRunnable()
        {
            @Override
            public void run()
            {
                if (player.getWorld() != (gameworld[0]))
                    this.cancel();

                if (all)
                {
                    for (Player p : Bukkit.getOnlinePlayers())
                    {
                        if (p.getWorld() == gameworld[0])
                            p.addPotionEffect(new PotionEffect(PotionEffectType.getByName(Effect), finalEffecttime, effectlevel - 1, false, false));
                    }
                }
                else
                {
                    if (player.getWorld() == gameworld[0])
                        player.addPotionEffect(new PotionEffect(PotionEffectType.getByName(Effect), finalEffecttime, effectlevel - 1, false, false));
                }
            }
        }.runTaskTimer(Objects.requireNonNull(Bukkit.getServer().getPluginManager().getPlugin("Invictools")), rand.nextInt(delayrandom) + min, rand.nextInt(delayrandom) + min);

    }
}

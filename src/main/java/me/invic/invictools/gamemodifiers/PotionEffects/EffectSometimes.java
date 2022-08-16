package me.invic.invictools.gamemodifiers.PotionEffects;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.screamingsandals.bedwars.api.BedwarsAPI;
import org.screamingsandals.bedwars.api.Team;
import org.screamingsandals.bedwars.api.game.Game;

import java.util.Objects;
import java.util.Random;

public class EffectSometimes
{
    Random rand = new Random();

    public EffectSometimes(Boolean all, Player player, String Effect, int delayrandom, int min, int effecttime, int effectlevel)
    {
        final World[] gameworld = {player.getWorld()};

        Game game = BedwarsAPI.getInstance().getGameOfPlayer(player);

        delayrandom = delayrandom * 20;
        min = min * 20;
        effecttime = effecttime * 20;

        int finalEffecttime = effecttime;
        new BukkitRunnable()
        {
            @Override
            public void run()
            {
                if (!BedwarsAPI.getInstance().isPlayerPlayingAnyGame(player) || !BedwarsAPI.getInstance().getGameOfPlayer(player).equals(game))
                    this.cancel();

                if (all)
                {
                    for (Player p : BedwarsAPI.getInstance().getGameOfPlayer(player).getConnectedPlayers())
                    {
                        if (BedwarsAPI.getInstance().isPlayerPlayingAnyGame(p) && BedwarsAPI.getInstance().getGameOfPlayer(player).equals(p))
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

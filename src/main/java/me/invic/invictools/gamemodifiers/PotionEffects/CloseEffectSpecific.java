package me.invic.invictools.gamemodifiers.PotionEffects;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Objects;

import static me.invic.invictools.commands.Commands.teammates;

public class CloseEffectSpecific
{
    public CloseEffectSpecific(Player player, String worldName, String effect, int amp) // AMP Is really level but too lazy to fix name
    {
        if (teammates.get(player) != null)
        {
            if (teammates.get(player) != null || !teammates.get(player).getGameMode().equals(GameMode.SPECTATOR) || teammates.get(player).getWorld().getName().equals(player.getWorld().getName()))
            {
                Player team = teammates.get(player);
                new BukkitRunnable()
                {
                    @Override
                    public void run()
                    {
                        if (!player.getWorld().getName().equalsIgnoreCase(worldName))
                            this.cancel();

                        if (DamageTeammates.withinDistance(player, team, 10) && player.getGameMode() != GameMode.SPECTATOR && team.getGameMode() != GameMode.SPECTATOR)
                        {
                            player.addPotionEffect(new PotionEffect(PotionEffectType.getByName(effect), 25, amp - 1, false, false));
                            // team.addPotionEffect(new PotionEffect(PotionEffectType.getByName(effect), 25, amp-1, false, false));
                        }
                    }
                }.runTaskTimer(Objects.requireNonNull(Bukkit.getServer().getPluginManager().getPlugin("Invictools")), 20L, 20L);
            }
            else
                player.sendMessage(ChatColor.AQUA + "Nothing happened because you don't have a teammate");
        }
        else
            player.sendMessage(ChatColor.AQUA + "Nothing happened because you don't have a teammate");
    }
}

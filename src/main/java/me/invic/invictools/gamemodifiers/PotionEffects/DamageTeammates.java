package me.invic.invictools.gamemodifiers.PotionEffects;

import me.invic.invictools.util.GrabTeammates;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.Objects;

public class DamageTeammates
{
    public DamageTeammates(double distance, double initialbox, String WorldName, int potionlevel)
    {

        for (Player player : Bukkit.getOnlinePlayers())
        {
            List<Player> teammates = GrabTeammates.getTeammates(player);
            if(teammates == null)
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "broadcast Warning: at least 1 player's teammate did not get registered");
            else
            {
                Player team = teammates.get(0);
                new BukkitRunnable()
                {
                    @Override
                    public void run()
                    {
                        if(!player.getWorld().getName().equalsIgnoreCase(WorldName))
                            this.cancel();

                        assert team != null;
                        if(withinDistance(player,team,distance) && player.getGameMode() != GameMode.SPECTATOR && team.getGameMode() != GameMode.SPECTATOR)
                        {
                            player.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 25, potionlevel -1, false, false));
                        }
                    }
                }.runTaskTimer(Objects.requireNonNull(Bukkit.getServer().getPluginManager().getPlugin("Invictools")), 20L, 20L); // repeat every second so you can have a floating timer with named invis armor stand and just add variable that checks if its at delay to do item spawn
            }
        }
    }

    public static String getFirstHuman(List<Entity> entities)
    {
        for (Entity entity : entities)
        {
            if (entity.getType() == EntityType.PLAYER)
                return entity.getName();
        }
        return null;
    }

    public static Boolean withinDistance(Player player1, Player player2, double requiredDistance)
    {
        double dist = player1.getLocation().distance(player2.getLocation());
        return dist <= requiredDistance;
    }
}

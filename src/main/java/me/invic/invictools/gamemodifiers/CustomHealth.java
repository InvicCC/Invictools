package me.invic.invictools.gamemodifiers;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.screamingsandals.bedwars.api.BedwarsAPI;
import org.screamingsandals.bedwars.api.game.Game;

import java.util.List;
import java.util.Objects;
import java.util.Random;

public class CustomHealth
{
    String DynamicWorldName;
    Random rand = new Random();
    int DynamicHealth;

    public CustomHealth(String allorone, Player player, int HealthValue, int interval, String worldName)
    {
        if(!BedwarsAPI.getInstance().isPlayerPlayingAnyGame(player))
            return;

        Game game = BedwarsAPI.getInstance().getGameOfPlayer(player);
        List<Player> players = BedwarsAPI.getInstance().getGameOfPlayer(player).getConnectedPlayers();

        if (allorone.equalsIgnoreCase("all"))
        {
            for (Player p : players)
            {
                if(p.isOnline() && BedwarsAPI.getInstance().getGameOfPlayer(p).equals(game))
                {
                    AttributeInstance attribute = p.getAttribute(Attribute.GENERIC_MAX_HEALTH);
                    assert attribute != null;
                    attribute.setBaseValue(HealthValue);
                    p.setHealth(HealthValue);
                }
            }
        }
        else if (allorone.equalsIgnoreCase("one"))
        {
            AttributeInstance attribute = player.getAttribute(Attribute.GENERIC_MAX_HEALTH);
            assert attribute != null;
            attribute.setBaseValue(HealthValue);
            player.setHealth(HealthValue);
        }
        else if (allorone.equalsIgnoreCase("reset"))
        {
            for (Player p : players)
            {
                if(p.isOnline())
                {
                    AttributeInstance attribute = p.getAttribute(Attribute.GENERIC_MAX_HEALTH);
                    assert attribute != null;
                    attribute.setBaseValue(20);
                    p.setHealth(20);
                }
            }
        }
        else if (allorone.equalsIgnoreCase("randomall"))
        {
            final int HV = HealthValue;
            new BukkitRunnable()
            {
                @Override
                public void run()
                {
                        DynamicWorldName = player.getLocation().getWorld().getName();

                        DynamicHealth = rand.nextInt(HV) + 2;

                        for (Player p : players)
                        {
                            if(BedwarsAPI.getInstance().isPlayerPlayingAnyGame(p) && BedwarsAPI.getInstance().getGameOfPlayer(p).equals(game))
                            {
                                AttributeInstance attribute = p.getAttribute(Attribute.GENERIC_MAX_HEALTH);
                                assert attribute != null;

                                double healthpercent = p.getHealth() / attribute.getValue();
                                double convert = DynamicHealth * healthpercent;

                                attribute.setBaseValue(DynamicHealth);
                                p.setHealth(convert);
                            }
                            else
                                this.cancel();
                        }
                }
            }.runTaskTimer(Objects.requireNonNull(Bukkit.getServer().getPluginManager().getPlugin("Invictools")), 0, interval * 20L);
        }
        else if (allorone.equalsIgnoreCase("decrease"))
        {
            final int[] HV = {HealthValue};
            new BukkitRunnable()
            {
                @Override
                public void run()
                {
                    DynamicWorldName = player.getLocation().getWorld().getName();

                        for (Player p : players)
                        {
                            if(BedwarsAPI.getInstance().isPlayerPlayingAnyGame(p) && BedwarsAPI.getInstance().getGameOfPlayer(p).equals(game))
                            {
                                AttributeInstance attribute = p.getAttribute(Attribute.GENERIC_MAX_HEALTH);
                                assert attribute != null;

                                double healthpercent = p.getHealth() / attribute.getValue();
                                double convert = HV[0] * healthpercent;

                                attribute.setBaseValue(HV[0]);
                                p.setHealth(convert);
                            }
                            else
                                this.cancel();
                        }

                        if (HV[0] > 2)
                            HV[0] -= 2;
                        else
                            this.cancel();
                }
            }.runTaskTimer(Objects.requireNonNull(Bukkit.getServer().getPluginManager().getPlugin("Invictools")), 0, interval * 20L);
        }
        else
            System.out.println("tf did u do");
    }
}

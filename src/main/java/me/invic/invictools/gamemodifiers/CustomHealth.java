package me.invic.invictools.gamemodifiers;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Objects;
import java.util.Random;

public class CustomHealth
{
    String DynamicWorldName;
    Random rand = new Random();
    int DynamicHealth;

    public CustomHealth(String allorone, Player player, int HealthValue, int interval, String worldName)
    {
        if (allorone.equalsIgnoreCase("all"))
        {
            for (Player p : Bukkit.getOnlinePlayers())
            {
                AttributeInstance attribute = p.getAttribute(Attribute.GENERIC_MAX_HEALTH);
                assert attribute != null;
                attribute.setBaseValue(HealthValue);
                p.setHealth(HealthValue);
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
            for (Player p : Bukkit.getOnlinePlayers())
            {
                AttributeInstance attribute = p.getAttribute(Attribute.GENERIC_MAX_HEALTH);
                assert attribute != null;
                attribute.setBaseValue(20);
                p.setHealth(20);
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

                    if (DynamicWorldName.equals(worldName))
                    {
                        DynamicHealth = rand.nextInt(HV) + 2;

                        for (Player p : Bukkit.getOnlinePlayers())
                        {
                            AttributeInstance attribute = p.getAttribute(Attribute.GENERIC_MAX_HEALTH);
                            assert attribute != null;

                            double healthpercent = p.getHealth() / attribute.getValue();
                            double convert = DynamicHealth * healthpercent;

                            attribute.setBaseValue(DynamicHealth);
                            p.setHealth(convert);
                        }
                    }
                    else
                        this.cancel();
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

                    if (DynamicWorldName.equals(worldName))
                    {
                        for (Player p : Bukkit.getOnlinePlayers())
                        {
                            AttributeInstance attribute = p.getAttribute(Attribute.GENERIC_MAX_HEALTH);
                            assert attribute != null;

                            double healthpercent = p.getHealth() / attribute.getValue();
                            double convert = HV[0] * healthpercent;

                            attribute.setBaseValue(HV[0]);
                            p.setHealth(convert);
                        }

                        if (HV[0] > 2)
                            HV[0] -= 2;
                        else
                            this.cancel();
                    }
                    else
                        this.cancel();
                }
            }.runTaskTimer(Objects.requireNonNull(Bukkit.getServer().getPluginManager().getPlugin("Invictools")), 0, interval * 20L);
        }
        else
            System.out.println("tf did u do");
    }
}

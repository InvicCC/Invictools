package me.invic.invictools.gamemodifiers.PotionEffects;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Objects;
import java.util.Random;

public class randomEffect
{
    String DynamicWorldName;

    int i = 0;
    int b = 1;
    int a = 0;

    public randomEffect(int intensityLimit, String Worldname, Player player, int time) // make sure to check string for validity before this point
    {
        new BukkitRunnable()
        {
            @Override
            public void run()
            {
                if(!player.getWorld().getName().equalsIgnoreCase(Worldname))
                {
                    this.cancel();
                }

                    DynamicWorldName = Objects.requireNonNull(player.getLocation().getWorld()).getName();
                    if (DynamicWorldName.equals(Worldname))
                    {
                        switch (a) // actual intelligent design of assigning effect, allowing code after to be altered easily
                        {
                            case 0:
                                player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 60, b, false, false));
                                break;
                            case 1:
                                player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 60, b, false, false));
                                break;
                            case 2:
                                player.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 60, b, false, false));
                                break;
                            case 3:
                                player.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 60, b, false, false));
                                break;
                            case 4:
                                player.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 60, b, false, false));
                                break;
                            case 5:
                                player.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, 60, b, false, false));
                                break;
                            case 6:
                                player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 60, b, false, false));
                                break;
                            case 7:
                                player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 60, b, false, false));
                                break;
                            case 8:
                                player.addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION, 60, b, false, false));
                                break;
                            case 9:
                                player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 60, b, false, false));
                                break;
                            case 10:
                                player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 60, b, false, false));
                                break;
                            case 11:
                                player.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 60, b, false, false));
                                break;
                            case 12:
                                player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 60, b, false, false));
                                break;
                            case 13:
                                player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 60, b, false, false));
                                break;
                            case 14:
                                player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_FALLING, 60, b, false, false));
                                break;
                            case 15:
                                player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 60, b, false, false));
                                break;
                            case 16:
                                player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 60, b, false, false));
                                break;
                            case 17:
                                player.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 60, b, false, false));
                                break;
                            case 18:
                                player.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 60, b, false, false));
                                break;
                            case 19:
                                player.addPotionEffect(new PotionEffect(PotionEffectType.BAD_OMEN, 60, b, false, false));
                                break;
                            default:
                                System.out.println("Invic tools random effect failed: i a b " + i + " " + a + " " + b);
                        }
                    }

                i++;

                if (i % time == 0)
                {
                    Random rand = new Random();
                    a = rand.nextInt(20);
                    b = rand.nextInt(intensityLimit);
                }
            }
        }.runTaskTimer(Objects.requireNonNull(Bukkit.getServer().getPluginManager().getPlugin("Invictools")), 1, 20);
    }
}
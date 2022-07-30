package cc.invic.invictools.items;

import cc.invic.invictools.gamemodifiers.PotionEffects.DamageTeammates;
import cc.invic.invictools.impl.Commands;
import cc.invic.invictools.util.GrabTeammates;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.screamingsandals.bedwars.api.BedwarsAPI;
import org.screamingsandals.bedwars.api.game.Game;

import java.util.HashMap;
import java.util.Locale;
import java.util.Objects;

public class ModBow implements Listener
{
    @EventHandler
    public void ArrowShot(EntityShootBowEvent e)
    {
        if (!e.getEntity().getType().equals(EntityType.PLAYER))
            return;
        if (!e.getBow().hasItemMeta())
            return;
        if (!e.getBow().getItemMeta().hasLore())
            return;

        if (e.getBow().getItemMeta().getLore().get(0).equalsIgnoreCase("§7Multi-purposed Ranged Weapon"))
        {
            BedwarsAPI api = BedwarsAPI.getInstance();
            if (!ItemListener.ModBow.containsKey((Player) e.getEntity()))
                ItemListener.ModBow.put((Player) e.getEntity(), 0);

            if (ItemListener.ModBow.get((Player) e.getEntity()).equals(1)) // FIREBALL
            {
                e.setCancelled(true);
                Player p = (Player) e.getEntity();
                int cooldown = 3; // seconds
                if (ModBowFireballCooldown.containsKey(p))
                {
                    long secondsLeft = ((ModBowFireballCooldown.get(p) / 1000) + cooldown) - (System.currentTimeMillis() / 1000);
                    if (secondsLeft <= 0)
                    {
                        doFireball(p);
                        ItemStack arrows = searchInventory(p, Material.ARROW.name());
                        arrows.setAmount(arrows.getAmount() - 1);
                    }
                    else
                    {
                        p.sendMessage(ChatColor.RED + "On cooldown for " + secondsLeft + " seconds.");
                        p.updateInventory();
                    }
                }
                else
                {
                    doFireball(p);
                    ItemStack arrows = searchInventory(p, Material.ARROW.name());
                    arrows.setAmount(arrows.getAmount() - 1);
                }
            }
            else if (ItemListener.ModBow.get((Player) e.getEntity()).equals(0)) // BRIDGE
            {
                if(api.isPlayerPlayingAnyGame((Player) e.getEntity()))
                {

                    new BukkitRunnable()
                    {
                        final Player player = (Player) e.getEntity();
                        final String wooltype = api.getGameOfPlayer(player).getTeamOfPlayer((Player) e.getEntity()).getColor().name().toUpperCase(Locale.ROOT) + "_WOOL";
                        final int[] runs = {0};

                        @Override
                        public void run()
                        {
                            if (api.getGameOfPlayer(player).isLocationInArena(e.getProjectile().getLocation()))
                            {
                                if (e.getProjectile().getLocation().getBlock().getType().equals(Material.AIR) || e.getProjectile().getLocation().getY() > 20) /*|| e.getProjectile().getLocation().getBlock().getType().equals(Material.WATER)*/
                                {
                                    // System.out.println(e.getProjectile().getLocation());
                                    Location loc = e.getProjectile().getLocation();
                                    BukkitRunnable runnable = new BukkitRunnable()
                                    {
                                        @Override
                                        public void run()
                                        {
                                            if (loc.getBlock().getType().equals(Material.AIR))
                                                player.playSound(loc, Sound.ENTITY_CHICKEN_EGG, 1, 1);
                                            loc.getBlock().setType(Material.getMaterial(wooltype));
                                            Block b2 = loc.clone().subtract(1.0D, 0.0D, 0.0D).getBlock();
                                            Block b4 = loc.clone().subtract(0.0D, 0.0D, 1.0D).getBlock();
                                            Block b3 = loc.clone().subtract(-1.0D, 0.0D, 0.0D).getBlock();
                                            Block b5 = loc.clone().subtract(0.0D, 0.0D, -1.0D).getBlock();
                                            if (b2.getType().equals(Material.AIR))
                                            {
                                                b2.setType(Material.getMaterial(wooltype));
                                                addLater(b2, player);
                                            }
                                            if (b3.getType().equals(Material.AIR))
                                            {
                                                b3.setType(Material.getMaterial(wooltype));
                                                addLater(b3, player);
                                            }
                                            if (b4.getType().equals(Material.AIR))
                                            {
                                                b4.setType(Material.getMaterial(wooltype));
                                                addLater(b4, player);
                                            }
                                            if (b5.getType().equals(Material.AIR))
                                            {
                                                b5.setType(Material.getMaterial(wooltype));
                                                addLater(b5, player);
                                            }
                                            addLater(loc.getBlock(), player);
                                        }
                                    };
                                    runnable.runTaskLater(Objects.requireNonNull(Bukkit.getServer().getPluginManager().getPlugin("Invictools")), 2);

                                    runs[0]++;
                                    if (runs[0] == 20 * 3) //5 seconds
                                        this.cancel();
                                } else
                                    this.cancel();
                            } else
                                this.cancel();
                        }
                    }.runTaskTimer(Objects.requireNonNull(Bukkit.getServer().getPluginManager().getPlugin("Invictools")), 2L, 0L);
                }
                else
                {
                    e.setCancelled(true);
                    ((Player)e.getEntity()).updateInventory();
                    (e.getEntity()).sendMessage(ChatColor.RED +"This module only works in game");
                }
            }
            else if (ItemListener.ModBow.get((Player) e.getEntity()).equals(2)) // HEAL
            {
                e.setCancelled(true);
                Player p = (Player) e.getEntity();
                int cooldown = 10; // seconds
                if (ModBowHealCooldown.containsKey(p))
                {
                    long secondsLeft = ((ModBowHealCooldown.get(p) / 1000) + cooldown) - (System.currentTimeMillis() / 1000);
                    if (secondsLeft <= 0)
                    {
                        doEffects(p);
                        ItemStack arrows = searchInventory(p, Material.ARROW.name());
                        arrows.setAmount(arrows.getAmount() - 1);
                    }
                    else
                    {
                        p.sendMessage(ChatColor.RED + "On cooldown for " + secondsLeft + " seconds.");
                        p.updateInventory();
                    }
                }
                else
                {
                    doEffects(p);
                    ItemStack arrows = searchInventory(p, Material.ARROW.name());
                    arrows.setAmount(arrows.getAmount() - 1);
                }
            }
        }
        else if (e.getBow().getItemMeta().getLore().get(0).equalsIgnoreCase("§7Does what it says, somehow.")) // Rideable Fireball Launcher
        {
            Player p = (Player) e.getEntity();
            e.setCancelled(true);
            p.updateInventory();

            if (e.getEntity().getWorld().getName().equalsIgnoreCase("bwlobby") && !Commands.FireStickEnabled)
            {
                e.getEntity().sendMessage(ChatColor.RED + "The " + ChatColor.GOLD + " Fireball Bow " + ChatColor.RED + " is currently disabled");
                return;
            }

            if (e.getEntity().getWorld().getName().equalsIgnoreCase("bwlobby") && !e.getEntity().hasPermission("invic.firestick"))
            {
                if (!p.getName().equalsIgnoreCase("Superman100"))
                {
                    e.getEntity().sendMessage(ChatColor.RED + "Requires a rank!");
                    e.getEntity().sendMessage(ChatColor.AQUA + "Type /ranks to learn more");
                    return;
                }
            }

            ItemStack arrows = searchInventory(p, Material.ARROW.name());
            arrows.setAmount(arrows.getAmount() - 1);
            p.updateInventory();
            Fireball ball = e.getEntity().launchProjectile(Fireball.class);
            ball.setYield(3);
            ball.setShooter(p);
            p.playSound(p.getLocation(), Sound.ENTITY_BLAZE_SHOOT, 1, 1);
            ball.setVelocity((e.getEntity().getLocation().getDirection().multiply(1.2)));
            ball.addPassenger(e.getEntity());
        }
        else if (e.getBow().getItemMeta().getLore().get(0).equalsIgnoreCase("§7Fires Rideable Arrows")) // Rideable Fireball Launcher
        {
            e.getProjectile().addPassenger(e.getEntity());
        }
    }

    public void doEffects(Player p)
    {
        ModBowHealCooldown.put(p, System.currentTimeMillis());
        p.getLocation().getWorld().strikeLightningEffect(p.getLocation());
        p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 100, 4, false, true));
        p.addPotionEffect(new PotionEffect(PotionEffectType.HEALTH_BOOST, 100, 0, false, true));
        p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 100, 1, false, true));
        for (Player team : GrabTeammates.getTeammates(p))
        {
            if (DamageTeammates.withinDistance(p, team, 15) && team.getGameMode() != GameMode.SPECTATOR)
            {
                team.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 100, 4, false, true));
                team.addPotionEffect(new PotionEffect(PotionEffectType.HEALTH_BOOST, 100, 0, false, true));
                team.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 100, 1, false, true));
            }
        }
    }

    public void doFireball(Player p)
    {
        ModBowFireballCooldown.put(p, System.currentTimeMillis());
        Fireball ball = p.launchProjectile(Fireball.class);
        //  new ProjTrailHandler().grabEffect(p,ball);
        ball.setYield(3);
        ball.setShooter(p);
        p.playSound(p.getLocation(), Sound.ENTITY_BLAZE_SHOOT, 1, 1);
        //    ball.setVelocity((p.getLocation().getDirection().multiply(1)));
    }

    public void addLater(Block block, Player player)
    {
        BukkitRunnable runnable = new BukkitRunnable()
        {
            @Override
            public void run()
            {
                BedwarsAPI.getInstance().getGameOfPlayer(player).getRegion().addBuiltDuringGame(block.getLocation());
            }
        };
        runnable.runTaskLater(Objects.requireNonNull(Bukkit.getServer().getPluginManager().getPlugin("Invictools")), 1);
    }

    public void addLater(Block block, Game game)
    {
        BukkitRunnable runnable = new BukkitRunnable()
        {
            @Override
            public void run()
            {
                game.getRegion().addBuiltDuringGame(block.getLocation());
            }
        };
        runnable.runTaskLater(Objects.requireNonNull(Bukkit.getServer().getPluginManager().getPlugin("Invictools")), 5);
    }

    public ItemStack searchInventory(Player p, String name)
    {
        for (ItemStack i : p.getInventory())
        {
            if (i != null)
                if (i.getType().toString().equalsIgnoreCase(name))
                    return i;
        }
        return null;
    }

    public ItemStack searchInventory(Player p, ItemStack item)
    {
        for (ItemStack i : p.getInventory())
        {
            if (i != null)
                if (i.equals(item))
                    return i;
        }
        return null;
    }

    public static HashMap<Player, Long> ModBowHealCooldown = new HashMap<>();
    public static HashMap<Player, Long> ModBowFireballCooldown = new HashMap<>();
}

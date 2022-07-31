package me.invic.invictools.gamemodifiers.LuckyBlocks.Blocks;

import me.invic.invictools.commands.OldCommands;
import me.invic.invictools.gamemodifiers.CustomHealth;
import me.invic.invictools.gamemodifiers.LuckyBlocks.dynamicSpawner;
import me.invic.invictools.util.GrabTeammates;
import me.invic.invictools.util.disableStats;
import me.invic.invictools.util.fixes.Protocol47Fix;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.screamingsandals.bedwars.api.BedwarsAPI;

import java.util.*;

public class badBlocks
{
    public badBlocks(Location loc, Player player)
    {
        Random rand = new Random();
        int choice = rand.nextInt(11);
        switch (choice)
        {
            case 0:
                String command = "execute at " + player.getName() + " run summon minecraft:tnt " + loc.getX() + " " + loc.getY() + " " + loc.getZ() + " {Fuse:" + 1 + "}";
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
                break;
            case 2:
                new BukkitRunnable()
                {
                    int i = 0;

                    @Override
                    public void run()
                    {
                        if (i == 20)
                            this.cancel();

                        Location ploc = player.getLocation();
                        String command2 = "execute at " + player.getName() + " run summon minecraft:tnt " + ploc.getX() + " " + ploc.getY() + " " + ploc.getZ() + " {Fuse:" + 500 + "} ";
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command2);

                        i++;
                    }
                }.runTaskTimer(Objects.requireNonNull(Bukkit.getServer().getPluginManager().getPlugin("Invictools")), 0, 5);
                break;
                /*
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&a&lMerry &c&lChristmas!"));

                ItemStack panes1 = new ItemStack(Material.GREEN_STAINED_GLASS_PANE);
                ItemStack panes2 = new ItemStack(Material.RED_STAINED_GLASS_PANE);
                panes1.setAmount(864);
                panes2.setAmount(864);

                ItemMeta meta = panes1.getItemMeta();
                meta.setDisplayName(ChatColor.translateAlternateColorCodes('&',"&l&aMerry Christmas"));
                panes1.setItemMeta(meta);
                ItemMeta meta2 = panes2.getItemMeta();
                meta2.setDisplayName(ChatColor.translateAlternateColorCodes('&',"&l&cMerry Christmas"));
                panes2.setItemMeta(meta2);

                panes1.addUnsafeEnchantment(Enchantment.BINDING_CURSE,1);
                panes2.addUnsafeEnchantment(Enchantment.VANISHING_CURSE,1);

                player.playSound(player.getLocation(),Sound.ENTITY_BAT_DEATH,1,1);
                final Map<Integer, ItemStack> map = player.getInventory().addItem(panes1);
                for (final ItemStack item : map.values())
                {
                    player.getWorld().dropItemNaturally(player.getLocation(), item);
                }
                final Map<Integer, ItemStack> map2 = player.getInventory().addItem(panes2);
                for (final ItemStack item : map2.values())
                {
                    player.getWorld().dropItemNaturally(player.getLocation(), item);
                }
                break;

                 */

            case 3:
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&lsuffer"));
                //    player.playSound(player.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 1, 1);
                   // player.setVelocity(new Vector(0, 20, 0));
                    player.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 150, 0, false, true));
                    break;
            case 4:
                ItemStack coal = new ItemStack(Material.AMETHYST_BLOCK);
                new BukkitRunnable()
                {
                    int i = 0;

                    @Override
                    public void run()
                    {
                        if (i >= 18)
                            this.cancel();

                        player.playSound(loc, Sound.ENTITY_CHICKEN_EGG, 1, 1);
                        player.getWorld().dropItemNaturally(loc, coal);

                        i++;
                    }
                }.runTaskTimer(OldCommands.Invictools, 0, 4); // 20 TICKS IS 1 SECOND NOT 1 TICK
                break;
            case 5:
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&lYou will swap locations with a random player in 2 seconds..."));
                BukkitRunnable runnable = new BukkitRunnable()
                {
                    @Override
                    public void run()
                    {
                        Player swapplayer = getRandomPlayer(player);
                        Location ploc = swapplayer.getLocation();
                        Location mploc = player.getLocation();
                        player.teleport(ploc);
                        swapplayer.teleport(mploc);
                        player.playSound(ploc, Sound.ENTITY_ENDERMAN_TELEPORT, 1, 1);
                        swapplayer.playSound(mploc, Sound.ENTITY_ENDERMAN_TELEPORT, 1, 1);
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&b&lYour position has been swapped with &e" + swapplayer.getName()));
                        swapplayer.sendMessage(ChatColor.translateAlternateColorCodes('&', "&b&lYour position has been swapped with &e" + player.getName()));
                    }
                };
                runnable.runTaskLater(Objects.requireNonNull(Bukkit.getServer().getPluginManager().getPlugin("Invictools")), 2 * 20);
                break;
            case 6:
                new dynamicSpawner("badshop", player, loc);
                break;
            case 7:
                int timehalfed;
                if(BedwarsAPI.getInstance().isPlayerPlayingAnyGame(player))
                {
                    if(disableStats.getGameType(BedwarsAPI.getInstance().getGameOfPlayer(player)).equalsIgnoreCase("bedfight"))
                        timehalfed = 30;
                    else
                        timehalfed = 60;
                }
                else
                    timehalfed = 60;

                new CustomHealth("one", player, 10, 0, player.getWorld().getName());
                for (Player p : GrabTeammates.getTeammates(player))
                {
                    new CustomHealth("one", p, 10, 0, p.getWorld().getName());
                    p.playSound(p.getLocation(), Sound.ENTITY_GENERIC_EAT, 1, 1);
                }
                player.playSound(loc, Sound.ENTITY_GENERIC_EAT, 1, 1);
                BukkitRunnable runnable2 = new BukkitRunnable()
                {
                    @Override
                    public void run()
                    {
                        new CustomHealth("reset", player, 10, 0, player.getWorld().getName());
                        player.playSound(loc, Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
                        for (Player p : GrabTeammates.getTeammates(player))
                        {
                            new CustomHealth("reset", p, 10, 0, p.getWorld().getName());
                            p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
                        }
                    }
                };
                runnable2.runTaskLater(Objects.requireNonNull(Bukkit.getServer().getPluginManager().getPlugin("Invictools")), 20 * timehalfed);
                break;
            case 1:
            case 8:
                ItemStack bed = new ItemStack(Material.BLACK_BED);
                ItemMeta meta4 = bed.getItemMeta();
                meta4.setDisplayName(ChatColor.DARK_RED + "Useless Bed");
                List<String> lore2 = new ArrayList<>();
                lore2.add(ChatColor.translateAlternateColorCodes('&', "&7It literally does nothing"));
                meta4.setLore(lore2);
                bed.setItemMeta(meta4);

                player.playSound(loc, Sound.ENTITY_CHICKEN_EGG, 1, 1);
                player.getWorld().dropItemNaturally(loc, bed);
                break;
            case 9:
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&b&lMydoeza time"));
                player.playSound(player.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 1, 1);
                player.setVelocity(new Vector(0, 150, 0));
                final Map<Integer, ItemStack> map3 = player.getInventory().addItem(new ItemStack(Material.WATER_BUCKET));
                for (final ItemStack item : map3.values())
                    player.getWorld().dropItemNaturally(player.getLocation(), item);
                break;
            case 10:
                //  Commands.MasterPlayer.sendMessage("random mob");
                int i = rand.nextInt(6);
                switch (i)
                {
                    case 0:
                        player.sendMessage(ChatColor.RED + "RUN");
                        player.getWorld().spawnEntity(player.getLocation(), EntityType.WARDEN);
                        break;
                    case 3:
                        boolean pass = false;
                        if(BedwarsAPI.getInstance().isPlayerPlayingAnyGame(player))
                        {
                            if (disableStats.getGameType(BedwarsAPI.getInstance().getGameOfPlayer(player)).equalsIgnoreCase("bedfight"))
                                pass =true;
                        }

                        if(!pass)
                        {
                            player.sendMessage(ChatColor.RED + "RUN");
                            EnderDragon dragon = (EnderDragon) player.getWorld().spawnEntity(player.getLocation().clone().add(0, 10, 0), EntityType.ENDER_DRAGON);
                            dragon.setAware(true);
                            dragon.setPhase(EnderDragon.Phase.STRAFING);
                            dragon.attack(player);
                            break;
                        }
                    case 5:
                        int withertime;
                        if(BedwarsAPI.getInstance().isPlayerPlayingAnyGame(player))
                        {
                            if(disableStats.getGameType(BedwarsAPI.getInstance().getGameOfPlayer(player)).equalsIgnoreCase("bedfight"))
                                withertime = 30;
                            else
                                withertime = 60;
                        }
                        else
                            withertime = 60;

                        if(BedwarsAPI.getInstance().isPlayerPlayingAnyGame(player))
                        {
                            if(!new Protocol47Fix().isAnyLivingPlayer47(BedwarsAPI.getInstance().getGameOfPlayer(player)))
                            {
                                player.sendMessage(ChatColor.RED + "RUN");
                                Entity wither = player.getWorld().spawnEntity(player.getLocation().clone().add(0, 40, 0), EntityType.WITHER);
                                new BukkitRunnable()
                                {
                                    @Override
                                    public void run()
                                    {
                                        if(!wither.isDead())
                                        {
                                            wither.getLocation().getWorld().playSound(wither.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 20, 1);
                                            wither.getLocation().getWorld().spawnParticle(Particle.EXPLOSION_HUGE, wither.getLocation(), 1);
                                            wither.remove();
                                        }
                                    }
                                }.runTaskLater(OldCommands.Invictools, withertime*20);
                                break;
                            }
                        }
                }
                break;
            default:
                System.out.println("default");
        }
    }

    public static Player getRandomPlayer(Player originalplayer)
    {
        List<Player> players = new ArrayList<>(Bukkit.getOnlinePlayers());

        int i = 0;
        Collections.shuffle(players);

        while (true)
        {
            if (players.get(i) != originalplayer)
                if (players.get(i).getWorld().getName().equalsIgnoreCase(originalplayer.getWorld().getName()))
                    if (players.get(i).getGameMode() != GameMode.SPECTATOR)
                        return players.get(i);

            i++;

            if (i > players.size() - 1)
                return null;
        }
    }
}

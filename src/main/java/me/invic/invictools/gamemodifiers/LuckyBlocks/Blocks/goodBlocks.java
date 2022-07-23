package me.invic.invictools.gamemodifiers.LuckyBlocks.Blocks;

import me.invic.invictools.Commands;
import me.invic.invictools.gamemodifiers.CustomHealth;
import me.invic.invictools.gamemodifiers.LuckyBlocks.Blocks.badBlocks;
import me.invic.invictools.gamemodifiers.LuckyBlocks.createLuckyBlocks;
import me.invic.invictools.gamemodifiers.PotionEffects.DamageTeammates;
import me.invic.invictools.items.createItems;
import me.invic.invictools.util.GrabTeammates;
import me.invic.invictools.util.LobbyLogic;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.screamingsandals.bedwars.api.BedwarsAPI;
import org.screamingsandals.bedwars.api.game.ItemSpawnerType;

import java.util.*;

public class goodBlocks
{
    BedwarsAPI api = BedwarsAPI.getInstance();

    public goodBlocks(Location loc, Player player)
    {
        String worldName = player.getWorld().getName();
        Random rand = new Random();
        int choice = rand.nextInt(22); //total case statements + 1
        switch (choice)
        {
            case 0:
                ItemSpawnerType type = api.getItemSpawnerTypeByName("diamond");
                ItemStack stack = type.getStack();

                new BukkitRunnable()
                {
                    int i = 0;

                    @Override
                    public void run()
                    {
                        if (i >= 7)
                            this.cancel();

                        player.playSound(loc, Sound.ENTITY_CHICKEN_EGG, 1, 1);
                        player.getWorld().dropItemNaturally(loc, stack);

                        i++;
                    }
                }.runTaskTimer(Objects.requireNonNull(Bukkit.getServer().getPluginManager().getPlugin("Invictools")), 0, 4); // 20 TICKS IS 1 SECOND NOT 1 TICK
                break;
            case 1:
                ItemSpawnerType type2 = api.getItemSpawnerTypeByName("emerald");
                ItemStack stack2 = type2.getStack();

                new BukkitRunnable()
                {
                    int i = 0;

                    @Override
                    public void run()
                    {
                        if (i >= 3)
                            this.cancel();

                        player.playSound(loc, Sound.ENTITY_CHICKEN_EGG, 1, 1);
                        player.getWorld().dropItemNaturally(loc, stack2);

                        i++;
                    }
                }.runTaskTimer(Objects.requireNonNull(Bukkit.getServer().getPluginManager().getPlugin("Invictools")), 0, 4); // 20 TICKS IS 1 SECOND NOT 1 TICK
                break;
            case 2:
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&lYou will be teleported to a random player in 3 seconds..."));
                BukkitRunnable runnable = new BukkitRunnable()
                {
                    @Override
                    public void run()
                    {
                        Player swapplayer = badBlocks.getRandomPlayer(player);
                        Location ploc = swapplayer.getLocation();
                        player.teleport(ploc);
                        player.playSound(ploc, Sound.ENTITY_ENDERMAN_TELEPORT, 1, 1);
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&b&lYou have teleported to &e" + swapplayer.getName()));
                        swapplayer.sendMessage(ChatColor.YELLOW + player.getName() + ChatColor.AQUA + "§l has teleported to you!");
                    }
                };
                runnable.runTaskLater(Objects.requireNonNull(Bukkit.getServer().getPluginManager().getPlugin("Invictools")), 3 * 20);
                break;
            case 3:
                double health = player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
                new CustomHealth("one", player, (int) health + 20, 0, player.getWorld().getName());
                for (Player p : GrabTeammates.getTeammates(player))
                {
                    new CustomHealth("one", p, (int) health + 20, 0, p.getWorld().getName());
                    p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
                }
                player.playSound(loc, Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
                BukkitRunnable runnable2 = new BukkitRunnable()
                {
                    @Override
                    public void run()
                    {
                        new CustomHealth("reset", player, (int) health, 0, player.getWorld().getName());
                        player.playSound(loc, Sound.ENTITY_PLAYER_BURP, 1, 1);
                        for (Player p : GrabTeammates.getTeammates(player))
                        {
                            new CustomHealth("reset", p, (int) health, 0, p.getWorld().getName());
                            p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_BURP, 1, 1);
                        }
                    }
                };
                runnable2.runTaskLater(Objects.requireNonNull(Bukkit.getServer().getPluginManager().getPlugin("Invictools")), 20 * 60);
                break;
            case 4:
                new BukkitRunnable()
                {
                    int i = 0;

                    @Override
                    public void run()
                    {
                        if (i >= 13)
                            this.cancel();

                        int cblocX = rand.nextInt(16) - 8;
                        int cblocZ = rand.nextInt(16) - 8;
                        Location cblocation = new Location(loc.getWorld(), loc.getX() + cblocX, loc.getY() + 10, loc.getZ() + cblocZ);
                        if (cblocation.getBlock().equals(Material.AIR))
                            Objects.requireNonNull(loc.getWorld()).spawnFallingBlock(cblocation, Material.DIRT, (byte) 0);

                        i++;
                    }
                }.runTaskTimer(Objects.requireNonNull(Bukkit.getServer().getPluginManager().getPlugin("Invictools")), 0, 12);
                break;
            case 5:
                ItemStack arrows = new ItemStack(Material.SPECTRAL_ARROW);
                arrows.setAmount(3);
                ItemStack bow = new ItemStack(Material.BOW);
                ItemMeta meta = bow.getItemMeta();
                meta.setDisplayName(ChatColor.YELLOW + "One Punch Bow");
                if (meta instanceof Damageable)
                    ((Damageable) meta).setDamage(Material.BOW.getMaxDurability() - 3);
                bow.setItemMeta(meta);
                bow.addUnsafeEnchantment(Enchantment.ARROW_KNOCKBACK, 5);
                player.getWorld().dropItemNaturally(loc, bow);
                player.getWorld().dropItemNaturally(loc, arrows);
                player.playSound(loc, Sound.ENTITY_CHICKEN_EGG, 1, 1);
                break;
            case 6:
                List<Player> teamm = GrabTeammates.getTeammates(player);
                if (!teamm.isEmpty())
                {
                    if (!teamm.get(0).getGameMode().equals(GameMode.SPECTATOR) || teamm.get(0).getWorld().getName().equals(player.getWorld().getName()))
                    {
                        player.sendMessage(ChatColor.AQUA + "You and your teammate will now receive a buff when near each other!");
                        new BukkitRunnable()
                        {
                            @Override
                            public void run()
                            {
                                if (!player.getWorld().getName().equalsIgnoreCase(worldName))
                                    this.cancel();

                                for (Player team : teamm)
                                {
                                    if (DamageTeammates.withinDistance(player, team, 10) && player.getGameMode() != GameMode.SPECTATOR && team.getGameMode() != GameMode.SPECTATOR)
                                    {
                                        player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 25, 1, false, false));
                                        team.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 25, 1, false, false));
                                    }
                                }
                            }
                        }.runTaskTimer(Objects.requireNonNull(Bukkit.getServer().getPluginManager().getPlugin("Invictools")), 20L, 20L);
                        break;
                    }
                    //  else
                    //     player.sendMessage(ChatColor.AQUA + "Nothing happened because you don't have a living teammate");
                }
                //    else
                //       player.sendMessage(ChatColor.AQUA + "Nothing happened because you don't have a teammate");
            case 7:
                List<Player> teamm2 = GrabTeammates.getTeammates(player);
                if (!teamm2.isEmpty())
                {
                    if (!teamm2.get(0).getGameMode().equals(GameMode.SPECTATOR) || teamm2.get(0).getWorld().getName().equals(player.getWorld().getName()))
                    {
                        player.sendMessage(ChatColor.AQUA + "You and your teammate will now receive a buff when near each other!");
                        new BukkitRunnable()
                        {
                            @Override
                            public void run()
                            {
                                if (!player.getWorld().getName().equalsIgnoreCase(worldName))
                                    this.cancel();

                                for (Player team : teamm2)
                                {
                                    if (DamageTeammates.withinDistance(player, team, 10) && player.getGameMode() != GameMode.SPECTATOR && team.getGameMode() != GameMode.SPECTATOR)
                                    {
                                        player.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, 25, 1, false, false));
                                        team.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, 25, 1, false, false));
                                    }
                                }
                            }
                        }.runTaskTimer(Objects.requireNonNull(Bukkit.getServer().getPluginManager().getPlugin("Invictools")), 20L, 20L);
                        break;
                    }
                    //  else
                    //      player.sendMessage(ChatColor.AQUA + "Nothing happened because you don't have a living teammate");
                }
                //  else
                //     player.sendMessage(ChatColor.AQUA + "Nothing happened because you don't have a teammate");
            case 8:
                ItemStack lb = new createLuckyBlocks().BAD();
                new BukkitRunnable()
                {
                    int i = 0;

                    @Override
                    public void run()
                    {
                        if (i >= 2)
                            this.cancel();

                        player.playSound(loc, Sound.ENTITY_CHICKEN_EGG, 1, 1);
                        player.getWorld().dropItemNaturally(loc, lb);

                        i++;
                    }
                }.runTaskTimer(Objects.requireNonNull(Bukkit.getServer().getPluginManager().getPlugin("Invictools")), 0, 4); // 20 TICKS IS 1 SECOND NOT 1 TICK
                break;
            case 9:
                ItemStack slime = new ItemStack(Material.SLIME_BALL);
                ItemMeta meta2 = slime.getItemMeta();
                meta2.setDisplayName(ChatColor.GREEN + "Little Knockback Ball");
                List<String> lore2 = new ArrayList<>();
                lore2.add(ChatColor.translateAlternateColorCodes('&', "&7Everyone's favorite... slightly nerfed"));
                meta2.setLore(lore2);
                slime.setItemMeta(meta2);
                slime.addUnsafeEnchantment(Enchantment.KNOCKBACK, 3);
                player.getWorld().dropItemNaturally(loc, slime);
                player.playSound(loc, Sound.ENTITY_CHICKEN_EGG, 1, 1);
                break;
            case 10:
                ItemStack totem = new ItemStack(Material.TOTEM_OF_UNDYING);
                player.playSound(loc, Sound.ENTITY_CHICKEN_EGG, 1, 1);
                player.getWorld().dropItemNaturally(loc, totem);
                break;
            case 11:
                ItemStack coal = new ItemStack(Material.ENDER_PEARL);
                new BukkitRunnable()
                {
                    int i = 0;

                    @Override
                    public void run()
                    {
                        if (i >= 3)
                            this.cancel();

                        player.playSound(loc, Sound.ENTITY_CHICKEN_EGG, 1, 1);
                        player.getWorld().dropItemNaturally(loc, coal);

                        i++;
                    }
                }.runTaskTimer(Objects.requireNonNull(Bukkit.getServer().getPluginManager().getPlugin("Invictools")), 0, 4); // 20 TICKS IS 1 SECOND NOT 1 TICK
                break;
            case 12:
                ItemStack oplb = new createLuckyBlocks().OP();
                player.playSound(loc, Sound.ENTITY_CHICKEN_EGG, 1, 1);
                player.getWorld().dropItemNaturally(loc, oplb);
                break;
            case 13:
                ItemStack goodlb = new createLuckyBlocks().GOOD();
                new BukkitRunnable()
                {
                    int i = 0;

                    @Override
                    public void run()
                    {
                        if (i >= 2)
                            this.cancel();

                        player.playSound(loc, Sound.ENTITY_CHICKEN_EGG, 1, 1);
                        player.getWorld().dropItemNaturally(loc, goodlb);

                        i++;
                    }
                }.runTaskTimer(Objects.requireNonNull(Bukkit.getServer().getPluginManager().getPlugin("Invictools")), 0, 4); // 20 TICKS IS 1 SECOND NOT 1 TICK
                break;
            case 14:
            case 17:
            case 18:
            case 19:
            case 20:
                ItemStack item = new createItems().getRandomItem();
                player.playSound(loc, Sound.ENTITY_CHICKEN_EGG, 1, 1);
                player.getWorld().dropItemNaturally(loc, item);
                giveArrows(player,item);
                break;
            case 15:
                player.setAllowFlight(true);
                player.setFlying(true);
                player.sendMessage(ChatColor.AQUA + "Flying has been enabled for 10 seconds!");
                player.playSound(loc, Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
                List<Player> teamm3 = GrabTeammates.getTeammates(player);
                if (!teamm3.isEmpty())
                {
                    for (Player team : teamm3)
                    {
                        team.setAllowFlight(true);
                        team.setFlying(true);
                        team.sendMessage(ChatColor.AQUA + "Flying has been enabled for 10 seconds!");
                        team.playSound(team.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
                    }
                }

                BukkitRunnable runnable3 = new BukkitRunnable()
                {
                    @Override
                    public void run()
                    {
                        player.setAllowFlight(false);
                        player.setFlying(false);
                        if (!teamm3.isEmpty())
                        {
                            for (Player team : teamm3)
                            {
                                team.setAllowFlight(false);
                                team.setFlying(false);
                            }
                        }
                    }
                };
                runnable3.runTaskLater(Objects.requireNonNull(Bukkit.getServer().getPluginManager().getPlugin("Invictools")), 10 * 20);
                break;
            case 16:
                ItemStack item2 = new createItems().getRandomItem();
                giveArrows(player,item2);
                player.getWorld().dropItemNaturally(loc, item2);
                player.playSound(loc, Sound.ENTITY_CHICKEN_EGG, 1, 1);
                break;
            case 21:
                player.getWorld().dropItemNaturally(loc, new ItemStack(Material.WARDEN_SPAWN_EGG));
                player.playSound(loc, Sound.ENTITY_CHICKEN_EGG, 1, 1);
                break;
            default:
                System.out.println("default");
        }
    }

    public static void giveArrows(Player p, ItemStack item)
    {
        if(item.getType().equals(Material.BOW) && BedwarsAPI.getInstance().isPlayerPlayingAnyGame(p))
        {
            if(new LobbyLogic().getMapConfiguration(BedwarsAPI.getInstance().getGameOfPlayer(p).getName()).getString("GameType").equalsIgnoreCase("bedfight"))
            {
                ItemStack arrow = new ItemStack(Material.ARROW);
                arrow.setAmount(new Random().nextInt(3)+1);
                p.getInventory().addItem(arrow);
            }
        }
    }
}

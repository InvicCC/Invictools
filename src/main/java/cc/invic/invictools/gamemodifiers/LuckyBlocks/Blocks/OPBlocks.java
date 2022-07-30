package cc.invic.invictools.gamemodifiers.LuckyBlocks.Blocks;

import cc.invic.invictools.gamemodifiers.LuckyBlocks.LuckyBlockSpawner;
import cc.invic.invictools.gamemodifiers.LuckyBlocks.createLuckyBlocks;
import cc.invic.invictools.gamemodifiers.LuckyBlocks.dynamicSpawner;
import cc.invic.invictools.gamemodifiers.gamemodeData;
import cc.invic.invictools.items.createItems;
import cc.invic.invictools.items.dareListener;
import cc.invic.invictools.util.GrabTeammates;
import cc.invic.invictools.util.disableStats;
import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.screamingsandals.bedwars.api.BedwarsAPI;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class OPBlocks
{
    public OPBlocks(Location loc, Player player)
    {
        //  String worldName = player.getWorld().getName();
        Random rand = new Random();
        int choice = rand.nextInt(12); //total case statements + 1
        switch (choice)
        {
            case 0:
                new LuckyBlockSpawner(player.getLocation(), "§b§lLucky Block Spawner", "random", 45,player);
            case 8:
                ItemStack item = new createItems().getRandomItem();
                if(BedwarsAPI.getInstance().isPlayerPlayingAnyGame(player))
                {
                    if(new gamemodeData().getLuckyBlockMode(BedwarsAPI.getInstance().getGameOfPlayer(player)).equalsIgnoreCase("47") && (item.equals(new createItems().getByName("FIREBOW"))||(item.equals(new createItems().getByName("DAREBONE")))))
                    {
                        item= new createItems().getByName("LOOTBOX");
                    }
                }
                item.setAmount(5);
                player.getWorld().dropItemNaturally(loc, item);
                player.playSound(loc, Sound.ENTITY_CHICKEN_EGG, 1, 1);
                goodBlocks.giveArrows(player,item);
                break;
            case 1:
                if(BedwarsAPI.getInstance().isPlayerPlayingAnyGame(player))
                {
                    if(!new gamemodeData().getLuckyBlockMode(BedwarsAPI.getInstance().getGameOfPlayer(player)).equalsIgnoreCase("47"))
                    {
                        loc.getWorld().strikeLightningEffect(loc);
                        ItemStack item2 = new ItemStack(Material.ELYTRA);
                        ItemStack item4 = new ItemStack(Material.FIREWORK_ROCKET);
                        item4.setAmount(3);
                        player.getWorld().dropItemNaturally(loc, item2);
                        player.getWorld().dropItemNaturally(loc, item4);
                        break;
                    }
                }
            case 2:
                if (rand.nextInt(2) == 1)
                    new dynamicSpawner("netheritedynamic", player, player.getLocation());
                else
                    new dynamicSpawner("oparmordynamic", player, player.getLocation());
                break;
            case 3:
                int flytime;
                if(BedwarsAPI.getInstance().isPlayerPlayingAnyGame(player))
                {
                    if(disableStats.getGameType(BedwarsAPI.getInstance().getGameOfPlayer(player)).equalsIgnoreCase("bedfight"))
                        flytime = 5;
                    else
                        flytime = 10;
                }
                else
                    flytime = 10;

                loc.getWorld().strikeLightningEffect(loc);
                player.setGameMode(GameMode.CREATIVE);
                if(BedwarsAPI.getInstance().isPlayerPlayingAnyGame(player))
                {
                    if (!disableStats.getGameType(BedwarsAPI.getInstance().getGameOfPlayer(player)).equalsIgnoreCase("bedfight"))
                    {
                        player.setAllowFlight(true);
                        player.setFlying(true);
                    }
                }
                player.sendMessage(ChatColor.AQUA + "You have been given creative mode for "+flytime+" seconds!");
                player.playSound(loc, Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
                List<Player> teamm3 = GrabTeammates.getTeammates(player);
                if (teamm3 != null)
                {
                    for (Player team : teamm3)
                    {
                        team.setGameMode(GameMode.CREATIVE);
                        if(BedwarsAPI.getInstance().isPlayerPlayingAnyGame(team))
                        {
                            if (!disableStats.getGameType(BedwarsAPI.getInstance().getGameOfPlayer(team)).equalsIgnoreCase("bedfight"))
                            {
                                team.setAllowFlight(true);
                                team.setFlying(true);
                            }
                        }
                        team.sendMessage(ChatColor.AQUA + "You have been given creative mode for "+flytime+" seconds!");
                        team.playSound(teamm3.get(0).getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
                    }
                }

                BukkitRunnable runnable3 = new BukkitRunnable()
                {
                    @Override
                    public void run()
                    {
                        player.setGameMode(GameMode.SURVIVAL);
                        if(BedwarsAPI.getInstance().isPlayerPlayingAnyGame(player))
                        {
                            if (!disableStats.getGameType(BedwarsAPI.getInstance().getGameOfPlayer(player)).equalsIgnoreCase("bedfight"))
                            {
                                player.setAllowFlight(false);
                                player.setFlying(false);
                            }
                        }
                        if (teamm3 != null)
                        {
                            for (Player team : teamm3)
                            {
                                team.setGameMode(GameMode.SURVIVAL);
                                if(BedwarsAPI.getInstance().isPlayerPlayingAnyGame(team))
                                {
                                    if (!disableStats.getGameType(BedwarsAPI.getInstance().getGameOfPlayer(team)).equalsIgnoreCase("bedfight"))
                                    {
                                        team.setAllowFlight(false);
                                        team.setFlying(false);
                                    }
                                }
                            }
                        }
                    }
                };
                runnable3.runTaskLater(Objects.requireNonNull(Bukkit.getServer().getPluginManager().getPlugin("Invictools")), flytime * 20);
                break;
            case 4:
                ItemStack slime = new ItemStack(Material.SLIME_BALL);
                ItemMeta meta2 = slime.getItemMeta();
                meta2.setDisplayName(ChatColor.GREEN + "The Original");
                List<String> lore2 = new ArrayList<>();
                lore2.add(ChatColor.translateAlternateColorCodes('&', "&7Everyone's favorite"));
                meta2.setLore(lore2);
                slime.setItemMeta(meta2);
                slime.addUnsafeEnchantment(Enchantment.KNOCKBACK, 10);
                player.getWorld().dropItemNaturally(loc, slime);
                loc.getWorld().strikeLightningEffect(loc);
                break;
            case 5:
                ItemStack goodlb = new createLuckyBlocks().OP();
                new BukkitRunnable()
                {
                    int i = 0;

                    @Override
                    public void run()
                    {
                        if (i >= 1)
                            this.cancel();

                        player.playSound(loc, Sound.ENTITY_CHICKEN_EGG, 1, 1);
                        player.getWorld().dropItemNaturally(loc, goodlb);

                        i++;
                    }
                }.runTaskTimer(Objects.requireNonNull(Bukkit.getServer().getPluginManager().getPlugin("Invictools")), 0, 4); // 20 TICKS IS 1 SECOND NOT 1 TICK
                break;
            case 6:
                ItemStack item3 = new createItems().WOOL();
                player.getWorld().dropItemNaturally(loc, item3);
                item3 = new createItems().SNAP();
                player.getWorld().dropItemNaturally(loc, item3);
                break;
            case 7:
                loc.getWorld().strikeLightningEffect(loc);
                ItemStack item5 = new ItemStack(Material.NETHERITE_INGOT);
                ItemStack item6 = new ItemStack(Material.SMITHING_TABLE);
                player.getWorld().dropItemNaturally(loc, item6);
                new BukkitRunnable()
                {
                    int i = 0;

                    @Override
                    public void run()
                    {
                        if (i >= 3)
                            this.cancel();

                        player.playSound(loc, Sound.ENTITY_CHICKEN_EGG, 1, 1);
                        player.getWorld().dropItemNaturally(loc, item5);

                        i++;
                    }
                }.runTaskTimer(Objects.requireNonNull(Bukkit.getServer().getPluginManager().getPlugin("Invictools")), 0, 4); // 20 TICKS IS 1 SECOND NOT 1 TICK
                break;
            case 9:
                if(BedwarsAPI.getInstance().isPlayerPlayingAnyGame(player))
                {
                    if (!disableStats.getGameType(BedwarsAPI.getInstance().getGameOfPlayer(player)).equalsIgnoreCase("bedfight"))
                    {
                        new dareListener().spawnDare(loc, player, true, false);
                        break;
                    }
                }
            case 10:
                ItemStack box = new createItems().getByName("LOOTBOX");
                box.setAmount(7);
                player.getWorld().dropItemNaturally(loc, box);
                player.getWorld().strikeLightningEffect(player.getLocation());
                break;
            case 11:
                player.getWorld().spawnEntity(loc, EntityType.ALLAY);
                player.getWorld().spawnEntity(loc, EntityType.ALLAY);
                player.getWorld().spawnEntity(loc, EntityType.ALLAY);
                player.getWorld().spawnEntity(loc, EntityType.ALLAY);
                player.getWorld().spawnEntity(loc, EntityType.ALLAY);
                break;
            default:
                System.out.println("default");
        }
    }
}

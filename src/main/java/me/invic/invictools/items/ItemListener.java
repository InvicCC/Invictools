package me.invic.invictools.items;

import me.invic.invictools.commands.Commands;
import me.invic.invictools.cosmetics.projtrail.ProjTrailHandler;
import me.invic.invictools.util.physics.grabSandstone;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.screamingsandals.bedwars.api.BedwarsAPI;

import java.util.*;

import static me.invic.invictools.util.physics.grabSandstone.getNearbyBlocks;
import static me.invic.invictools.util.physics.grabSandstone.taggedBlocks;

public class ItemListener implements Listener
{
    BedwarsAPI api = BedwarsAPI.getInstance();

    @EventHandler
    public void RightClickEvent(PlayerInteractEvent e)
    {
        if (e.getItem() == null)
            return;

        /* YOU CANNOT CHECK FOR CANCELED BECAUSE THEN IT ONLY WORKS IF PLAYER IS WITHIN RANGE OF A BLOCK
        if(e.isCancelled())
            return;
         */

        if (Objects.requireNonNull(e.getItem().getItemMeta()).getLore() != null)
        {
            List<String> lore = e.getItem().getItemMeta().getLore();

            if (lore.isEmpty())
                return;

            if (lore.get(0).isEmpty())
                return;

            if (lore.get(0).equalsIgnoreCase("§7Flings you high into the air when right clicked"))
            {
                String playername = e.getPlayer().getName();
                Player p = Bukkit.getPlayer(playername);
                e.getItem().setAmount(e.getItem().getAmount() - 1);
                p.setVelocity(new Vector(0, 60, 0));
                new ProjTrailHandler().barrier(p, false);
                Falling.add(p);
                BukkitRunnable runnable = new BukkitRunnable()
                {
                    @Override
                    public void run()
                    {
                        Falling.remove(p);
                    }
                };
                runnable.runTaskLater(Objects.requireNonNull(Bukkit.getServer().getPluginManager().getPlugin("Invictools")), 20 * 20);
            }
            else if (lore.get(0).equalsIgnoreCase("§7Blinds everyone within 20 blocks of you for 5 seconds"))
            {
                e.getItem().setAmount(e.getItem().getAmount() - 1);
                List<Entity> nearby = e.getPlayer().getNearbyEntities(40, 40, 40);
                // e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 20*3, 0, false, false));
                for (Entity entity : nearby)
                {
                    if (entity.getType().equals(EntityType.PLAYER))
                    {
                        Player p = (Player) entity;
                        if (!p.getName().equalsIgnoreCase(e.getPlayer().getName()))
                        {
                            p.addPotionEffect(new PotionEffect(PotionEffectType.DARKNESS, 20 * 5, 0, false, true));
                            p.sendMessage(ChatColor.AQUA + "You have been blinded by " + ChatColor.YELLOW + e.getPlayer().getName());
                            p.playSound(p.getLocation(), Sound.BLOCK_LAVA_POP, 1, 1);
                        }
                    }
                }
            }
            else if (lore.get(0).equalsIgnoreCase("§7Summons mobs where you're looking"))
            {
                e.getItem().setAmount(e.getItem().getAmount() - 1);
                Location loc = e.getPlayer().getTargetBlock(null, 200).getLocation();

                Random rand = new Random();

                Creeper creeper = (Creeper) loc.getWorld().spawnEntity(loc.add(2, 2, 0), EntityType.CREEPER);
                creeper.setPowered(true);
                //loc.getWorld().spawnEntity(loc.add(0,0,2),EntityType.CREEPER);
                loc.add(-2, 0, -2);

                new BukkitRunnable()
                {
                    int i = 0;

                    @Override
                    public void run()
                    {
                        if (i >= 2)
                            this.cancel();

                        if (rand.nextInt(2) == 1)
                            loc.getWorld().spawnEntity(loc, EntityType.WITHER_SKELETON);
                        else
                        {
                            loc.getWorld().spawnEntity(loc.add(0, 5, 0), EntityType.GHAST);
                            loc.add(0, -5, 0);
                        }

                        i++;
                    }
                }.runTaskTimer(Objects.requireNonNull(Bukkit.getServer().getPluginManager().getPlugin("Invictools")), 15, 15);

            }
            else if (lore.get(0).equalsIgnoreCase("§7Shoots a Fireball") && e.getAction().equals(Action.RIGHT_CLICK_AIR) || lore.get(0).equalsIgnoreCase("§7Shoots a Fireball") && e.getAction().equals(Action.RIGHT_CLICK_BLOCK))
            {
                e.setCancelled(true);
                Player p = e.getPlayer();
                double cooldown = .3; // seconds
                if (FireballCooldown.containsKey(p))
                {
                    double secondsLeft = ((FireballCooldown.get(p) / 1000) + cooldown) - (System.currentTimeMillis() / 1000);
                    if (secondsLeft <= 0)
                    {
                        doFireball(e.getItem(), p);
                    }
                } else
                {
                    doFireball(e.getItem(), p);
                }
            }
            else if (lore.get(0).equalsIgnoreCase("§7Sends a barrage of anvils raining down where you're looking"))
            {

                e.getItem().setAmount(e.getItem().getAmount() - 1);

                if (e.getPlayer().getWorld().getName().equalsIgnoreCase("bwlobby"))
                {
                    e.getPlayer().kickPlayer("The anvil somehow transcended reality and squashed your connection. Unfortunate.");
                    return;
                }

                Location loc = e.getPlayer().getTargetBlock(null, 200).getLocation();

                Random rand = new Random();
                new BukkitRunnable()
                {
                    int i = 0;

                    @Override
                    public void run()
                    {
                        if (i >= 40)
                            this.cancel();

                        int cblocX = rand.nextInt(12) - 6;
                        int cblocZ = rand.nextInt(12) - 6;
                        Location cblocation = new Location(loc.getWorld(), loc.getX() + cblocX, loc.getY() + 20, loc.getZ() + cblocZ);

                        if (cblocation.getBlock().getType().equals(Material.AIR) || cblocation.getBlock().getType().equals(Material.WATER))
                        {
                            cblocation.getBlock().setType(Material.ANVIL);
                            if (api.isPlayerPlayingAnyGame(e.getPlayer()))
                                api.getFirstRunningGame().getRegion().addBuiltDuringGame(cblocation);
                        }

                        i++;
                    }
                }.runTaskTimer(Objects.requireNonNull(Bukkit.getServer().getPluginManager().getPlugin("Invictools")), 0, 3);
            }
            else if (lore.get(0).equalsIgnoreCase("§7Spawns lots of tnt where you're looking"))
            {
                e.getItem().setAmount(e.getItem().getAmount() - 1);
                Location loc = e.getPlayer().getTargetBlock(null, 200).getLocation();
                Random rand = new Random();
                new BukkitRunnable()
                {
                    int i = 0;

                    @Override
                    public void run()
                    {
                        if (i >= 20)
                            this.cancel();

                        double x = rand.nextInt(5) - 2;
                        double z = rand.nextInt(5) - 2;
                        x = x / 10;
                        z = z / 10;

                        if (x == 0)
                            x = .05;
                        if (z == 0)
                            z = .05;

                        TNTPrimed tnt = loc.getWorld().spawn(loc, TNTPrimed.class);
                        tnt.setVelocity(new Vector(x, 1, z));

                        i++;
                    }
                }.runTaskTimer(Objects.requireNonNull(Bukkit.getServer().getPluginManager().getPlugin("Invictools")), 0, 3);
            }
            else if (lore.get(0).equalsIgnoreCase("§7Violently ignites all wool in a 20 block radius"))
            {
                e.getItem().setAmount(e.getItem().getAmount() - 1);
                e.setCancelled(true);
                List<Block> wool = new ArrayList<>();

                wool.addAll(getNearbyBlocks(e.getPlayer().getLocation(), 20, "BLUE_WOOL", true));
                wool.addAll(getNearbyBlocks(e.getPlayer().getLocation(), 20, "GRAY_WOOL", true));
                wool.addAll(getNearbyBlocks(e.getPlayer().getLocation(), 20, "RED_WOOL", true));
                wool.addAll(getNearbyBlocks(e.getPlayer().getLocation(), 20, "WHITE_WOOL", true));
                wool.addAll(getNearbyBlocks(e.getPlayer().getLocation(), 20, "YELLOW_WOOL", true));
                wool.addAll(getNearbyBlocks(e.getPlayer().getLocation(), 20, "PINK_WOOL", true));
                wool.addAll(getNearbyBlocks(e.getPlayer().getLocation(), 20, "ORANGE_WOOL", true));
                wool.addAll(getNearbyBlocks(e.getPlayer().getLocation(), 20, "LIME_WOOL", true));
                wool.addAll(getNearbyBlocks(e.getPlayer().getLocation(), 20, "LIGHT_BLUE_WOOL", true));
                wool.addAll(getNearbyBlocks(e.getPlayer().getLocation(), 20, "LIGHT_GRAY_WOOL", true));

                wool.removeAll(taggedBlocks);
                grabSandstone.taggedBlocks.addAll(wool);
/*
                if(!api.getGameOfPlayer(e.getPlayer()).getName().equalsIgnoreCase("SnowThree"))
                    wool.removeIf(b -> !api.getGameOfPlayer(e.getPlayer()).getRegion().isBlockAddedDuringGame(b.getLocation()));

                if(!api.getGameOfPlayer(e.getPlayer()).getName().equalsIgnoreCase("MarsFours"))
                    wool.removeIf(b -> !api.getGameOfPlayer(e.getPlayer()).getRegion().isBlockAddedDuringGame(b.getLocation()));
*/
                Collections.shuffle(wool);

                List<Block> wool2 = new ArrayList<>();
                List<Block> wool3 = new ArrayList<>();
                List<Block> wool4 = new ArrayList<>();
                List<Block> wool5 = new ArrayList<>();
                List<Block> wool6 = new ArrayList<>();

                if (wool.size() < 21)
                {
                    int inc = wool.size() / 5;
                    for (int i = 0; i < wool.size(); i++)
                    {
                        if (i <= inc)
                        {
                            wool2.add(wool.get(i));
                        }
                        else if (i <= inc * 2)
                        {
                            wool3.add(wool.get(i));
                        }
                        else if (i <= inc * 3)
                        {
                            wool4.add(wool.get(i));
                        }
                        else if (i <= inc * 4)
                        {
                            wool5.add(wool.get(i));
                        }
                        else if (i <= inc * 5)
                        {
                            wool6.add(wool.get(i));
                        }
                    }
/*
                    Collections.shuffle(wool3);
                    Collections.shuffle(wool4);
                    Collections.shuffle(wool5);
                    Collections.shuffle(wool6);
 */
                    incChange(wool2, true, e.getPlayer().getLocation(), true);
                    incChange(wool3, true, e.getPlayer().getLocation(), false);
                    incChange(wool4, true, e.getPlayer().getLocation(), true);
                    incChange(wool5, true, e.getPlayer().getLocation(), false);
                    incChange(wool6, true, e.getPlayer().getLocation(), true);
                }
                else
                {
                    // Collections.shuffle(wool);
                    incChange(wool, false, e.getPlayer().getLocation(), true);
                }
            }
            else if (lore.get(0).equalsIgnoreCase("§7It's like a magic toy stick, but slightly cooler"))
            {
                new FireStick(e.getPlayer());
            }
            else if (lore.get(0).equalsIgnoreCase("§7Summons a legend"))
            {
                e.getItem().setAmount(e.getItem().getAmount() - 1);
                new dareListener().spawnDare(e.getPlayer().getLocation(), e.getPlayer(), !lore.get(1).equalsIgnoreCase("§7Doesnt despawn"), false);
            }
            else if (lore.get(0).equalsIgnoreCase("§7Turns every placed block within 20 meters into ice"))
            {
                e.getItem().setAmount(e.getItem().getAmount() - 1);

                List<Block> wool = new ArrayList<>();
                wool.addAll(getNearbyPlacedBlocks(e.getPlayer().getLocation(), 20));
                e.getPlayer().getLocation().getWorld().strikeLightningEffect(e.getPlayer().getLocation());

                for (Block block : wool)
                {
                    Bukkit.getOnlinePlayers().forEach((player) -> player.playSound(block.getLocation(), Sound.BLOCK_GLASS_BREAK, 1, 1));

                    if (api.getGameOfPlayer(e.getPlayer()).isBlockAddedDuringGame(block.getLocation()))
                        block.setType(Material.PACKED_ICE);
                }

                wool.clear();
            }
            else if (lore.get(0).equalsIgnoreCase("§7Multi-purposed Ranged Weapon") && e.getAction().equals(Action.LEFT_CLICK_AIR) || lore.get(0).equalsIgnoreCase("§7Multi-purposed Ranged Weapon") && e.getAction().equals(Action.LEFT_CLICK_BLOCK))
            {
                e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.BLOCK_LEVER_CLICK, 1, 1);
                if (!ModBow.containsKey(e.getPlayer()))
                {
                    ModBow.put(e.getPlayer(), 0);
                    e.getPlayer().sendMessage(ChatColor.YELLOW + "Mod Bow mode changed to Bridge");
                    ItemMeta meta = e.getItem().getItemMeta();
                    meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&f&lBridge Mod Bow"));
                    e.getItem().setItemMeta(meta);
                }
                else
                {
                    int i = ModBow.get(e.getPlayer());
                    i++;

                    if (i == 3)
                        i = 0;
                    switch (i)
                    {
                        case 0:
                            e.getPlayer().sendMessage(ChatColor.YELLOW + "Mod Bow mode changed to Bridge");
                            ItemMeta meta = e.getItem().getItemMeta();
                            meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&f&lBridge Mod Bow"));
                            e.getItem().setItemMeta(meta);
                            break;
                        case 1:
                            e.getPlayer().sendMessage(ChatColor.YELLOW + "Mod Bow mode changed to Fireball");
                            ItemMeta meta2 = e.getItem().getItemMeta();
                            meta2.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&c&lFireball Mod Bow"));
                            e.getItem().setItemMeta(meta2);
                            break;
                        case 2:
                            e.getPlayer().sendMessage(ChatColor.YELLOW + "Mod Bow mode changed to Heal");
                            ItemMeta meta3 = e.getItem().getItemMeta();
                            meta3.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&e&lHealing Mod Bow"));
                            e.getItem().setItemMeta(meta3);
                            break;
                    }

                    // System.out.println(i);
                    ModBow.put(e.getPlayer(), i);
                }
            }
        }
    }

    public static HashMap<Player, Integer> ModBow = new HashMap<>();
    public static List<Entity> Falling = new ArrayList<>();

    public static List<Block> getNearbyPlacedBlocks(Location location, int radius)
    {
        List<Block> blocks = new ArrayList<>();
        for (int x = location.getBlockX() - radius; x <= location.getBlockX() + radius; x++)
        {
            for (int y = location.getBlockY() - radius; y <= location.getBlockY() + radius; y++)
            {
                for (int z = location.getBlockZ() - radius; z <= location.getBlockZ() + radius; z++)
                {
                    if (BedwarsAPI.getInstance().getFirstRunningGame().isBlockAddedDuringGame(new Location(BedwarsAPI.getInstance().getFirstRunningGame().getGameWorld(), x, y, z)))
                        blocks.add(location.getWorld().getBlockAt(x, y, z));
                }
            }
        }
        return blocks;
    }

    void doFireball(ItemStack item, Player p)
    {
        item.setAmount(item.getAmount() - 1);
        FireballCooldown.put(p, System.currentTimeMillis());
        new BukkitRunnable()
        {
            final Location spawnloc = p.getLocation();
            final Vector facing = p.getLocation().getDirection();
            @Override
            public void run()
            {
                Fireball ball = p.launchProjectile(Fireball.class);
                ball.setMetadata("sender", new FixedMetadataValue(Objects.requireNonNull(Bukkit.getServer().getPluginManager().getPlugin("Invictools")), p.getName()));
                ball.setInvulnerable(true);
                ball.setYield(3);
                ball.setShooter(p);
                //new ProjTrailHandler().grabEffect(p, ball);
                p.playSound(p.getLocation(), Sound.ENTITY_BLAZE_SHOOT, 1, 1);
                ball.teleport(spawnloc);
                ball.setVelocity((facing.multiply(1.1)));

                BukkitRunnable runnable = new BukkitRunnable()
                {
                    @Override
                    public void run()
                    {
                        ball.setInvulnerable(false);
                    }
                };
                runnable.runTaskLater(Commands.Invictools, 15);
            }
        }.runTaskLater(Commands.Invictools, 5L); // corrects directional errors
    }

    void incChange(List<Block> blocks, boolean type, Location loc, boolean sound)
    {

        new BukkitRunnable()
        {
            int i = 0;

            @Override
            public void run()
            {
                // System.out.println(i + " "+blocks.size());
                if (i < blocks.size())
                {
                    blocks.get(i).setType(Material.BLACK_WOOL);
                    if (type)
                    {
                        if (blocks.size() > i + 1)
                            blocks.get(i + 1).setType(Material.BLACK_WOOL);
                        if (blocks.size() > i + 2)
                            blocks.get(i + 2).setType(Material.BLACK_WOOL);
                        if (blocks.size() - i - 1 > 0)
                            blocks.get(blocks.size() - i - 1).setType(Material.BLACK_WOOL);
                        if (blocks.size() - i - 2 > 0)
                            blocks.get(blocks.size() - i - 2).setType(Material.BLACK_WOOL);
                        if (blocks.size() - i - 3 > 0)
                            blocks.get(blocks.size() - i - 3).setType(Material.BLACK_WOOL);
                    }

                    if (sound)
                        Bukkit.getOnlinePlayers().forEach((player) -> player.playSound(blocks.get(i).getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, (float) ((double) i/10+.8)));

                    if (type)
                        i += 3;
                    else
                        i++;

                    if (i > 20 * 3)
                    {
                        i = blocks.size() + 1;
                        for (Block block : blocks)
                        {
                            block.setType(Material.BLACK_WOOL);
                        }
                    }
                    else if (type && i > blocks.size() - 3)
                    {
                        i = blocks.size() + 1;
                        for (Block block : blocks)
                        {
                            block.setType(Material.BLACK_WOOL);
                        }
                    }
                }
                else
                {
                    for (Block block : blocks)
                    {
                        if (new Random().nextInt(9) == 3)
                            block.getLocation().getWorld().strikeLightningEffect(block.getLocation());

                        TNTPrimed tnt = block.getLocation().getWorld().spawn(block.getLocation(), TNTPrimed.class);
                        tnt.setFuseTicks(10);
                    }
                    grabSandstone.taggedBlocks.removeAll(blocks);
                    blocks.clear();
                    this.cancel();
                }
            }
        }.runTaskTimer(Objects.requireNonNull(Bukkit.getServer().getPluginManager().getPlugin("Invictools")), 2L, 1L);
    }

    public static HashMap<Player, Long> FireballCooldown = new HashMap<>();
}
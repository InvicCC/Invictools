package me.invic.invictools.cosmetics;

import me.invic.invictools.commands.Commands;
import me.invic.invictools.util.Leaderboards.leaderboard;
import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.*;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class Lobby1Handler
{
    public static HashMap<Player, Boolean> cancelLobbyCosmetic = new HashMap<>();
    public static HashMap<Player, Long> lobbyCooldown = new HashMap<>();
    static int cooldown = 5; // seconds

    public static void FullHandle(Player p)
    {
        if (lobbyCooldown.containsKey(p))
        {
            long secondsLeft = ((lobbyCooldown.get(p) / 1000) + cooldown) - (System.currentTimeMillis() / 1000);
            if (secondsLeft <= 0)
            {
                into(p);
                lobbyCooldown.put(p, System.currentTimeMillis());
            }
        }
        else
        {
            into(p);
            lobbyCooldown.put(p, System.currentTimeMillis());
        }
    }

    public static void into(Player effectOwner)
    {
        if (!effectOwner.getWorld().getName().equalsIgnoreCase("bwlobby"))
            return;

        File Folder = new File(Commands.Invictools.getDataFolder(), "PlayerData");
        File pFile = new File(Folder, effectOwner.getUniqueId() + ".yml");
        FileConfiguration balls = YamlConfiguration.loadConfiguration(pFile);
        String effect = balls.getString("Lobby1");

        //effectOwner.playSound(effectOwner.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10, 350);
        if (effect == null)
            return;

        if (cancelLobbyCosmetic.get(effectOwner) != null)
            return;

        switch (effect.toLowerCase(Locale.ROOT))
        {
            case "firefeet":
                FireFeet(effectOwner);
                break;
            case "watercrown":
                WaterCrown(effectOwner);
                break;
            case "lavacrown":
                LavaCrown(effectOwner);
                break;
            case "error":
                Error(effectOwner);
                break;
            case "rank":
                Rank(effectOwner);
                break;
        }
    }

    public static HashMap<String, Integer> Lobby1Effects()
    {
        HashMap<String, Integer> e = new HashMap<>();
        e.put("FireFeet", -2);
        e.put("WaterCrown", 50);
        e.put("LavaCrown", 100);
        e.put("Rank", -1);
        e.put("Error", 20);
        return e;
    }

    public static void configHandler(Player player, String effect, boolean bypass)
    {
        Plugin plugin = Commands.Invictools;
        File Folder = new File(plugin.getDataFolder(), "PlayerData");
        File pFile = new File(Folder, player.getUniqueId() + ".yml");
        final FileConfiguration playerData = YamlConfiguration.loadConfiguration(pFile);

        Plugin bw = Bukkit.getServer().getPluginManager().getPlugin("BedWars");
        File f2 = new File(bw.getDataFolder(), "database");
        File File = new File(f2, "bw_stats_players.yml");
        final FileConfiguration data = YamlConfiguration.loadConfiguration(File);
        int wins = data.getInt("data." + player.getUniqueId() + ".score");
        wins = new leaderboard().starLevel(wins);

        if (effect.equals("none"))
        {
            playerData.set("Lobby1", "none");
            cancelLobbyCosmetic.put(player, true);
            player.sendMessage(ChatColor.YELLOW + "Your Lobby Cosmetic is now disabled.");
        }
        else if (Lobby1Effects().get(effect) != null)
        {

            if (Lobby1Effects().get(effect) <= wins && Lobby1Effects().get(effect) > -1 || bypass)
            {
                playerData.set("Lobby1", effect);
                player.sendMessage(ChatColor.YELLOW + "Your Lobby Cosmetic is now set to " + ChatColor.AQUA + effect);
                cancelLobbyCosmetic.put(player, true);
                new BukkitRunnable()
                {
                    @Override
                    public void run()
                    {
                        new BukkitRunnable()
                        {
                            @Override
                            public void run()
                            {
                                Lobby1Handler.FullHandle(player);
                            }
                        }.runTaskLater(Commands.Invictools, 5L);
                        cancelLobbyCosmetic.remove(player);
                    }
                }.runTaskLater(Commands.Invictools, 5L);
            }
            else if (Lobby1Effects().get(effect) == -1 && player.hasPermission("invic.firestick"))
            {
                playerData.set("Lobby1", effect);
                player.sendMessage(ChatColor.YELLOW + "Your Lobby Cosmetic is now set to " + ChatColor.AQUA + effect);
                cancelLobbyCosmetic.put(player, true);
                new BukkitRunnable()
                {
                    @Override
                    public void run()
                    {
                        new BukkitRunnable()
                        {
                            @Override
                            public void run()
                            {
                                Lobby1Handler.FullHandle(player);
                            }
                        }.runTaskLater(Commands.Invictools, 5L);
                        cancelLobbyCosmetic.remove(player);
                    }
                }.runTaskLater(Commands.Invictools, 5L);
            }
            else
            {
                player.sendMessage(ChatColor.RED + "You haven't unlocked this effect yet!");
                if (Lobby1Effects().get(effect) == -1)
                {
                    player.sendMessage(ChatColor.RED + "This effect can also be unlocked with a rank.");
                    player.sendMessage(ChatColor.AQUA + "Type /ranks to learn how to get one");
                }
                else
                    player.sendMessage(ChatColor.RED + "You only have " + wins + " / " + Lobby1Effects().get(effect) + " stars.");
            }

            try
            {
                playerData.save(pFile);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    public static void FireFeet(Player p)
    {
        World world = p.getWorld();
        new BukkitRunnable()
        {
            @Override
            public void run()
            {
                p.getWorld().spawnParticle(Particle.FLAME, p.getLocation(), 1, 0, 0, 0, 0);

                if (!p.isOnline() || !p.getWorld().equals(world))
                    this.cancel();

                if (cancelLobbyCosmetic.get(p) != null)
                {
                    this.cancel();
                    cancelLobbyCosmetic.remove(p);
                }
            }
        }.runTaskTimer(Commands.Invictools, 0L, 1L);
    }

    private static void WaterCrown(Player p)
    {
        World world = p.getWorld();
        new BukkitRunnable()
        {
            Location loc = p.getLocation();
            int i = 0;

            @Override
            public void run()
            {
                if (p.getLocation().getX() == loc.getX()
                        && p.getLocation().getY() == loc.getY()
                        && p.getLocation().getZ() == loc.getZ())
                {
                    switch (i)
                    {
                        case 0:
                            p.getWorld().spawnParticle(Particle.DRIP_WATER, p.getLocation().clone().add(.4, 2, 0), 1);
                            break;
                        case 1:
                            p.getWorld().spawnParticle(Particle.DRIP_WATER, p.getLocation().clone().add(.3, 2, .3), 1);
                            break;
                        case 2:
                            p.getWorld().spawnParticle(Particle.DRIP_WATER, p.getLocation().clone().add(0, 2, .4), 1);
                            break;
                        case 3:
                            p.getWorld().spawnParticle(Particle.DRIP_WATER, p.getLocation().clone().add(-.3, 2, .3), 1);
                            break;
                        case 4:
                            p.getWorld().spawnParticle(Particle.DRIP_WATER, p.getLocation().clone().add(-.4, 2, 0), 1);
                            break;
                        case 5:
                            p.getWorld().spawnParticle(Particle.DRIP_WATER, p.getLocation().clone().add(-.3, 2, -.3), 1);
                            break;
                        case 6:
                            p.getWorld().spawnParticle(Particle.DRIP_WATER, p.getLocation().clone().add(0, 2, -.4), 1);
                            break;
                        case 7:
                            p.getWorld().spawnParticle(Particle.DRIP_WATER, p.getLocation().clone().add(.3, 2, -.3), 1);
                            break;
                    }
                    i++;
                    if (i == 8)
                        i = 0;
                }

                if (!p.isOnline() || !p.getWorld().equals(world))
                    this.cancel();

                if (cancelLobbyCosmetic.get(p) != null)
                {
                    this.cancel();
                    cancelLobbyCosmetic.remove(p);
                }

                loc = p.getLocation();
            }
        }.runTaskTimer(Commands.Invictools, 0L, 5L);
    }

    private static void LavaCrown(Player p)
    {
        World world = p.getWorld();
        new BukkitRunnable()
        {
            Location loc = p.getLocation();
            int i = 0;

            @Override
            public void run()
            {
                if (p.getLocation().getX() == loc.getX()
                        && p.getLocation().getY() == loc.getY()
                        && p.getLocation().getZ() == loc.getZ())
                {
                    switch (i)
                    {
                        case 0:
                            p.getWorld().spawnParticle(Particle.DRIP_LAVA, p.getLocation().clone().add(.4, 2, 0), 1);
                            break;
                        case 1:
                            p.getWorld().spawnParticle(Particle.DRIP_LAVA, p.getLocation().clone().add(.3, 2, .3), 1);
                            break;
                        case 2:
                            p.getWorld().spawnParticle(Particle.DRIP_LAVA, p.getLocation().clone().add(0, 2, .4), 1);
                            break;
                        case 3:
                            p.getWorld().spawnParticle(Particle.DRIP_LAVA, p.getLocation().clone().add(-.3, 2, .3), 1);
                            break;
                        case 4:
                            p.getWorld().spawnParticle(Particle.DRIP_LAVA, p.getLocation().clone().add(-.4, 2, 0), 1);
                            break;
                        case 5:
                            p.getWorld().spawnParticle(Particle.DRIP_LAVA, p.getLocation().clone().add(-.3, 2, -.3), 1);
                            break;
                        case 6:
                            p.getWorld().spawnParticle(Particle.DRIP_LAVA, p.getLocation().clone().add(0, 2, -.4), 1);
                            break;
                        case 7:
                            p.getWorld().spawnParticle(Particle.DRIP_LAVA, p.getLocation().clone().add(.3, 2, -.3), 1);
                            break;
                    }
                    i++;
                    if (i == 8)
                        i = 0;
                }

                if (!p.isOnline() || !p.getWorld().equals(world))
                    this.cancel();

                if (cancelLobbyCosmetic.get(p) != null)
                {
                    this.cancel();
                    cancelLobbyCosmetic.remove(p);
                }

                loc = p.getLocation();
            }
        }.runTaskTimer(Commands.Invictools, 0L, 5L);
    }

    private static void Rank(Player p)
    {
        File Folder2 = new File(Commands.Invictools.getDataFolder(), "PlayerData");
        File pFile = new File(Folder2, p.getUniqueId() + ".yml");
        final FileConfiguration playerData = YamlConfiguration.loadConfiguration(pFile);

        String s = playerData.getString("Displayname");
        Color color;

        if (s.charAt(1) == 'f')
            color = ChatColorToColor(s.charAt(4));
        else
            color = ChatColorToColor(s.charAt(1));
        Particle.DustOptions dust = new Particle.DustOptions(color, 1);


        World world = p.getWorld();
        new BukkitRunnable()
        {
            Location loc = p.getLocation();
            int i = 0;

            @Override
            public void run()
            {
                if (p.getLocation().getX() == loc.getX()
                        && p.getLocation().getY() == loc.getY()
                        && p.getLocation().getZ() == loc.getZ())
                {
                    p.getWorld().spawnParticle(Particle.REDSTONE, p.getLocation().clone().add(0, 1, 0), 5, .5, 1, .5, dust);

                    if (!p.isOnline() || !p.getWorld().equals(world))
                        this.cancel();

                    if (cancelLobbyCosmetic.get(p) != null)
                    {
                        this.cancel();
                        cancelLobbyCosmetic.remove(p);
                    }
                }
                loc = p.getLocation();
            }
        }.runTaskTimer(Commands.Invictools, 0L, 5L);
    }

    private static void Error(Player p)
    {
        ChatColor color = new leaderboard().presColor(p);
        World world = p.getWorld();
        new BukkitRunnable()
        {
            @Override
            public void run()
            {
                Bat f = (Bat) world.spawnEntity(p.getLocation().add((new Random().nextDouble() - .5) * 2, new Random().nextDouble() + 2, (new Random().nextDouble() - .5) * 2), EntityType.BAT);
                f.setSilent(true);
                f.setInvisible(true);
                f.setInvulnerable(true);
                ArmorStand bat = (ArmorStand) world.spawnEntity(f.getLocation().clone().add(0, 100, 0), EntityType.ARMOR_STAND);
                new BukkitRunnable()
                {
                    @Override
                    public void run()
                    {
                        bat.teleport(f);
                        f.addPassenger(bat);
                    }
                }.runTaskLater(Commands.Invictools, 1L);
                bat.setInvisible(true);
                bat.setCustomName(color + ChatColor.translateAlternateColorCodes('&', "&k&l[O]"));
                bat.setCustomNameVisible(true);
                // bat.setVelocity(bat.getVelocity().add(new Vector((new Random().nextDouble()-.5)/4,.3,(new Random().nextDouble()-.5)/4)));
                new BukkitRunnable()
                {
                    @Override
                    public void run()
                    {
                        bat.remove();
                        f.remove();
                    }
                }.runTaskLater(Commands.Invictools, 25L);

                if (!p.isOnline() || !p.getWorld().equals(world))
                    this.cancel();

                if (cancelLobbyCosmetic.get(p) != null)
                {
                    this.cancel();
                    cancelLobbyCosmetic.remove(p);
                }
            }
        }.runTaskTimer(Commands.Invictools, 0L, 5L);
    }

    public static Color ChatColorToColor(char c)
    {
        switch (c)
        {
            case 'c':
                return Color.RED;
            case 'b':
                return Color.AQUA;
            case '6':
                return Color.ORANGE;
            case 'a':
                return Color.LIME;
            case '7':
                return Color.GRAY;
            case '5':
                return Color.PURPLE;
            case 'd':
                return Color.FUCHSIA;
            default:
                return Color.BLACK;
        }
    }
}

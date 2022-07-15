package me.invic.invictools.cosmetics;

import it.unimi.dsi.fastutil.Hash;
import me.invic.invictools.Commands;
import me.invic.invictools.cosmetics.bedbreaks.TornadoBedBreak;
import me.invic.invictools.cosmetics.projtrail.ProjTrailHandler;
import me.invic.invictools.util.leaderboard;
import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;

public class NormalKillHandler
{
    public void grabEffect(Player p, Player killed, Location loc)
    {
        Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("Invictools");
        File Folder = new File(plugin.getDataFolder(), "PlayerData");
        File pFile = new File(Folder, p.getUniqueId() + ".yml");
        FileConfiguration balls = YamlConfiguration.loadConfiguration(pFile);
        String effect = balls.getString("NormalKill");
        if (effect == null)
            return;

        if (effect.equalsIgnoreCase("none"))
            return;

        effectSwitch(p, killed, loc, effect);
    }

    public void effectSwitch(Player player, Player killed, Location loc, String s)
    {
        switch (s.toLowerCase(Locale.ROOT))
        {
            case "blood":
                blood(loc);
                break;
            case "xp":
                exp(loc);
                break;
            case "tnt":
                tnt(loc);
                break;
            case "sizzle":
                sizzle(loc);
                break;
            case "portal":
                portal(loc);
                break;
            case "prestige":
                prestige(loc, player);
                break;
        }
    }

    public static HashMap<String, Integer> NormalKillEffects()
    {
        HashMap<String, Integer> e = new HashMap<>();
        e.put("blood", 20);
        e.put("xp", 40);
        e.put("tnt", 5);
        e.put("sizzle", 50);
        e.put("portal", 30);
        e.put("prestige", 10);
        return e;
    }

    private void portal(Location loc)
    {
        loc.getWorld().playSound(loc, Sound.ENTITY_ENDERMAN_DEATH, 1, 1);
        loc.getWorld().spawnParticle(Particle.PORTAL, loc, 20, 0, 1.5, 0);
        loc.getWorld().playEffect(loc, Effect.STEP_SOUND, Material.NETHER_PORTAL);
    }

    private void prestige(Location loc, Player p)
    {
        Particle.DustOptions dust = new Particle.DustOptions(ProjTrailHandler.presColor(p), 2);
        loc.getWorld().spawnParticle(Particle.REDSTONE, loc, 7, .5, 1, .5, dust);
        loc.getWorld().spawnParticle(Particle.CRIT_MAGIC, loc, 20, 0, 1.5, 0);
        loc.getWorld().playSound(loc, Sound.BLOCK_FIRE_EXTINGUISH, 5, 1);
    }

    private void blood(Location loc)
    {
        new BukkitRunnable()
        {
            @Override
            public void run()
            {
                loc.getWorld().playEffect(loc, Effect.STEP_SOUND, Material.REDSTONE_BLOCK);
            }
        }.runTaskLater(Commands.Invictools, 1L);
        loc.getWorld().playEffect(loc.clone().add(0, .6, 0), Effect.STEP_SOUND, Material.REDSTONE_BLOCK);
    }

    private void exp(Location loc)
    {
        loc.getWorld().spawnEntity(loc, EntityType.EXPERIENCE_ORB);
        loc.getWorld().spawnEntity(loc, EntityType.EXPERIENCE_ORB);
        loc.getWorld().spawnEntity(loc, EntityType.EXPERIENCE_ORB);
        loc.getWorld().spawnEntity(loc, EntityType.EXPERIENCE_ORB);
        loc.getWorld().spawnEntity(loc, EntityType.EXPERIENCE_ORB);
        loc.getWorld().playSound(loc, Sound.ENTITY_PLAYER_LEVELUP, 10, 1);
    }

    private void tnt(Location loc)
    {
        loc.getWorld().playSound(loc, Sound.ENTITY_GENERIC_EXPLODE, 1, 1);
        loc.getWorld().spawnParticle(Particle.EXPLOSION_LARGE, loc, 1);
    }

    private void sizzle(Location loc)
    {
        loc.getWorld().playSound(loc, Sound.BLOCK_FIRE_EXTINGUISH, 1, 1);
        loc.getWorld().spawnParticle(Particle.LAVA, loc, 5);
    }

    public void configHandler(Player player, String effect, boolean bypass)
    {
        effect = effect.toLowerCase(Locale.ROOT);
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


        // DOES NOT SET CORRECTLY.
        //   System.out.println("setting");
        //   System.out.println(effect);
        if (effect.equals("none"))
        {
            playerData.set("NormalKill", "none");
            player.sendMessage(ChatColor.YELLOW + "Your Normal Kill Effect is now disabled.");
        }
        else /*if(NormalKillEffects().get(effect) != null)*/
        {
            if (NormalKillEffects().get(effect) <= wins || bypass)
            {
                playerData.set("NormalKill", effect);
                player.sendMessage(ChatColor.YELLOW + "Your Normal Kill effect is now set to " + ChatColor.AQUA + effect);
            }
            else if (NormalKillEffects().get(effect) == 1000 && player.hasPermission("invic.firestick"))
            {
                playerData.set("NormalKill", effect);
                player.sendMessage(ChatColor.YELLOW + "Your Normal Kill effect is now set to " + ChatColor.AQUA + effect);
            }
            else
            {
                player.sendMessage(ChatColor.RED + "You haven't unlocked this effect yet!");
                player.sendMessage(ChatColor.RED + "You only have " + wins + " stars.");
                if (NormalKillEffects().get(effect) == -1)
                {
                    player.sendMessage(ChatColor.RED + "This effect can also be unlocked with a rank.");
                    player.sendMessage(ChatColor.AQUA + "Type /ranks to learn how to get one");
                }
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
}

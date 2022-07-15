package me.invic.invictools.cosmetics.projtrail;

import me.invic.invictools.cosmetics.VictoryDances.VictoryDanceHandler;
import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.screamingsandals.bedwars.api.BedwarsAPI;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class ProjTrailHandler
{
    public void grabEffect(Player effectOwner, Entity entity)
    {
        //System.out.println("called on "+ entity);
        Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("Invictools");
        File Folder = new File(plugin.getDataFolder(), "PlayerData");
        File pFile = new File(Folder, effectOwner.getUniqueId() + ".yml");
        FileConfiguration balls = YamlConfiguration.loadConfiguration(pFile);
        String effect = balls.getString("ProjTrail");

        if(effect == null)
            new ProjTrailConfig(effectOwner,"Crit",false);

        effectSwitch(effect, effectOwner, entity);
    }

    public List<String> getEffects()
    {
        List<String> effects = new ArrayList<>();
        effects.add("notes");
        effects.add("hearts");
        effects.add("pres");
        effects.add("lava");
        effects.add("sculk");
        effects.add("smoke");
        effects.add("crit");
        return effects;
    }

    public void effectSwitch(String s, Player player, Entity entity)
    {
        switch (s.toLowerCase(Locale.ROOT))
        {
            case "notes":
                Notes(entity);
                break;
            case "hearts":
                Hearts(entity);
                break;
            case "pres":
                pres(entity,player);
                break;
            case "lava":
                Lava(entity);
                break;
            case "sculk":
                barrier(entity,true);
            case "bubble":
                bubble(entity);
                break;
            case "smoke":
                smoke(entity);
                break;
            case "crit":
                crit(entity);
                break;
            default:
                crit(entity);
        }
    }

    // not real effect
    public void bubble(Entity entity)
    {
        new BukkitRunnable()
        {
            int i=0;
            @Override
            public void run()
            {
                entity.getWorld().spawnParticle(Particle.WATER_BUBBLE, entity.getLocation(), 10, .8, .8, .8,.2);
                //  System.out.println(entity.getLocation() + " "+entity.getWorld().getName());

                if(entity.isDead() || entity.isOnGround())
                {
                    entity.getWorld().spawnParticle(Particle.BUBBLE_POP, entity.getLocation(), 10, .8, .8, .8,.2);
                    this.cancel();
                }

                if(entity.getType().equals(EntityType.PLAYER))
                {
                    Player p = (Player) entity;
                    if(!p.isGliding())
                    {
                        entity.getWorld().spawnParticle(Particle.BUBBLE_POP, entity.getLocation(), 10, .8, .8, .8,.2);
                        this.cancel();
                    }
                }
                else if(i>100)
                    this.cancel();

                i++;
            }
        }.runTaskTimer(Objects.requireNonNull(Bukkit.getServer().getPluginManager().getPlugin("Invictools")), 3L, 1L);
    }

    public void smoke(Entity entity)
    {
        new BukkitRunnable()
        {
            int i=0;
            @Override
            public void run()
            {
                entity.getWorld().spawnParticle(Particle.CAMPFIRE_COSY_SMOKE, entity.getLocation(), 1, 0, 0, 0,0);
                entity.getWorld().spawnParticle(Particle.CAMPFIRE_SIGNAL_SMOKE, entity.getLocation(), 1, 0, 0, 0,0);
                //  System.out.println(entity.getLocation() + " "+entity.getWorld().getName());

                if(entity.isDead() || entity.isOnGround())
                {
                    entity.getLocation().getWorld().spawnParticle(Particle.LAVA, entity.getLocation(),12);
                    this.cancel();
                }

                if(entity.getType().equals(EntityType.PLAYER))
                {
                    Player p = (Player) entity;
                    if(!p.isGliding())
                    {
                        entity.getLocation().getWorld().spawnParticle(Particle.LAVA, entity.getLocation(), 5);
                        this.cancel();
                    }
                }
                else if(i>100)
                    this.cancel();

                i++;
            }
        }.runTaskTimer(Objects.requireNonNull(Bukkit.getServer().getPluginManager().getPlugin("Invictools")), 3L, 1L);
    }

    public void barrier(Entity entity, boolean glide)
    {
        new BukkitRunnable()
        {
            int i=0;
            @Override
            public void run()
            {
                entity.getWorld().spawnParticle(Particle.SONIC_BOOM, entity.getLocation(), 1, 0, 0, 0);

                if(entity.isDead() || entity.isOnGround())
                {
                    new BukkitRunnable()
                    {
                        @Override
                        public void run()
                        {
                            entity.getWorld().spawnParticle(Particle.SCULK_SOUL, entity.getLocation(), 30, 0, 0, 0,.5);
                            entity.getWorld().spawnParticle(Particle.SOUL_FIRE_FLAME, entity.getLocation(), 30, 0, 0, 0,.5);
                        }
                    }.runTaskLater(Objects.requireNonNull(Bukkit.getServer().getPluginManager().getPlugin("Invictools")), 10L);
                    this.cancel();
                }

                if(entity.getType().equals(EntityType.PLAYER) && glide)
                {
                    Player p = (Player) entity;
                    if(!p.isGliding())
                    {
                        new BukkitRunnable()
                        {
                            @Override
                            public void run()
                            {
                                entity.getWorld().spawnParticle(Particle.SCULK_SOUL, entity.getLocation(), 10, 0, 0, 0,.5);
                                entity.getWorld().spawnParticle(Particle.SOUL_FIRE_FLAME, entity.getLocation(), 30, 0, 0, 0,.5);
                            }
                        }.runTaskLater(Objects.requireNonNull(Bukkit.getServer().getPluginManager().getPlugin("Invictools")), 10L);

                        this.cancel();
                    }
                }
                else if(i>100)
                    this.cancel();

                i++;
            }
        }.runTaskTimer(Objects.requireNonNull(Bukkit.getServer().getPluginManager().getPlugin("Invictools")), 3L, 1L);
    }

    public void crit(Entity entity)
    {
        new BukkitRunnable()
        {
            int i=0;
            @Override
            public void run()
            {
                entity.getWorld().spawnParticle(Particle.CRIT, entity.getLocation(), 5, 0, 0, 0);
                //  System.out.println(entity.getLocation() + " "+entity.getWorld().getName());

                if(entity.isDead())
                    this.cancel();

                if(entity.isOnGround())
                    this.cancel();

                if(entity.getType().equals(EntityType.PLAYER))
                {
                    Player p = (Player) entity;
                    if(!p.isGliding())
                        this.cancel();
                }
                else if(i>100)
                    this.cancel();

                i++;
            }
        }.runTaskTimer(Objects.requireNonNull(Bukkit.getServer().getPluginManager().getPlugin("Invictools")), 3L, 4L);
    }

    public void Notes(Entity entity)
    {
        new BukkitRunnable()
        {
            int i=0;
            @Override
            public void run()
            {
                entity.getWorld().spawnParticle(Particle.NOTE, entity.getLocation(), 5, .6, .6, .6);
               // System.out.println(entity.getLocation() + " "+entity.getWorld().getName());

                if(entity.isDead())
                    this.cancel();

                if(entity.isOnGround())
                    this.cancel();

                if(entity.getType().equals(EntityType.PLAYER))
                {
                   Player p = (Player) entity;
                   if(!p.isGliding())
                       this.cancel();
                }
                else if(i>100)
                    this.cancel();

                    i++;
            }
        }.runTaskTimer(Objects.requireNonNull(Bukkit.getServer().getPluginManager().getPlugin("Invictools")), 3L, 2L);
    }

    public void Hearts(Entity entity)
    {
        new BukkitRunnable()
        {
            int i=0;
            @Override
            public void run()
            {
                entity.getWorld().spawnParticle(Particle.HEART, entity.getLocation(), 1, 0, 0, 0);
              //  System.out.println(entity.getLocation() + " "+entity.getWorld().getName());

                if(entity.isDead())
                    this.cancel();

                if(entity.isOnGround())
                    this.cancel();

                if(entity.getType().equals(EntityType.PLAYER))
                {
                    Player p = (Player) entity;
                    if(!p.isGliding())
                        this.cancel();
                }
                else if(i>100)
                    this.cancel();

                i++;
            }
        }.runTaskTimer(Objects.requireNonNull(Bukkit.getServer().getPluginManager().getPlugin("Invictools")), 3L, 2L);
    }

    public void Lava(Entity entity)
    {
        new BukkitRunnable()
        {
            int i=0;
            @Override
            public void run()
            {
                entity.getLocation().getWorld().spawnParticle(Particle.DRIP_LAVA, entity.getLocation(),1);
                entity.getLocation().getWorld().spawnParticle(Particle.LAVA, entity.getLocation(),1);
                //  System.out.println(entity.getLocation() + " "+entity.getWorld().getName());

                if(entity.isDead())
                    this.cancel();

                if(entity.isOnGround())
                    this.cancel();

                if(entity.getType().equals(EntityType.PLAYER))
                {
                    Player p = (Player) entity;
                    if(!p.isGliding())
                        this.cancel();
                }
                else if(i>100)
                    this.cancel();

                i++;
            }
        }.runTaskTimer(Objects.requireNonNull(Bukkit.getServer().getPluginManager().getPlugin("Invictools")), 3L, 1L);
    }

    public void pres(Entity entity, Player p)
    {
        Particle.DustOptions dust = new Particle.DustOptions(presColor(p),5);
      //  Random random = new Random();
      //  int i;
        new BukkitRunnable()
        {
            int i=0;
            @Override
            public void run()
            {
                /*
                i = random.nextInt(8);
                if(i==0)
                    entity.getLocation().getWorld().spawnParticle(Particle.REDSTONE, entity.getLocation().clone().add(0,0,.5), 0, 0, 0, 0, dust);
                else if(i == 1)
                    entity.getLocation().getWorld().spawnParticle(Particle.REDSTONE, entity.getLocation().clone().add(.5,0,.5), 0, 0, 0, 0, dust);
                else if(i == 2)
                    entity.getLocation().getWorld().spawnParticle(Particle.REDSTONE, entity.getLocation().clone().add(0,0,-.5), 0, 0, 0, 0, dust);
                else if(i == 3)
                    entity.getLocation().getWorld().spawnParticle(Particle.REDSTONE, entity.getLocation().clone().add(-.5,0,-.5), 0, 0, 0, 0, dust);
                else if(i == 4)
                    entity.getLocation().getWorld().spawnParticle(Particle.REDSTONE, entity.getLocation().clone().add(-.5,0,.5), 0, 0, 0, 0, dust);
                else if(i == 5)
                    entity.getLocation().getWorld().spawnParticle(Particle.REDSTONE, entity.getLocation().clone().add(.5,0,-.5), 0, 0, 0, 0, dust);
                else if(i == 6)
                    entity.getLocation().getWorld().spawnParticle(Particle.REDSTONE, entity.getLocation().clone().add(0,-.5,0), 0, 0, 0, 0, dust);
                else
                    entity.getLocation().getWorld().spawnParticle(Particle.REDSTONE, entity.getLocation().clone().add(0,0,-.5), 0, 0, 0, 0, dust);
*/
                entity.getLocation().getWorld().spawnParticle(Particle.REDSTONE, entity.getLocation(), 0, 0, 0, 0, dust);
                entity.getLocation().getWorld().spawnParticle(Particle.CRIT, entity.getLocation(),10);

                if(entity.isDead())
                {
                    this.cancel();
                    if(VictoryDanceHandler.isVictoryDancing.get(p.getName())!= null && entity.getType().equals(EntityType.SNOWBALL))
                    {
                        TNTPrimed tnt = (TNTPrimed) entity.getLocation().getWorld().spawnEntity(entity.getLocation(),EntityType.PRIMED_TNT);
                        tnt.setFuseTicks(1);
                        tnt.setYield(3);
                    }
                }


                if(entity.isOnGround())
                    this.cancel();

                if(entity.getType().equals(EntityType.PLAYER))
                {
                    Player p = (Player) entity;
                    if(!p.isGliding())
                        this.cancel();
                }
                else if(i>100)
                    this.cancel();

                i++;
            }
        }.runTaskTimer(Objects.requireNonNull(Bukkit.getServer().getPluginManager().getPlugin("Invictools")), 3L, 1L);
    }

    public static Color presColor(Player p)
    {
        Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("BedWars");
        File Folder = new File(plugin.getDataFolder(), "database");
        File pFile = new File(Folder, "bw_stats_players.yml");
        final FileConfiguration data = YamlConfiguration.loadConfiguration(pFile);
        int defaultlvl = data.getInt("data."+p.getUniqueId()+".score");

        int intlvl = defaultlvl/500+1;
      //  String strlvl = String.valueOf(intlvl);

            if(intlvl >= 0 && intlvl <= 9)
                return Color.GRAY;
            else if(intlvl >= 10 && intlvl <= 19)
                return Color.WHITE;
            else if(intlvl >= 20 && intlvl <= 29)
                return Color.ORANGE;
            else if(intlvl >= 30 && intlvl <= 39)
                return Color.YELLOW;
            else if(intlvl >= 40 && intlvl <= 49)
                return Color.GREEN;
            else if(intlvl >= 50 && intlvl <= 59)
                return Color.TEAL;
            else if(intlvl >= 60 && intlvl <= 69)
                return Color.RED;
            else if(intlvl >= 70 && intlvl <= 79)
                return Color.LIME;
            else if(intlvl >= 80 && intlvl <= 89)
                return Color.BLUE;
            else if(intlvl >= 90 && intlvl <= 99)
                return Color.PURPLE;
            else
                return Color.AQUA;
    }

    public Material PrestigeMaterial(Player p)
    {
        Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("BedWars");
        File Folder = new File(plugin.getDataFolder(), "database");
        File pFile = new File(Folder, "bw_stats_players.yml");
        final FileConfiguration data = YamlConfiguration.loadConfiguration(pFile);
        int defaultlvl = data.getInt("data."+p.getUniqueId()+".score");

        int intlvl = defaultlvl/500+1;
        //  String strlvl = String.valueOf(intlvl);

        if(intlvl >= 0 && intlvl <= 9)
            return Material.GRAY_STAINED_GLASS;
        else if(intlvl >= 10 && intlvl <= 19)
            return Material.WHITE_STAINED_GLASS;
        else if(intlvl >= 20 && intlvl <= 29)
            return Material.ORANGE_STAINED_GLASS;
        else if(intlvl >= 30 && intlvl <= 39)
            return Material.YELLOW_STAINED_GLASS;
        else if(intlvl >= 40 && intlvl <= 49)
            return Material.GREEN_STAINED_GLASS;
        else if(intlvl >= 50 && intlvl <= 59)
            return Material.CYAN_STAINED_GLASS;
        else if(intlvl >= 60 && intlvl <= 69)
            return Material.RED_STAINED_GLASS;
        else if(intlvl >= 70 && intlvl <= 79)
            return Material.LIME_STAINED_GLASS;
        else if(intlvl >= 80 && intlvl <= 89)
            return Material.BLUE_STAINED_GLASS;
        else if(intlvl >= 90 && intlvl <= 99)
            return Material.PURPLE_STAINED_GLASS;
        else
            return Material.LIGHT_BLUE_STAINED_GLASS;
    }
}

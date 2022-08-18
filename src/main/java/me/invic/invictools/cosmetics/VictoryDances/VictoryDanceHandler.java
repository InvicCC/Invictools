package me.invic.invictools.cosmetics.VictoryDances;

import me.invic.invictools.commands.OldCommands;
import me.invic.invictools.cosmetics.finalkills.FinalKillHandler;
import me.invic.invictools.cosmetics.projtrail.ProjTrailConfig;
import me.invic.invictools.cosmetics.projtrail.ProjTrailHandler;
import me.invic.invictools.gamemodifiers.perGameJumpingHandler;
import me.invic.invictools.gamemodifiers.perGameJumpingListener;
import me.invic.invictools.items.ItemListener;
import me.invic.invictools.items.createItems;
import me.invic.invictools.items.dareListener;
import me.invic.invictools.util.ExplosionsListener;
import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.*;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.screamingsandals.bedwars.api.BedwarsAPI;

import java.io.File;
import java.util.*;

public class VictoryDanceHandler implements Listener
{
    static final int effectDuration = 14; // seconds
    public static HashMap<String, Boolean> isVictoryDancing = new HashMap<>();

    public void grabEffect(Player effectOwner)
    {
        File Folder = new File(OldCommands.Invictools.getDataFolder(), "PlayerData");
        File pFile = new File(Folder, effectOwner.getUniqueId() + ".yml");
        FileConfiguration balls = YamlConfiguration.loadConfiguration(pFile);
        String effect = balls.getString("VictoryDance","Firework");

        effectOwner.playSound(effectOwner.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10, 20);
        effectSwitch(effect, effectOwner);
    }

    public List<String> getEffects()
    {
        List<String> effects = new ArrayList<>();
        effects.add("Firestick");
        effects.add("Firework");
        effects.add("Dragon");
        effects.add("Bow");
        effects.add("Snowball");
        effects.add("Dare");
        effects.add("Storm");
        return effects;
    }

    public void effectSwitch(String s, Player player)
    {
        switch (s.toLowerCase(Locale.ROOT))
        {
            case "firestick":
                Firestick(player);
                break;
            case "firework":
                Firework(player);
                break;
            case "bow":
                Bow(player);
                break;
            case "dragon":
                Dragon(player);
                break;
            case "dare":
                Dare(player);
                break;
            case "snowball":
                Snowball(player);
                break;
            case "storm":
                Storm(player);
                break;
            default:
                Firework(player);
        }

        isVictoryDancing.put(player.getName(), true);
        new BukkitRunnable()
        {
            @Override
            public void run()
            {
                isVictoryDancing.remove(player.getName());
            }
        }.runTaskLater(OldCommands.Invictools, effectDuration * 20);
    }

    private void Storm(Player p)
    {

        new BukkitRunnable()
        {
            final Boolean storm = p.getWorld().hasStorm();
            final World world = p.getWorld();
            final Random rand = new Random();
            boolean cancel = false;

            @Override
            public void run()
            {
                world.setStorm(true);
                world.setThundering(true);

                new BukkitRunnable()
                {
                    @Override
                    public void run()
                    {
                        p.getLocation().getWorld().strikeLightningEffect(new Location(p.getWorld(), p.getLocation().getX() + rand.nextInt(50) - 25, p.getLocation().getY(), p.getLocation().getZ() + rand.nextInt(25) - 50));

                        if (rand.nextInt(2) == 1)
                        {
                            new BukkitRunnable()
                            {
                                @Override
                                public void run()
                                {
                                    p.getLocation().getWorld().strikeLightningEffect(new Location(p.getWorld(), p.getLocation().getX() + rand.nextInt(50) - 25, p.getLocation().getY(), p.getLocation().getZ() + rand.nextInt(50) - 25));
                                }
                            }.runTaskLater(OldCommands.Invictools, 3L);
                        }

                        if (rand.nextInt(3) == 1)
                        {
                            new BukkitRunnable()
                            {
                                @Override
                                public void run()
                                {
                                    p.getLocation().getWorld().strikeLightningEffect(new Location(p.getWorld(), p.getLocation().getX() + rand.nextInt(50) - 25, p.getLocation().getY(), p.getLocation().getZ() + rand.nextInt(50) - 25));
                                }
                            }.runTaskLater(OldCommands.Invictools, 6L);
                        }

                        if (rand.nextInt(3) == 1)
                        {
                            new BukkitRunnable()
                            {
                                @Override
                                public void run()
                                {
                                    p.getLocation().getWorld().strikeLightningEffect(new Location(p.getWorld(), p.getLocation().getX() + rand.nextInt(50) - 25, p.getLocation().getY(), p.getLocation().getZ() + rand.nextInt(50) - 25));
                                }
                            }.runTaskLater(OldCommands.Invictools, 6L);
                        }

                        if (rand.nextInt(3) == 1)
                        {
                            new BukkitRunnable()
                            {
                                @Override
                                public void run()
                                {
                                    p.getLocation().getWorld().strikeLightningEffect(new Location(p.getWorld(), p.getLocation().getX() + rand.nextInt(50) - 25, p.getLocation().getY(), p.getLocation().getZ() + rand.nextInt(50) - 25));
                                }
                            }.runTaskLater(OldCommands.Invictools, 9L);
                        }
                        if (rand.nextInt(4) == 1)
                        {
                            p.getLocation().getWorld().strikeLightningEffect(new Location(p.getWorld(), p.getLocation().getX() + rand.nextInt(25) - 50, p.getLocation().getY(), p.getLocation().getZ() + rand.nextInt(25) - 50));
                        }

                        if (cancel)
                            this.cancel();
                    }
                }.runTaskTimer(OldCommands.Invictools, 40L, 13L);

                new BukkitRunnable()
                {
                    @Override
                    public void run()
                    {
                        if (!storm || world.getName().equals("bwlobby"))
                        {
                            world.setStorm(false);
                            world.setThundering(false);
                            cancel = true;
                        }
                    }
                }.runTaskLater(OldCommands.Invictools, (effectDuration * 18));
            }
        }.runTaskLater(OldCommands.Invictools, 40);
    }

    private void Bow(Player p)
    {
        ItemStack bow = new createItems().RIDEBOW();
        ItemStack arrow = new ItemStack(Material.ARROW);
        arrow.setAmount(64);
        p.getInventory().setItem(9, arrow);

        for (int i = 0; i <= 8; i++)
        {
            p.getInventory().setItem(i, bow);
        }
    }

    private void Snowball(Player p)
    {
        File Folder = new File(OldCommands.Invictools.getDataFolder(), "PlayerData");
        File pFile = new File(Folder, p.getUniqueId() + ".yml");
        FileConfiguration balls = YamlConfiguration.loadConfiguration(pFile);
        String effect = balls.getString("ProjTrail");

        ItemStack arrow = new ItemStack(Material.SNOWBALL);
        ItemMeta meta = arrow.getItemMeta();
        meta.setDisplayName(ChatColor.BOLD + "Snowball");
        arrow.setItemMeta(meta);
        arrow.setAmount(64);
        new ProjTrailConfig(p, "pres", true);

        for (int i = 0; i <= 8; i++)
        {
            p.getInventory().setItem(i, arrow);
        }

        new BukkitRunnable()
        {
            @Override
            public void run()
            {
                new ProjTrailConfig(p, effect, false);
            }
        }.runTaskLater(OldCommands.Invictools, (effectDuration * 20));
    }

    private void Firework(Player player)
    {
        new BukkitRunnable()
        {
            int stop = 0;

            @Override
            public void run()
            {
                new BukkitRunnable()
                {
                    int i = 0;

                    @Override
                    public void run()
                    {
                        switch (i)
                        {
                            case 0:
                                creation(player.getLocation().clone().add(6, 4, 0), player);
                                break;
                            case 1:
                                creation(player.getLocation().clone().add(4, 4, 4), player);
                                break;
                            case 2:
                                creation(player.getLocation().clone().add(0, 4, 6), player);
                                break;
                            case 3:
                                creation(player.getLocation().clone().add(-4, 4, 4), player);
                                break;
                            case 4:
                                creation(player.getLocation().clone().add(-6, 4, 0), player);
                                break;
                            case 5:
                                creation(player.getLocation().clone().add(-4, 4, -4), player);
                                break;
                            case 6:
                                creation(player.getLocation().clone().add(0, 4, -6), player);
                                break;
                            case 7:
                                creation(player.getLocation().clone().add(4, 4, -4), player);
                                break;
                        }
                        i++;
                        if (i == 8)
                            this.cancel();
                    }
                }.runTaskTimer(OldCommands.Invictools, 0L, 5L);
                stop += 70;
                if (effectDuration * 20 < stop)
                    this.cancel();
            }
        }.runTaskTimer(OldCommands.Invictools, 0L, 70L);
    }

    private void creation(Location loc, Player player)
    {
        BedwarsAPI api = BedwarsAPI.getInstance();
        Firework fw = (Firework) loc.getWorld().spawnEntity(loc, EntityType.FIREWORK);
        FireworkMeta fwm = fw.getFireworkMeta();

        if (new Random().nextInt(2) == 1)
        {
            Color color = ProjTrailHandler.presColor(player);
            fwm.addEffect(FireworkEffect.builder().withColor(color).with(FireworkEffect.Type.STAR).flicker(true).trail(true).build());
            fwm.addEffect(FireworkEffect.builder().withColor(Color.WHITE).with(FireworkEffect.Type.STAR).trail(true).flicker(true).build());
            fwm.setPower(25);
        }
        else
        {
            if (api.isPlayerPlayingAnyGame(player) && api.getGameOfPlayer(player).getTeamOfPlayer(player) != null)
            {
                Color color = new FinalKillHandler().teamToColor(api.getGameOfPlayer(player).getTeamOfPlayer(player).getColor().toString());
                fwm.addEffect(FireworkEffect.builder().withColor(color).flicker(true).trail(true).build());
            }
            fwm.addEffect(FireworkEffect.builder().withColor(Color.WHITE).trail(true).flicker(true).build());
            fwm.setPower(10);
        }


        fw.setMetadata("nodamage", new FixedMetadataValue(Objects.requireNonNull(Bukkit.getServer().getPluginManager().getPlugin("Invictools")), true));
        fw.setFireworkMeta(fwm);
        fw.detonate();
    }

    private void Firestick(Player p)
    {
        if (BedwarsAPI.getInstance().isPlayerPlayingAnyGame(p))
        {
            perGameJumpingHandler jumpInfo = new perGameJumpingHandler();
            jumpInfo.setOP(true);
            jumpInfo.setY(5);
            jumpInfo.setX(3);
            perGameJumpingListener.jumpInfo.put(BedwarsAPI.getInstance().getGameOfPlayer(p),jumpInfo);
        }
        ItemStack firestick = new createItems().FIRESTICK();
        for (int i = 0; i <= 8; i++)
        {
            p.getInventory().setItem(i, firestick);
        }
        ItemListener.Falling.add(p);
        p.setInvulnerable(true);
        new BukkitRunnable()
        {
            @Override
            public void run()
            {
                p.setInvulnerable(false);
                ItemListener.Falling.remove(p);
            }
        }.runTaskLater(OldCommands.Invictools, (effectDuration * 20));
    }

    private void Dragon(Player p)
    {
        new BukkitRunnable()
        {
            @Override
            public void run()
            {
                Allay dragon = p.getWorld().spawn(p.getLocation(), Allay.class);
                //  Bat bat = p.getWorld().spawn(p.getLocation(), Bat.class);
                // bat.setInvisible(true);
                //    bat.setInvulnerable(true);
                dragon.setCollidable(false);
                //    bat.addPassenger(dragon);
                dragon.setAware(true);
                dragon.addPassenger(p);
                dragon.setInvulnerable(true);
                //     bat.addPassenger(dragon);
                p.setInvulnerable(true);

                p.getInventory().setItem(40, new ItemStack(Material.SHIELD));

                new BukkitRunnable()
                {
                    @Override
                    public void run()
                    {
                        if (!dragon.isDead())
                        {
                            // System.out.println(p.getLocation().getDirection().multiply(new Vector(2,10,2)));
                            if (!p.isBlocking())
                            {
                                dragon.getLocation().setDirection(p.getLocation().getDirection());
                                dragon.setVelocity((p.getLocation().getDirection().multiply(new Vector(2, 2, 2))));
                                if (!dragon.hasAI())
                                    dragon.setAI(true);
                            }
                            else
                                dragon.setAI(false);
                            dragon.getLocation().setDirection(p.getLocation().getDirection());
                        }
                        else
                            this.cancel();
                    }
                }.runTaskTimer(OldCommands.Invictools, 0L, 1L);
                new BukkitRunnable()
                {
                    @Override
                    public void run()
                    {
                        if (!dragon.isDead())
                        {
                            p.getWorld().playSound(dragon.getLocation(), Sound.ENTITY_ALLAY_HURT, 1, 1);
                            LargeFireball ball = dragon.launchProjectile(LargeFireball.class);
                            new ProjTrailHandler().Hearts(ball);
                            ball.setVelocity((p.getLocation().getDirection()).multiply(5));
                            ball.setYield(5);
                        }
                        else
                            this.cancel();
                    }
                }.runTaskTimer(OldCommands.Invictools, 2L, 5L);
                new BukkitRunnable()
                {
                    @Override
                    public void run()
                    {
                        p.setInvulnerable(false);

                        if (!dragon.isDead())
                            dragon.remove();

                        //        if(!bat.isDead())
                        //       bat.remove();

                    }
                }.runTaskLater(OldCommands.Invictools, effectDuration * 20);
            }
        }.runTaskLater(OldCommands.Invictools, 20);
    }

    private void Dare(Player p)
    {
        new BukkitRunnable()
        {
            @Override
            public void run()
            {
                SkeletonHorse horse = new dareListener().spawnDare(p.getLocation(), p, true, true);
                new BukkitRunnable()
                {
                    @Override
                    public void run()
                    {
                        if (!horse.isDead())
                            horse.damage(horse.getHealth() * 99999);
                    }
                }.runTaskLater(OldCommands.Invictools, (effectDuration * 20));
            }
        }.runTaskLater(OldCommands.Invictools, 20);
    }
}

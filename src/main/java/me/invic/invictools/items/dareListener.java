package me.invic.invictools.items;

import me.invic.invictools.Commands;
import me.invic.invictools.cosmetics.VictoryDances.VictoryDanceHandler;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.SkeletonHorse;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.HorseJumpEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.screamingsandals.bedwars.api.events.BedwarsGameEndEvent;
import org.spigotmc.event.entity.EntityDismountEvent;
import org.spigotmc.event.entity.EntityMountEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class dareListener implements Listener
{
    FileConfiguration config = Commands.Invictools.getConfig();
    public String dareName = ChatColor.translateAlternateColorCodes('§', "§f§lDare Devil");
    public int dareTimeout = config.getInt("dareTimeout") * 20;
    public static List<Entity> dareAir = new ArrayList<>();
    public static HashMap<Player, Long> dareItemCooldown = new HashMap<>();
    int cooldown = 90; //seconds

    public static int dareCounter = 0;

    // spawns dare
    public SkeletonHorse spawnDare(Location loc, Player player, boolean timed, boolean lobby)
    {
        if (dareCounter > 110)
            return null;

        dareCounter++;
        loc.getWorld().strikeLightningEffect(loc);
        SkeletonHorse horse = (SkeletonHorse) player.getWorld().spawnEntity(loc, EntityType.SKELETON_HORSE);
        horse.setCustomName(dareName);
        horse.setJumpStrength(1);
        horse.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(.65);
        horse.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(60);
        // horse.addPotionEffect(PotionEffectType.DAMAGE_RESISTANCE.createEffect(999999,0));
        horse.setHealth(60);
        horse.getInventory().setSaddle(new ItemStack(Material.SADDLE, 1));
        horse.setTamed(true);
        horse.setOwner(player);
        if (!lobby)
            horse.setGlowing(true);
        else
            horse.addPassenger(player);

        ItemListener.Falling.add(horse);
        dareParticle(horse);
        dareBoomLoop(horse);

        //messages and full timer
        if (timed && !lobby)
        {
            dareDeathMessages(player, dareTimeout - (30 * 20), horse, 30);
            dareDeathMessages(player, dareTimeout - (10 * 20), horse, 10);
            dareDeathMessages(player, dareTimeout - (5 * 20), horse, 5);
            new BukkitRunnable()
            {
                @Override
                public void run()
                {
                    if (!horse.isDead())
                        horse.damage(horse.getHealth() * 99999);
                }
            }.runTaskLater(Commands.Invictools, dareTimeout);
        }
        else if (timed) // short timer for lobby / victory dance
        {
            if (VictoryDanceHandler.isVictoryDancing.get(player.getName()) == null)
                dareItemCooldown.put(player, System.currentTimeMillis());

            new BukkitRunnable()
            {
                @Override
                public void run()
                {
                    if (!horse.isDead())
                        horse.damage(horse.getHealth() * 99999);
                }
            }.runTaskLater(Commands.Invictools, 60 * 20);
        }

        heightWatcher(horse);

        return horse;
    }

    // handles lobby item command cooldown
    public void handleItem(Location loc, Player player, boolean timed, boolean lobby)
    {
        if (dareItemCooldown.containsKey(player))
        {
            long secondsLeft = ((dareItemCooldown.get(player) / 1000) + cooldown) - (System.currentTimeMillis() / 1000);
            //  System.out.println(secondsLeft);
            if (secondsLeft <= 0)
                spawnDare(loc, player, timed, lobby);
            else
                player.sendMessage(ChatColor.RED + "On cooldown for " + secondsLeft + " seconds!");
        }
        else
            spawnDare(loc, player, timed, lobby);
    }

    // creates constant particle trail on top of dare
    private void dareParticle(SkeletonHorse dare)
    {
        new BukkitRunnable()
        {
            @Override
            public void run()
            {
                dare.getLocation().getWorld().spawnParticle(Particle.LAVA, dare.getLocation().clone().subtract(0, 1, 0), 1, .5, 0, .5, 0);

                if (dare.isDead())
                    this.cancel();
            }
        }.runTaskTimer(Commands.Invictools, 0L, 2L);
    }

    // creates explosion when dare lands from big jump
    private void dareBoomLoop(SkeletonHorse dare)
    {
        new BukkitRunnable()
        {
            @Override
            public void run()
            {
                if (dareAir.contains(dare) && dare.isOnGround())
                {
                    dareAir.remove(dare);
                    dare.getWorld().playSound(dare.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 1, 1);
                    dare.getLocation().getWorld().spawnParticle(Particle.EXPLOSION_HUGE, dare.getLocation(), 1);
                }

                if (dare.isDead())
                    this.cancel();
            }
        }.runTaskTimer(Commands.Invictools, 0L, 1L);
    }

    // creates message so player knows when the dare will die, called multiple times on spawn
    private void dareDeathMessages(Player p, int delay, SkeletonHorse dare, int till)
    {
        new BukkitRunnable()
        {
            @Override
            public void run()
            {
                if (!dare.isDead())
                {
                    p.sendMessage(ChatColor.AQUA + "Your Dare Devil will die in " + ChatColor.YELLOW + till + ChatColor.AQUA + " seconds!");
                    p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, delay ^ 2);
                }
            }
        }.runTaskLater(Commands.Invictools, delay);
    }

    // creates super jump
    @EventHandler
    public void dareJump(HorseJumpEvent e)
    {
        if (e.getEntity().getName().equalsIgnoreCase(dareName))
        {
            if (!e.getEntity().isOnGround())
            {
                new BukkitRunnable()
                {
                    @Override
                    public void run()
                    {
                        e.getEntity().getLocation().getWorld().spawnParticle(Particle.EXPLOSION_NORMAL, e.getEntity().getLocation().clone(), 25, 0, 0, 0, .15);
                        // e.getEntity().setVelocity(e.getEntity().getFacing().getDirection().add(new Vector(0,1.75,0)));
                        e.getEntity().setVelocity((e.getEntity().getLocation().getDirection().add(new Vector(0, 2, 0))));
                        if (!ItemListener.Falling.contains(e.getEntity().getPassengers().get(0)) && !e.getEntity().isDead())
                            ItemListener.Falling.add(e.getEntity().getPassengers().get(0));
                        if (!dareAir.contains(e.getEntity()))
                            dareAir.add(e.getEntity());
                    }
                }.runTaskLater(Commands.Invictools, 3L);
            }
        }
    }

    // strikes lightning on death and clears air time
    @EventHandler
    public void dareDeath(EntityDeathEvent e)
    {
        dareCounter--;
        if (e.getEntity().getName().equalsIgnoreCase(dareName) && e.getEntity() instanceof SkeletonHorse)
        {
            e.getEntity().getWorld().strikeLightningEffect(e.getEntity().getLocation());
            dareAir.remove(e.getEntity());
            e.getDrops().clear();
            if (e.getEntity().getPassengers().size() != 0)
                if (ItemListener.Falling.contains(e.getEntity().getPassengers().get(0)))
                    ItemListener.Falling.remove(e.getEntity().getPassengers().get(0));
        }
    }

    public void heightWatcher(SkeletonHorse e)
    {
        new BukkitRunnable()
        {
            @Override
            public void run()
            {
                if (e.getLocation().getY() > 350)
                {
                    e.damage(e.getHealth() * 999999);

                    if (!e.isDead())
                        e.remove();
                }
            }
        }.runTaskTimer(Commands.Invictools, 500L, 120L);
    }

    @EventHandler
    public void horseMountEvent(EntityMountEvent e)
    {
        if (e.isCancelled())
            return;

        if (e.getMount().getName().equalsIgnoreCase(dareName) && e.getMount() instanceof SkeletonHorse)
        {
            if (!ItemListener.Falling.contains(e.getMount()))
                ItemListener.Falling.add(e.getMount());
        }
    }

    // makes dare float on dismount and reenables fall damage
    @EventHandler
    public void horseDismountEvent(EntityDismountEvent e)
    {
        if (e.isCancelled())
            return;

        if (e.getDismounted().getName().equalsIgnoreCase(dareName) && e.getDismounted() instanceof SkeletonHorse)
        {
            if (ItemListener.Falling.contains(e.getEntity()))
                ItemListener.Falling.remove(e.getEntity());

            if (!e.getDismounted().isOnGround())
            {
                e.getEntity().getWorld().playSound(e.getDismounted(), Sound.BLOCK_CONDUIT_DEACTIVATE, 1, 1);
                e.getDismounted().setGravity(false);
                new BukkitRunnable()
                {
                    @Override
                    public void run()
                    {
                        if (e.getDismounted().isDead() || e.getDismounted().getPassengers().size() != 0)
                        {
                            this.cancel();
                            if (!ItemListener.Falling.contains(e.getEntity()) && !e.getDismounted().isDead())
                                ItemListener.Falling.add(e.getEntity());
                            e.getDismounted().setGravity(true);
                        }
                        e.getDismounted().getLocation().getWorld().spawnParticle(Particle.CAMPFIRE_COSY_SMOKE, e.getDismounted().getLocation().clone(), 2, 1, .3, 1, 0);
                    }
                }.runTaskTimer(Commands.Invictools, 0, 1);
            }
        }
    }

    //clears dare air list on game end
    @EventHandler
    public void gameEnd(BedwarsGameEndEvent e)
    {
        dareAir.clear();
    }
}

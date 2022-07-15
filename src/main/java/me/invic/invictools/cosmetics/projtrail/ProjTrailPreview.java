package me.invic.invictools.cosmetics.projtrail;

import me.invic.invictools.Commands;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.*;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.Objects;

public class ProjTrailPreview
{
    FileConfiguration config = Commands.Invictools.getConfig();

    public void handle(String effect, Player p)
    {
        Location oldloc = p.getLocation();
        String[] array = config.getString("ProjPreview.Player").split(";");
        p.teleport(new Location(Bukkit.getWorld(array[0]), Double.parseDouble(array[1]), Double.parseDouble(array[2]), Double.parseDouble(array[3]),Float.parseFloat(array[4]),Float.parseFloat(array[5])));
        array = config.getString("ProjPreview.Projectile").split(";");

        String[] finalArray = array;
        Location loc = new Location(Bukkit.getWorld(finalArray[0]), Double.parseDouble(finalArray[1])+1, Double.parseDouble(finalArray[2]), Double.parseDouble(finalArray[3]),Float.parseFloat(finalArray[4]),Float.parseFloat(finalArray[5]));
        Bat bat = (Bat) loc.getWorld().spawnEntity(loc, EntityType.BAT);
        bat.setInvisible(true);
        bat.setAI(false);
        bat.setGravity(false);

        BukkitRunnable runnable = new BukkitRunnable()
        {
            int i = 0;
            @Override
            public void run()
            {
                if(i==0)
                {
                    Arrow arrow = bat.launchProjectile(Arrow.class);
                 //   arrow.teleport(loc);
                    arrow.setVelocity(loc.getDirection().multiply(2.5));
                    new ProjTrailHandler().effectSwitch(effect,p,arrow);
                }
                else if(i==1)
                {
                    Fireball fireball = bat.launchProjectile(Fireball.class);
                  //  fireball.teleport(loc.add(0,2,0));
                    fireball.setVelocity(loc.getDirection().multiply(1));
                    new ProjTrailHandler().effectSwitch(effect,p,fireball);
                }
                else
                {
                    bat.remove();
                    this.cancel();
                }

                i++;
            }};runnable.runTaskTimer(Objects.requireNonNull(Bukkit.getServer().getPluginManager().getPlugin("Invictools")), 0,45);

        p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.BOLD + "Hold crouch to cancel"));
        BukkitRunnable runnable2 = new BukkitRunnable()
        {
            final int cancelAt = 1*20 * 6;
            int i = 0;
            @Override
            public void run()
            {
                i++;
                if((p.getLocation().getWorld()==oldloc.getWorld() && i == cancelAt) || p.isSneaking())
                {
                    p.teleport(oldloc);
                    this.cancel();
                }

                if(i == cancelAt)
                    this.cancel();
            }
        };runnable2.runTaskTimer(Objects.requireNonNull(Bukkit.getServer().getPluginManager().getPlugin("Invictools")), 10,1);
    }
}

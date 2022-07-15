package me.invic.invictools.cosmetics.bedbreaks;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Objects;


public class HoloBedBreak
{
    public HoloBedBreak(Location loc, Player player, String bed, boolean natural) // natural as in in game with bed and player breaking it opposed to a test scenario
    {
        loc.setX(loc.getX()+.5);
        loc.setZ(loc.getZ()+.5);

        ArmorStand as1 = (ArmorStand) loc.getWorld().spawnEntity(loc, EntityType.ARMOR_STAND);
        String[] teamColor = bed.split("_");
        teamColor[0] = teamColor[0].toLowerCase();
        if(teamColor[0].equals("light"))
            teamColor[0] = teamColor[1];

        String playerName = player.getName();
        String world = player.getWorld().getName();

        as1.setVisible(false);
        as1.setGravity(false);

        if (natural)
            as1.setCustomName(ChatColor.RED + playerName + ChatColor.AQUA + " has " + ChatColor.YELLOW + "#rekt" + ChatColor.AQUA + " the bed of " + ChatColor.RED + teamColor[0] + ChatColor.AQUA + " team here!");
        else
            as1.setCustomName(ChatColor.RED + playerName + ChatColor.AQUA + " has " + ChatColor.YELLOW + "#rekt" + ChatColor.AQUA + " the bed of " + ChatColor.RED + "red" + ChatColor.AQUA + " team here!");
        as1.setCustomNameVisible(true);
        loc.getWorld().playSound(loc, Sound.ENTITY_DRAGON_FIREBALL_EXPLODE,10.0F,1.0F);

        new BukkitRunnable() // kills at max 1 minute after match ends or player dies and leaves
        {
            @Override
            public void run()
            {
                if (!player.getLocation().getWorld().getName().equals(world))
                {
                    as1.remove();
                    this.cancel();
                }
                else if (!natural)
                {
                    this.cancel();
                }
            }
        }.runTaskTimer(Objects.requireNonNull(Bukkit.getServer().getPluginManager().getPlugin("Invictools")), 0L, 1200L);

        new BukkitRunnable() // kills after viewed in lobby
        {
            @Override
            public void run()
            {
                if (!natural)
                    as1.remove();
                this.cancel();
            }
        }.runTaskTimer(Objects.requireNonNull(Bukkit.getServer().getPluginManager().getPlugin("Invictools")), 40L, 40L);
    }
}


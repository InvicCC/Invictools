package me.invic.invictools.gamemodifiers.LuckyBlocks;

import me.invic.invictools.gamemodifiers.gamemodeData;
import org.bukkit.*;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.screamingsandals.bedwars.api.BedwarsAPI;

import java.util.Objects;

public class LuckyBlockSpawner
{
    public LuckyBlockSpawner(Location loc, String text, String type, int delay, Player p)
    {
        new BukkitRunnable()
        {
            @Override
            public void run()
            {
                if (new gamemodeData().getLuckyBlockMode(BedwarsAPI.getInstance().getGameOfPlayer(p)).equalsIgnoreCase("none"))
                    this.cancel();
                else
                {
                    ItemStack lb;
                    if (type.equalsIgnoreCase("random"))
                        lb = new createLuckyBlocks().getRandomBlockWeighted();
                    else
                        lb = new createLuckyBlocks().getByName(type);

                    loc.getWorld().dropItemNaturally(loc, lb);

                }
            }
        }.runTaskTimer(Objects.requireNonNull(Bukkit.getServer().getPluginManager().getPlugin("Invictools")), delay * 20L, delay * 20L); // repeat every second so you can have a floating timer with named invis armor stand and just add variable that checks if its at delay to do item spawn


        Location ArmorStand1Loc = new Location(loc.getWorld(), loc.getX(), loc.getY() + .3, loc.getZ());
        Location ArmorStand2Loc = loc;
        ArmorStand as1 = (ArmorStand) loc.getWorld().spawnEntity(ArmorStand1Loc, EntityType.ARMOR_STAND);
        ArmorStand as2 = (ArmorStand) loc.getWorld().spawnEntity(ArmorStand2Loc, EntityType.ARMOR_STAND);

        as1.setGravity(false);
        as1.setCustomName(text);
        as1.setCustomNameVisible(true);
        as1.setVisible(false);

        as2.setGravity(false);
        as2.setCustomName(ChatColor.YELLOW + "Spawns in " + ChatColor.RED + delay + ChatColor.YELLOW + " seconds");
        as2.setCustomNameVisible(true);
        as2.setVisible(false);

        final int[] resettableDelay = {delay - 1};
        new BukkitRunnable()
        {
            @Override
            public void run()
            {
                if (new gamemodeData().getLuckyBlockMode(BedwarsAPI.getInstance().getGameOfPlayer(p)).equalsIgnoreCase("none"))
                {
                    as1.remove();
                    as2.remove();
                    this.cancel();
                }

                if (resettableDelay[0] == -1)
                    resettableDelay[0] = delay - 1;

                as2.setCustomName(ChatColor.YELLOW + "Spawns in " + ChatColor.RED + resettableDelay[0] + ChatColor.YELLOW + " seconds");
                resettableDelay[0]--;
            }
        }.runTaskTimer(Objects.requireNonNull(Bukkit.getServer().getPluginManager().getPlugin("Invictools")), 20L, 20L); // repeat every second so you can have a floating timer with named invis armor stand and just add variable that checks if its at delay to do item spawn
    }
}

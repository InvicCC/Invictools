package cc.invic.invictools.cosmetics.bedbreaks;

import cc.invic.invictools.cosmetics.projtrail.ProjTrailHandler;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Objects;
import java.util.Random;

public class FireworkBedBreak
{
    public FireworkBedBreak(Location loc, Player p)
    {
        Random color = new Random();
        int c = color.nextInt(2);
        final int[] i = {0};
        new BukkitRunnable()
        {
            @Override
            public void run()
            {
                if (i[0] == 0)
                    spawnFireworks(loc, c, false, p);
                else if (i[0] == 1 || i[0] == 2 || i[0] == 3)
                {
                    spawnFireworks(loc, c, true, p);
                }
                else this.cancel();
                i[0]++;
            }
        }.runTaskTimer(Objects.requireNonNull(Bukkit.getServer().getPluginManager().getPlugin("Invictools")), 0, 5);
    }

    public void spawnFireworks(Location loc, int c, boolean pos, Player p)
    {
        Random rand = new Random();

        int x = rand.nextInt(6);
        int z = rand.nextInt(6);
        int t = rand.nextInt(2);

        if (pos)
        {
            if (t == 1)
                loc.add(x, 4, z);
            else
                loc.subtract(x, -4, z);
        }

        Firework fw = (Firework) loc.getWorld().spawnEntity(loc, EntityType.FIREWORK);
        FireworkMeta fwm = fw.getFireworkMeta();

        fwm.setPower(0);

        if (pos)
        {
            fwm.addEffect(FireworkEffect.builder().withColor(ProjTrailHandler.presColor(p)).flicker(true).trail(true).with(FireworkEffect.Type.STAR).build());
            fwm.addEffect(FireworkEffect.builder().withColor(Color.WHITE).trail(true).with(FireworkEffect.Type.STAR).flicker(true).build());
        }
        else
            fwm.addEffect(FireworkEffect.builder().withColor(ProjTrailHandler.presColor(p)).trail(true).flicker(true).build());


        fw.setMetadata("nodamage", new FixedMetadataValue(Objects.requireNonNull(Bukkit.getServer().getPluginManager().getPlugin("Invictools")), true));
        fw.setFireworkMeta(fwm);
        fw.detonate();

        if (pos)
        {
            if (t == 1)
                loc.subtract(x, 0, z);
            else
                loc.add(x, 0, z);
        }
    }
}

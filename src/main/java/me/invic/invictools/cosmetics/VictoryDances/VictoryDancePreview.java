package me.invic.invictools.cosmetics.VictoryDances;

import me.invic.invictools.commands.OldCommands;
import me.invic.invictools.util.fixes.LobbyInventoryFix;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Objects;

public class VictoryDancePreview
{
    static FileConfiguration config = OldCommands.Invictools.getConfig();
    public static boolean VictoryPreviewEnabled = config.getBoolean("VictoryDance.enabled", true);

    public void handle(String string, Player p)
    {
        if (!VictoryPreviewEnabled && p.getWorld().getName().equalsIgnoreCase("bwlobby"))
        {
            p.sendMessage(ChatColor.RED + "Victory Dance previews are disabled.");
            return;
        }

        if (!p.getWorld().getName().equalsIgnoreCase("bwlobby"))
            return;

        new LobbyInventoryFix().saveInventory(p);
        p.getInventory().clear();
        Location oldloc = p.getLocation();
        String[] array = config.getString("VictoryDance.Preview").split(";");
        p.teleport(new Location(Bukkit.getWorld(array[0]), Double.parseDouble(array[1]), Double.parseDouble(array[2]), Double.parseDouble(array[3]), Float.parseFloat(array[4]), Float.parseFloat(array[5])));
        new VictoryDanceHandler().effectSwitch(string, p);

        // p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.BOLD + "Hold crouch to cancel"));
        BukkitRunnable runnable2 = new BukkitRunnable()
        {
            final int cancelAt = VictoryDanceHandler.effectDuration * 20;
            int i = 0;

            @Override
            public void run()
            {
                i++;
                if ((p.getLocation().getWorld() == oldloc.getWorld() && i == cancelAt) /*|| p.isSneaking()*/)
                {
                    new LobbyInventoryFix().loadInventory(p, p);
                    p.teleport(oldloc);
                    this.cancel();
                }

                if (i == cancelAt)
                    this.cancel();
            }
        };
        runnable2.runTaskTimer(Objects.requireNonNull(Bukkit.getServer().getPluginManager().getPlugin("Invictools")), 10, 1);
    }
}

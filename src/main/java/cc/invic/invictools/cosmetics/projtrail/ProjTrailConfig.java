package cc.invic.invictools.cosmetics.projtrail;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;

public class ProjTrailConfig
{
    public ProjTrailConfig(Player player, String effect, boolean bypass)
    {
        Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("Invictools");
        //  File pFile = new File(plugin.getDataFolder().getPath() + "\\PlayerData" + File.separator + player.getUniqueId() + ".yml");
        File Folder = new File(plugin.getDataFolder(), "PlayerData");
        File pFile = new File(Folder, player.getUniqueId() + ".yml");
        final FileConfiguration playerData = YamlConfiguration.loadConfiguration(pFile);
        if (playerData.getString("ProjTrail") == null)
        {
            playerData.set("ProjTrail", "Crit");
        }

        Plugin bw = Bukkit.getServer().getPluginManager().getPlugin("BedWars");
        File f2 = new File(bw.getDataFolder(), "database");
        File File = new File(f2, "bw_stats_players.yml");
        final FileConfiguration data = YamlConfiguration.loadConfiguration(File);
        int wins = data.getInt("data." + player.getUniqueId() + ".kills");

        //  int wins = api.getStatisticsManager().loadStatistic(player.getUniqueId()).getDestroyedBeds();

        if (effect.equalsIgnoreCase("Crit") || bypass)
        {
            playerData.set("ProjTrail", effect);
            player.sendMessage(ChatColor.YELLOW + "Your Projectile Trail is now set to " + ChatColor.AQUA + effect);
        }
        else if (wins == 0)
        {
            player.sendMessage(ChatColor.RED + "You have not unlocked this effect yet!");
        }
        else if (effect.equalsIgnoreCase("Hearts") && wins >= 25)
        {
            playerData.set("ProjTrail", effect);
            player.sendMessage(ChatColor.YELLOW + "Your Projectile Trail is now set to " + ChatColor.AQUA + effect);
        }
        else if (effect.equalsIgnoreCase("Pres") && wins >= 100 || effect.equalsIgnoreCase("Lava") && wins >= 100)
        {
            playerData.set("ProjTrail", effect);
            player.sendMessage(ChatColor.YELLOW + "Your Projectile Trail is now set to " + ChatColor.AQUA + effect);
        }
        else if (effect.equalsIgnoreCase("Notes") && wins >= 250)
        {
            playerData.set("ProjTrail", effect);
            player.sendMessage(ChatColor.YELLOW + "Your Projectile Trail is now set to " + ChatColor.AQUA + effect);
        }
        else if (effect.equalsIgnoreCase("Smoke") && wins >= 500)
        {
            playerData.set("ProjTrail", effect);
            player.sendMessage(ChatColor.YELLOW + "Your Projectile Trail is now set to " + ChatColor.AQUA + effect);
        }
        else if (effect.equalsIgnoreCase("Sculk"))
        {
            if (player.hasPermission("invic.firestick") || wins >= 1000)
            {
                playerData.set("ProjTrail", effect);
                player.sendMessage(ChatColor.YELLOW + "Your Projectile Trail is now set to " + ChatColor.AQUA + effect);
            }
            else
            {
                player.sendMessage(ChatColor.RED + "This effect requires any rank or 1000 kills.");
                player.sendMessage(ChatColor.AQUA + "Type /ranks to learn how to get one");
                player.sendMessage(ChatColor.RED + "You've killed " + wins + " players.");
            }
        }
        else
        {
            player.sendMessage(ChatColor.RED + "You haven't unlocked this effect yet!");
            player.sendMessage(ChatColor.RED + "You've killed " + wins + " players.");
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

package cc.invic.invictools.cosmetics.bedbreaks;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.bedwars.api.BedwarsAPI;

import java.io.File;
import java.io.IOException;


public class BedBreakConfig
{
    BedwarsAPI api = BedwarsAPI.getInstance();

    public BedBreakConfig(Player player, String effect, boolean bypass)
    {
        Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("Invictools");
        //  File pFile = new File(plugin.getDataFolder().getPath() + "\\PlayerData" + File.separator + player.getUniqueId() + ".yml");
        File Folder = new File(plugin.getDataFolder(), "PlayerData");
        File pFile = new File(Folder, player.getUniqueId() + ".yml");
        final FileConfiguration playerData = YamlConfiguration.loadConfiguration(pFile);

        // int wins = api.getStatisticsManager().loadStatistic(player.getUniqueId()).getWins();
        Plugin bw = Bukkit.getServer().getPluginManager().getPlugin("BedWars");
        File f2 = new File(bw.getDataFolder(), "database");
        File File = new File(f2, "bw_stats_players.yml");
        final FileConfiguration data = YamlConfiguration.loadConfiguration(File);
        int wins = data.getInt("data." + player.getUniqueId() + ".wins");

        if (effect.equalsIgnoreCase("Fireworks") || bypass)
        {
            playerData.set("BedBreak", effect);
            player.sendMessage(ChatColor.YELLOW + "Your Bed Break cosmetic is now set to " + ChatColor.AQUA + effect);
        }
        else if (wins == 0)
        {
            player.sendMessage(ChatColor.RED + "You have not unlocked this effect yet!");
        }
        else if (effect.equalsIgnoreCase("Lightning") && wins >= 1)
        {
            playerData.set("BedBreak", effect);
            player.sendMessage(ChatColor.YELLOW + "Your Bed Break cosmetic is now set to " + ChatColor.AQUA + effect);
        }
        else if (effect.equalsIgnoreCase("Tornado") && wins >= 2 || effect.equalsIgnoreCase("Holo") && wins >= 2)
        {
            playerData.set("BedBreak", effect);
            player.sendMessage(ChatColor.YELLOW + "Your Bed Break cosmetic is now set to " + ChatColor.AQUA + effect);
        }
        else if (effect.equalsIgnoreCase("Ranked") && wins >= 5)
        {
            playerData.set("BedBreak", effect);
            player.sendMessage(ChatColor.YELLOW + "Your Bed Break cosmetic is now set to " + ChatColor.AQUA + effect);
        }
        else if (effect.equalsIgnoreCase("Guardian") && wins >= 50)
        {
            playerData.set("BedBreak", effect);
            player.sendMessage(ChatColor.YELLOW + "Your Bed Break cosmetic is now set to " + ChatColor.AQUA + effect);
        }
        else if (effect.equalsIgnoreCase("Enderman") && wins >= 15)
        {
            playerData.set("BedBreak", effect);
            player.sendMessage(ChatColor.YELLOW + "Your Bed Break cosmetic is now set to " + ChatColor.AQUA + effect);
        }
        else if (effect.equalsIgnoreCase("pres"))
        {
            if (player.hasPermission("invic.firestick") || wins >= 500)
            {
                playerData.set("FinalKill", effect);
                player.sendMessage(ChatColor.YELLOW + "Your Final Kill effect is now set to " + ChatColor.AQUA + effect);
            }
            else
            {
                player.sendMessage(ChatColor.RED + "This effect requires any rank or 500 bed breaks.");
                player.sendMessage(ChatColor.AQUA + "Type /ranks to learn how to get one");
                player.sendMessage(ChatColor.RED + "You've destroyed " + wins + " beds.");
            }
        }
        else
        {
            player.sendMessage(ChatColor.RED + "You haven't unlocked this effect yet!");
            player.sendMessage(ChatColor.RED + "You've only won " + wins + " times.");
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

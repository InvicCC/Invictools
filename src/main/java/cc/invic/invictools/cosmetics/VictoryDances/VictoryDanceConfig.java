package cc.invic.invictools.cosmetics.VictoryDances;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;

public class VictoryDanceConfig
{
    public VictoryDanceConfig(Player player, String effect, boolean bypass)
    {
        Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("Invictools");
        //  File pFile = new File(plugin.getDataFolder().getPath() + "\\PlayerData" + File.separator + player.getUniqueId() + ".yml");
        java.io.File Folder = new File(plugin.getDataFolder(), "PlayerData");
        File pFile = new File(Folder, player.getUniqueId() + ".yml");
        final FileConfiguration playerData = YamlConfiguration.loadConfiguration(pFile);
        int wins = playerData.getInt("FinalKills");

        if (effect.equalsIgnoreCase("Firework") || bypass)
        {
            playerData.set("VictoryDance", effect);
            player.sendMessage(ChatColor.YELLOW + "Your Victory Dance is now set to " + ChatColor.AQUA + effect);
        }
        else if (wins == 0)
        {
            player.sendMessage(ChatColor.RED + "You have not unlocked this effect yet!");
            player.sendMessage(ChatColor.RED + "You have " + wins + " final kills.");
            player.sendMessage(ChatColor.RED + "(Finals have only tracked since June 2022)");
        }
        else if (effect.equalsIgnoreCase("Bow") && wins >= 3) // rideable arrows
        {
            playerData.set("VictoryDance", effect);
            player.sendMessage(ChatColor.YELLOW + "Your Victory Dance is now set to " + ChatColor.AQUA + effect);
        }
        else if (effect.equalsIgnoreCase("Firestick") && wins >= 12)
        {
            playerData.set("VictoryDance", effect);
            player.sendMessage(ChatColor.YELLOW + "Your Victory Dance is now set to " + ChatColor.AQUA + effect);
        }
        else if (effect.equalsIgnoreCase("Dragon") && wins >= 25)
        {
            playerData.set("VictoryDance", effect);
            player.sendMessage(ChatColor.YELLOW + "Your Victory Dance is now set to " + ChatColor.AQUA + effect);
        }
        else if (effect.equalsIgnoreCase("Snowball") && wins >= 50) // snowballs with forced prestige projectile trail
        {
            playerData.set("VictoryDance", effect);
            player.sendMessage(ChatColor.YELLOW + "Your Victory Dance is now set to " + ChatColor.AQUA + effect);
        }
        else if (effect.equalsIgnoreCase("Storm") && wins >= 75)
        {
            playerData.set("VictoryDance", effect);
            player.sendMessage(ChatColor.YELLOW + "Your Victory Dance is now set to " + ChatColor.AQUA + effect);
        }
        else if (effect.equalsIgnoreCase("pls") && wins >= 90)
        {
            playerData.set("VictoryDance", effect);
            player.sendMessage(ChatColor.YELLOW + "Your Victory Dance is now set to " + ChatColor.AQUA + effect);
        }
        else if (effect.equalsIgnoreCase("Dare"))
        {
            if (player.hasPermission("invic.firestick") || wins >= 150)
            {
                playerData.set("VictoryDance", effect);
                player.sendMessage(ChatColor.YELLOW + "Your Victory Dance is now set to " + ChatColor.AQUA + effect);
            }
            else
            {
                player.sendMessage(ChatColor.RED + "This effect requires any rank or 150 final kills.");
                player.sendMessage(ChatColor.AQUA + "Type /ranks to learn how to get one");
                player.sendMessage(ChatColor.RED + "You have " + wins + " final kills.");
                player.sendMessage(ChatColor.RED + "(Finals have only tracked since June 2022)");
            }
        }
        else
        {
            player.sendMessage(ChatColor.RED + "You haven't unlocked this effect yet!");
            player.sendMessage(ChatColor.RED + "You have " + wins + " final kills.");
            player.sendMessage(ChatColor.RED + "(Finals have only tracked since June 2022)");
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

package me.invic.invictools;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.clip.placeholderapi.external.EZPlaceholderHook;
import me.invic.invictools.commands.Commands;
import me.invic.invictools.gamemodes.bedfightStatistics;
import org.antlr.v4.runtime.misc.NotNull;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.io.File;

public class PAPIexpansion extends PlaceholderExpansion
{
    @Override
    public
    String getIdentifier()
    {
        return "it";
    }

    @Override
    public
    String getAuthor()
    {
        return "Invictable";
    }

    @Override
    public
    String getVersion()
    {
        return "1.0";
    }

    File Folder = new File(Commands.Invictools.getDataFolder(), "Bedfight");
    File pFile = new File(Folder, "bedfightstats.yml");
    final FileConfiguration data = YamlConfiguration.loadConfiguration(pFile);

    Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("BedWars");
    File Folder2 = new File(plugin.getDataFolder(), "database");
    File pFile2 = new File(Folder2, "bw_stats_players.yml");
    final FileConfiguration data2 = YamlConfiguration.loadConfiguration(pFile2);

    @Override
    public String onPlaceholderRequest(Player player, String identifier)
    {
        // bf
        int intlvl = new bedfightStatistics().calculateScore(data,player.getUniqueId().toString());

        //bw
        int defaultlvl = data2.getInt("data."+player.getUniqueId()+".score");
        int bwlvl = defaultlvl/500+1;
        String strlvl = String.valueOf(bwlvl);

        if(identifier.equalsIgnoreCase("bfpres"))
        {
            if (intlvl >= 0 && intlvl <= 9999)
                return ChatColor.translateAlternateColorCodes('&', "&7");
            else if (intlvl >= 10000 && intlvl <= 19999)
                return ChatColor.translateAlternateColorCodes('&', "&f");
            else if (intlvl >= 20000 && intlvl <= 29999)
                return ChatColor.translateAlternateColorCodes('&', "&6");
            else if (intlvl >= 30000 && intlvl <= 39999)
                return ChatColor.translateAlternateColorCodes('&', "&e");
            else if (intlvl >= 40000 && intlvl <= 49999)
                return ChatColor.translateAlternateColorCodes('&', "&2");
            else if (intlvl >= 50000 && intlvl <= 59999)
                return ChatColor.translateAlternateColorCodes('&', "&3");
            else if (intlvl >= 60000 && intlvl <= 69999)
                return ChatColor.translateAlternateColorCodes('&', "&c");
            else if (intlvl >= 70000 && intlvl <= 79999)
                return ChatColor.translateAlternateColorCodes('&', "&d");
            else if (intlvl >= 80000 && intlvl <= 89999)
                return ChatColor.translateAlternateColorCodes('&', "&9");
            else if (intlvl >= 90000 && intlvl <= 99999)
                return ChatColor.translateAlternateColorCodes('&', "&5");
            else if (intlvl >= 100000)
                return ChatColor.translateAlternateColorCodes('&', "&b&l");
        }
        else if(identifier.equalsIgnoreCase("bfpres2"))
        {
            if (intlvl >= 0 && intlvl <= 9999)
                return ChatColor.translateAlternateColorCodes('&', "&7");
            else if (intlvl >= 10000 && intlvl <= 19999)
                return ChatColor.translateAlternateColorCodes('&', "&f");
            else if (intlvl >= 20000 && intlvl <= 29999)
                return ChatColor.translateAlternateColorCodes('&', "&6");
            else if (intlvl >= 30000 && intlvl <= 39999)
                return ChatColor.translateAlternateColorCodes('&', "&e");
            else if (intlvl >= 40000 && intlvl <= 49999)
                return ChatColor.translateAlternateColorCodes('&', "&2");
            else if (intlvl >= 50000 && intlvl <= 59999)
                return ChatColor.translateAlternateColorCodes('&', "&3");
            else if (intlvl >= 60000 && intlvl <= 69999)
                return ChatColor.translateAlternateColorCodes('&', "&c");
            else if (intlvl >= 70000 && intlvl <= 79999)
                return ChatColor.translateAlternateColorCodes('&', "&d");
            else if (intlvl >= 80000 && intlvl <= 89999)
                return ChatColor.translateAlternateColorCodes('&', "&9");
            else if (intlvl >= 90000 && intlvl <= 99999)
                return ChatColor.translateAlternateColorCodes('&', "&5");
            else if (intlvl >= 100000)
                return ChatColor.translateAlternateColorCodes('&', "&f&l");
        }
        else if(identifier.equalsIgnoreCase("bflvls"))
        {
            return String.valueOf(intlvl)+"○";
        }
        else if(identifier.equalsIgnoreCase("bflvl"))
        {
            return String.valueOf(intlvl);
        }
        else if(identifier.equalsIgnoreCase("bwlvl"))
            return strlvl;
        else if(identifier.equalsIgnoreCase("bwlvls"))
            return strlvl+"✰";
        else if(identifier.equalsIgnoreCase("bwpres"))
        {
            if(bwlvl >= 0 && bwlvl <= 9)
                return ChatColor.translateAlternateColorCodes('&',"&7");
            else if(bwlvl >= 10 && bwlvl <= 19)
                return ChatColor.translateAlternateColorCodes('&',"&f");
            else if(bwlvl >= 20 && bwlvl <= 29)
                return ChatColor.translateAlternateColorCodes('&',"&6");
            else if(bwlvl >= 30 && bwlvl <= 39)
                return ChatColor.translateAlternateColorCodes('&',"&e");
            else if(bwlvl >= 40 && bwlvl <= 49)
                return ChatColor.translateAlternateColorCodes('&',"&2");
            else if(bwlvl >= 50 && bwlvl <= 59)
                return ChatColor.translateAlternateColorCodes('&',"&3");
            else if(bwlvl >= 60 && bwlvl <= 69)
                return ChatColor.translateAlternateColorCodes('&',"&c");
            else if(bwlvl >= 70 && bwlvl <= 79)
                return ChatColor.translateAlternateColorCodes('&',"&d");
            else if(bwlvl >= 80 && bwlvl <= 89)
                return ChatColor.translateAlternateColorCodes('&',"&9");
            else if(bwlvl >= 90 && bwlvl <= 99)
                return ChatColor.translateAlternateColorCodes('&',"&5");
            else if(bwlvl >=100)
                return ChatColor.translateAlternateColorCodes('&',"&b&l");
        }
        else if(identifier.equalsIgnoreCase("bwpres2"))
        {
            if(bwlvl >= 0 && bwlvl <= 9)
                return ChatColor.translateAlternateColorCodes('&',"&7");
            else if(bwlvl >= 10 && bwlvl <= 19)
                return ChatColor.translateAlternateColorCodes('&',"&f");
            else if(bwlvl >= 20 && bwlvl <= 29)
                return ChatColor.translateAlternateColorCodes('&',"&6");
            else if(bwlvl >= 30 && bwlvl <= 39)
                return ChatColor.translateAlternateColorCodes('&',"&e");
            else if(bwlvl >= 40 && bwlvl <= 49)
                return ChatColor.translateAlternateColorCodes('&',"&2");
            else if(bwlvl >= 50 && bwlvl <= 59)
                return ChatColor.translateAlternateColorCodes('&',"&3");
            else if(bwlvl >= 60 && bwlvl <= 69)
                return ChatColor.translateAlternateColorCodes('&',"&c");
            else if(bwlvl >= 70 && bwlvl <= 79)
                return ChatColor.translateAlternateColorCodes('&',"&d");
            else if(bwlvl >= 80 && bwlvl <= 89)
                return ChatColor.translateAlternateColorCodes('&',"&9");
            else if(bwlvl >= 90 && bwlvl <= 99)
                return ChatColor.translateAlternateColorCodes('&',"&5");
            else if(bwlvl >=100)
                return ChatColor.translateAlternateColorCodes('&',"&f&l");
        }
        return null;
    }
}

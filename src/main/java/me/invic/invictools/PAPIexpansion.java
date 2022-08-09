package me.invic.invictools;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.invic.invictools.commands.OldCommands;
import me.invic.invictools.gamemodes.bedfightStatistics;
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

    File Folder = new File(OldCommands.Invictools.getDataFolder(), "Bedfight");
    File pFile = new File(Folder, "bedfightstats.yml");
    final FileConfiguration data = YamlConfiguration.loadConfiguration(pFile);

    Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("BedWars");
    File Folder2 = new File(plugin.getDataFolder(), "database");
    File pFile2 = new File(Folder2, "bw_stats_players.yml");
    final FileConfiguration data2 = YamlConfiguration.loadConfiguration(pFile2);

    File Folder3 = Bukkit.getServer().getPluginManager().getPlugin("SBA").getDataFolder();
    final FileConfiguration sba = YamlConfiguration.loadConfiguration(new File(Folder3,"sbaconfig.yml"));

    @Override
    public String onPlaceholderRequest(Player player, String identifier)
    {
        // bf
        int intlvl = new bedfightStatistics().calculateScore(data,player.getUniqueId().toString());

        //bw
        int defaultlvl = data2.getInt("data."+player.getUniqueId()+".score");
        int bwlvl = (defaultlvl/(sba.getInt("player-statistics.xp-to-level-up",(500)))+1);
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
                return ChatColor.translateAlternateColorCodes('&', "&3&l");
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
                return ChatColor.translateAlternateColorCodes('&', "&8&l");
        }
        else if(identifier.equalsIgnoreCase("bflvls"))
        {
            return intlvl +"山";
        }
        else if(identifier.equalsIgnoreCase("bfs"))
        {
            return "山";
        }
        else if(identifier.equalsIgnoreCase("bws"))
        {
            return "✰";
        }
        else if(identifier.equalsIgnoreCase("bflvl"))
        {
            return String.valueOf(intlvl);
        }
        else if(identifier.equalsIgnoreCase("bflvlc"))
        {
            StringBuilder sb = new StringBuilder(String.valueOf(intlvl));
            if(sb.length()>=4)
            {
                sb.insert(sb.length() - 3, '.');
                sb.deleteCharAt(sb.length());
                sb.deleteCharAt(sb.length()-1);
            }
            return sb.toString();
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
                return ChatColor.translateAlternateColorCodes('&',"&3&l");
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
                return ChatColor.translateAlternateColorCodes('&',"&8&l");
        }
        else if(identifier.equalsIgnoreCase("3"))
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
        else if(identifier.equalsIgnoreCase("pro1"))
        {
            double decimal = (defaultlvl/sba.getDouble("player-statistics.xp-to-level-up",(500))) -(bwlvl-1);
            double finalDouble = Math.round(decimal*sba.getDouble("player-statistics.xp-to-level-up",(500)));
            return String.valueOf((int)finalDouble);
        }
        else if(identifier.equalsIgnoreCase("pro2"))
        {
            return sba.getString("player-statistics.xp-to-level-up",String.valueOf(500));
        }
        else if(identifier.equalsIgnoreCase("bfw"))
        {
            return data.getString("data."+player.getUniqueId()+".Wins",String.valueOf(0));
        }
        else if(identifier.equalsIgnoreCase("bfl"))
        {
            return data.getString("data."+player.getUniqueId()+".Losses",String.valueOf(0));
        }
        else if(identifier.equalsIgnoreCase("bfk"))
        {
            return data.getString("data."+player.getUniqueId()+".NormalKills",String.valueOf(0));
        }
        else if(identifier.equalsIgnoreCase("bfd"))
        {
            return data.getString("data."+player.getUniqueId()+".NormalDeaths",String.valueOf(0));
        }
        else if(identifier.equalsIgnoreCase("bffk"))
        {
            return data.getString("data."+player.getUniqueId()+".FinalKills",String.valueOf(0));
        }
        else if(identifier.equalsIgnoreCase("bffd"))
        {
            return data.getString("data."+player.getUniqueId()+".FinalDeaths",String.valueOf(0));
        }
        else if(identifier.equalsIgnoreCase("bfbb"))
        {
            return data.getString("data."+player.getUniqueId()+".BedBreaks",String.valueOf(0));
        }
        else if(identifier.equalsIgnoreCase("bfws"))
        {
            return data.getString("data."+player.getUniqueId()+".BestWinStreak",String.valueOf(0));
        }
        return null;
    }
}

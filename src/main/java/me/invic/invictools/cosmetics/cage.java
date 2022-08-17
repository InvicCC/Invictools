package me.invic.invictools.cosmetics;

import me.invic.invictools.commands.OldCommands;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class cage
{
    static private List<String> reqTypes = new ArrayList<String>();

    private HashMap<String, Integer> requirements = new HashMap<>();
    private String name;

    cage(String n)
    {
        name = n;
    }

    void addReq(String type, Integer amount)
    {
        requirements.put(type,amount);
    }

    String getName()
    {
        return name;
    }

    //char at 1 != 8
    // on resest load hashmap of every cage
    // when player logs in add their player and cage in config to hashmap after checking req
    // check reqs before cage loading too
    boolean checkReq(Player p)
    {
        for (String s:requirements.keySet())
        {
            if(requirements.get(s) >= getStatistic(s,p))
                return true;
        }
        return false;
    }

    static File Folder = new File(OldCommands.Invictools.getDataFolder(), "Bedfight");
    static Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("BedWars");
    static File Folder2 = new File(plugin.getDataFolder(), "database");

    public static void makeReqs() // reqs for ranks should be 1 or 0 as in true or false
    {
        reqTypes.add("bf_FinalKills");
        reqTypes.add("bf_FinalDeaths");
        reqTypes.add("bf_NormalKills");
        reqTypes.add("bf_NormalDeaths");
        reqTypes.add("bf_Wins");
        reqTypes.add("bf_Losses");
        reqTypes.add("bf_BedBreaks");
        reqTypes.add("bf_WinStreak");
        reqTypes.add("bf_BestWinStreak");
        reqTypes.add("rank_1_c"); // char place 1, color c, creator
        reqTypes.add("rank_1_a"); // char place 1, color a, mod
        reqTypes.add("rank_1_b"); // char place 1, color b, invic
        reqTypes.add("antirank_1_8"); // true if not 8 at place 1
    }

    static Integer getStatistic(String type,Player p)
    {
        if(!reqTypes.contains(type))
            return 999999999;

        String[] split = type.split("_");
        if(split[0].equalsIgnoreCase("bw"))
        {
            File pFile2 = new File(Folder2, "bw_stats_players.yml");
            final FileConfiguration data2 = YamlConfiguration.loadConfiguration(pFile2);

            return data2.getInt("data."+p.getUniqueId()+"."+type);
        }
        else if(split[0].equalsIgnoreCase("bf"))
        {
            File pFile = new File(Folder, "bedfightstats.yml");
            final FileConfiguration data = YamlConfiguration.loadConfiguration(pFile);

            return data.getInt("data."+p.getUniqueId()+"."+type);
        }
        else if(split[0].equalsIgnoreCase("rank"))
        {
            if(p.getDisplayName().charAt(Integer.parseInt(split[1]))==split[2].toCharArray()[0])
                return 1;
            else
                return 0;
        }
        else if(split[0].equalsIgnoreCase("antirank"))
        {
            if(p.getDisplayName().charAt(Integer.parseInt(split[1]))!=split[2].toCharArray()[0])
                return 1;
            else
                return 0;
        }

        return 999999999;
    }
}

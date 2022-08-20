package me.invic.invictools.cosmetics;

import me.invic.invictools.commands.OldCommands;
import me.invic.invictools.gamemodes.bf.bedfightStatistics;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class statisticRequirments
{
    static public HashMap<String, cage> loadedCages = new HashMap<>();
    static private List<String> reqTypes = new ArrayList<>();
    static public List<OfflinePlayer> bedfightPointsLeaderboard = new ArrayList<>();

    static File Folder = new File(OldCommands.Invictools.getDataFolder(), "Bedfight");
    static Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("BedWars");
    static File Folder2 = new File(plugin.getDataFolder(), "database");

    static File Folder3 = new File(OldCommands.Invictools.getDataFolder(), "PlayerData");

    public statisticRequirments()
    {
        loadCages();
        makeReqs();
    }

    private static void loadCages()
    {
        cage glass = new cage("glass");
        loadedCages.put(glass.getName(),glass);

        cage chalice = new cage("olymp");
        chalice.addReq("bfpos",3);
        loadedCages.put(chalice.getName(),chalice);

        cage globe = new cage("globe");
        globe.addReq("bfpos",6);
        loadedCages.put(globe.getName(),globe);

        cage magicbox = new cage("invic");
        magicbox.addReq("bfpos",10);
        loadedCages.put(magicbox.getName(),magicbox);

        cage sub = new cage("sub");
        sub.addReq("rank_1_8",0); // false for having 8
        loadedCages.put(sub.getName(),sub);

        cage mod = new cage("mod");
        mod.addReq("rank_1_a",1); // true for having a
        loadedCages.put(mod.getName(),mod);

        cage creator = new cage("creator");
        creator.addReq("rank_1_c",1);
        loadedCages.put(creator.getName(),creator);

        cage farm = new cage("farm"); // 10000
        farm.addReq("bf_Points",10000);
        loadedCages.put(farm.getName(),farm);

        cage pigolas = new cage("nic"); // 20000
        pigolas.addReq("bf_Points",20000);
        loadedCages.put(pigolas.getName(),pigolas);

        cage portal = new cage("nether"); // 50000
        portal.addReq("bf_Points",50000);
        loadedCages.put(portal.getName(),portal);

        cage prismarine = new cage("pris"); // 100000
        prismarine.addReq("bf_Points",100000);
        loadedCages.put(prismarine.getName(),prismarine);

        cage deepslate = new cage("dark"); // 50000 coins
        deepslate.addReq("purchased_dark",1);
        loadedCages.put(deepslate.getName(),deepslate);

        cage king = new cage("one"); // 100000 coins
        king.addReq("purchased_one",1);
        loadedCages.put(king.getName(),king);
    }

    private static void makeReqs() // reqs for ranks should be 1 or 0 as in true or false
    {
        reqTypes.add("bf_FinalKills");
        reqTypes.add("bf_FinalDeaths");
        reqTypes.add("bf_NormalKills");
        reqTypes.add("bf_NormalDeaths");
        reqTypes.add("bf_Wins");
        reqTypes.add("bf_Points");
        reqTypes.add("bf_Losses");
        reqTypes.add("bf_BedBreaks");
        reqTypes.add("bf_WinStreak");
        reqTypes.add("bf_BestWinStreak");
        reqTypes.add("purchased");
        reqTypes.add("rank"/*_1_c"*/); // char place 1, color c, creator
       // reqTypes.add("rank"/*_1_a"*/); // char place 1, color a, mod
       // reqTypes.add("rank"/*_1_b"*/); // char place 1, color b, invic
        //reqTypes.add("antirank"/*_1_8"*/); // true if not 8 at place 1, not default
        reqTypes.add("bfpos"/*_3"*/); // if position is equal number given
    }

    static Integer getStatistic(String type, Player p)
    {
        String[] split = type.split("_");
        if(!reqTypes.contains(type) && !reqTypes.contains(split[0]))
            return -1;

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

            if(split[1].equalsIgnoreCase("Points"))
                return new bedfightStatistics().calculateScore(data,p.getUniqueId().toString());
            else
                return data.getInt("data."+p.getUniqueId()+"."+type);
        }
        else if(split[0].equalsIgnoreCase("rank"))
        {
            if(p.getDisplayName().charAt(Integer.parseInt(split[1]))==split[2].toCharArray()[0])
                return 1;
            else
                return 0;
        }
        else if(split[0].equalsIgnoreCase("bfpos"))
        {
            for(int i = 0;i < bedfightPointsLeaderboard.size();i++)
            {
                if(bedfightPointsLeaderboard.get(i).equals(p));
                    return i;
            }
        }
        else if(split[0].equalsIgnoreCase("purchased"))
        {
            File pFile = new File(Folder3, p.getUniqueId() + ".yml");
            final FileConfiguration playerData = YamlConfiguration.loadConfiguration(pFile);
            if(!playerData.getStringList("purchased").isEmpty() && playerData.getStringList("purchased").contains(split[1]))
                return 1;
            else
                return 0;
        }

        return -1;
    }
}

package me.invic.invictools.util.Leaderboards;

import me.invic.invictools.commands.OldCommands;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.text.DecimalFormat;
import java.util.*;

public class leaderboard
{
    Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("BedWars");
    File Folder = new File(plugin.getDataFolder(), "database");
    File pFile = new File(Folder, "bw_stats_players.yml");
    final FileConfiguration data = YamlConfiguration.loadConfiguration(pFile);

    public static HashMap<OfflinePlayer, Double> leaderboard = new HashMap<>();
    public static HashMap<OfflinePlayer, Integer> Placement = new HashMap<>();
    public static List<String> formattedLeaderboard = new ArrayList<>();
    public static int leaderboardTotal = 0;
    public static String Sort;

    private static final DecimalFormat df = new DecimalFormat("0.00");
    public static int lbsize = OldCommands.Invictools.getConfig().getInt("Leaderboard.Size", 10);
    public static int gamesBeforeLeaderboard = OldCommands.Invictools.getConfig().getInt("Leaderboard.MinGames", 5);

    public void loadLeaderboard(String sort)
    {
        Sort = sort;
        formattedLeaderboard.clear();
        leaderboardTotal = 0;
        Placement.clear();
        leaderboard.clear();
        File Folder2 = new File(OldCommands.Invictools.getDataFolder(), "PlayerData");
        File[] yamlFiles = Folder2.listFiles();
        for (File file : yamlFiles)
        {
            String[] id = file.getName().split("\\.");
            if (!Bukkit.getOfflinePlayer(UUID.fromString(id[0])).getName().equalsIgnoreCase("Tower")
                    && !Bukkit.getOfflinePlayer(UUID.fromString(id[0])).getName().equalsIgnoreCase("Unchargeableness")
                    && !Bukkit.getOfflinePlayer(UUID.fromString(id[0])).getName().equalsIgnoreCase("Chargeable")
                    && !Bukkit.getOfflinePlayer(UUID.fromString(id[0])).getName().equalsIgnoreCase("mayberry15"))
            {
                if (sort.equalsIgnoreCase("star"))
                {
                    if (data.getInt("data." + id[0] + ".score") > 0)
                    {
                        leaderboard.put(Bukkit.getOfflinePlayer(UUID.fromString(id[0])), data.getDouble("data." + id[0] + ".score"));
                    }
                }
                else if (sort.equalsIgnoreCase("kdr"))
                {
                    if (data.getInt("data." + id[0] + ".loses") + data.getInt("data." + id[0] + ".wins") >= gamesBeforeLeaderboard)
                    {
                        if (data.getInt("data." + id[0] + ".deaths") > 0)
                            leaderboard.put(Bukkit.getOfflinePlayer(UUID.fromString(id[0])), data.getDouble("data." + id[0] + ".kills") / data.getDouble("data." + id[0] + ".deaths"));
                        else
                            leaderboard.put(Bukkit.getOfflinePlayer(UUID.fromString(id[0])), data.getDouble("data." + id[0] + ".kills"));
                    }
                }
                else if (sort.equalsIgnoreCase("wl"))
                {
                    if (data.getInt("data." + id[0] + ".loses") + data.getInt("data." + id[0] + ".wins") >= gamesBeforeLeaderboard)
                    {
                        if (data.getInt("data." + id[0] + ".loses") > 0)
                            leaderboard.put(Bukkit.getOfflinePlayer(UUID.fromString(id[0])), data.getDouble("data." + id[0] + ".wins") / data.getDouble("data." + id[0] + ".loses"));
                        else
                            leaderboard.put(Bukkit.getOfflinePlayer(UUID.fromString(id[0])), data.getDouble("data." + id[0] + ".wins"));
                    }
                }
                else if (sort.equalsIgnoreCase("fkdr"))
                {
                    if (data.getInt("data." + id[0] + ".loses") + data.getInt("data." + id[0] + ".wins") >= gamesBeforeLeaderboard)
                    {
                        FileConfiguration temp = YamlConfiguration.loadConfiguration(file);
                        if (temp.getInt("FinalDeaths") > 0)
                            leaderboard.put(Bukkit.getOfflinePlayer(UUID.fromString(id[0])), temp.getDouble("FinalKills") / temp.getDouble("FinalDeaths"));
                        else
                            leaderboard.put(Bukkit.getOfflinePlayer(UUID.fromString(id[0])), temp.getDouble("FinalKills"));
                    }
                }
                else if (sort.equalsIgnoreCase("finals"))
                {
                    FileConfiguration temp = YamlConfiguration.loadConfiguration(file);
                    leaderboard.put(Bukkit.getOfflinePlayer(UUID.fromString(id[0])), temp.getDouble("FinalKills"));
                }
                else
                {
                    if (data.getInt("data." + id[0] + "." + sort) > 0)
                    {
                        leaderboard.put(Bukkit.getOfflinePlayer(UUID.fromString(id[0])), data.getDouble("data." + id[0] + "." + sort));
                    }
                }
                leaderboardTotal++;
            }
        }

        List<Map.Entry<OfflinePlayer, Double>> list
                = new LinkedList<>(
                leaderboard.entrySet());

        list.sort(Map.Entry.comparingByValue());
        Collections.reverse(list);

        HashMap<OfflinePlayer, Double> temp = new LinkedHashMap<>();
        for (Map.Entry<OfflinePlayer, Double> aa : list)
        {
            temp.put(aa.getKey(), aa.getValue());
        }

        leaderboard = temp;

        if (sort.equalsIgnoreCase("star"))
        {
            for (OfflinePlayer player : leaderboard.keySet())
            {
                leaderboard.replace(player, leaderboard.get(player), (double) starLevel((int) Math.round(leaderboard.get(player))));
            }
        }
/*
        for (OfflinePlayer player:leaderboard.keySet())
        {
            if(leaderboard.get(player) == null)
                leaderboard.replace(player,0.0);
        }

 */
        makeLeaderboardText(lbsize);
    }

    public int starLevel(int score)
    {
        return score / 500 + 1;
    }

    public void makeLeaderboardText(int size)
    {
        int i = 0;
        for (OfflinePlayer player : leaderboard.keySet())
        {
            Placement.put(player, i + 1);
            if (i < size)
            {
                String playername = safeDisplayName(player);
                if (leaderboard.get(player) == null)
                {
                    String s = ChatColor.translateAlternateColorCodes('&', " &b&lUNRANKED &f" + playername + "&b - ");
                    formattedLeaderboard.add(s);
                }
                else if (Sort.equalsIgnoreCase("star"))
                {
                    String s = ChatColor.translateAlternateColorCodes('&', " &b&l" + (i + 1) + ". &f" + playername + "&b - " + presColor((int) Math.round(leaderboard.get(player))) + (int) Math.round(leaderboard.get(player)) + presColor2((int) Math.round(leaderboard.get(player)))+"✰ ");
                    formattedLeaderboard.add(s);
                }
                else if (Sort.equalsIgnoreCase("fkdr") || Sort.equalsIgnoreCase("wl") || Sort.equalsIgnoreCase("kdr"))
                {
                    String s = ChatColor.translateAlternateColorCodes('&', " &b&l" + (i + 1) + ". &f" + playername + "&b - &f" + (df.format(leaderboard.get(player))));
                    formattedLeaderboard.add(s);
                }
                else
                {
                    String s = ChatColor.translateAlternateColorCodes('&', " &b&l" + (i + 1) + ". &f" + playername + "&b - &f" + ((int) Math.round(leaderboard.get(player))) + " ");
                    formattedLeaderboard.add(s);
                }
            }
            i++;
        }
    }

    public void givePosition(OfflinePlayer finded, Player p)
    {
        if (leaderboard.get(finded) == null)
        {
            p.sendMessage(ChatColor.AQUA + "" + ChatColor.BOLD + finded.getName() + ChatColor.WHITE + " is unranked");
        }
        else if (Sort.equalsIgnoreCase("fkdr") || Sort.equalsIgnoreCase("wl") || Sort.equalsIgnoreCase("kdr"))
            p.sendMessage(ChatColor.AQUA + "" + ChatColor.BOLD + finded.getName() + ChatColor.WHITE + " is number" + ChatColor.AQUA + " " + ChatColor.BOLD + Placement.get(finded) + ChatColor.WHITE + " out of" + ChatColor.AQUA + " " + ChatColor.BOLD + leaderboardTotal + ChatColor.WHITE + " with" + ChatColor.AQUA + " " + ChatColor.BOLD + df.format(leaderboard.get(finded)) + ChatColor.WHITE +" "+new leaderboardHologram().modifySort(Sort).toLowerCase(Locale.ROOT));
        else if (Sort.equalsIgnoreCase("star"))
            p.sendMessage(ChatColor.AQUA + "" + ChatColor.BOLD + finded.getName() + ChatColor.WHITE + " is number" + ChatColor.AQUA + " " + ChatColor.BOLD + Placement.get(finded) + ChatColor.WHITE + " out of" + ChatColor.AQUA + " " + ChatColor.BOLD + leaderboardTotal + ChatColor.WHITE + " with " + presColor((int) Math.round(leaderboard.get(finded))) + (int) Math.round(leaderboard.get(finded)) + ChatColor.WHITE +" "+new leaderboardHologram().modifySort(Sort).toLowerCase(Locale.ROOT));
        else
            p.sendMessage(ChatColor.AQUA + "" + ChatColor.BOLD + finded.getName() + ChatColor.WHITE + " is number" + ChatColor.AQUA + " " + ChatColor.BOLD + Placement.get(finded) + ChatColor.WHITE + " out of" + ChatColor.AQUA + " " + ChatColor.BOLD + leaderboardTotal + ChatColor.WHITE + " with" + ChatColor.AQUA + " " + ChatColor.BOLD + (int) Math.round(leaderboard.get(finded))  + ChatColor.WHITE +" "+new leaderboardHologram().modifySort(Sort).toLowerCase(Locale.ROOT));
    }

    public void printFormattedLeaderboard(Player p)
    {
        p.sendMessage(" ");
        p.sendMessage(ChatColor.BOLD + "" + ChatColor.WHITE + new leaderboardHologram().modifySort(Sort) + ChatColor.AQUA + " " + ChatColor.BOLD + "Bedwars Leaderboard:");
        formattedLeaderboard.forEach(p::sendMessage);
        p.sendMessage(ChatColor.WHITE + " You are number" + ChatColor.AQUA + " " + ChatColor.BOLD + Placement.get(p) + ChatColor.WHITE + " out of" + ChatColor.AQUA + " " + ChatColor.BOLD + leaderboardTotal);
        p.sendMessage(ChatColor.WHITE + " /lb bw (name) to check other players ");
    }

    public String safeDisplayName(OfflinePlayer p)
    {
        File Folder2 = new File(OldCommands.Invictools.getDataFolder(), "PlayerData");
        File pFile = new File(Folder2, p.getUniqueId() + ".yml");
        //   System.out.println(p.getUniqueId() + ".yml");
        final FileConfiguration playerData = YamlConfiguration.loadConfiguration(pFile);
        String s;

        if (Bukkit.getPlayer(p.getUniqueId()) != null)
        {
            Player online = Bukkit.getPlayer(p.getUniqueId());
            new leaderboardCycle().saveDisplayName(online);
            return online.getDisplayName();
        }
        else
        {
            Object o = playerData.get("Displayname");
            //    System.out.println(o);
            try
            {
                s = o.toString();
            }
            catch (NullPointerException e)
            {
                s = p.getName();
            }
            return s;
        }
    }

    public ChatColor presColor(Player p)
    {
        File pFile = new File(Folder, "bw_stats_players.yml");
        final FileConfiguration data = YamlConfiguration.loadConfiguration(pFile);
        int intlvl = (data.getInt("data." + p.getUniqueId() + ".score") / 500) + 1;

        if (intlvl >= 0 && intlvl <= 9)
            return ChatColor.GRAY;
        else if (intlvl >= 10 && intlvl <= 19)
            return ChatColor.WHITE;
        else if (intlvl >= 20 && intlvl <= 29)
            return ChatColor.GOLD;
        else if (intlvl >= 30 && intlvl <= 39)
            return ChatColor.YELLOW;
        else if (intlvl >= 40 && intlvl <= 49)
            return ChatColor.DARK_GREEN;
        else if (intlvl >= 50 && intlvl <= 59)
            return ChatColor.DARK_AQUA;
        else if (intlvl >= 60 && intlvl <= 69)
            return ChatColor.RED;
        else if (intlvl >= 70 && intlvl <= 79)
            return ChatColor.LIGHT_PURPLE;
        else if (intlvl >= 80 && intlvl <= 89)
            return ChatColor.BLUE;
        else if (intlvl >= 90 && intlvl <= 99)
            return ChatColor.DARK_PURPLE;
        else if (intlvl >= 100)
            return ChatColor.DARK_AQUA;

        return ChatColor.GRAY;
    }

    public ChatColor presColor(int intlvl)
    {
        if (intlvl >= 0 && intlvl <= 9)
            return ChatColor.GRAY;
        else if (intlvl >= 10 && intlvl <= 19)
            return ChatColor.WHITE;
        else if (intlvl >= 20 && intlvl <= 29)
            return ChatColor.GOLD;
        else if (intlvl >= 30 && intlvl <= 39)
            return ChatColor.YELLOW;
        else if (intlvl >= 40 && intlvl <= 49)
            return ChatColor.DARK_GREEN;
        else if (intlvl >= 50 && intlvl <= 59)
            return ChatColor.DARK_AQUA;
        else if (intlvl >= 60 && intlvl <= 69)
            return ChatColor.RED;
        else if (intlvl >= 70 && intlvl <= 79)
            return ChatColor.LIGHT_PURPLE;
        else if (intlvl >= 80 && intlvl <= 89)
            return ChatColor.BLUE;
        else if (intlvl >= 90 && intlvl <= 99)
            return ChatColor.DARK_PURPLE;
        else if (intlvl >= 100)
            return ChatColor.DARK_AQUA;

        return ChatColor.GRAY;
    }

    public ChatColor presColor2(int intlvl)
    {
        if (intlvl >= 0 && intlvl <= 9)
            return ChatColor.GRAY;
        else if (intlvl >= 10 && intlvl <= 19)
            return ChatColor.WHITE;
        else if (intlvl >= 20 && intlvl <= 29)
            return ChatColor.GOLD;
        else if (intlvl >= 30 && intlvl <= 39)
            return ChatColor.YELLOW;
        else if (intlvl >= 40 && intlvl <= 49)
            return ChatColor.DARK_GREEN;
        else if (intlvl >= 50 && intlvl <= 59)
            return ChatColor.DARK_AQUA;
        else if (intlvl >= 60 && intlvl <= 69)
            return ChatColor.RED;
        else if (intlvl >= 70 && intlvl <= 79)
            return ChatColor.LIGHT_PURPLE;
        else if (intlvl >= 80 && intlvl <= 89)
            return ChatColor.BLUE;
        else if (intlvl >= 90 && intlvl <= 99)
            return ChatColor.DARK_PURPLE;
        else if (intlvl >= 100)
            return ChatColor.AQUA;

        return ChatColor.GRAY;
    }
}

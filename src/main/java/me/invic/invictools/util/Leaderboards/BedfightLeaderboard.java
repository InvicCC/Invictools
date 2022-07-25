package me.invic.invictools.util.Leaderboards;

import me.invic.invictools.commands.Commands;
import me.invic.invictools.gamemodes.bedfightStatistics;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.text.DecimalFormat;
import java.util.*;

import static me.invic.invictools.util.Leaderboards.leaderboard.gamesBeforeLeaderboard;
import static me.invic.invictools.util.Leaderboards.leaderboard.lbsize;

public class BedfightLeaderboard
{
    File Folder = new File(Commands.Invictools.getDataFolder(), "Bedfight");
    File pFile = new File(Folder, "bedfightstats.yml");
    final FileConfiguration data = YamlConfiguration.loadConfiguration(pFile);

    public static HashMap<OfflinePlayer, Double> leaderboard = new HashMap<>();
    public static HashMap<OfflinePlayer, Integer> Placement = new HashMap<>();
    public static List<String> formattedLeaderboard = new ArrayList<>();
    public static int leaderboardTotal = 0;
    public static String Sort;

    private static final DecimalFormat df = new DecimalFormat("0.00");

    public void loadBFLeaderboard(String sort)
    {
        Sort = sort;
        formattedLeaderboard.clear();
        leaderboardTotal = 0;
        Placement.clear();
        leaderboard.clear();

      //  List<String> playerData = ;/*getStringList("data");*/
        for (String stringUuid : data.getConfigurationSection("data").getKeys(false))
        {
            if (!Bukkit.getOfflinePlayer(UUID.fromString(stringUuid)).getName().equalsIgnoreCase("Tower")
                    && !Bukkit.getOfflinePlayer(UUID.fromString(stringUuid)).getName().equalsIgnoreCase("Unchargeableness")
                    && !Bukkit.getOfflinePlayer(UUID.fromString(stringUuid)).getName().equalsIgnoreCase("Chargeable")
                    && !Bukkit.getOfflinePlayer(UUID.fromString(stringUuid)).getName().equalsIgnoreCase("mayberry15"))
            {
                if (sort.equalsIgnoreCase("score"))
                {
                    leaderboard.put(Bukkit.getOfflinePlayer(UUID.fromString(stringUuid)), (double) new bedfightStatistics().calculateScore(data,stringUuid));
                }
                else if (sort.equalsIgnoreCase("kdr"))
                {
                    if (data.getInt("data." + stringUuid + ".Losses") + data.getInt("data." + stringUuid + ".Wins") >= gamesBeforeLeaderboard)
                    {
                        if (data.getInt("data." + stringUuid + ".NormalDeaths") > 0)
                            leaderboard.put(Bukkit.getOfflinePlayer(UUID.fromString(stringUuid)), data.getDouble("data." + stringUuid + ".NormalKills") / data.getDouble("data." + stringUuid + ".NormalDeaths"));
                        else
                            leaderboard.put(Bukkit.getOfflinePlayer(UUID.fromString(stringUuid)), data.getDouble("data." + stringUuid + ".NormalKills"));
                    }
                }
                else if (sort.equalsIgnoreCase("wl"))
                {
                    if (data.getInt("data." + stringUuid + ".Losses") + data.getInt("data." + stringUuid + ".Wins") >= gamesBeforeLeaderboard)
                    {
                        if (data.getInt("data." + stringUuid + ".Losses") > 0)
                            leaderboard.put(Bukkit.getOfflinePlayer(UUID.fromString(stringUuid)), data.getDouble("data." + stringUuid + ".Wins") / data.getDouble("data." + stringUuid + ".Losses"));
                        else
                            leaderboard.put(Bukkit.getOfflinePlayer(UUID.fromString(stringUuid)), data.getDouble("data." + stringUuid + ".Wins"));
                    }
                }
                else if (sort.equalsIgnoreCase("fkdr"))
                {
                    if (data.getInt("data." + stringUuid + ".Losses") + data.getInt("data." + stringUuid + ".Wins") >= gamesBeforeLeaderboard)
                    {
                        if (data.getInt("data." + stringUuid + ".FinalDeaths") > 0)
                            leaderboard.put(Bukkit.getOfflinePlayer(UUID.fromString(stringUuid)), data.getDouble("data." + stringUuid + ".FinalKills") / data.getInt("data." + stringUuid + ".FinalDeaths"));
                        else
                            leaderboard.put(Bukkit.getOfflinePlayer(UUID.fromString(stringUuid)), data.getDouble("data." + stringUuid + ".FinalKills"));
                    }
                }
                else
                {
                    if (data.getInt("data." + stringUuid + "." + sort) > 0)
                    {
                        leaderboard.put(Bukkit.getOfflinePlayer(UUID.fromString(stringUuid)), data.getDouble("data." + stringUuid + "." + sort));
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
        makeLeaderboardText(lbsize);
    }

    private void makeLeaderboardText(int size)
    {
        int i = 0;
        for (OfflinePlayer player : leaderboard.keySet())
        {
            Placement.put(player, i + 1);
            if (i < size)
            {
                String playername = new leaderboard().safeDisplayName(player);
                if (leaderboard.get(player) == null)
                {
                    String s = ChatColor.translateAlternateColorCodes('&', " &b&lUNRANKED &f" + playername + "&b - ");
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

    public void giveBFPosition(OfflinePlayer finded, Player p)
    {
        if (leaderboard.get(finded) == null)
        {
            p.sendMessage(ChatColor.AQUA + "" + ChatColor.BOLD + finded.getName() + ChatColor.WHITE + " is unranked");
        }
        else if (Sort.equalsIgnoreCase("fkdr") || Sort.equalsIgnoreCase("wl") || Sort.equalsIgnoreCase("kdr"))
            p.sendMessage(ChatColor.AQUA + "" + ChatColor.BOLD + finded.getName() + ChatColor.WHITE + " is number" + ChatColor.AQUA + " " + ChatColor.BOLD + Placement.get(finded) + ChatColor.WHITE + " out of" + ChatColor.AQUA + " " + ChatColor.BOLD + leaderboardTotal + ChatColor.WHITE + " with" + ChatColor.AQUA + " " + ChatColor.BOLD + df.format(leaderboard.get(finded)) + ChatColor.WHITE +" "+new BedfightLeaderboardHologram().modifySort(Sort).toLowerCase(Locale.ROOT));
        else
            p.sendMessage(ChatColor.AQUA + "" + ChatColor.BOLD + finded.getName() + ChatColor.WHITE + " is number" + ChatColor.AQUA + " " + ChatColor.BOLD + Placement.get(finded) + ChatColor.WHITE + " out of" + ChatColor.AQUA + " " + ChatColor.BOLD + leaderboardTotal + ChatColor.WHITE + " with" + ChatColor.AQUA + " " + ChatColor.BOLD + (int) Math.round(leaderboard.get(finded)) + ChatColor.WHITE +" "+new BedfightLeaderboardHologram().modifySort(Sort).toLowerCase(Locale.ROOT));
    }

    public void printBFFormattedLeaderboard(Player p)
    {
        p.sendMessage(" ");
        p.sendMessage(ChatColor.BOLD + "" + ChatColor.WHITE + new BedfightLeaderboardHologram().modifySort(Sort) + ChatColor.AQUA + " " + ChatColor.BOLD + "Bedfight Leaderboard:");
        formattedLeaderboard.forEach(p::sendMessage);
        p.sendMessage(ChatColor.WHITE + " You are number" + ChatColor.AQUA + " " + ChatColor.BOLD + Placement.get(p) + ChatColor.WHITE + " out of" + ChatColor.AQUA + " " + ChatColor.BOLD + leaderboardTotal);
        p.sendMessage(ChatColor.WHITE + " /lb bf (name) to check other players ");
    }
}

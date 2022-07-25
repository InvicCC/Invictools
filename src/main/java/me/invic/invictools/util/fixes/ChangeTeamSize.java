package me.invic.invictools.util.fixes;

import me.invic.invictools.commands.Commands;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.bedwars.api.BedwarsAPI;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ChangeTeamSize
{
    public static List<String> shortArena = new ArrayList<>();
    public static List<String> longArena = new ArrayList<>();
    static String msg = "&cYou must do /bw reload for these changes to take effect";

    public static void createLists(String config) // creates the lists to pull the short and long arena names when server loads
    {
        String[] cutconfig = config.split("_");
        shortArena.add(cutconfig[0]);
        longArena.add(cutconfig[1]);
    }

    public static void printTeamSizes(Player player)
    {
        Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("BedWars");
        File Folder = new File(plugin.getDataFolder(), "arenas");
        List<String> teams = new ArrayList<>();
        for (String arena : shortArena)
        {
            if (CheckValidity(arena))
            {
                BedwarsAPI.getInstance().getGameByName(arena).getAvailableTeams().forEach(team -> teams.add(team.getName()));
                File pFile = new File(Folder, ConfigConversion(arena) + ".yml");
                final FileConfiguration data = YamlConfiguration.loadConfiguration(pFile);
                for (String team : teams)
                {
                    player.sendMessage(ChatColor.YELLOW + arena + ": " + team + ": " + ChatColor.AQUA + data.get("teams." + team + ".maxPlayers"));
                }
                teams.clear();
                player.sendMessage(" ");
            }
        }
    }

    public static void ChangeEveryArenaTeamSize(int teamSize)
    {
        for (String s : shortArena)
        {
            boolean fuckOFF = CheckValidity(s);
            if (fuckOFF)
            {
                System.out.println("editing " + s);
                EditEveryTeamSize(s, teamSize);
            }
            else
                System.out.println("skipping " + s);
        }

        Commands.MasterPlayer.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
    }

    public static void ChangeSingleArenaTeamSize(String config, int teamSize)
    {
        if (CheckValidity(config))
            EditEveryTeamSize((config), teamSize);

        Commands.MasterPlayer.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
    }

    public static void ChangeSingleTeamSize(String config, int teamSize, String teamColor)
    {
        if (CheckValidity(config))
            EditSingleTeamSize((config), teamSize, teamColor);

        Commands.MasterPlayer.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
    }

    public static boolean CheckValidity(String config) // checks if config is correctly grabbed from short name only
    {
        Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("BedWars");
        File Folder = new File(plugin.getDataFolder(), "arenas");
        File pFile = new File(Folder, ConfigConversion(config) + ".yml");
        final FileConfiguration data = YamlConfiguration.loadConfiguration(pFile);
        if (data.getString("name") == null)
        {
            Commands.MasterPlayer.sendMessage(ChatColor.RED + "It looks like this arena config isn't loading properly. Mismatched ID or not setup?: " + ChatColor.WHITE + config);
            return false;
        }
        else if (!data.getString("name").toLowerCase(Locale.ROOT).equals(config.toLowerCase(Locale.ROOT)))
        {
            Commands.MasterPlayer.sendMessage(ChatColor.RED + "It looks like this arena's name does not match the config it's corresponding to: " + ChatColor.WHITE + config);
            return false;
        }

        try
        {
            List<String> teams = new ArrayList<>();
            BedwarsAPI.getInstance().getGameByName(config).getAvailableTeams().forEach(team -> teams.add(team.getName()));
        }
        catch (NullPointerException e)
        {
            return false;
        }

        return true;
    }

    public static String ConfigConversion(String shortName) // uses shortname to return long yml name
    {
        for (int i = 0; i < longArena.size(); i++)
        {
            if (shortArena.get(i).equalsIgnoreCase(shortName))
                return longArena.get(i);
        }
        return null;
    }

    private static void EditEveryTeamSize(String config, int teamSize)
    {
        List<String> teams = new ArrayList<>();
        try
        {
            BedwarsAPI.getInstance().getGameByName(config).getAvailableTeams().forEach(team -> teams.add(team.getName()));
        }
        catch (NullPointerException e)
        {
            Commands.MasterPlayer.sendMessage(ChatColor.RED + "Skipping " + config);
            return;
        }

        Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("BedWars");
        File Folder = new File(plugin.getDataFolder(), "arenas");
        File pFile = new File(Folder, ConfigConversion(config) + ".yml");
        final FileConfiguration data = YamlConfiguration.loadConfiguration(pFile);

        for (String team : teams)
        {
            data.set("teams." + team + ".maxPlayers", teamSize);
        }

        try
        {
            data.save(pFile);
            Commands.MasterPlayer.sendMessage(ChatColor.YELLOW + data.getString("name") + ChatColor.AQUA + " can now hold " + ChatColor.YELLOW + teamSize + ChatColor.AQUA + " players.");
        }
        catch (IOException ex)
        {
            Commands.MasterPlayer.sendMessage(ChatColor.RED + "Error updating team sizes");
        }
    }

    private static void EditSingleTeamSize(String config, int teamSize, String teamColor)
    {
        List<String> teams = new ArrayList<>();
        try
        {
            BedwarsAPI.getInstance().getGameByName(config).getAvailableTeams().forEach(team -> teams.add(team.getName()));
        }
        catch (NullPointerException e)
        {
            Commands.MasterPlayer.sendMessage(ChatColor.RED + "Skipping " + config);
            return;
        }

        Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("BedWars");
        File Folder = new File(plugin.getDataFolder(), "arenas");
        File pFile = new File(Folder, ConfigConversion(config) + ".yml");
        final FileConfiguration data = YamlConfiguration.loadConfiguration(pFile);

        if (data.getString("teams." + teamColor + ".maxPlayers") != null)
            data.set("teams." + teamColor + ".maxPlayers", teamSize);
        else
        {
            Commands.MasterPlayer.sendMessage(ChatColor.RED + "Invalid Team Color");
            return;
        }

        try
        {
            data.save(pFile);
            Commands.MasterPlayer.sendMessage(ChatColor.YELLOW + data.getString("name") + " " + teamColor + ChatColor.AQUA + " can now hold " + ChatColor.YELLOW + teamSize + ChatColor.AQUA + " players.");
        }
        catch (IOException ex)
        {
            Commands.MasterPlayer.sendMessage(ChatColor.RED + "Error updating team sizes");
        }
    }
}

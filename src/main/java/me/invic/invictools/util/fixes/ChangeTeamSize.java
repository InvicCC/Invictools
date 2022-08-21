package me.invic.invictools.util.fixes;

import me.invic.invictools.commands.OldCommands;
import me.invic.invictools.util.ingame.LobbyLogic;
import me.invic.invictools.util.disableStats;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.bedwars.api.BedwarsAPI;
import org.screamingsandals.bedwars.api.game.Game;

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

    public static void grabConfigs()
    {
        shortArena.clear();
        longArena.clear();
        File Folder = new File(OldCommands.Invictools.getDataFolder(), "Maps");
        File[] yamlFiles = Folder.listFiles();
        for (File file : yamlFiles)
        {
            FileConfiguration map = YamlConfiguration.loadConfiguration(file);
            String[] mapName = file.getName().split("\\.");
            createLists(mapName[0] + "_" + map.getString("Conversion"));
        }
    }

    public static void createLists(String config) // creates the lists to pull the short and long arena names when server loads
    {
        String[] cutconfig = config.split("_");
        if(!shortArena.contains(cutconfig[0]))
            shortArena.add(cutconfig[0]);
        if(!longArena.contains(cutconfig[1]))
            longArena.add(cutconfig[1]);
    }

    public static int getTeamSize(Game game)
    {
        Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("BedWars");
        File Folder = new File(plugin.getDataFolder(), "arenas");
        List<String> teams = new ArrayList<>();

        game.getAvailableTeams().forEach(team -> teams.add(team.getName()));
        File pFile = new File(Folder, ConfigConversion(game.getName()) + ".yml");
        final FileConfiguration data = YamlConfiguration.loadConfiguration(pFile);

        if(teams.size() >=4)
        {
            if (data.getInt("teams." + teams.get(0) + ".maxPlayers") == data.getInt("teams." + teams.get(3) + ".maxPlayers"))
                return data.getInt("teams." + teams.get(0) + ".maxPlayers");
            else
                return data.getInt("teams." + teams.get(6) + ".maxPlayers");
        }
        else
            return data.getInt("teams." + teams.get(0) + ".maxPlayers");
    }

    public static void printTeamSizes(CommandSender player)
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
                player.sendMessage(ChatColor.YELLOW + arena+":");
                player.sendMessage(ChatColor.YELLOW + "GameType: " + ChatColor.AQUA+ new LobbyLogic().getMapConfiguration(BedwarsAPI.getInstance().getGameByName(arena).getName()).getString("GameType","normal"));
                for (String team : teams)
                {
                    player.sendMessage(ChatColor.YELLOW + team + ": " + ChatColor.AQUA + data.get("teams." + team + ".maxPlayers"));
                }
                teams.clear();
                player.sendMessage(" ");
            }
        }
    }

    public static void printTeamSizesGameType(CommandSender player,String gameType)
    {
        Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("BedWars");
        File Folder = new File(plugin.getDataFolder(), "arenas");
        List<String> teams = new ArrayList<>();
        for (String arena : shortArena)
        {
            if(disableStats.getGameType(BedwarsAPI.getInstance().getGameByName(arena)).equalsIgnoreCase(gameType))
            {
                if (CheckValidity(arena))
                {
                    BedwarsAPI.getInstance().getGameByName(arena).getAvailableTeams().forEach(team -> teams.add(team.getName()));
                    File pFile = new File(Folder, ConfigConversion(arena) + ".yml");
                    final FileConfiguration data = YamlConfiguration.loadConfiguration(pFile);
                    player.sendMessage(ChatColor.YELLOW + arena + ":");
                    player.sendMessage(ChatColor.YELLOW + "GameType: " + ChatColor.AQUA+new LobbyLogic().getMapConfiguration(BedwarsAPI.getInstance().getGameByName(arena).getName()).getString("GameType", "normal"));
                    for (String team : teams)
                    {
                        player.sendMessage(ChatColor.YELLOW + team + ": " + ChatColor.AQUA + data.get("teams." + team + ".maxPlayers"));
                    }
                    teams.clear();
                    player.sendMessage(" ");
                }
            }
        }
    }

    public static void ChangeEveryOfGameType(int teamSize, String gameType)
    {
        for (String s : shortArena)
        {
            if(disableStats.getGameType(BedwarsAPI.getInstance().getGameByName(s)).equalsIgnoreCase(gameType))
            {
                boolean fuckOFF = CheckValidity(s);
                if (fuckOFF)
                {
                    EditEveryTeamSize(s, teamSize);
                }
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
                if(BedwarsAPI.getInstance().getGameByName(s).getConnectedPlayers().size() == 0)
                    EditEveryTeamSize(s, teamSize);
                else
                    System.out.println("Skipping "+ s+ " because a game is running in this arena");
            }
        }

   //     Commands.MasterPlayer.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
    }

    public static void ChangeSingleArenaTeamSize(String config, int teamSize)
    {
        if (CheckValidity(config))
        {
            if(BedwarsAPI.getInstance().getGameByName(config).getConnectedPlayers().size() == 0)
                EditEveryTeamSize((config), teamSize);
            else
                System.out.println("Skipping "+ config+ " because a game is running in this arena");
        }

      //  Commands.MasterPlayer.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
    }

    public static void ChangeSingleTeamSize(String config, int teamSize, String teamColor)
    {
        if (CheckValidity(config))
        {
            if(BedwarsAPI.getInstance().getGameByName(config).getConnectedPlayers().size() == 0)
                EditSingleTeamSize((config), teamSize, teamColor);
            else
                System.out.println("Skipping "+ config+ " because a game is running in this arena");
        }

     //   Commands.MasterPlayer.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
    }

    public static boolean CheckValidity(String config) // checks if config is correctly grabbed from short name only
    {
        Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("BedWars");
        File Folder = new File(plugin.getDataFolder(), "arenas");
        File pFile = new File(Folder, ConfigConversion(config) + ".yml");
        final FileConfiguration data = YamlConfiguration.loadConfiguration(pFile);
        if (data.getString("name") == null)
        {
            OldCommands.MasterPlayer.sendMessage(ChatColor.RED + "It looks like this arena config isn't loading properly. Mismatched ID or not setup?: " + ChatColor.WHITE + config);
            return false;
        }
        else if (!data.getString("name").toLowerCase(Locale.ROOT).equals(config.toLowerCase(Locale.ROOT)))
        {
            OldCommands.MasterPlayer.sendMessage(ChatColor.RED + "It looks like this arena's name does not match the config it's corresponding to: " + ChatColor.WHITE + config);
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
           // Commands.MasterPlayer.sendMessage(ChatColor.RED + "Skipping " + config);
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
        data.set("pauseCountdown",6000);

        try
        {
            BedwarsAPI.getInstance().getGameByName(config).stop();
            data.save(pFile);
            //new safeSizeChange().leaveRejoinGame(BedwarsAPI.getInstance().getGameByName(config),0);
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(),"bw singlereload "+ChangeTeamSize.ConfigConversion(config)+".yml");
           // Commands.MasterPlayer.sendMessage(ChatColor.YELLOW + data.getString("name") + ChatColor.AQUA + " can now hold " + ChatColor.YELLOW + teamSize + ChatColor.AQUA + " players per team.");
        }
        catch (IOException ex)
        {
          //  Commands.MasterPlayer.sendMessage(ChatColor.RED + "Error updating team sizes");
        }
    }

    private static void EditSingleTeamSize(String config, int teamSize, String teamColor)
    {
        Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("BedWars");
        File Folder = new File(plugin.getDataFolder(), "arenas");
        File pFile = new File(Folder, ConfigConversion(config) + ".yml");
        final FileConfiguration data = YamlConfiguration.loadConfiguration(pFile);

        if (data.getString("teams." + teamColor + ".maxPlayers") != null)
            data.set("teams." + teamColor + ".maxPlayers", teamSize);
        else
        {
            OldCommands.MasterPlayer.sendMessage(ChatColor.RED + "Invalid Team Color");
            return;
        }

        try
        {
            BedwarsAPI.getInstance().getGameByName(config).stop();
            data.save(pFile);
         //   new safeSizeChange().leaveRejoinGame(BedwarsAPI.getInstance().getGameByName(config),0);
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(),"bw singlereload "+ConfigConversion(config)+".yml");
           // Commands.MasterPlayer.sendMessage(ChatColor.YELLOW + data.getString("name") + " " + teamColor + ChatColor.AQUA + " can now hold " + ChatColor.YELLOW + teamSize + ChatColor.AQUA + " players.");
        }
        catch (IOException ex)
        {
           // Commands.MasterPlayer.sendMessage(ChatColor.RED + "Error updating team sizes");
        }
    }
}

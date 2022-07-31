package me.invic.invictools.impl;

import me.invic.invictools.commandManagerLib.SubCommand;
import me.invic.invictools.commands.Commands;
import me.invic.invictools.gamemodes.bedfight;
import me.invic.invictools.util.fixes.LobbyInventoryFix;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.screamingsandals.bedwars.api.BedwarsAPI;
import org.screamingsandals.bedwars.api.game.GameStatus;

import java.util.List;

public class GamejoinerCommand implements SubCommand
{
    @Override
    public String getName()
    {
        return "gamejoiner";
    }

    @Override
    public String getDescription()
    {
        return null;
    }

    @Override
    public String getSyntax()
    {
        return "/it gamejoiner";
    }

    @Override
    public String getPermission()
    {
        return "invic.all";
    }

    @Override
    public List<String> getTabCompletion(int index, String[] args)
    {
        return null;
    }

    @Override
    public void perform(CommandSender sender, String[] args)
    {
        Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("Invictools");
        final FileConfiguration Config = plugin.getConfig();

        String currentgame = Config.getString("loadedgame");
        Player player = (Player) sender;

        for (Player p : Bukkit.getOnlinePlayers()) //party catch
        {
            if (p.getWorld().getName().equalsIgnoreCase("bwlobby"))
                new LobbyInventoryFix().saveInventory(p);
        }

        if (currentgame.equalsIgnoreCase("none"))
        {
            player.sendMessage(ChatColor.RED + "No games are currently running! sorry :(");
            player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_HURT, 2, 1);
        }
        else
        {
            BedwarsAPI.getInstance().getGameByName(currentgame).joinToGame(player);
            //  new SafeOpCommand(player, "bw join " + currentgame);
            new BukkitRunnable()
            {
                @Override
                public void run()
                {
                    if (!BedwarsAPI.getInstance().getGameByName(currentgame).getStatus().equals(GameStatus.WAITING))
                        if (player.getWorld().equals(BedwarsAPI.getInstance().getGameByName(currentgame).getGameWorld()))
                            player.setGameMode(GameMode.SPECTATOR);
                    player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 2, 1);
                }
            }.runTaskLater(Commands.Invictools, 5L);
        }
    }
}
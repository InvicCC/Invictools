package me.invic.invictools.commands;

import me.invic.invictools.gamemodes.bedfight;
import me.invic.invictools.util.queue;
import me.invic.invictools.util.safeSizeChange;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.screamingsandals.bedwars.api.BedwarsAPI;
import org.screamingsandals.bedwars.api.game.Game;

import java.util.ArrayList;
import java.util.List;

public class utilityCommands implements CommandExecutor, TabExecutor // end game by disabling stats then .leave every player. debug. test safesizeedit.
{
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args)
    {
        List<String> tabComplete = new ArrayList<>();

        if(sender instanceof Player)
        {
            if(!sender.hasPermission("invic.invictools"))
            {
                sender.sendMessage(Commands.permissionsError);
                return tabComplete;
            }
        }

        if(args.length == 1)
        {
            tabComplete.add("test");
            tabComplete.add("debug");
            tabComplete.add("endgame");
            tabComplete.add("resetbf");
        }
        else if(args.length==2 && args[0].equalsIgnoreCase("test"))
        {
            tabComplete.add("changeTeamSize game size");
        }
        else if(args.length==2 && args[0].equalsIgnoreCase("resetbf"))
        {
            for (Player p:Bukkit.getOnlinePlayers())
            {
                tabComplete.add(p.getName());
            }
        }
        else if(args.length==3 && args[0].equalsIgnoreCase("resetbf"))
        {
            tabComplete.add("default");
        }
        else if(args.length==2 && args[0].equalsIgnoreCase("endgame"))
        {
            tabComplete.add("game");
        }

        return tabComplete;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if(args[0].equalsIgnoreCase("test") && args[1].equalsIgnoreCase("changeteamsize"))
        {
            new safeSizeChange().safeSizeEdit(args[2],sender,Integer.parseInt(args[3]));
        }
        if(args[0].equalsIgnoreCase("resetbf"))
        {
            new bedfight().loadBedfightInventory(args[2], Bukkit.getPlayer(args[1]), true);
            sender.sendMessage(ChatColor.AQUA + "reset bedfight inventory " + args[2] +" for " + args[1]);
        }
        else if(args[0].equalsIgnoreCase("test") && args[1].equalsIgnoreCase("bfq"))
        {
            if(sender instanceof Player)
            {
                Player p = (Player)sender;
                new joinCommands().safeInventorySave();
                queue.activeBedfightGame.joinToGame(p);
                sender.sendMessage(ChatColor.AQUA+"Sending you to "+ChatColor.WHITE + queue.activeBedfightGame.getName());
                new BukkitRunnable()
                {
                    @Override
                    public void run()
                    {
                        System.out.println(p.getWorld().getName());
                        if(p.getWorld().equals(Bukkit.getWorld("bwlobby")))
                        {
                            queue.activeBedfightGame = new queue().getRandomGame("bedfight");
                            queue.activeBedfightGame.joinToGame(p);
                            sender.sendMessage(ChatColor.AQUA+"Map load failure, Sending you to "+ChatColor.WHITE + queue.activeBedfightGame.getName());
                        }
                    }
                }.runTaskLater(Commands.Invictools, 5L);
            }
            else
            {
                sender.sendMessage("Must be a player to execute this");
            }
        }
        else if(args[0].equalsIgnoreCase("test") && args[1].equalsIgnoreCase("bwq"))
        {
            if(sender instanceof Player)
            {
                Player p = (Player)sender;
                new joinCommands().safeInventorySave();
                queue.activeBedwarsGame.joinToGame(p);
                sender.sendMessage(ChatColor.AQUA+"Sending you to "+ChatColor.WHITE + queue.activeBedwarsGame.getName());
                new BukkitRunnable()
                {
                    @Override
                    public void run()
                    {
                        System.out.println(p.getWorld().getName());
                        if(p.getWorld().equals(Bukkit.getWorld("bwlobby")))
                        {
                            queue.activeBedwarsGame = new queue().getRandomGame("normal");
                            queue.activeBedwarsGame.joinToGame(p);
                            sender.sendMessage(ChatColor.AQUA+"Map load failure, Sending you to "+ChatColor.WHITE + queue.activeBedwarsGame.getName());
                        }
                    }
                }.runTaskLater(Commands.Invictools, 5L);
            }
            else
            {
                sender.sendMessage("Must be a player to execute this");
            }
        }
        else if(args[0].equalsIgnoreCase("debug"))
        {
            Commands.debug(sender);
        }
        else if(args.length == 1 && args[0].equalsIgnoreCase("endgame"))
        {
            Player p = (Player) sender;
            endgameNoStats(BedwarsAPI.getInstance().getGameOfPlayer(p));
        }
        else if(args.length >= 2 && args[0].equalsIgnoreCase("endgame"))
        {
            endgameNoStats(BedwarsAPI.getInstance().getGameByName(args[1]));
        }
        return true;
    }

    public void endgameNoStats(Game game)
    {
        List<Player> players = game.getConnectedPlayers();

        boolean swapTrack = false;
        if(Commands.StatsTrack)
        {
            Commands.StatsTrack = false;
            swapTrack = true;
        }

        for (Player p:players)
        {
            game.leaveFromGame(p);
        }

        boolean finalSwapTrack = swapTrack;
        new BukkitRunnable()
        {
            @Override
            public void run()
            {
                if(finalSwapTrack)
                    Commands.StatsTrack = true;
            }
        }.runTaskLater(Commands.Invictools, 20L);
    }
}

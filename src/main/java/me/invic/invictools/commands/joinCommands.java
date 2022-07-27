package me.invic.invictools.commands;

import me.invic.invictools.util.fixes.LobbyInventoryFix;
import me.invic.invictools.util.queue;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
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

public class joinCommands implements CommandExecutor, TabExecutor
{

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args)
    {
        List<String> tabComplete = new ArrayList<>();
        if(args.length == 0)
        {
            tabComplete.add("bf");
            tabComplete.add("bw");
            tabComplete.add("selector");
            tabComplete.add("join");
            tabComplete.add("joinall");
        }
        else if(args.length == 1 && args[0].equalsIgnoreCase("join"))
        {
            for (Game game:BedwarsAPI.getInstance().getGames())
            {
                tabComplete.add(game.getName());
            }
        }
        else if(args.length == 2 && args[0].equalsIgnoreCase("join"))
        {
            for (Player p:Bukkit.getOnlinePlayers())
            {
                tabComplete.add(p.getName());
            }
        }
        else if(args.length == 2 && args[0].equalsIgnoreCase("joinall"))
        {
            for (Game game:BedwarsAPI.getInstance().getGames())
            {
                tabComplete.add(game.getName());
            }
        }

        return tabComplete;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        boolean perms = false;
        if(sender instanceof Player)
        {
            if(sender.hasPermission("invic.invictools"))
            {
                perms = true;
            }
        }
        else
        {
            perms = true;
        }

        if(args[0].equals("bf"))
        {
            if(toggleCommands.bedfightQueue)
            {
                if(sender instanceof Player)
                {
                    Player p = (Player)sender;
                    new joinCommands().safeInventorySave();
                    queue.activeBedfightGame.joinToGame(p);
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
                            }

                            sender.sendMessage(ChatColor.AQUA+"Sending you to "+ChatColor.WHITE + queue.activeBedfightGame.getName());
                        }
                    }.runTaskLater(Commands.Invictools, 5L);
                }
                else
                {
                    sender.sendMessage("Must be a player to execute this");
                }
            }
        }
        else if(args[0].equalsIgnoreCase("bw"))
        {
            if(toggleCommands.bedwarsQueue)
            {
                if(sender instanceof Player)
                {
                    Player p = (Player)sender;
                    new joinCommands().safeInventorySave();
                    queue.activeBedwarsGame.joinToGame(p);
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
                            }

                            sender.sendMessage(ChatColor.AQUA+"Sending you to "+ChatColor.WHITE + queue.activeBedwarsGame.getName());
                        }
                    }.runTaskLater(Commands.Invictools, 5L);
                }
                else
                {
                    sender.sendMessage("Must be a player to execute this");
                }
            }
            else
            {
                sender.sendMessage(ChatColor.RED + "Bedwars queue is currently disabled");
                if(sender instanceof Player)
                    ((Player) sender).playSound(((Player) sender).getLocation(), Sound.ENTITY_VILLAGER_NO,1,1);
            }

        }
        else if(args[0].equalsIgnoreCase("selector"))
        {
            if(toggleCommands.bedwarsSelector && false)
            {
                sender.sendMessage(ChatColor.RED+"coming soon");
                // new playerMapSelector()
            }
            else
                sender.sendMessage(ChatColor.RED+"The map selector is currently disabled");
        }
        else if(args.length == 2 && args[0].equalsIgnoreCase("join") && perms && sender instanceof Player)
        {
            if(BedwarsAPI.getInstance().isGameWithNameExists(args[1]) && !BedwarsAPI.getInstance().isPlayerPlayingAnyGame((Player)sender))
            {
                safeInventorySave();
                BedwarsAPI.getInstance().getGameByName(args[1]).joinToGame((Player)sender);
                sender.sendMessage(ChatColor.AQUA+"Sending you to "+ChatColor.WHITE + args[1]);
            }
            else
                sender.sendMessage(ChatColor.RED + "invalid game");

        }
        else if(args.length == 3 && args[0].equalsIgnoreCase("join") && perms)
        {
            if(BedwarsAPI.getInstance().isGameWithNameExists(args[1]) && !BedwarsAPI.getInstance().isPlayerPlayingAnyGame(Bukkit.getPlayer(args[2])))
            {
                safeInventorySave();
                BedwarsAPI.getInstance().getGameByName(args[1]).joinToGame(Bukkit.getPlayer(args[2]));
                Bukkit.getPlayer(args[2]).sendMessage(ChatColor.AQUA+"Sending you to "+ChatColor.WHITE + args[1]);
            }
            else
                sender.sendMessage(ChatColor.RED + "invalid game");
        }
        else if(args[0].equalsIgnoreCase("alljoin") && perms)
        {
            if (BedwarsAPI.getInstance().isGameWithNameExists(args[1]))
            {
                for (Player player : Bukkit.getOnlinePlayers())
                {
                    if(!BedwarsAPI.getInstance().isPlayerPlayingAnyGame(player))
                    {
                        if(player.getWorld().getName().equalsIgnoreCase("bwlobby"))
                            new LobbyInventoryFix().saveInventory(player);

                        BedwarsAPI.getInstance().getGameByName(args[1]).joinToGame(player);
                    }
                }
                sender.sendMessage(ChatColor.AQUA + "sending to game " + ChatColor.YELLOW + args[1]);
            }
            else
            {
                sender.sendMessage(ChatColor.RED + "Invalid Game: " + args[1]);
            }
        }
        return true;
    }

    public void safeInventorySave()
    {
        for (Player player : Bukkit.getOnlinePlayers())
        {
            if(!BedwarsAPI.getInstance().isPlayerPlayingAnyGame(player))
            {
                if(player.getWorld().getName().equalsIgnoreCase("bwlobby"))
                    new LobbyInventoryFix().saveInventory(player);
            }
        }
    }
}

package cc.invic.invictools.impl;

import cc.invic.invictools.util.fixes.LobbyInventoryFix;
import cc.invic.invictools.util.queue;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.screamingsandals.bedwars.api.BedwarsAPI;
import org.screamingsandals.bedwars.api.game.Game;
import org.screamingsandals.bedwars.api.game.GameStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class joinCommands implements CommandExecutor, TabExecutor
{

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args)
    {
        boolean perms = false;
        if (sender instanceof Player)
        {
            if (sender.hasPermission("invic.invictools"))
            {
                perms = true;
            }
        } else
        {
            perms = true;
        }

        List<String> tabComplete = new ArrayList<>();
        if (args.length == 1)
        {
            if (perms)
            {
                tabComplete.add("bf");
                tabComplete.add("bw");
                tabComplete.add("selector");
                tabComplete.add("join");
                tabComplete.add("joinall");
                tabComplete.add("host");
            }
            tabComplete.add("spec");
        } else if ((args.length == 2 && args[0].equalsIgnoreCase("join") || args.length == 2 && args[0].equalsIgnoreCase("host"))&& perms)
        {
            for (Game game : BedwarsAPI.getInstance().getGames())
            {
                tabComplete.add(game.getName());
            }
        } else if (args.length == 3 && args[0].equalsIgnoreCase("join") && perms)
        {
            for (Player p : Bukkit.getOnlinePlayers())
            {
                tabComplete.add(p.getName());
            }
        } else if (args.length == 2 && args[0].equalsIgnoreCase("alljoin") && perms)
        {
            for (Game game : BedwarsAPI.getInstance().getGames())
            {
                tabComplete.add(game.getName());
            }
        } else if (args.length == 2 && args[0].equalsIgnoreCase("spec"))
        {
            for (Player p : Bukkit.getOnlinePlayers())
            {
                if (BedwarsAPI.getInstance().isPlayerPlayingAnyGame(p))
                {
                    if(BedwarsAPI.getInstance().getGameOfPlayer(p).getStatus().equals(GameStatus.RUNNING))
                        tabComplete.add(p.getName());
                }
            }
        }

        return tabComplete;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        boolean perms = false;
        if (sender instanceof Player)
        {
            if (sender.hasPermission("invic.invictools"))
            {
                perms = true;
            }
        } else
        {
            perms = true;
        }

        if (args[0].equals("bf"))
        {
            if (toggleCommands.bedfightQueue)
            {
                if (sender instanceof Player)
                {
                    Player p = (Player) sender;
                    new joinCommands().safeInventorySave();
                    queue.activeBedfightGame.joinToGame(p);
                    new BukkitRunnable()
                    {
                        @Override
                        public void run()
                        {
                            if (p.getWorld().equals(Bukkit.getWorld("bwlobby")))
                            {
                                queue.activeBedfightGame = new queue().getRandomGame("bedfight");
                                queue.activeBedfightGame.joinToGame(p);
                            }

                            safeSpec(p, queue.activeBedfightGame);
                            sender.sendMessage(ChatColor.AQUA + "Sending you to " + ChatColor.WHITE + queue.activeBedfightGame.getName());
                            announceJoin("Bedfight");
                        }
                    }.runTaskLater(Commands.Invictools, 20L);
                } else
                {
                    sender.sendMessage("Must be a player to execute this");
                }
            }
        } else if (args[0].equalsIgnoreCase("bw"))
        {
            if (toggleCommands.bedwarsQueue)
            {
                if (sender instanceof Player)
                {
                    Player p = (Player) sender;
                    new joinCommands().safeInventorySave();
                    queue.activeBedwarsGame.joinToGame(p);
                    new BukkitRunnable()
                    {
                        @Override
                        public void run()
                        {
                            System.out.println(p.getWorld().getName());
                            if (p.getWorld().equals(Bukkit.getWorld("bwlobby")))
                            {
                                queue.activeBedwarsGame = new queue().getRandomGame("normal");
                                queue.activeBedwarsGame.joinToGame(p);
                            }

                            safeSpec(p, queue.activeBedwarsGame);
                            sender.sendMessage(ChatColor.AQUA + "Sending you to " + ChatColor.WHITE + queue.activeBedwarsGame.getName());
                            announceJoin("Bedwars");
                        }
                    }.runTaskLater(Commands.Invictools, 20L);
                } else
                {
                    sender.sendMessage("Must be a player to execute this");
                }
            } else
            {
                sender.sendMessage(ChatColor.RED + "Bedwars queue is currently disabled");
                if (sender instanceof Player)
                    ((Player) sender).playSound(((Player) sender).getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
            }

        } else if (args[0].equalsIgnoreCase("selector"))
        {
            if (toggleCommands.bedwarsSelector && false)
            {
                sender.sendMessage(ChatColor.RED + "coming soon");
                // new playerMapSelector()
            } else
                sender.sendMessage(ChatColor.RED + "The map selector is currently disabled");
        }
        else if (args.length == 2 && args[0].equalsIgnoreCase("host"))
        {
            Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("Invictools");
            final FileConfiguration Config = plugin.getConfig();
            String arena = args[1];
            if (BedwarsAPI.getInstance().isGameWithNameExists(args[1]))
            {
                Config.set("loadedgame", arena);
                plugin.saveConfig();
                sender.sendMessage(ChatColor.AQUA + "Arena " + ChatColor.WHITE + arena + ChatColor.AQUA + " has been loaded into the npc");
            } else
                sender.sendMessage(ChatColor.RED + "Arena name is not valid");
        }
        else if (args.length == 1 && args[0].equalsIgnoreCase("host"))
        {
            Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("Invictools");
            final FileConfiguration Config = Commands.Invictools.getConfig();

            Config.set("loadedgame", "none");
            plugin.saveConfig();
        }
        else if (args.length == 2 && args[0].equalsIgnoreCase("join") && perms && sender instanceof Player)
        {
            if (BedwarsAPI.getInstance().isGameWithNameExists(args[1]) && !BedwarsAPI.getInstance().isPlayerPlayingAnyGame((Player) sender))
            {
                safeInventorySave();
                BedwarsAPI.getInstance().getGameByName(args[1]).joinToGame((Player) sender);
                safeSpec((Player) sender, BedwarsAPI.getInstance().getGameByName(args[1]));
                sender.sendMessage(ChatColor.AQUA + "Sending you to " + ChatColor.WHITE + args[1]);
            } else
                sender.sendMessage(ChatColor.RED + "invalid game");

        } else if (args.length == 3 && args[0].equalsIgnoreCase("join") && perms)
        {
            if (BedwarsAPI.getInstance().isGameWithNameExists(args[1]) && !BedwarsAPI.getInstance().isPlayerPlayingAnyGame(Bukkit.getPlayer(args[2])))
            {
                safeInventorySave();
                BedwarsAPI.getInstance().getGameByName(args[1]).joinToGame(Bukkit.getPlayer(args[2]));
                safeSpec(Bukkit.getPlayer(args[2]), BedwarsAPI.getInstance().getGameByName(args[1]));
                Bukkit.getPlayer(args[2]).sendMessage(ChatColor.AQUA + "Sending you to " + ChatColor.WHITE + args[1]);
            } else
                sender.sendMessage(ChatColor.RED + "invalid game");
        } else if (args.length == 2 && args[0].equalsIgnoreCase("spec") && sender instanceof Player)
        {
            if (BedwarsAPI.getInstance().isPlayerPlayingAnyGame(Bukkit.getPlayer(args[1])) && BedwarsAPI.getInstance().getGameOfPlayer(Bukkit.getPlayer(args[1])).getStatus().equals(GameStatus.RUNNING)&& !BedwarsAPI.getInstance().isPlayerPlayingAnyGame((Player) sender))
            {
                safeInventorySave();
                sender.sendMessage(ChatColor.AQUA + "Sending you to " + ChatColor.WHITE + args[1]);
                BedwarsAPI.getInstance().getGameOfPlayer(Bukkit.getPlayer(args[1])).joinToGame((Player) sender);
                safeSpec((Player) sender, BedwarsAPI.getInstance().getGameOfPlayer(Bukkit.getPlayer(args[1])));
            } else
                sender.sendMessage(ChatColor.RED + "Player is not ingame");
        } else if (args[0].equalsIgnoreCase("alljoin") && perms)
        {
            if (BedwarsAPI.getInstance().isGameWithNameExists(args[1]))
            {
                for (Player player : Bukkit.getOnlinePlayers())
                {
                    if (!BedwarsAPI.getInstance().isPlayerPlayingAnyGame(player))
                    {
                        if (player.getWorld().getName().equalsIgnoreCase("bwlobby"))
                            new LobbyInventoryFix().saveInventory(player);

                        BedwarsAPI.getInstance().getGameByName(args[1]).joinToGame(player);
                        safeSpec(player, BedwarsAPI.getInstance().getGameByName(args[1]));
                    }
                }
                sender.sendMessage(ChatColor.AQUA + "sending to game " + ChatColor.YELLOW + args[1]);
            } else
            {
                sender.sendMessage(ChatColor.RED + "Invalid Game: " + args[1]);
            }
        }
        else
            sender.sendMessage(ChatColor.RED+"invalid syntax");
        return true;
    }

    public void safeInventorySave()
    {
        for (Player player : Bukkit.getOnlinePlayers())
        {
            if (!BedwarsAPI.getInstance().isPlayerPlayingAnyGame(player))
            {
                if (player.getWorld().getName().equalsIgnoreCase("bwlobby"))
                    new LobbyInventoryFix().saveInventory(player);
            }
        }
    }

    public void safeSpec(Player player, Game game)
    {
        new BukkitRunnable()
        {
            @Override
            public void run()
            {
                if (!BedwarsAPI.getInstance().getGameByName(game.getName()).getStatus().equals(GameStatus.WAITING))
                    if (player.getWorld().equals(BedwarsAPI.getInstance().getGameByName(game.getName()).getGameWorld()))
                        player.setGameMode(GameMode.SPECTATOR);
            }
        }.runTaskLater(Commands.Invictools, 5L);
    }


    int cooldown = 30;
    public static HashMap<String, Long> joinCooldown = new HashMap<>();

    public void announceJoin(String type)
    {
        boolean bigAnnounce = false;
        if (joinCooldown.containsKey(type))
        {
            long secondsLeft = ((joinCooldown.get(type) / 1000) + cooldown) - (System.currentTimeMillis() / 1000);
            if (secondsLeft <= 0)
                bigAnnounce = true;
        } else
            bigAnnounce = true;

        joinCooldown.put(type,System.currentTimeMillis());

        for (Player p : Bukkit.getOnlinePlayers())
        {
            if (p.getWorld().equals(Bukkit.getWorld("bwlobby")))
            {
                if (bigAnnounce)
                {
                    p.sendTitle(ChatColor.translateAlternateColorCodes('&', "&e" + type + " is Queuing"), ChatColor.translateAlternateColorCodes('&', "&r&fUse the compass to join!"), 10, 3 * 20, 15);
                    p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, .5F);
                } else
                {
                    p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.BOLD + " " + ChatColor.WHITE + type + " is Queuing. Join with the compass!"));
                }
            }
        }
    }

    public void announceHost()
    {
        String type = "host";
        boolean bigAnnounce = false;
        if (joinCooldown.containsKey(type))
        {
            long secondsLeft = ((joinCooldown.get(type) / 1000) + cooldown) - (System.currentTimeMillis() / 1000);
            if (secondsLeft <= 0)
                bigAnnounce = true;
        } else
            bigAnnounce = true;

        joinCooldown.put(type,System.currentTimeMillis());

        for (Player p : Bukkit.getOnlinePlayers())
        {
                if (bigAnnounce)
                {
                    p.sendTitle(ChatColor.translateAlternateColorCodes('&', "&e" + "Hosted Games Starting!"), ChatColor.translateAlternateColorCodes('&', "&r&fUse the compass + nether star to join!"), 10, 3 * 20, 15);
                    p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 1, .5F);
                } else
                {
                    p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.BOLD + " " + ChatColor.WHITE + "Games are being hosted. Join with the compass + nether star!"));
                }
        }
    }
}

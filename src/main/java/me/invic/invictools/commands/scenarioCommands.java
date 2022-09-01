package me.invic.invictools.commands;

import me.invic.invictools.gamemodifiers.*;
import me.invic.invictools.gamemodifiers.LuckyBlocks.LuckyBlockSpawner;
import me.invic.invictools.util.ingame.LobbyLogic;
import me.invic.invictools.util.disableStats;
import me.invic.invictools.util.fixes.Protocol47Fix;
import me.invic.invictools.util.ingame.blockDecay;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.data.type.Bed;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.screamingsandals.bedwars.api.BedwarsAPI;
import org.screamingsandals.bedwars.api.game.Game;
import org.screamingsandals.bedwars.api.game.GameStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class scenarioCommands implements TabExecutor, CommandExecutor
{
    public static List<Entity> nofall = new ArrayList<>();

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args)
    {
        List<String> tabComplete = new ArrayList<>();
        if (sender instanceof Player)
        {
            if (!sender.hasPermission("invic.invictools"))
            {
                sender.sendMessage(OldCommands.permissionsError);
                return tabComplete;
            }
        }

        if(args.length == 1)
        {
            tabComplete.add("lucky");
            tabComplete.add("creative");
            tabComplete.add("creativeall");
            tabComplete.add("nostats");
            tabComplete.add("nofall");
            tabComplete.add("newcombat");
            tabComplete.add("decay");
            tabComplete.add("infest");
            tabComplete.add("minutemob");
            tabComplete.add("albi");
        }
        else if(args.length == 2 && args[0].equalsIgnoreCase("minutemob"))
        {
            for (EntityType type:EntityType.values())
            {
                if(type.isSpawnable())
                   tabComplete.add(type.toString());
            }
        }
        else if(args.length == 3 && args[0].equalsIgnoreCase("minutemob"))
        {
            tabComplete.add("30");
            tabComplete.add("60");
            tabComplete.add("10");
            tabComplete.add("1");
        }
        else if(args.length == 2 && args[0].equalsIgnoreCase("lucky"))
        {
            tabComplete.add("47");
            tabComplete.add("normal");
        }
        else if(args.length == 3 && args[0].equalsIgnoreCase("lucky"))
        {
            for (Player p : Bukkit.getOnlinePlayers())
            {
                if (BedwarsAPI.getInstance().isPlayerPlayingAnyGame(p))
                    tabComplete.add(p.getName());
            }
        }

        return tabComplete;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if (sender instanceof Player)
        {
            if (!sender.hasPermission("invic.invictools"))
            {
                sender.sendMessage(OldCommands.permissionsError);
                return true;
            }
        }

        if(args.length==1 && args[0].equalsIgnoreCase("lucky") && sender instanceof Player)
        {
            if(new Protocol47Fix().isAnyPlayer47(BedwarsAPI.getInstance().getGameOfPlayer(((Player) sender))))
                luckyblockEnable((Player)sender,"47");
            else
                luckyblockEnable((Player)sender,"normal");
        }
        else if(args.length==3 && args[0].equalsIgnoreCase("lucky"))
        {
            luckyblockEnable(Bukkit.getPlayer(args[2]),args[1]);
        }
        else if(args.length==1 && args[0].equalsIgnoreCase("infest") && sender instanceof Player p)
        {
            infestation.addGame(BedwarsAPI.getInstance().getGameOfPlayer(p));
        }
        else if(args.length==1 && args[0].equalsIgnoreCase("albi") && sender instanceof Player p)
        {
            Game game = BedwarsAPI.getInstance().getGameOfPlayer(p);
            int delay;
            if(!disableStats.getGameType(game).equalsIgnoreCase("bedfight"))
                delay = 2;
            else
                delay = 10;

            new BukkitRunnable()
            {
                @Override
                public void run()
                {
                    if(!disableStats.getGameType(game).equalsIgnoreCase("bedfight"))
                    {
                        albi.spawnDragon(game);
                        albi.spawnDragon(game);
                    }

                    for (Player pl:game.getConnectedPlayers())
                    {
                        albi.spawnDragon(game,pl);
                    }
                }
            }.runTaskLater(OldCommands.Invictools, delay*20);
        }
        else if(args.length == 1 && args[0].equalsIgnoreCase("nostats") && sender instanceof Player)
        {
            disableStats.singleDisable.add(BedwarsAPI.getInstance().getGameOfPlayer((Player) sender));
        }
        else if(args.length == 1 && args[0].equalsIgnoreCase("decay") && sender instanceof Player)
        {
            blockDecay.decayGamemode.add(BedwarsAPI.getInstance().getGameOfPlayer((Player) sender));
        }
        else if(args.length == 3 && args[0].equalsIgnoreCase("minutemob") && sender instanceof Player)
        {
            new mobEveryMinute(EntityType.valueOf(args[1]),BedwarsAPI.getInstance().getGameOfPlayer((Player) sender),Integer.parseInt(args[2]));
        }
        else if(args.length == 1 && args[0].equalsIgnoreCase("creativeall") && sender instanceof Player)
        {
            if(BedwarsAPI.getInstance().isPlayerPlayingAnyGame((Player) sender))
            {
                sender.sendMessage(ChatColor.RED + "Must be activated in game!");
                return true;
            }

            boolean isBedfight = disableStats.getGameType(BedwarsAPI.getInstance().getGameOfPlayer(((Player) sender))).equalsIgnoreCase("bedfight");

            for(Player p: BedwarsAPI.getInstance().getGameOfPlayer(((Player) sender)).getConnectedPlayers())
            {
                if(isBedfight)
                    new creative(60,10,p,BedwarsAPI.getInstance().getGameOfPlayer(((Player) sender)));
                else
                    new creative(180,20,p,BedwarsAPI.getInstance().getGameOfPlayer(((Player) sender)));
            }
        }
        else if(args.length == 2 && args[0].equalsIgnoreCase("creative") && sender instanceof Player)
        {
            Player p = Bukkit.getPlayer(args[1]);
            if(BedwarsAPI.getInstance().isPlayerPlayingAnyGame(p))
            {
                sender.sendMessage(ChatColor.RED + "Must be activated in game!");
                return true;
            }

            boolean isBedfight = disableStats.getGameType(BedwarsAPI.getInstance().getGameOfPlayer(p)).equalsIgnoreCase("bedfight");

                if(isBedfight)
                    new creative(60,10,p,BedwarsAPI.getInstance().getGameOfPlayer(p));
                else
                    new creative(180,20,p,BedwarsAPI.getInstance().getGameOfPlayer((p)));
        }
        else if(args.length >= 1 && args[0].equalsIgnoreCase("nofall") && sender instanceof Player p)
        {
            if(!BedwarsAPI.getInstance().isPlayerPlayingAnyGame(p))
            {
                sender.sendMessage(ChatColor.RED + "Must be activated in game!");
                return true;
            }

            nofall.addAll(BedwarsAPI.getInstance().getGameOfPlayer(((Player) sender)).getConnectedPlayers());
        }
        else if(args.length >= 1 && args[0].equalsIgnoreCase("newcombat") && sender instanceof Player p)
        {
            for (Player pl:BedwarsAPI.getInstance().getGameOfPlayer(((Player) sender)).getConnectedPlayers())
            {
                tempCombatSwap.swap(pl);
            }
        }
        else
            sender.sendMessage(ChatColor.RED+"Incomplete syntax.");

        return true;
    }

    public void luckyblockEnable(Player p,String mode)
    {
        if (BedwarsAPI.getInstance().isPlayerPlayingAnyGame(p))
        {
            int spawnerSize = 0;
            new gamemodeData().setLuckyBlockMode(BedwarsAPI.getInstance().getGameOfPlayer(p),mode);

            String game = BedwarsAPI.getInstance().getGameOfPlayer(p).getName();
            FileConfiguration config = new LobbyLogic().getMapConfiguration(game);
            // p.sendMessage(ChatColor.AQUA + "Lucky Blocks enabled in game " + ChatColor.YELLOW + game);

            while (true)
            {

                String temp = config.getString("LuckySpawners." + spawnerSize + ".x");
                if (temp != null)
                {
                    spawnerSize++;
                }
                else
                    break;
            }

            if (spawnerSize != 0)
            {
                Random rand = new Random();
                for (int a = 0; a < spawnerSize; a++)
                {
                    int delay = rand.nextInt(30) + 40;
                    Location spawnerloc = new Location(p.getWorld(), config.getDouble("LuckySpawners." + a + ".x"), config.getDouble("LuckySpawners." + a + ".y"), config.getDouble("LuckySpawners." + a + ".z"));
                    if(disableStats.getGameType(BedwarsAPI.getInstance().getGameOfPlayer(p)).equalsIgnoreCase("BedFight"))
                        new LuckyBlockSpawner(spawnerloc, "§b§lLucky Block Spawner", "random", delay/3,p);
                    else
                        new LuckyBlockSpawner(spawnerloc, "§b§lLucky Block Spawner", "random", delay,p);
                }
            }

            new BukkitRunnable()
            {
                @Override
                public void run()
                {
                    if (!BedwarsAPI.getInstance().getGameByName(game).getStatus().equals(GameStatus.RUNNING))
                    {
                        new gamemodeData().setLuckyBlockMode(BedwarsAPI.getInstance().getGameByName(game),"none");
                        this.cancel();
                    }
                }
            }.runTaskTimer(Objects.requireNonNull(Bukkit.getServer().getPluginManager().getPlugin("Invictools")), 100, 100);
        }
        else
            p.sendMessage(ChatColor.RED +"Must be in-game to enable!");
    }
}

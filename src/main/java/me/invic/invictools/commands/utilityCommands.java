package me.invic.invictools.commands;

import me.invic.invictools.gamemodes.bf.bedfight;
import me.invic.invictools.Leaderboards.statsHolo;
import me.invic.invictools.Leaderboards.statsHoloListener;
import me.invic.invictools.gamemodifiers.tempCombatSwap;
import me.invic.invictools.util.disableStats;
import me.invic.invictools.util.gui.scenSelector.perGameScenSelHolder;
import me.invic.invictools.util.ingame.blockDecay;
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
import org.screamingsandals.bedwars.api.events.BedwarsGameEndEvent;
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
                sender.sendMessage(OldCommands.permissionsError);
                return tabComplete;
            }
        }

        if(args.length == 1)
        {
            tabComplete.add("test");
            tabComplete.add("debug");
            tabComplete.add("scenq");
            tabComplete.add("endgame");
            tabComplete.add("resetbf");
            tabComplete.add("timedCombat");
            tabComplete.add("quickCombat");
            tabComplete.add("swapall");
        }
        else if(args.length==2 && args[0].equalsIgnoreCase("test"))
        {
            tabComplete.add("changeTeamSize game size");
            tabComplete.add("decay");
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
            tabComplete.add("kb");
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
        if(sender instanceof Player)
        {
            if(!sender.hasPermission("invic.invictools"))
            {
                sender.sendMessage(OldCommands.permissionsError);
                return true;
            }
        }

        if(args[0].equalsIgnoreCase("test") && args[1].equalsIgnoreCase("changeteamsize"))
        {
            new safeSizeChange().safeSizeEdit(args[2],sender,Integer.parseInt(args[3]));
        }
        else if(args[0].equalsIgnoreCase("test") && args[1].equalsIgnoreCase("decay") && sender instanceof Player p)
        {
            new blockDecay(p.getTargetBlock(null,10).getLocation(),BedwarsAPI.getInstance().getGameOfPlayer(p),3);
        }
        else if(args[0].equalsIgnoreCase("test") && args[1].equalsIgnoreCase("holo") && sender instanceof Player p)
        {
            p.sendMessage("destructing own stands via packet");
            statsHolo.sendPacket(p, statsHoloListener.activeBedfightHolos.get(p).getStands());
        }
        else if(args[0].equalsIgnoreCase("resetbf"))
        {
            new bedfight().loadBedfightInventory(args[2], Bukkit.getPlayer(args[1]), true);
            sender.sendMessage(ChatColor.AQUA + "reset bedfight inventory " + args[2] +" for " + args[1]);
        }
        else if(args[0].equalsIgnoreCase("debug"))
        {
            OldCommands.debug(sender);
        }
        else if(args[0].equalsIgnoreCase("scenq") && args.length ==2 && sender instanceof Player p)
        {
           perGameScenSelHolder.queue.get(BedwarsAPI.getInstance().getGameOfPlayer(p)).addCommand(args[1]);
        }
        else if(args[0].equalsIgnoreCase("scenq") && args.length ==1 && sender instanceof Player p)
        {
           perGameScenSelHolder.queue.get(BedwarsAPI.getInstance().getGameOfPlayer(p)).debugPrint(p);
        }
        else if(args.length == 1 && args[0].equalsIgnoreCase("endgame"))
        {
            Player p = (Player) sender;
            endgameNoStats(BedwarsAPI.getInstance().getGameOfPlayer(p));
        }
        else if(args.length >= 2 && args[0].equalsIgnoreCase("endgame"))
        {
            if(BedwarsAPI.getInstance().isGameWithNameExists(args[1]))
                endgameNoStats(BedwarsAPI.getInstance().getGameByName(args[1]));
            else if(BedwarsAPI.getInstance().isGameWithNameExists(args[0]))
                endgameNoStats(BedwarsAPI.getInstance().getGameByName(args[0]));
            else
                sender.sendMessage(ChatColor.RED +"This game does not exist");
        }
        else if(args.length == 1 && args[0].equalsIgnoreCase("quickcombat"))
        {
            for (Player p:Bukkit.getOnlinePlayers())
            {
                tempCombatSwap.swap(p);
            }
            tempCombatSwap.quickCombatConfig();
        }
        else if(args.length == 1 && args[0].equalsIgnoreCase("timedcombat"))
        {
            tempCombatSwap.timedCombatConfig();
            for (Player p:Bukkit.getOnlinePlayers())
            {
                tempCombatSwap.swap(p);
            }
        }
        else if(args.length == 1 && args[0].equalsIgnoreCase("swapall"))
        {
            for (Player p:Bukkit.getOnlinePlayers())
            {
                tempCombatSwap.swap(p);
            }
        }
        else
            sender.sendMessage(ChatColor.RED+"Incomplete command");
        return true;
    }

    public void endgameNoStats(Game game)
    {
        List<Player> players = game.getConnectedPlayers();

        disableStats.singleDisable.add(game);
        for (Player p:players)
        {
            game.leaveFromGame(p);
        }

        new BedwarsGameEndEvent(game);

        new BukkitRunnable()
        {
            @Override
            public void run()
            {
                disableStats.singleDisable.remove(game);
            }
        }.runTaskLater(OldCommands.Invictools, 40L);
    }
}

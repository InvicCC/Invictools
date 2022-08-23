package me.invic.invictools.econ;

import me.invic.invictools.Invictools;
import me.invic.invictools.commands.OldCommands;
import me.invic.invictools.util.disableStats;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.screamingsandals.bedwars.api.events.BedwarsGameEndEvent;
import org.screamingsandals.bedwars.api.events.BedwarsGameStartEvent;
import org.screamingsandals.bedwars.api.events.BedwarsPlayerLeaveEvent;
import org.screamingsandals.bedwars.api.game.Game;
import org.screamingsandals.bedwars.api.game.GameStatus;

import java.util.HashMap;

public class givePoints implements Listener
{
    static HashMap<Game,String> modeHolder = new HashMap<>();
    static HashMap<String,Integer> types = new HashMap<>();
    static HashMap<Player,Integer> totals = new HashMap<>();

    @EventHandler
    public void join(BedwarsGameStartEvent e)
    {
        if(!modeHolder.containsKey(e.getGame()))
            modeHolder.put(e.getGame(),disableStats.getGameType(e.getGame()));
    }

    @EventHandler
    public void end(BedwarsPlayerLeaveEvent e)
    {
        if(totals.getOrDefault(e.getPlayer(),0)==0)
            return;

        Player p = e.getPlayer();
        p.playSound(p, Sound.BLOCK_NOTE_BLOCK_BELL,1,1);
        p.sendMessage(ChatColor.AQUA/*+" "+ChatColor.BOLD+"(!)"*/+"You earned "+ChatColor.WHITE+totals.get(p)+ChatColor.AQUA+" Invictacoins that round.");

        totals.remove(p);
    }

    public givePoints()
    {
        FileConfiguration config = OldCommands.Invictools.getConfig();
        types.put("BedwarsKill",config.getInt("points.BedwarsKill",30));
        types.put("BedwarsFinalKill",config.getInt("points.BedwarsFinalKill",75));
        types.put("BedwarsBedBreak",config.getInt("points.BedwarsBedBreak",120));
        types.put("BedwarsWin",config.getInt("points.BedwarsWin",250));
        types.put("BedwarsLoss",config.getInt("points.BedwarsLoss",50));

        types.put("BedfightKill",config.getInt("points.BedfightKill",5));
        types.put("BedfightFinalKill",config.getInt("points.BedfightFinalKill",15));
        types.put("BedfightBedBreak",config.getInt("points.BedfightBedBreak",25));
        types.put("BedfightWin",config.getInt("points.BedfightWin",50));
    }

    public givePoints(Player p, String type)
    {
        int points = types.getOrDefault(type,5);
        totals.put(p,totals.getOrDefault(p,0)+points);
        Invictools.econ.depositPlayer(p,points);
    }
}

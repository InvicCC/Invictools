package me.invic.invictools.util;

import me.invic.invictools.commands.Commands;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.screamingsandals.bedwars.api.RunningTeam;
import org.screamingsandals.bedwars.api.TeamColor;
import org.screamingsandals.bedwars.api.events.BedwarsGameEndEvent;
import org.screamingsandals.bedwars.api.events.BedwarsGameStartEvent;
import org.screamingsandals.bedwars.api.events.BedwarsPlayerKilledEvent;
import org.screamingsandals.bedwars.api.game.Game;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class GameLogger implements Listener
{

    HashMap<Game, RunningTeam> livingTeam = new HashMap<>();
    HashMap<Game, RunningTeam> teamWon = new HashMap<>();

    HashMap<Player, Integer> playerDeaths = new HashMap<>();
    HashMap<Player, Integer> playerKills = new HashMap<>();

    @EventHandler
    public void gameStart(BedwarsGameStartEvent e) throws IOException
    {
        File bedfightLog = new File(Commands.Invictools.getDataFolder(), "game.log");
        BufferedWriter br = new BufferedWriter(new FileWriter(bedfightLog.getAbsolutePath(), true));
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        new disableStats();
        String gameType = disableStats.getGameType(e.getGame());
        br.write("[" + dateFormat.format(date) + "] Game started");
        br.newLine();
        br.write("-----");
        br.newLine();
        br.write("- Game type: " + gameType);
        br.newLine();
        br.write("- Game map: " + e.getGame().getName());
        br.newLine();
        br.write("- Player count: " + e.getGame().getConnectedPlayers().size());
        br.newLine();
        br.write("- Teams: ");
        br.newLine();
        for (RunningTeam r : e.getGame().getRunningTeams())
        {
            br.write("-- " + r.getColor().toString() + ":");
            br.newLine();
            for (Player p : r.getConnectedPlayers())
            {
                br.write("--- " + p.getName());
                br.newLine();
            }
        }
        br.write("-----");
        br.newLine();
        br.close();
    }


    @EventHandler
    public void gameEnd(BedwarsGameEndEvent e) throws IOException
    {
        File bedfightLog = new File(Commands.Invictools.getDataFolder(), "game.log");
        BufferedWriter br = new BufferedWriter(new FileWriter(bedfightLog.getAbsolutePath(), true));
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        new disableStats();
        String gameType = disableStats.getGameType(e.getGame());
        br.write("[" + dateFormat.format(date) + "] Game finished\n-----");
        br.newLine();
        br.write("- Game type: " + gameType);
        br.newLine();
        br.write("- Game map: " + e.getGame().getName());
        br.newLine();
        br.write("- Player count: " + e.getGame().getConnectedPlayers().size());
        br.newLine();
        br.write("- Winners: " + teamWon.get(e.getGame()).getColor().toString());
        br.newLine();
        for (Player p : teamWon.get(e.getGame()).getConnectedPlayers())
        {
            br.write("-- " + p.getName() + ":");
            br.newLine();
            if (playerDeaths.containsKey(p))
            {
                br.write("--- Player Deaths:" + playerDeaths.get(p));
                br.newLine();
            }
            if (playerKills.containsKey(p))
            {
                br.write("--- Player Kills:" + playerKills.get(p));
                br.newLine();
            }
        }
        br.write("- Losers: ");
        br.newLine();
        for (Player p : e.getGame().getConnectedPlayers())
        {

            if (e.getGame().getTeamOfPlayer(p) != teamWon)
            {
                br.write("-- " + p.getName() + ":");
                br.newLine();
                if (playerDeaths.containsKey(p))
                {
                    br.write("--- Player Deaths:" + playerDeaths.get(p));
                    br.newLine();
                }
                if (playerKills.containsKey(p))
                {
                    br.write("--- Player Kills:" + playerKills.get(p));
                    br.newLine();
                }
            }
        }
        br.write("-----");
        br.newLine();
        br.newLine();
        br.close();
    }

    @EventHandler
    public void bwdeath(BedwarsPlayerKilledEvent e)
    {
        int AliveTeams = e.getGame().getRunningTeams().size();

        if (playerDeaths.containsKey(e.getPlayer()))
        {
            playerDeaths.put(e.getPlayer(), playerDeaths.get(e.getPlayer()) + 1);
        }
        else
        {
            playerDeaths.put(e.getPlayer(), 1);
        }

        if (e.getKiller() != null)
        {
            if (playerKills.containsKey(e.getKiller().getPlayer()))
            {
                playerKills.put(e.getKiller().getPlayer(), playerKills.get(e.getKiller().getPlayer()) + 1);
            }
            else
            {
                playerKills.put(e.getKiller().getPlayer(), 1);
            }
        }

        for (RunningTeam team : e.getGame().getRunningTeams())
        {
            if (team.isDead())
                AliveTeams--;
            else
                livingTeam.put(e.getGame(), team);
        }

        if (AliveTeams == 1)
        {
            teamWon.put(e.getGame(), livingTeam.get(e.getGame()));
        }
    }
}

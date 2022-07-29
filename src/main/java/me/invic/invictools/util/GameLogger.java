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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;

public class GameLogger implements Listener
{

    // [time] [bedwars / bedfight] [map] [playercount] Winners: [name] [kills]K [deaths]D Losers: [name] [kills]K [deaths]D

    public static void sendLog(boolean action, String gameType, String map, int playerCount, String winner, int winnerKills, int winnerDeaths, Player loser, int loserKills, int loserDeaths) throws IOException
    {
        File bedfightLog = new File(Commands.Invictools.getDataFolder(), "bedfight.log");
        BufferedWriter br = new BufferedWriter(new FileWriter(bedfightLog.getAbsolutePath()));
        if (gameEnd)
        {
            br.write("[" + Calendar.DATE + "][" + gameType + "][" + map + "]" + "][" + playerCount + "]");
        }
        else
        {
            br.write("");
        }
        br.newLine();
        br.close();
    }

    RunningTeam livingTeam = null;
    RunningTeam teamWon = null;

    HashMap<TeamColor, Integer> teamDeaths = new HashMap<>();
    HashMap<TeamColor, Integer> teamKills = new HashMap<>();

    @EventHandler
    public void gameStart(BedwarsGameStartEvent e)
    {
        new disableStats();
        disableStats.getGameType(e.getGame());
        sendLog();
    }

    @EventHandler
    public void gameEnd(BedwarsGameEndEvent e)
    {
        new disableStats();
        String gameType = disableStats.getGameType(e.getGame());
        sendLog(true, gameType, e.getGame().getGameWorld().getName(), e.getGame().getConnectedPlayers().size(), teamWon.getColor().toString(), teamKills.get(teamWon.getColor()), teamDeaths.get(teamWon.getColor()), );
    }

    @EventHandler
    public void bwdeath(BedwarsPlayerKilledEvent e)
    {
        int AliveTeams = e.getGame().getRunningTeams().size();

        if (teamDeaths.containsKey(e.getGame().getTeamOfPlayer(e.getPlayer()).getColor()))
        {
            teamDeaths.put(e.getGame().getTeamOfPlayer(e.getPlayer()).getColor(), teamDeaths.get(e.getGame().getTeamOfPlayer(e.getPlayer()).getColor()) + 1);
        }
        else
        {
            teamDeaths.put(e.getGame().getTeamOfPlayer(e.getPlayer()).getColor(), 1);
        }

        if (teamKills.containsKey(e.getGame().getTeamOfPlayer(e.getKiller().getPlayer()).getColor()))
        {
            teamKills.put(e.getGame().getTeamOfPlayer(e.getKiller().getPlayer()).getColor(), teamDeaths.get(e.getGame().getTeamOfPlayer(e.getKiller().getPlayer()).getColor()) + 1);
        }
        else
        {
            teamDeaths.put(e.getGame().getTeamOfPlayer(e.getKiller().getPlayer()).getColor(), 1);
        }

        for (RunningTeam team : e.getGame().getRunningTeams())
        {
            if (team.isDead())
                AliveTeams--;
            else
                livingTeam = team;
        }

        if (AliveTeams == 1)
        {
            teamWon = livingTeam;
        }
    }
}

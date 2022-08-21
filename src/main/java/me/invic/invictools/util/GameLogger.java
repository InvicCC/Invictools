package me.invic.invictools.util;

import me.invic.invictools.commands.OldCommands;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.screamingsandals.bedwars.api.RunningTeam;
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
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;

public class GameLogger implements Listener
{

    HashMap<Game, RunningTeam> livingTeam = new HashMap<>();
    HashMap<Game, RunningTeam> teamWon = new HashMap<>();
    HashMap<Game, Long> startTime = new HashMap<>();
    HashMap<Player, Integer> playerDeaths = new HashMap<>();
    HashMap<Player, Integer> playerKills = new HashMap<>();


    @EventHandler
    public void gameStart(BedwarsGameStartEvent e)
    {
        startTime.put(e.getGame(), Instant.now().getEpochSecond());
    }

    @EventHandler
    public void gameEnd(BedwarsGameEndEvent e) throws IOException
    {
        File bedfightLog = new File(OldCommands.Invictools.getDataFolder(), "game.log");
        BufferedWriter br = new BufferedWriter(new FileWriter(bedfightLog.getAbsolutePath(), true));
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        String gameType = disableStats.getGameType(e.getGame());
        long t1 = startTime.get(e.getGame());
        long t2 = Instant.now().getEpochSecond();
        long minutes = Math.abs(t1 - t2) / 60;
        long seconds = Math.abs(t1 - t2);
        br.write("[" + dateFormat.format(date) + "] Game finished:");
        br.newLine();
        br.write("  GameType: " + gameType);
        br.newLine();
        br.write("  Map: " + e.getGame().getName());
        br.newLine();
        if(disableStats.singleDisable.contains(e.getGame()))
            br.write("  StatsTrack: " + "ManualDisable");
        else if(!OldCommands.StatsTrack)
            br.write("  StatsTrack: " + "GlobalDisable");
        else
            br.write("  StatsTrack: " + "PossiblyTracked");
        br.newLine();
        br.write("  Players: " + e.getGame().getConnectedPlayers().size());
        br.newLine();
        br.write("  FinalTime: " + minutes + "m " + (seconds - minutes * 60) + "s");
        br.newLine();
        br.write("  Winners: ");
        br.newLine();
        for (Player p : teamWon.get(e.getGame()).getConnectedPlayers())
        {
            br.write("    " + teamWon.get(e.getGame()).getName() + ":");
            br.newLine();
            br.write("     " + p.getName() + ":");
            br.newLine();
            if (playerDeaths.containsKey(p))
            {
                br.write("      Deaths: " + playerDeaths.get(p));
                br.newLine();
            }
            if (playerKills.containsKey(p))
            {
                br.write("      Kills: " + playerKills.get(p));
                br.newLine();
            }
        }
        br.write("  Losers: ");
        br.newLine();


        for (RunningTeam r : e.getGame().getRunningTeams())
        {
            if (r != teamWon.get(e.getGame()))
            {
                br.write("    " + r.getName() + ":");
                br.newLine();
                for (Player p : e.getGame().getConnectedPlayers())
                {
                    if (e.getGame().getTeamOfPlayer(p) != teamWon.get(e.getGame()))
                    {
                        br.write("     " + p.getName() + ":");
                        br.newLine();
                        if (playerDeaths.containsKey(p))
                        {
                            br.write("      Deaths: " + playerDeaths.get(p));
                            br.newLine();
                        }
                        if (playerKills.containsKey(p))
                        {
                            br.write("      Kills: " + playerKills.get(p));
                            br.newLine();
                        }
                    }
                }
            }
        }
        br.newLine();
        br.close();
        livingTeam.remove(e.getGame());
        teamWon.remove(e.getGame());
        startTime.remove(e.getGame());
        for (Player p : e.getGame().getConnectedPlayers())
        {
            playerDeaths.remove(p);
            playerKills.remove(p);
        }
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

package me.invic.invictools.gamemodes;

import me.invic.invictools.Commands;
import me.invic.invictools.cosmetics.finalkills.FinalKillListener;
import me.invic.invictools.util.GrabTeammates;
import me.invic.invictools.util.disableStats;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.screamingsandals.bedwars.api.RunningTeam;
import org.screamingsandals.bedwars.api.events.BedwarsGameEndingEvent;
import org.screamingsandals.bedwars.api.events.BedwarsGameStartEvent;
import org.screamingsandals.bedwars.api.events.BedwarsPlayerKilledEvent;
import org.screamingsandals.bedwars.api.events.BedwarsTargetBlockDestroyedEvent;
import org.screamingsandals.bedwars.api.game.Game;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class bedfightStatistics implements Listener
{
    File Folder = new File(Commands.Invictools.getDataFolder(), "Bedfight");

    static HashMap<UUID,Integer> finalDeath = new HashMap<>();
    static HashMap<UUID,Integer> finalKill = new HashMap<>();
    static HashMap<UUID,Integer> normalDeath = new HashMap<>();
    static HashMap<UUID,Integer> normalKill = new HashMap<>();
    static HashMap<UUID,Integer> Win = new HashMap<>();
    static HashMap<UUID,Integer> Loss = new HashMap<>();
    static HashMap<UUID,Integer> BedBreak = new HashMap<>();
    static HashMap<UUID,Game> Game = new HashMap<>();

    @EventHandler
    public void bwdeath(BedwarsPlayerKilledEvent e)
    {
        if(!disableStats.getGameType(e.getGame()).equalsIgnoreCase("bedfight") || !Commands.StatsTrack)
            return;

        if ((e.getKiller() != null))
        {
            if (e.getGame().getTeamOfPlayer(e.getPlayer()) == null) // team eliminated
            {
                finallogic(e.getKiller(), e.getPlayer());
            }
            else if (FinalKillListener.sizes.get(e.getGame().getName()+"_"+e.getGame().getTeamOfPlayer(e.getPlayer()).getName()) != GrabTeammates.getTeamSize(e.getPlayer())) // team size decreased
            {
                FinalKillListener.sizes.put(e.getGame().getName()+"_"+e.getGame().getTeamOfPlayer(e.getPlayer()).getName(), GrabTeammates.getTeamSize(e.getPlayer()));
                finallogic(e.getKiller(), e.getPlayer());
            }
            else // regular kill with valid killer, hopefully
                normalLogic(e.getKiller(),e.getPlayer());
        }
        else
        {
            if (e.getGame().getTeamOfPlayer(e.getPlayer()) == null) // team eliminated
            {
                bedfightFinalDeath(e.getPlayer());
            }
            else if (FinalKillListener.sizes.get(e.getGame().getName()+"_"+e.getGame().getTeamOfPlayer(e.getPlayer()).getName()) != GrabTeammates.getTeamSize(e.getPlayer())) // team size decreased
            {
                FinalKillListener.sizes.put(e.getGame().getName()+"_"+e.getGame().getTeamOfPlayer(e.getPlayer()).getName(), GrabTeammates.getTeamSize(e.getPlayer()));
                bedfightFinalDeath(e.getPlayer());
            }
            else
                bedfightNormalDeath(e.getPlayer());
        }

        RunningTeam livingTeam = null;
        int AliveTeams = e.getGame().getRunningTeams().size();
        for (RunningTeam team : e.getGame().getRunningTeams())
        {
            if (team.isDead())
                AliveTeams--;
            else
                livingTeam = team;
        }

        if (AliveTeams == 1)
        {
            for (Player p : livingTeam.getConnectedPlayers())
            {
                bedfightWin(p);
            }
        }
    }

    @EventHandler
    public void bedbreak(BedwarsTargetBlockDestroyedEvent e)
    {
        if(!disableStats.getGameType(e.getGame()).equalsIgnoreCase("bedfight") || !Commands.StatsTrack)
            return;

        bedfightBedBreak(e.getPlayer());
    }

    @EventHandler
    public void start(BedwarsGameStartEvent e)
    {
        if(!disableStats.getGameType(e.getGame()).equalsIgnoreCase("bedfight") || !Commands.StatsTrack)
            return;

        File file = new File(Folder,"bedfightstats.yml");
        FileConfiguration stats = YamlConfiguration.loadConfiguration(file);
        for (Player p:e.getGame().getConnectedPlayers())
        {
            finalKill.put(p.getUniqueId(),stats.getInt("data."+p.getUniqueId()+".FinalKills"));
            finalDeath.put(p.getUniqueId(),stats.getInt("data."+p.getUniqueId()+".FinalDeaths"));
            normalKill.put(p.getUniqueId(),stats.getInt("data."+p.getUniqueId()+".NormalKills"));
            normalDeath.put(p.getUniqueId(),stats.getInt("data."+p.getUniqueId()+".NormalDeaths"));
            BedBreak.put(p.getUniqueId(),stats.getInt("data."+p.getUniqueId()+".BedBreaks"));
            Win.put(p.getUniqueId(),stats.getInt("data."+p.getUniqueId()+".Wins"));
            Loss.put(p.getUniqueId(),stats.getInt("data."+p.getUniqueId()+".Losses"));
            Game.put(p.getUniqueId(),e.getGame());
        }
    }
    
    @EventHandler
    public void saveStats(BedwarsGameEndingEvent e)
    {
        if(!disableStats.getGameType(e.getGame()).equalsIgnoreCase("bedfight") || !Commands.StatsTrack)
            return;

        File file = new File(Folder,"bedfightstats.yml");
        FileConfiguration stats = YamlConfiguration.loadConfiguration(file);

        List<UUID> remove = new ArrayList<>();
        for (UUID uuid:Game.keySet())
        {
           // System.out.println(uuid);
            if(Game.get(uuid).equals(e.getGame()))
            {
                stats.set("data."+uuid+".FinalKills",finalKill.get(uuid));
                stats.set("data."+uuid+".FinalDeaths",finalDeath.get(uuid));
                stats.set("data."+uuid+".NormalKills",normalKill.get(uuid));
                stats.set("data."+uuid+".NormalDeaths",normalDeath.get(uuid));
                stats.set("data."+uuid+".Wins",Win.get(uuid));
                stats.set("data."+uuid+".Losses",Loss.get(uuid));
                stats.set("data."+uuid+".BedBreaks",BedBreak.get(uuid));
                stats.set("data."+uuid+".Username",Bukkit.getOfflinePlayer(uuid).getName());
                finalKill.remove(uuid);
                finalDeath.remove(uuid);
                normalKill.remove(uuid);
                normalDeath.remove(uuid);
                Win.remove(uuid);
                Loss.remove(uuid);
                BedBreak.remove(uuid);
                remove.add(uuid);
            }
        }

        for (UUID uuid:remove)
        {
            Game.remove(uuid);
        }

        try
        {
            stats.save(file);
        }
        catch (IOException a)
        {
            a.printStackTrace();
        }
    }

    private void finallogic(Player k, Player p)
    {
        bedfightFinalDeath(p);
        bedfightFinalKill(k);
        bedfightLoss(p);
    }

    private void normalLogic(Player k, Player p)
    {
        bedfightNormalKill(k);
        bedfightNormalDeath(p);
    }

    private void bedfightFinalKill(Player p)
    {
        finalKill.put(p.getUniqueId(),finalKill.get(p.getUniqueId())+1);
    }

    private void bedfightFinalDeath(Player p)
    {
        finalDeath.put(p.getUniqueId(),finalDeath.get(p.getUniqueId())+1);
    }

    private void bedfightNormalKill(Player p)
    {
        normalKill.put(p.getUniqueId(),normalKill.get(p.getUniqueId())+1);
    }

    private void bedfightNormalDeath(Player p)
    {
        normalDeath.put(p.getUniqueId(),normalDeath.get(p.getUniqueId())+1);
    }

    private void bedfightWin(Player p)
    {
        Win.put(p.getUniqueId(),Win.get(p.getUniqueId())+1);
    }

    private void bedfightLoss(Player p)
    {
        Loss.put(p.getUniqueId(),Loss.get(p.getUniqueId())+1);
    }

    private void bedfightBedBreak(Player p)
    {
        BedBreak.put(p.getUniqueId(),BedBreak.get(p.getUniqueId())+1);
    }
}

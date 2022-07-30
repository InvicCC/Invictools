package cc.invic.invictools.cosmetics.finalkills;

import cc.invic.invictools.cosmetics.NormalKillHandler;
import cc.invic.invictools.impl.Commands;
import cc.invic.invictools.util.GrabTeammates;
import cc.invic.invictools.util.disableStats;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.bedwars.api.BedwarsAPI;
import org.screamingsandals.bedwars.api.events.BedwarsGameEndingEvent;
import org.screamingsandals.bedwars.api.events.BedwarsGameStartedEvent;
import org.screamingsandals.bedwars.api.events.BedwarsPlayerKilledEvent;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class FinalKillListener implements Listener
{
    BedwarsAPI api = BedwarsAPI.getInstance();
    public static HashMap<String, Integer> sizes = new HashMap<>();

    Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("Invictools");
    File Folder = new File(plugin.getDataFolder(), "PlayerData");

    @EventHandler
    public void bwdeath(BedwarsPlayerKilledEvent e)
    {
        if ((e.getKiller() != null))
        {
            if (api.getGameOfPlayer(e.getPlayer()).getTeamOfPlayer(e.getPlayer()) == null) // team eliminated
                logic(e.getKiller(), e.getPlayer());
            else if (sizes.get(e.getGame().getName()+"_"+e.getGame().getTeamOfPlayer(e.getPlayer()).getName()) != GrabTeammates.getTeamSize(e.getPlayer())) // team size decreased
            {
                sizes.put(e.getGame().getName()+"_"+e.getGame().getTeamOfPlayer(e.getPlayer()).getName(), GrabTeammates.getTeamSize(e.getPlayer()));
                logic(e.getKiller(), e.getPlayer());
            }
            else // regular kill with valid killer, hopefully
                new NormalKillHandler().grabEffect(e.getKiller(), e.getPlayer(), e.getPlayer().getLocation());
        }
        else
        {
            if (api.getGameOfPlayer(e.getPlayer()).getTeamOfPlayer(e.getPlayer()) == null) // team eliminated
            {
                addFinalDeath(e.getPlayer());
            }
            else if (sizes.get(e.getGame().getName()+"_"+e.getGame().getTeamOfPlayer(e.getPlayer()).getName()) != GrabTeammates.getTeamSize(e.getPlayer())) // team size decreased
            {
                sizes.put(e.getGame().getName()+"_"+e.getGame().getTeamOfPlayer(e.getPlayer()).getName(), GrabTeammates.getTeamSize(e.getPlayer()));
                addFinalDeath(e.getPlayer());
            }
        }
    }

    private void logic(Player k, Player p)
    {
        new FinalKillHandler().grabEffect(k, p, p.getLocation());
        new FinalKillListener().addFinalKill(k);
        new FinalKillListener().addFinalDeath(p);
    }


    private void addFinalKill(Player k)
    {
        if (!Commands.StatsTrack || !disableStats.shouldTrack(k))
            return;

        File pFile = new File(Folder, k.getUniqueId() + ".yml");
        final FileConfiguration playerData = YamlConfiguration.loadConfiguration(pFile);
        if (!playerData.contains("FinalKills"))
            playerData.set("FinalKills", 1);
        else
        {
            int finals = playerData.getInt("FinalKills");
            playerData.set("FinalKills", finals + 1);
        }

        try
        {
            playerData.save(pFile);
        }
        catch (IOException a)
        {
            a.printStackTrace();
        }
    }

    private void addFinalDeath(Player p)
    {
        if (!Commands.StatsTrack || !disableStats.shouldTrack(p))
            return;

        File pFile2 = new File(Folder, p.getUniqueId() + ".yml");
        final FileConfiguration playerData2 = YamlConfiguration.loadConfiguration(pFile2);
        if (!playerData2.contains("FinalDeaths"))
            playerData2.set("FinalDeaths", 1);
        else
        {
            int finals = playerData2.getInt("FinalDeaths");
            playerData2.set("FinalDeaths", finals + 1);
        }

        try
        {
            playerData2.save(pFile2);
        }
        catch (IOException a)
        {
            a.printStackTrace();
        }

    }

    @EventHandler
    public void bwstart(BedwarsGameStartedEvent e)
    {
        for (Player p : e.getGame().getConnectedPlayers())
        {
            sizes.put(e.getGame().getName()+"_"+api.getGameOfPlayer(p).getTeamOfPlayer(p).getName(), GrabTeammates.getTeamSize(p));
        }
    }

    @EventHandler
    public void bwend(BedwarsGameEndingEvent e)
    {
        HashMap<String, Integer> sizesReduced = new HashMap<>(sizes);
        for (String s:sizes.keySet())
        {
            String[] split = s.split("_");
            if(split[0].equalsIgnoreCase(e.getGame().getName()))
                sizesReduced.remove(s);
        }
        sizes = new HashMap<>(sizesReduced); // dawg idek
    }
}

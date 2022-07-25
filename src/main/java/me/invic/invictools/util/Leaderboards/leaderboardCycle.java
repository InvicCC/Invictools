package me.invic.invictools.util.Leaderboards;

import me.invic.invictools.commands.Commands;
import me.invic.invictools.gamemodifiers.WardenSpawner;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class leaderboardCycle implements Listener
{
    public static int ticker = 0;
    public static int tickerBF = 0;
    public static HashMap<Player, Long> HoloSwapCooldown = new HashMap<>();
    int cooldown = 1; // seconds

    FileConfiguration config = Commands.Invictools.getConfig();
    Location locBW = new WardenSpawner().locationFromConfig(config.getString("Leaderboard.Holo"));
    Location locBF = new WardenSpawner().locationFromConfig(config.getString("BedfightLeaderboard.Holo"));

    @EventHandler
    public void rightClick(PlayerInteractAtEntityEvent e)
    {
        if (e.getRightClicked() instanceof ArmorStand && e.getPlayer().getWorld().getName().equalsIgnoreCase("bwlobby"))
        {
            Player p = e.getPlayer();
            boolean direction = p.isSneaking();

            if (p.getLocation().distance(locBW) < 6)
            {
                if (HoloSwapCooldown.containsKey(p))
                {
                    long secondsLeft = ((HoloSwapCooldown.get(p) / 1000) + cooldown) - (System.currentTimeMillis() / 1000);
                    if (secondsLeft <= 0)
                        doSwap(p, direction);
                }
                else
                    doSwap(p, direction);
            }
            else if(p.getLocation().distance(locBF) < 6)
            {
                if (HoloSwapCooldown.containsKey(p))
                {
                    long secondsLeft = ((HoloSwapCooldown.get(p) / 1000) + cooldown) - (System.currentTimeMillis() / 1000);
                    if (secondsLeft <= 0)
                        doSwapBF(p, direction);
                }
                else
                    doSwapBF(p, direction);
            }
        }
    }

    @EventHandler
    public void disconnect(PlayerQuitEvent e)
    {
        saveDisplayName(e.getPlayer());
    }

    public void saveDisplayName(Player p)
    {
        File Folder = new File(Commands.Invictools.getDataFolder(), "PlayerData");
        File pFile = new File(Folder, p.getUniqueId() + ".yml");
        final FileConfiguration playerData = YamlConfiguration.loadConfiguration(pFile);
        playerData.set("Displayname", p.getDisplayName());
        // System.out.println(p.getDisplayName());

        try
        {
            playerData.save(pFile);
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }
    }

    private void doSwap(Player p, boolean direction)
    {
        HoloSwapCooldown.put(p, System.currentTimeMillis());

        if (!direction)
            ticker++;
        else
            ticker--;

        if (ticker == 11)
            ticker = 0;

        if (ticker == -1)
            ticker = 10;

        p.getLocation().getWorld().playSound(p.getLocation(), Sound.BLOCK_LEVER_CLICK, 1, 1);
        switch (ticker)
        {
            case 4:
                new leaderboard().loadLeaderboard("destroyedBeds");
                new leaderboardHologram().createLeaderboard();
                break;
            case 1:
                new leaderboard().loadLeaderboard("wins");
                new leaderboardHologram().createLeaderboard();
                break;
            case 2:
                new leaderboard().loadLeaderboard("score");
                new leaderboardHologram().createLeaderboard();
                break;
            case 3:
                new leaderboard().loadLeaderboard("kills");
                new leaderboardHologram().createLeaderboard();
                break;
            case 9:
                new leaderboard().loadLeaderboard("loses");
                new leaderboardHologram().createLeaderboard();
                break;
            case 8:
                new leaderboard().loadLeaderboard("deaths");
                new leaderboardHologram().createLeaderboard();
                break;
            case 7:
                new leaderboard().loadLeaderboard("fkdr");
                new leaderboardHologram().createLeaderboard();
                break;
            case 5:
                new leaderboard().loadLeaderboard("kdr");
                new leaderboardHologram().createLeaderboard();
                break;
            case 6:
                new leaderboard().loadLeaderboard("wl");
                new leaderboardHologram().createLeaderboard();
                break;
            case 0:
                new leaderboard().loadLeaderboard("star");
                new leaderboardHologram().createLeaderboard();
                break;
            case 10:
                new leaderboard().loadLeaderboard("finals");
                new leaderboardHologram().createLeaderboard();
                break;
        }
    }

    private void doSwapBF(Player p, boolean direction)
    {
        HoloSwapCooldown.put(p, System.currentTimeMillis());

        if (!direction)
            tickerBF++;
        else
            tickerBF--;

        if (tickerBF == 10)
            tickerBF = 0;

        if (tickerBF == -1)
            tickerBF = 9;

        p.getLocation().getWorld().playSound(p.getLocation(), Sound.BLOCK_LEVER_CLICK, 1, 1);
        switch (tickerBF)
        {
            case 4:
                new BedfightLeaderboard().loadBFLeaderboard("BedBreaks");
                break;
            case 1:
                new BedfightLeaderboard().loadBFLeaderboard("Wins");
                break;
            case 2:
                new BedfightLeaderboard().loadBFLeaderboard("Losses");
                break;
            case 3:
                new BedfightLeaderboard().loadBFLeaderboard("FinalKills");
                break;
            case 9:
                new BedfightLeaderboard().loadBFLeaderboard("FinalDeaths");
                break;
            case 8:
                new BedfightLeaderboard().loadBFLeaderboard("NormalKills");
                break;
            case 7:
                new BedfightLeaderboard().loadBFLeaderboard("NormalDeaths");
                break;
            case 5:
                new BedfightLeaderboard().loadBFLeaderboard("kdr");
                break;
            case 6:
                new BedfightLeaderboard().loadBFLeaderboard("wl");
                break;
            case 0:
                new BedfightLeaderboard().loadBFLeaderboard("score");
                break;
        }
        new BedfightLeaderboardHologram().createBFLeaderboard();
    }
}

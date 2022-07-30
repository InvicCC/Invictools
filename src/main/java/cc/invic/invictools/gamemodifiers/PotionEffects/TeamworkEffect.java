package cc.invic.invictools.gamemodifiers.PotionEffects;

import cc.invic.invictools.util.GrabTeammates;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.screamingsandals.bedwars.api.BedwarsAPI;
import org.screamingsandals.bedwars.api.events.BedwarsPlayerKilledEvent;
import org.screamingsandals.bedwars.api.game.Game;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class TeamworkEffect implements Listener
{
    BedwarsAPI api = BedwarsAPI.getInstance();

    public static String effect1;
    public static String effect2;
    public static int amp1;
    public static int amp2;
    public static boolean gameongoing = true;
    public static Game game;

    public static List<Player> players1 = new ArrayList<>();
    public static List<Player> players2 = new ArrayList<>();

    public void createEffect(String e1, String e2, Game currentGame, int a1, int a2)
    {
        effect1 = e1.toUpperCase(Locale.ROOT);
        effect2 = e2.toUpperCase(Locale.ROOT);
        amp1 = a1;
        amp2 = a2;
        game = currentGame;
        gameongoing = true;
        players2.clear();
        players1.clear();

        for (Player p : Bukkit.getOnlinePlayers())
        {
            if (api.isPlayerPlayingAnyGame(p))
            {
                if (api.getGameOfPlayer(p).equals(currentGame) && GrabTeammates.getTeamSize(p) > 1 && !players1.contains(p) && !players2.contains(p))
                {
                    Player team = GrabTeammates.getTeammates(p).get(0);
                    double dist1 = p.getLocation().distance(new Location(p.getWorld(), 0, 100, 0));
                    double dist2 = team.getLocation().distance(new Location(team.getWorld(), 0, 100, 0));

                    if (dist1 < dist2)
                    {
                        players1.add(p);
                        players2.add(team);
                        p.addPotionEffect(new PotionEffect(PotionEffectType.getByName(effect1), 1000000, amp1 - 1, false, false));
                        team.addPotionEffect(new PotionEffect(PotionEffectType.getByName(effect2), 1000000, amp2 - 1, false, false));
                    }
                    else
                    {
                        players1.add(team);
                        players2.add(p);
                        team.addPotionEffect(new PotionEffect(PotionEffectType.getByName(effect1), 1000000, amp1 - 1, false, false));
                        p.addPotionEffect(new PotionEffect(PotionEffectType.getByName(effect2), 1000000, amp2 - 1, false, false));
                    }
                }
            }
        }
    }

    @EventHandler
    public void deathEvent(BedwarsPlayerKilledEvent e)
    {
        BukkitRunnable runnable = new BukkitRunnable()
        {
            @Override
            public void run()
            {
                if (!gameongoing)
                    this.cancel();

                if (e.getPlayer().getGameMode().equals(GameMode.SURVIVAL) && gameongoing)
                {
                    if (players1.contains(e.getPlayer()))
                    {
                        e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.getByName(effect1), 1000000, amp1 - 1, false, false));
                        System.out.println("added e1");
                    }
                    else if (players2.contains(e.getPlayer()))
                    {
                        e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.getByName(effect2), 1000000, amp2 - 1, false, false));
                        System.out.println("added e2");
                    }
                }
            }
        };
        runnable.runTaskLater(Objects.requireNonNull(Bukkit.getServer().getPluginManager().getPlugin("Invictools")), (20 * 5 + 1));
    }
}

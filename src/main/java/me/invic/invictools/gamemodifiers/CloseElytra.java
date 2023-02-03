package me.invic.invictools.gamemodifiers;

import com.sk89q.worldedit.history.change.Change;
import me.invic.invictools.commands.OldCommands;
import me.invic.invictools.gamemodifiers.PotionEffects.DamageTeammates;
import me.invic.invictools.util.GrabTeammates;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.screamingsandals.bedwars.api.BedwarsAPI;
import org.screamingsandals.bedwars.api.game.Game;

import java.util.List;
import java.util.Objects;

public class CloseElytra
{
    public CloseElytra(double distance, Player player, ItemStack item)
    {
        ItemStack Changeable = player.getInventory().getItem(38);
        List<Player> teammates = GrabTeammates.getTeammates(player);
        List<Player> unnullableteammates = teammates;

        if (teammates == null)
            return;

        if (teammates.size() == 0)
            return;

        Game game = BedwarsAPI.getInstance().getGameOfPlayer(player);

        new BukkitRunnable()
        {
            boolean elytra = false;
            int counter = 0;
            int pl = 0;

            @Override
            public void run()
            {
                if (BedwarsAPI.getInstance().isPlayerPlayingAnyGame(player) && BedwarsAPI.getInstance().getGameOfPlayer(player) == game)
                {
                    for (Player p : teammates)
                    {
                        if (p == null)
                        {
                            teammates.remove(unnullableteammates.get(pl));
                        }
                        else
                        {
                            if (DamageTeammates.withinDistance(player, p, distance) && !p.getGameMode().equals(GameMode.SPECTATOR))
                            {
                                player.getInventory().setItem(38, item);
                                elytra = true;
                            }
                            else
                            {
                                counter++;
                            }

                            if (counter == teammates.size())
                                elytra = false;
                        }
                    }

                    pl++;
                    counter = 0;

                    if (!elytra)
                        player.getInventory().setItem(38, Changeable);
                }
                else
                {
                    OldCommands.ProximityElytra.remove(player);
                    this.cancel();
                }
            }
        }.runTaskTimer(Objects.requireNonNull(Bukkit.getServer().getPluginManager().getPlugin("Invictools")), 20L, 1L);
    }
}

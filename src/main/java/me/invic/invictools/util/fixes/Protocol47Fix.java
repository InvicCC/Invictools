package me.invic.invictools.util.fixes;

import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.api.ViaAPI;
import me.invic.invictools.commands.OldCommands;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LightningStrike;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityPotionEffectEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.weather.LightningStrikeEvent;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.screamingsandals.bedwars.api.game.Game;

import java.util.ArrayList;
import java.util.List;

import static me.invic.invictools.items.ItemListener.Falling;

public class Protocol47Fix implements Listener
{
    @EventHandler
    public void FatigueTrap(EntityPotionEffectEvent e)
    {
        if (e.getEntity().getType() != EntityType.PLAYER)
            return;

        if(e.getNewEffect() ==null)
            return;

        if(e.getNewEffect().getType().equals(PotionEffectType.SLOW_FALLING) && Via.getAPI().getPlayerVersion(e.getEntity()) == 47)
        {
            Falling.add(e.getEntity());
            new BukkitRunnable()
            {
                @Override
                public void run()
                {
                    Falling.remove(e.getEntity());
                }
            }.runTaskLater(OldCommands.Invictools, e.getNewEffect().getDuration()* 20L);
        }
    }

    ViaAPI api = Via.getPlatform().getApi();
    @EventHandler
    public void joinevent(PlayerJoinEvent e)
    {

        new BukkitRunnable()
        {
            @Override
            public void run()
            {
                Player p = e.getPlayer();
                if(api.getPlayerVersion(e.getPlayer()) == 47)
                {
                    p.sendTitle(ChatColor.translateAlternateColorCodes('&', "&c" + "1.8 Causes Bugs"), ChatColor.translateAlternateColorCodes('&', "&r&fUse 1.19 for a better experience"), 10, 5 * 20, 15);
                    p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1F);
                }
            }
        }.runTaskLater(OldCommands.Invictools, 300L);
    }

    public boolean isAnyPlayer47(Game game)
    {
        for (Player p:game.getConnectedPlayers())
        {
            if(api.getPlayerVersion(p) == 47)
                return true;
        }
        return false;
    }

    public boolean isEveryPlayer47(Game game)
    {
        for (Player p:game.getConnectedPlayers())
        {
            if(api.getPlayerVersion(p) != 47)
                return false;
        }
        return true;
    }

    public boolean isAnyLivingPlayer47(Game game)
    {
        for (Player p:game.getConnectedPlayers())
        {
            if(game.isPlayerInAnyTeam(p) && api.getPlayerVersion(p) == 47)
                return true;
        }
        return false;
    }

    List<Player> playersOn47(World world)
    {
        List<Player> list = new ArrayList<>();
        for (Player p: Bukkit.getOnlinePlayers())
        {
            if(api.getPlayerVersion(p) == 47 && p.getWorld().equals(world))
            {
                list.add(p);
            }
        }
        return list;
    }

    @EventHandler
    public void Lightning(LightningStrikeEvent e)
    {
        for (Player p:playersOn47(e.getWorld()))
        {
            p.playSound(e.getLightning().getLocation(),Sound.ENTITY_LIGHTNING_BOLT_THUNDER,50,1);
            p.playSound(e.getLightning().getLocation(),Sound.ENTITY_LIGHTNING_BOLT_IMPACT,2,1);
        }
    }

    @EventHandler
    public void placeEvent(BlockPlaceEvent e)
    {
        if(api.getPlayerVersion(e.getPlayer()) == 47)
        {
            e.getPlayer().playSound(e.getBlock().getLocation(),e.getBlock().getBlockData().getSoundGroup().getPlaceSound(),1,1);
        }
    }
}

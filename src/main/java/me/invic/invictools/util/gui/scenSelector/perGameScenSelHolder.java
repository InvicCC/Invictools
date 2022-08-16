package me.invic.invictools.util.gui.scenSelector;

import com.mojang.authlib.minecraft.TelemetrySession;
import me.invic.invictools.commands.OldCommands;
import me.invic.invictools.util.disableStats;
import me.invic.invictools.util.ingame.LobbyLogic;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import org.screamingsandals.bedwars.api.BedwarsAPI;
import org.screamingsandals.bedwars.api.events.BedwarsGameStartEvent;
import org.screamingsandals.bedwars.api.events.BedwarsPlayerJoinEvent;
import org.screamingsandals.bedwars.api.events.BedwarsPlayerLeaveEvent;
import org.screamingsandals.bedwars.api.game.Game;
import org.screamingsandals.bedwars.api.game.GameStatus;
import scala.concurrent.impl.FutureConvertersImpl;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import static me.invic.invictools.util.gui.scenSelector.scenSelInventoryHandle.antimode;

public class perGameScenSelHolder implements Listener
{
    static HashMap<Game,scenarioQueue> queue = new HashMap<>();
    public static HashMap<Game, Inventory> mainSelector = new HashMap<>();

    @EventHandler
    public void bwjoin(BedwarsPlayerJoinEvent e)
    {
        if(!queue.containsKey(e.getGame()))
        {
            queue.put(e.getGame(),new scenarioQueue());
            mainSelector.put(e.getGame(),new scenSelInventoryHandle().loadInventory("scensel",true));
        }
    }

    @EventHandler
    public void bwleave(BedwarsPlayerLeaveEvent e)
    {
        if(e.getGame().getConnectedPlayers().size() == 1 && e.getGame().getStatus().equals(GameStatus.WAITING)) // 0
        {
            queue.remove(e.getGame());
            mainSelector.remove(e.getGame());
        }
    }

    @EventHandler
    public void bwstart(BedwarsGameStartEvent e)
    {
        if(queue.containsKey(e.getGame()))
        {
            queue.get(e.getGame()).executeOP(e.getGame().getConnectedPlayers().get(0));
            queue.get(e.getGame()).print(e.getGame().getConnectedPlayers());
            queue.get(e.getGame()).remove();
            queue.remove(e.getGame());
            mainSelector.remove(e.getGame());
        }
    }

    @EventHandler
    public void clickMain(InventoryClickEvent e)
    {
        if(BedwarsAPI.getInstance().isPlayerPlayingAnyGame((Player) e.getWhoClicked())
                && mainSelector.containsKey(BedwarsAPI.getInstance().getGameOfPlayer((Player)e.getWhoClicked()))
                && e.getInventory().equals(mainSelector.get(BedwarsAPI.getInstance().getGameOfPlayer((Player)e.getWhoClicked()))))
        {
            e.setCancelled(true);
        }

        if(BedwarsAPI.getInstance().isPlayerPlayingAnyGame((Player) e.getWhoClicked())
                && mainSelector.containsKey(BedwarsAPI.getInstance().getGameOfPlayer((Player)e.getWhoClicked()))
                && e.getInventory().equals(mainSelector.get(BedwarsAPI.getInstance().getGameOfPlayer((Player)e.getWhoClicked())))
                && !e.getClick().isShiftClick()
                && e.getCurrentItem() != null
                && e.getCurrentItem().getItemMeta() != null
                && e.getCurrentItem().getItemMeta().getLore() != null
                && scenSelInventoryHandle.commands.containsKey(slotFromItem(e.getCurrentItem(),BedwarsAPI.getInstance().getGameOfPlayer((Player)e.getWhoClicked()))))
        {
            Game game = BedwarsAPI.getInstance().getGameOfPlayer((Player)e.getWhoClicked());

            if(checkMapPermit(game,e.getCurrentItem().getItemMeta().getLore().get(0)) && checkModePermit(game,e.getCurrentItem(), e.getCurrentItem().getItemMeta().getLore().get(0)))
            {
                if (e.getCurrentItem().getEnchantments().isEmpty())
                {
                    if(queue.get(BedwarsAPI.getInstance().getGameOfPlayer((Player) e.getWhoClicked())).loadedCommands() <= 5 || e.getWhoClicked().getName().equalsIgnoreCase("Invictable"))
                    {
                        queue.get(BedwarsAPI.getInstance().getGameOfPlayer((Player) e.getWhoClicked())).addCommand(scenSelInventoryHandle.commands.getOrDefault(slotFromItem(e.getCurrentItem(), game), "broadcast error"));
                        ((Player) e.getWhoClicked()).playSound(e.getWhoClicked().getLocation(), Sound.BLOCK_CONDUIT_ACTIVATE,1,1);
                        scenSelInventoryHandle.flipItem(e.getCurrentItem());
                    }
                    else
                    {
                        e.getWhoClicked().sendMessage(ChatColor.RED +"Too many Scenarios are already active.");
                        ((Player) e.getWhoClicked()).playSound(e.getWhoClicked().getLocation(), Sound.ENTITY_VILLAGER_NO,1,1);
                    }
                }
                else
                {
                    queue.get(BedwarsAPI.getInstance().getGameOfPlayer((Player) e.getWhoClicked())).removeCommand(scenSelInventoryHandle.commands.getOrDefault(slotFromItem(e.getCurrentItem(),game),"broadcast error"));
                    scenSelInventoryHandle.flipItem(e.getCurrentItem());
                    ((Player) e.getWhoClicked()).playSound(e.getWhoClicked().getLocation(), Sound.BLOCK_CONDUIT_DEACTIVATE,1,1);
                }

            }
            else
            {
                e.getWhoClicked().sendMessage(ChatColor.RED + "This scenario cannot be activated on this map");
                ((Player) e.getWhoClicked()).playSound(e.getWhoClicked().getLocation(), Sound.ENTITY_VILLAGER_NO,1,1);
            }
        }
    }

    int slotFromItem(ItemStack item,Game game)
    {
        if(item == null)
            return 0;

        for (int i = 0;i<mainSelector.get(game).getSize();i++)
        {
            if(mainSelector.get(game).getItem(i) != null)
                if(mainSelector.get(game).getItem(i).equals(item))
                    return i;
        }
        return 0;
    }

    boolean checkMapPermit(Game game, String s)
    {
        if(s.equalsIgnoreCase("SCULK_SHRIEKER"))
        {
            List<String> shrieks = new LobbyLogic().getMapConfiguration(game.getName()).getStringList("Shrieker");
            return !shrieks.isEmpty();
        }
        return true;
    }

    boolean checkModePermit(Game game,ItemStack item,String s)
    {
        return !antimode.getOrDefault(slotFromItem(item, game),"pass").equals(disableStats.getGameType(game));
    }
}

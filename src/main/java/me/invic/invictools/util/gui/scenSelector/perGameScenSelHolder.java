package me.invic.invictools.util.gui.scenSelector;

import me.invic.invictools.commands.OldCommands;
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

import java.io.File;
import java.util.HashMap;
import java.util.List;

public class perGameScenSelHolder implements Listener
{
    static HashMap<Game,scenarioQueue> queue = new HashMap<>();
    static HashMap<Game, Inventory> mainSelector = new HashMap<>();

    @EventHandler
    public void bwjoin(BedwarsPlayerJoinEvent e)
    {
        if(!queue.containsKey(e.getGame()))
        {
            queue.put(e.getGame(),new scenarioQueue());
            mainSelector.put(e.getGame(),new scenSelInventoryHandle().loadInventory("mainSelector"));
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
    public void bwjoin(BedwarsGameStartEvent e)
    {
        if(queue.containsKey(e.getGame()))
        {
            queue.get(e.getGame()).executeOP(e.getGame().getConnectedPlayers().get(0));
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
                && !e.getClick().isShiftClick()
                && e.getCurrentItem() != null
                && e.getCurrentItem().getItemMeta() != null
                && e.getCurrentItem().getItemMeta().getLore() != null)
        {
            Game game = BedwarsAPI.getInstance().getGameOfPlayer((Player)e.getWhoClicked());

            if(checkMapPermit(game,e.getCurrentItem().getItemMeta().getLore().get(0)) && checkModePermit(game,e.getCurrentItem().getItemMeta().getLore().get(0)))
            {
                if (e.getCurrentItem().getEnchantments().isEmpty())
                    queue.get(BedwarsAPI.getInstance().getGameOfPlayer((Player) e.getWhoClicked())).addCommand(loreToCommand(e.getCurrentItem()));
                else
                    queue.get(BedwarsAPI.getInstance().getGameOfPlayer((Player) e.getWhoClicked())).removeCommand(loreToCommand(e.getCurrentItem()));

                scenSelInventoryHandle.flipItem(e.getCurrentItem());
            }
            else
            {
                e.getWhoClicked().sendMessage(ChatColor.RED + "This scenario cannot be activated on this map");
                ((Player) e.getWhoClicked()).playSound(e.getWhoClicked().getLocation(), Sound.ENTITY_VILLAGER_NO,1,1);
            }
        }
    }

    List<String> convert;
    public perGameScenSelHolder()
    {
        File file = new File(OldCommands.Invictools.getDataFolder(), "CommandLore.yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        convert = config.getStringList("list");
    }

    String loreToCommand(ItemStack item)
    {
        //todo: fill yml file of all commands item type and command translations. "SEA_LANTERN;scen lucky"
        for (String unsplit:convert)
        {
            String[] split = unsplit.split(";");
            if(split[0].equalsIgnoreCase(item.getType().toString()))
                return split[1];
        }
        return item.getItemMeta().getLore().get(0);
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

    boolean checkModePermit(Game game,String s)
    {
        //todo check per mode blacklist
        return true;
    }
}

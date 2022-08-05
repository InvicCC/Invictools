package me.invic.invictools.util.gui;

import me.invic.invictools.commands.toggleCommands;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.screamingsandals.bedwars.api.BedwarsAPI;
import org.screamingsandals.bedwars.api.events.BedwarsPlayerJoinEvent;
import org.screamingsandals.bedwars.api.events.BedwarsPlayerLeaveEvent;
import org.screamingsandals.bedwars.api.game.Game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class gameLobbyItems implements Listener
{
    final static String selectorMsg = ChatColor.translateAlternateColorCodes('&', "&r&7Right click to select your team!");
    final static String scenMsg = ChatColor.translateAlternateColorCodes('&', "&r&fRight Click to enable scenarios!");
    final static String startMsg = ChatColor.translateAlternateColorCodes('&', "&r&fRight Click to vote to start!");
    final static String sizeMsg = ChatColor.translateAlternateColorCodes('&', "&r&fRight Click to change team sizes!");
    BedwarsAPI api = BedwarsAPI.getInstance();

    public void giveItems(Player p)
    {
        if (!api.isPlayerPlayingAnyGame(p))
            return;

        ItemStack block = new ItemStack(Material.RECOVERY_COMPASS);
        ItemMeta meta = block.getItemMeta();

        List<String> lore = new ArrayList<>();
        lore.add(selectorMsg);

        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&b&lSelect Team"));
        meta.setLore(lore);
        block.setItemMeta(meta);

        p.getInventory().setItem(0, block);

        block = new ItemStack(Material.COMPARATOR);
        meta = block.getItemMeta();

        lore = new ArrayList<>();
        lore.add(scenMsg);

        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&c&lScenario Selector"));
        meta.setLore(lore);
        block.setItemMeta(meta);

        p.getInventory().setItem(3, block);

        block = new ItemStack(Material.ANVIL);
        meta = block.getItemMeta();

        lore = new ArrayList<>();
        lore.add(sizeMsg);

        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&f&lTeam Size Editor"));
        meta.setLore(lore);
        block.setItemMeta(meta);

        p.getInventory().setItem(5, block);

        if(toggleCommands.startButton)
        {
            block = new ItemStack(Material.LIME_DYE);
            meta = block.getItemMeta();

            lore = new ArrayList<>();
            lore.add(startMsg);

            meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&a&lStart Game Vote"));
            meta.setLore(lore);
            block.setItemMeta(meta);

            p.getInventory().setItem(4, block);
        }
    }

    @EventHandler
    public void RightClickEvent(PlayerInteractEvent e)
    {
        if (e.getItem() == null)
            return;

        if(e.getItem().getItemMeta() == null)
            return;

        if ((e.getItem().getItemMeta()).getLore() != null)
        {
            List<String> lore = e.getItem().getItemMeta().getLore();

            if (lore.isEmpty())
                return;

            if (lore.get(0).isEmpty())
                return;

            if (("§r" + lore.get(0)).equals(selectorMsg))
                new TeamSelection().openInventory(e.getPlayer(), api.getGameOfPlayer(e.getPlayer()), true);

            if (("§r" + lore.get(0)).equals(scenMsg))
                e.getPlayer().sendMessage(ChatColor.RED + "Coming soon!");

            if (("§r" + lore.get(0)).equals(sizeMsg) && !toggleCommands.startButton)
                Bukkit.dispatchCommand(e.getPlayer(),"invictools panel size");

            if (("§r" + lore.get(0)).equals(startMsg))
                vote(BedwarsAPI.getInstance().getGameOfPlayer(e.getPlayer()),e.getPlayer());
        }
    }

    static HashMap<Game,Integer> vote = new HashMap<>();
    static HashMap<Game,Integer> current = new HashMap<>();
    static HashMap<Player,Game> voted = new HashMap<>();

    @EventHandler
    public void join(BedwarsPlayerJoinEvent e)
    {
        if(toggleCommands.isHosting && e.getGame().getConnectedPlayers().contains(Bukkit.getPlayer("Invictable")))
            vote.put(e.getGame(),1000);
        else if(e.getGame().getConnectedPlayers().size() == 0)
        {
            vote.put(e.getGame(),2);
            current.put(e.getGame(),0);
        }
        else if(e.getGame().getConnectedPlayers().size() >= 1)
            vote.put(e.getGame(),e.getGame().getConnectedPlayers().size()/2+1);
    }

    @EventHandler
    public void leave(BedwarsPlayerLeaveEvent e)
    {
        voted.remove(e.getPlayer());
        if(toggleCommands.isHosting && e.getGame().getConnectedPlayers().contains(Bukkit.getPlayer("Invictable")))
            return;
        else if(e.getGame().getConnectedPlayers().size() == 1)
        {
            vote.remove(e.getGame());
            current.remove(e.getGame());
        }
        else
        {
            vote.put(e.getGame(),e.getGame().getConnectedPlayers().size()/2+1);
            checkVote(e.getGame());
        }
    }

    private void vote(Game game,Player p)
    {
        if(!voted.containsKey(p))
        {
            voted.put(p,game);
            current.put(game,current.get(game)+1);
            checkVote(game);
        }
    }

    private void checkVote(Game game)
    {
        if(vote.get(game).equals(current.get(game)))
        {
            game.start();
            vote.remove(game);
            current.remove(game);
            for (Player p:voted.keySet())
            {
                if(voted.get(p).equals(game))
                    voted.remove(p);
            }
        }
    }

}

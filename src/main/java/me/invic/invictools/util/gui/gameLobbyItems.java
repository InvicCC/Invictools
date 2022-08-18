package me.invic.invictools.util.gui;

import me.invic.invictools.commands.OldCommands;
import me.invic.invictools.commands.toggleCommands;
import me.invic.invictools.util.gui.scenSelector.perGameScenSelHolder;
import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
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
import org.screamingsandals.bedwars.api.game.GameStatus;

import java.util.*;

public class gameLobbyItems implements Listener
{
    final static String selectorMsg = ChatColor.translateAlternateColorCodes('&', "&r&7Right click to select your team!");
    final static String scenMsg = ChatColor.translateAlternateColorCodes('&', "&r&7Right Click to enable scenarios!");
    final static String startMsg = ChatColor.translateAlternateColorCodes('&', "&r&7Right Click to vote to start!");
    final static String fastMsg = ChatColor.translateAlternateColorCodes('&', "&r&7Right Click to start the game!");
    final static String sizeMsg = ChatColor.translateAlternateColorCodes('&', "&r&7Right Click to change team sizes!");
    final static int sunflower = 2;
    BedwarsAPI api = BedwarsAPI.getInstance();

    public void giveItems(Player p)
    {
        if (!api.isPlayerPlayingAnyGame(p) || api.getGameOfPlayer(p).getStatus().equals(GameStatus.RUNNING))
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

        p.getInventory().setItem(6, block);

        block = new ItemStack(Material.ANVIL);
        meta = block.getItemMeta();

        lore = new ArrayList<>();
        lore.add(sizeMsg);

        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&f&lTeam Size Editor"));
        meta.setLore(lore);
        block.setItemMeta(meta);

        p.getInventory().setItem(7, block);

            block = new ItemStack(Material.SUNFLOWER);
            meta = block.getItemMeta();

            lore = new ArrayList<>();
            lore.add(startMsg);

            meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&a&lStart Game Vote"));
            meta.setLore(lore);
            block.setItemMeta(meta);

            p.getInventory().setItem(sunflower, block);


        if(toggleCommands.isHosting && (p.getDisplayName().charAt(1) == 'b' || p.getDisplayName().charAt(1) == 'a' || p.isOp()))
        {
            block = new ItemStack(Material.LIME_DYE);
            meta = block.getItemMeta();

            lore = new ArrayList<>();
            lore.add(fastMsg);

            meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&a&lStart Game"));
            meta.setLore(lore);
            block.setItemMeta(meta);

            p.getInventory().addItem(block);
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
                e.getPlayer().openInventory(perGameScenSelHolder.mainSelector.get(BedwarsAPI.getInstance().getGameOfPlayer(e.getPlayer())));

            if (("§r" + lore.get(0)).equals(sizeMsg) && toggleCommands.startButton && !BedwarsAPI.getInstance().getGameOfPlayer(e.getPlayer()).getName().equalsIgnoreCase("Multiverse"))
                Bukkit.dispatchCommand(e.getPlayer(),"invictools panel size");

            if (("§r" + lore.get(0)).equals(startMsg) && toggleCommands.startButton)
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
        else if(e.getGame().getConnectedPlayers().size() > 1)
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
            p.getWorld().playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL,1,1);
            voted.put(p,game);
            current.put(game,current.get(game)+1);
            game.getConnectedPlayers().forEach((pl)->pl.sendMessage(ChatColor.WHITE+p.getDisplayName()+ ChatColor.AQUA+" voted to start the game "+ChatColor.WHITE+"("+current.get(game)+"/"+vote.get(game)+")"));
            checkVote(game);
        }
        else
        {
            p.playSound(p, Sound.ENTITY_WANDERING_TRADER_NO,1,1);
            p.sendMessage(ChatColor.RED+"You already voted to start!");
        }
    }

    private void checkVote(Game game)
    {
       // System.out.println(vote.get(game)+" vote/current "+current.get(game));
        if(vote.get(game).equals(current.get(game)))
        {
            ItemStack dye = new ItemStack(Material.LIME_DYE);
            ItemMeta meta = dye.getItemMeta();
            meta.setDisplayName(ChatColor.translateAlternateColorCodes('&',"&f&lPress to Start!"));
            dye.setItemMeta(meta);

            for (Player p:game.getConnectedPlayers())
            {
                p.getInventory().setItem(sunflower,dye);
            }

            vote.remove(game);
            current.remove(game);

            Iterator<Map.Entry<Player, Game> >
                    iterator = voted.entrySet().iterator();

            // Iterate over the HashMap
            while (iterator.hasNext()) {

                // Get the entry at this iteration
                Map.Entry<Player,Game>
                        entry
                        = iterator.next();

                // Check if this key is the required key
                if (entry.getValue().equals(game)) {

                    // Remove this entry from HashMap
                    iterator.remove();
                }
            }
        }
    }

}

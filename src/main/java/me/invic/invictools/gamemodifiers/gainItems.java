package me.invic.invictools.gamemodifiers;

import org.bukkit.ChatColor;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.screamingsandals.bedwars.api.game.Game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class gainItems
{
    public static HashMap<Player,gainItems> activeGains = new HashMap<>();

    Game game;
    List<ItemStack> killItems = new ArrayList<>();
    List<ItemStack> deathItems = new ArrayList<>();
    List<String> killEffects = new ArrayList<>();

    public gainItems(Game gamel)
    {
        game = gamel;
    }

    public void complete(Player p)
    {
        activeGains.put(p,this);
    }

    public void addKillItem(ItemStack item)
    {
        killItems.add(item);
    }

    public void addDeathItem(ItemStack item)
    {
        deathItems.add(item);
    }

    public void addKillEffect(String effect)
    {
        killEffects.add(effect);
       // else if(type.equalsIgnoreCase("deathEffects"))
    }

    public List<ItemStack> getKillItems()
    {
        return  killItems;
    }

    public List<ItemStack> getDeathItems()
    {
        return deathItems;
    }

    public List<String> getKillEffects()
    {
        return killEffects;
    }

    public boolean checkGame(Game currentGame)
    {
        return game==currentGame;
    }

    public void destroy(Player p)
    {
        killItems.clear();
        deathItems.clear();
        killEffects.clear();
        activeGains.remove(p);
    }
}

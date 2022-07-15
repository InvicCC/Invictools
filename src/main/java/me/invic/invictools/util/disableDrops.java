package me.invic.invictools.util;

import me.invic.invictools.items.createItems;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;
import org.screamingsandals.bedwars.api.events.BedwarsGameEndingEvent;

import java.util.HashMap;

public class disableDrops implements Listener
{
    public static HashMap<Player, String> forbidDrop = new HashMap<>();

    @EventHandler
    public void DropListener(PlayerDropItemEvent e)
    {
        //  System.out.println("hey");
        if (!forbidDrop.containsKey(e.getPlayer()))
            return;

        // System.out.println("check");
        if (e.getItemDrop().getItemStack().getItemMeta().getDisplayName().equalsIgnoreCase(forbidDrop.get(e.getPlayer())))
        {
            e.setCancelled(true);
        }

        if (e.getItemDrop().getItemStack().getItemMeta().getLore() == null)
            return;

        ItemStack i = new createItems().MODBOW();
        if (forbidDrop.get(e.getPlayer()).equalsIgnoreCase(i.getItemMeta().getDisplayName()))
        {
            if (e.getItemDrop().getItemStack().getItemMeta().getLore().get(0).equalsIgnoreCase("§7Multi-purposed Ranged Weapon"))
                e.setCancelled(true);
        }
    }

    @EventHandler
    public void clear(BedwarsGameEndingEvent e)
    {
        //System.out.println("clearing");
        forbidDrop.clear();
    }

    @Deprecated
    public void addRestriction(Player p, String item, String type)
    {/*
        if(type.equalsIgnoreCase("normal"))
        {
            forbidDrop.put(p,item);
        }
        else if(type.equalsIgnoreCase("item"))
        {
            item = new createItems().getByName("item").getItemMeta().getDisplayName();
            forbidDrop.put(p,item);
        }
        */
    }
}

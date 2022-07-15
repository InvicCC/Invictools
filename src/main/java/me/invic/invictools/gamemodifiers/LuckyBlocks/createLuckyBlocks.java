package me.invic.invictools.gamemodifiers.LuckyBlocks;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class createLuckyBlocks
{
    public ItemStack BAD()
    {
        ItemStack block = new ItemStack(Material.SEA_LANTERN);
        ItemMeta meta = block.getItemMeta();

        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.translateAlternateColorCodes('&', "&7Unlucky I"));

        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&c&lUnlucky Block"));
        meta.setLore(lore);
        block.setItemMeta(meta);

        return block;
    }

    public ItemStack GOOD()
    {
        ItemStack block = new ItemStack(Material.SEA_LANTERN);
        ItemMeta meta = block.getItemMeta();

        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.translateAlternateColorCodes('&', "&7Lucky I"));

        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&b&lLucky Block"));
        meta.setLore(lore);
        block.setItemMeta(meta);

        return block;
    }

    public ItemStack OP()
    {
        ItemStack block = new ItemStack(Material.SEA_LANTERN);
        ItemMeta meta = block.getItemMeta();

        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.translateAlternateColorCodes('&', "&7Lucky II"));

        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&e&lSuper lucky Block"));
        meta.setLore(lore);
        block.setItemMeta(meta);

        return block;
    }

    public ItemStack getRandomBlock()
    {
        Random rand = new Random();
        switch(rand.nextInt(3))
        {
            case 0:
                return BAD();
            case 1:
                return GOOD();
            case 2:
                return OP();
            default:
                return null;
        }
    }

    public ItemStack getRandomBlockWeighted()
    {
        Random rand = new Random();
        int choice = rand.nextInt(100);
        if(choice > 0 && choice < 80)
                return GOOD();
        else if(choice >= 80 && choice < 95)
                return BAD();
        else
                return OP();
    }

    public ItemStack getByName(String item)
    {
        item = item.toLowerCase(Locale.ROOT);
        switch (item)
        {
            case "bad":
                return BAD();
            case "good":
                return GOOD();
            case "op":
                return OP();
            default:
                return null;
        }
    }
}

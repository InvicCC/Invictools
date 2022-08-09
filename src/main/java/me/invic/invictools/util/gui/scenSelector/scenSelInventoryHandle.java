package me.invic.invictools.util.gui.scenSelector;

import me.invic.invictools.commands.OldCommands;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class scenSelInventoryHandle
{
    static HashMap<String,String> allLore = new HashMap<>();

    Inventory loadInventory(String panel)
    {
        File Folder = new File(OldCommands.Invictools.getDataFolder(), "Panels");
        File pFile = new File(Folder, panel + ".yml");
        final FileConfiguration panelFile = YamlConfiguration.loadConfiguration(pFile);

        String title = ChatColor.translateAlternateColorCodes('&', panelFile.getString("panels." + panel + ".title","&7&lSCENARIO SELECTOR"));
        Inventory inventory = Bukkit.createInventory(null, panelFile.getInt("panels." + panel + ".rows") * 9, title);

        for (int s = 0; s <= panelFile.getInt("panels." + panel + ".rows") * 9; s++)
        {
            if (panelFile.getString("panels." + panel + ".item." + s + ".material") != null)
            {
                ItemStack item = new ItemStack(Material.getMaterial(panelFile.getString("panels." + panel + ".item." + s + ".material")));
                item.setAmount(panelFile.getInt("panels." + panel + ".item." + s + ".stack"));
                if (item.getAmount() == 0)
                    item.setAmount(1);

                ItemMeta meta = item.getItemMeta();
                List<String> lore = panelFile.getStringList("panels." + panel + ".item." + s + ".lore");
                List<String> mlore = new ArrayList<>();
                for (String l : lore)
                {
                    mlore.add(ChatColor.translateAlternateColorCodes('&', l));
                }
                meta.setLore(mlore);
                meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', panelFile.getString("panels." + panel + ".item." + s + ".name","&c"+item.getType().toString().toLowerCase(Locale.ROOT))));
                item.setItemMeta(meta);
                inventory.setItem(s, item);
                allLore.put(item.getType().toString().toUpperCase(Locale.ROOT), mlore.get(0));
            }
        }
        return inventory;
    }

    public static ItemStack flipItem(ItemStack itemStack)
    {
        if(itemStack.getEnchantments().isEmpty())
        {
            itemStack.addUnsafeEnchantment(Enchantment.DURABILITY, 1);
            final ItemMeta meta = itemStack.getItemMeta();
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            itemStack.setItemMeta(meta);
        }
        else
        {
            itemStack.removeEnchantment(Enchantment.DURABILITY);
        }
        return itemStack;
    }
}

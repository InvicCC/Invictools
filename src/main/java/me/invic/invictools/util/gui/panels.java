package me.invic.invictools.util.gui;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class panels implements Listener
{
    public static HashMap<ItemStack, String> rightClick = new HashMap<>();
    public static HashMap<ItemStack, String> leftClick = new HashMap<>();
    public static HashMap<ItemStack, String> anyClick = new HashMap<>();
    public static HashMap<ItemStack, String> close = new HashMap<>();
    public static HashMap<ItemStack, Sound> sound = new HashMap<>();
    public static HashMap<Inventory, Sound> openSound = new HashMap<>();
    public static HashMap<String, Inventory> activeInventory = new HashMap<>();

    public void loadPanels()
    {
        Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("Invictools");
        File Folder = new File(plugin.getDataFolder(), "Panels");
        File[] yamlFiles = Folder.listFiles();
        for (File file : yamlFiles)
        {
            String[] mapName = file.getName().split("\\.");
            //    System.out.println(mapName[0]);
            createPanel(mapName[0]);
        }
    }

    private String getTitle(FileConfiguration config, String panel)
    {
        //  if(config.getString("panels."+panel+".title") != null)
        return ChatColor.translateAlternateColorCodes('&', config.getString("panels." + panel + ".title"));
        //    else
        //    {
        //      System.out.println(panel + " "+config.getString("panels"));
        //      return " ";
        // }
    }

    private String getName(FileConfiguration config, String panel, int s)
    {
        if (config.getString("panels." + panel + ".item." + s + ".name") != null)
            return ChatColor.translateAlternateColorCodes('&', config.getString("panels." + panel + ".item." + s + ".name"));
        else
        {
            //  System.out.println("null name");
            return " ";
        }
    }

    public Inventory createPanel(String panel)
    {
        Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("Invictools");
        File Folder = new File(plugin.getDataFolder(), "Panels");
        File pFile = new File(Folder, panel + ".yml");
        final FileConfiguration panelFile = YamlConfiguration.loadConfiguration(pFile);

        String title = getTitle(panelFile, panel);
        Inventory inventory = Bukkit.createInventory(null, panelFile.getInt("panels." + panel + ".rows") * 9, title);

        for (int s = 0; s <= panelFile.getInt("panels." + panel + ".rows") * 9; s++)
        {
            //    System.out.println(s);
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
                meta.setDisplayName(getName(panelFile, panel, s));
                item.setItemMeta(meta);
                inventory.setItem(s, item);

                List<String> commands = panelFile.getStringList("panels." + panel + ".item." + s + ".commands");
                if (commands.size() != 0)
                {
                    for (String c : commands)
                    {
                        String[] command = c.split(" ");
                        // ArrayList<String> a = new ArrayList<>(Arrays.asList(command));
                        //  a.remove(0);
                        StringBuilder translated = new StringBuilder();
                        boolean first = true;
                        for (String piece : command)
                        {
                            if (!first)
                            {
                                translated.append(piece).append(" ");
                            }
                            else
                                first = false;
                        }

                        if (command[0].equalsIgnoreCase("left="))
                            leftClick.put(item, translated.toString());
                        else if (command[0].equalsIgnoreCase("right="))
                            rightClick.put(item, translated.toString());
                        else if (command[0].equalsIgnoreCase("cpc"))
                            close.put(item, translated.toString());
                        else if (command[0].equalsIgnoreCase("sound="))
                            sound.put(item, Sound.valueOf(command[1]));
                        else
                            anyClick.put(item, c);
                    }
                }
            }
        }

        if (panelFile.getString("panels." + panel + ".sound-on-open") != null)
            openSound.put(inventory, Sound.valueOf(panelFile.getString("panels." + panel + ".sound-on-open")));

        activeInventory.put(panel, inventory);
/*
            leftClick.forEach((attribute, value) ->
                   System.out.println( attribute.toString() + " LEFT " + value));
            rightClick.forEach((attribute, value) ->
                   System.out.println(ChatColor.WHITE + attribute.toString() + " RIGHT " + value));
            anyClick.forEach((attribute, value) ->
                    System.out.println(ChatColor.WHITE + attribute.toString() + " ANY " + value));

 */

        return inventory;
    }

    public void openInventory(String panel, Player p)
    {
        //    p.openInventory(createPanel(panel));
        //   p.playSound(p.getLocation(),openSound.get(activeInventory.get(panel)),1,1);
        if (activeInventory.containsKey(panel))
        {
            p.openInventory(activeInventory.get(panel));
            if (openSound.containsKey(activeInventory.get(panel)))
                p.playSound(p.getLocation(), openSound.get(activeInventory.get(panel)), 1, 1);
        }
    }

    @EventHandler
    public void InventoryClick(InventoryClickEvent e)
    {
        // System.out.println("click");
        if (activeInventory.containsValue(e.getClickedInventory()))
        {
            e.setCancelled(true);
            Player p = (Player) e.getWhoClicked();
            if (leftClick.containsKey(e.getCurrentItem()) && e.getClick().equals(ClickType.LEFT))
            {
                Bukkit.dispatchCommand(e.getWhoClicked(), leftClick.get(e.getCurrentItem()));
                if (sound.containsKey(e.getCurrentItem()))
                {
                    p.playSound(p.getLocation(), sound.get(e.getCurrentItem()), 1, 1);
                }
            }
            else if (rightClick.containsKey(e.getCurrentItem()) && e.getClick().equals(ClickType.RIGHT))
            {
                Bukkit.dispatchCommand(e.getWhoClicked(), rightClick.get(e.getCurrentItem()));
                if (sound.containsKey(e.getCurrentItem()))
                {
                    p.playSound(p.getLocation(), sound.get(e.getCurrentItem()), 1, 1);
                }
            }

            if (anyClick.containsKey(e.getCurrentItem()))
                Bukkit.dispatchCommand(e.getWhoClicked(), anyClick.get(e.getCurrentItem()));

            if (close.containsValue(e.getCurrentItem()))
                p.closeInventory();
        }
    }
}

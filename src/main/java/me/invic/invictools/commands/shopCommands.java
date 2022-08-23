package me.invic.invictools.commands;

import me.invic.invictools.Invictools;
import me.invic.invictools.cosmetics.statisticRequirments;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.screamingsandals.bedwars.api.BedwarsAPI;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class shopCommands implements CommandExecutor, TabExecutor, Listener
{
    static File Folder = new File(OldCommands.Invictools.getDataFolder(), "PlayerData");
    static HashMap<String,Integer> purchaseable = new HashMap<>();
    static Inventory confirmation;

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args)
    {
        boolean perms = false;
        if (sender instanceof Player)
        {
            if (sender.hasPermission("invic.invictools"))
            {
                perms = true;
            }
        } else
        {
            perms = true;
        }

        List<String> tabComplete = new ArrayList<>();

        if(args.length==0)
        {
            tabComplete.add("cage");
            if(perms)
                tabComplete.add("append");
        }
        else if(args.length==1 && args[0].equalsIgnoreCase("cage") || args.length == 2 && args[1].equalsIgnoreCase("cage") && args[0].equalsIgnoreCase("append"))
        {
            tabComplete.addAll(statisticRequirments.loadedCages.keySet());
        }
        else if(args[0].equalsIgnoreCase("append") && args.length == 3)
        {
            for (Player p: Bukkit.getOnlinePlayers())
            {
                tabComplete.add(p.getName());
            }
        }

        return tabComplete;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        boolean perms = false;
        if (sender instanceof Player)
        {
            if (sender.hasPermission("invic.invictools"))
            {
                perms = true;
            }
        } else
        {
            perms = true;
        }

        if(args.length==4 && args[0].equalsIgnoreCase("append"))
        {
            File pFile = new File(Folder, Bukkit.getOfflinePlayer(args[3]).getUniqueId() + ".yml");
            final FileConfiguration playerData = YamlConfiguration.loadConfiguration(pFile);
            List<String> bought = playerData.getStringList("purchased");
            bought.add(args[2]);
            playerData.set("purchased",bought);
            try
            {
                playerData.save(pFile);
                sender.sendMessage(ChatColor.AQUA+"Unlocked "+ChatColor.WHITE+args[2]+ChatColor.AQUA+ " for "+ChatColor.WHITE+args[3]);
            }
            catch (IOException e)
            {
                sender.sendMessage(ChatColor.RED+"Error saving this config");
                e.printStackTrace();
            }
        }
        else if(args.length != 4 && args[0].equalsIgnoreCase("append"))
        {
            sender.sendMessage(ChatColor.RED + " shop append type name player");
        }
        else if(args.length == 2 && args[0].equalsIgnoreCase("cage") && sender instanceof Player p)
        {
            probePurchase(p,args[1]);
        }

        return true;
    }

    void loadPurchases()
    {
        purchaseable.put(statisticRequirments.loadedCages.get("dark").getName(),100000);
        purchaseable.put(statisticRequirments.loadedCages.get("one").getName(),50000);
        purchaseable.put("magicTrail",150000);
        purchaseable.put("bubbleTrail",25000);
        purchaseable.put("potionTrail",75000);
    }

    Material confirmMaterial = Material.LIME_CONCRETE;
    Material denyMaterial = Material.RED_CONCRETE;
    void generateConfirmation()
    {
        Inventory inventory = Bukkit.createInventory(null, 9,"Confirm Purchase");
        ItemStack confirm = new ItemStack(confirmMaterial);
        ItemMeta meta = confirm.getItemMeta();
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&',"&a&lConfirm"));
        confirm.setItemMeta(meta);
        inventory.setItem(0,confirm);
        inventory.setItem(1,confirm);
        inventory.setItem(2,confirm);
        inventory.setItem(3,confirm);
        ItemStack mid = new ItemStack(Material.BEACON);
        meta = mid.getItemMeta();
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&',"&f&lAre you sure you want to purchase this?"));
        mid.setItemMeta(meta);
        mid.addUnsafeEnchantment(Enchantment.DURABILITY,1);
        inventory.addItem(mid);
        ItemStack deny = new ItemStack(denyMaterial);
        meta = deny.getItemMeta();
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&',"&c&lCancel"));
        deny.setItemMeta(meta);
        inventory.setItem(5,deny);
        inventory.setItem(6,deny);
        inventory.setItem(7,deny);
        inventory.setItem(8,deny);
        confirmation = inventory;
    }

    @EventHandler
    public void open(InventoryClickEvent e)
    {
        if(e.getInventory().equals(confirmation) && attemptingPurchase.containsKey((Player)e.getWhoClicked()))
        {
            e.setCancelled(true);
            if(e.getCurrentItem().getType().equals(confirmMaterial))
            {
                doPurchase((Player)e.getWhoClicked(),attemptingPurchase.get((Player)e.getWhoClicked()));
                attemptingPurchase.remove((Player)e.getWhoClicked());
                e.getWhoClicked().closeInventory();
            }
            else if(e.getCurrentItem().getType().equals(denyMaterial))
            {
                attemptingPurchase.remove((Player)e.getWhoClicked());
                e.getWhoClicked().closeInventory();
            }
        }
    }

    @EventHandler
    public void quit(PlayerQuitEvent e)
    {
        attemptingPurchase.remove(e.getPlayer());
    }

    @EventHandler
    public void world(PlayerChangedWorldEvent e)
    {
        attemptingPurchase.remove(e.getPlayer());
    }

    @EventHandler
    public void antiratio(AsyncPlayerChatEvent e)
    {
        if(e.getMessage().toLowerCase(Locale.ROOT).contains("ratio")
                && !BedwarsAPI.getInstance().isPlayerPlayingAnyGame(e.getPlayer())
                && new Random().nextInt(5) == 1)
        {
            e.getPlayer().sendMessage(ChatColor.AQUA+" "+ChatColor.BOLD+"(!)"+ChatColor.WHITE+" Counter Ratio!");
            e.getPlayer().playSound(e.getPlayer(), Sound.ENTITY_LIGHTNING_BOLT_IMPACT,1,1);
            new BukkitRunnable() {
                @Override
                public void run() {
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(),"kill "+e.getPlayer().getName());
                }
            }.runTask(OldCommands.Invictools);

        }
    }

    public void loadShop()
    {
        loadPurchases();
        generateConfirmation();
    }

    static HashMap<Player,String> attemptingPurchase = new HashMap<>();
    void probePurchase(Player p, String item)
    {
        File pFile = new File(Folder, p.getUniqueId() + ".yml");
        final FileConfiguration playerData = YamlConfiguration.loadConfiguration(pFile);
        List<String> bought = playerData.getStringList("purchased");
        if(bought.contains(item))
            p.sendMessage(ChatColor.RED + "You already own this!");
        else if(Invictools.econ.getBalance(p) >= purchaseable.getOrDefault(item,999999999))
        {
            attemptingPurchase.put(p,item);
            p.openInventory(confirmation);
        }
        else
            p.sendMessage(ChatColor.RED +"You cannot afford this!");
    }

    void doPurchase(Player p, String item)
    {
        File pFile = new File(Folder, p.getUniqueId() + ".yml");
        final FileConfiguration playerData = YamlConfiguration.loadConfiguration(pFile);
        List<String> bought = playerData.getStringList("purchased");
        bought.add(item);
        playerData.set("purchased",bought);

        try
        {
            Invictools.econ.withdrawPlayer(p,purchaseable.get(item));
            playerData.save(pFile);
            p.sendMessage(ChatColor.RED+ "-"+purchaseable.get(item)+" Invictacoins");
            p.sendMessage(ChatColor.GREEN+""+ChatColor.GREEN+"Unlocked!");
        }
        catch (IOException e)
        {
            p.sendMessage(ChatColor.RED+"Error making your purchase");
            e.printStackTrace();
        }
    }

}

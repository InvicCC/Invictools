package me.invic.invictools.util.fixes;

import me.invic.invictools.commands.Commands;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.InventoryEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.screamingsandals.bedwars.api.BedwarsAPI;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class LobbyInventoryFix implements Listener
{
    File Folder = new File(Commands.Invictools.getDataFolder(), "PlayerData");
    public static HashMap<Player, Long> InventorySaveCooldown = new HashMap<>();
    int cooldown = 60; // seconds before file can save again

    @EventHandler
    public void worldChange(PlayerChangedWorldEvent e)
    {
        if (e.getPlayer().getWorld().getName().equalsIgnoreCase("bwlobby_nether") || e.getPlayer().getWorld().getName().equalsIgnoreCase("bwlobby_the_end"))
            return;

        if (e.getFrom().getName().equalsIgnoreCase("bwlobby_nether") || e.getFrom().getName().equalsIgnoreCase("bwlobby_the_end"))
            return;

        if (e.getFrom().getName().equalsIgnoreCase("bwlobby") && !BedwarsAPI.getInstance().isPlayerPlayingAnyGame(e.getPlayer()))
        {
            saveInventory(e.getPlayer());
        }
        else if (e.getPlayer().getWorld().getName().equalsIgnoreCase("bwlobby"))
        {
            loadInventory(e.getPlayer(), e.getPlayer());
        }
    }
/*
    @EventHandler
    public void teleport(PlayerDeathEvent e)
    {
        if(e.getEntity().getWorld().getName().equalsIgnoreCase("bwlobby"))
        {
            saveInventory(e.getEntity());
            loadInventory(e.getEntity(), e.getEntity());
        }
    }

 */

    @EventHandler
    public void JoinEvent(PlayerJoinEvent e)
    {
        if (!e.getPlayer().getWorld().getName().equalsIgnoreCase("bwlobby") && !e.getPlayer().isOp())
            Bukkit.dispatchCommand(e.getPlayer(), "spawn");

        if (e.getPlayer().hasPlayedBefore())
        {
            if (e.getPlayer().getWorld().getName().equalsIgnoreCase("bwlobby"))
            {
                loadInventory(e.getPlayer(), e.getPlayer());
            }
        }
    }

    @EventHandler
    public void LeaveEvent(PlayerQuitEvent e)
    {
        if (e.getPlayer().getWorld().getName().equalsIgnoreCase("bwlobby"))
            saveInventory(e.getPlayer());
    }

    public void saveInventory(Player p)
    {
        if (InventorySaveCooldown.containsKey(p))
        {
            long secondsLeft = ((InventorySaveCooldown.get(p) / 1000) + cooldown) - (System.currentTimeMillis() / 1000);
            if (secondsLeft <= 0)
                saveInventoryAfter(p, true);
        }
        else
            saveInventoryAfter(p, true);
    }

    private void saveInventoryAfter(Player p, boolean respectCooldown)
    {
        if (respectCooldown)
            InventorySaveCooldown.put(p, System.currentTimeMillis());

        File pFile = new File(Folder, p.getUniqueId() + ".yml");
        FileConfiguration balls = YamlConfiguration.loadConfiguration(pFile);

        List<ItemStack> inventoryList = new ArrayList<>();
        for (int i = 0; i <= 40; i++)
        {
            ItemStack item = p.getInventory().getItem(i);
            if (item != null)
            {
                inventoryList.add(item);
            }
            else
            {
                ItemStack voidItem = new ItemStack(Material.STRUCTURE_VOID);
                ItemMeta meta = voidItem.getItemMeta();
                meta.setDisplayName(String.valueOf(i));
                voidItem.setItemMeta(meta);
                inventoryList.add(voidItem);
            }
        }
        balls.set("lobby", inventoryList);

        try
        {
            balls.save(pFile);
            if (!p.getWorld().getName().equalsIgnoreCase("bwlobby"))
                p.getInventory().clear();
        }
        catch (IOException ioException)
        {
            ioException.printStackTrace();
        }
    }

    public void loadInventory(Player p, Player recipient)
    {
        File pFile = new File(Folder, p.getUniqueId() + ".yml");
        FileConfiguration balls = YamlConfiguration.loadConfiguration(pFile);

        if (balls.get("lobby") != null)
        {
            recipient.getInventory().clear();
            @SuppressWarnings("unchecked")
            List<ItemStack> inventoryList = (List<ItemStack>) balls.get("lobby");
            for (int i = 0; i <= 40; i++)
            {
                ItemStack item = inventoryList.get(i);
                if (item.getType() != Material.STRUCTURE_VOID)
                    recipient.getInventory().setItem(i, item);
            }
/*
            new BukkitRunnable()
            {
                @Override
                public void run()
                {
                    for (ItemStack item:recipient.getInventory())
                    {
                        if(item != null)
                            if(item.getType().equals(Material.STRUCTURE_VOID))
                                item.setType(Material.AIR);
                    }
                }
            }.runTaskLater(Commands.Invictools, 1L);

 */
        }
    }

    @EventHandler
    public void onRightClick(PlayerInteractEntityEvent e)
    {
        if (e.getRightClicked().getType() == EntityType.ALLAY)
        {
            e.setCancelled(true);
        }
    }

    public void clearMainInventory(Player p)
    {
        for (int i = 0; i <= 35; i++)
        {
            ItemStack item = p.getInventory().getItem(i);
            if (item != null)
                item.setType(Material.AIR);
        }
    }
}

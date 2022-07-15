package me.invic.invictools.util.fixes;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerVelocityEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class keepitems implements Listener
{
    Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("Invictools");
    Random rand = new Random();

    @EventHandler
    public void keepitems(PlayerMoveEvent e)
    {
/*
        if(rand.nextInt(15) == 3)
        {
            Player player = e.getPlayer();

            if (player.getGameMode() == GameMode.SURVIVAL)
            {
                File pFile = new File(plugin.getDataFolder().getPath() + "\\PlayerData" + File.separator + player.getUniqueId() + ".yml");
                final FileConfiguration playerData = YamlConfiguration.loadConfiguration(pFile);
                FileConfiguration Config = plugin.getConfig();

                List<String> items = Config.getStringList("items");
                List<ItemStack> passableItems = new ArrayList<>();

                ItemStack[] inv = player.getInventory().getContents();

                for (int i = 0; i < inv.length; i++)
                {
                    for (int a = 0; a < items.size(); a++)
                    {
                        if (inv[i] != null)
                        {
                            if (inv[i].getType().name().equalsIgnoreCase(items.get(a)))
                            {
                                passableItems.add(inv[i]);
                            }
                        }
                    }
                }

                final int[] ii = {0}; // elaborate wait();
                new BukkitRunnable()
                {
                    @Override
                    public void run()
                    {
                        if (ii[0] > 0)
                        {
                            playerData.set("items", passableItems);

                            try
                            {
                                playerData.save(pFile);
                            } catch (IOException ioException)
                            {
                                ioException.printStackTrace();
                            }

                            this.cancel();
                        } else
                            ii[0]++;

                    }
                }.runTaskTimer(Objects.requireNonNull(Bukkit.getServer().getPluginManager().getPlugin("Invictools")), 0, 40);
            }
        }
    }

    @EventHandler
    public void giveitems(PlayerDeathEvent e)
    {
        /*
        Player player = e.getEntity().getPlayer();
        assert player != null;
        File pFile = new File(plugin.getDataFolder().getPath() + "\\PlayerData" + File.separator + player.getUniqueId() + ".yml");
        final FileConfiguration playerData = YamlConfiguration.loadConfiguration(pFile);
        FileConfiguration Config = plugin.getConfig();

        List<String> worlds = Config.getStringList("worlds");
        String currentWorld = player.getWorld().getName();

        final int[] ii = {0}; // elaborate wait();
        new BukkitRunnable()
        {
            @Override
            public void run()
            {
                if(worlds.contains(currentWorld))
                {
                    @SuppressWarnings("unchecked")
                    List<ItemStack> items = (List<ItemStack>) playerData.get("items");
                    if(ii[0] > 0 && Objects.requireNonNull(player).getGameMode() != GameMode.SPECTATOR)
                    {

                        for (int i = 0; i < Objects.requireNonNull(items).size() ; i ++)
                        {
                            ItemStack currentItem = items.get(i);
                            System.out.println(items.get(i) + " " +  i);
                            if(currentItem.getType().name().equalsIgnoreCase("DIAMOND_PICKAXE"))
                                currentItem.setType(Material.valueOf("GOLDEN_PICKAXE"));
                            else if(currentItem.getType().name().equalsIgnoreCase("GOLDEN_PICKAXE"))
                                currentItem.setType(Material.valueOf("IRON_PICKAXE"));
                            else if(currentItem.getType().name().equalsIgnoreCase("IRON_PICKAXE"))
                                currentItem.setType(Material.valueOf("STONE_PICKAXE"));

                            if(currentItem.getType().name().equalsIgnoreCase("IRON_AXE"))
                            {
                                player.getInventory().setItem(8, new ItemStack(Material.STONE_AXE));
                                currentItem.setType(Material.AIR);
                            }
                            if(currentItem.getType().name().equalsIgnoreCase("SHEARS"))
                            {
                                ItemStack slot1 = player.getInventory().getItem(1);
                                if (slot1 != null && slot1.getType().name().equalsIgnoreCase("SHEARS"))
                                    currentItem.setType(Material.AIR);
                            }

                            if(player.getWorld().getName().equalsIgnoreCase(currentWorld))
                                player.getInventory().addItem(currentItem);
                        }

                        ItemStack slot8 = player.getInventory().getItem(8);
                        if(slot8 != null && slot8.getType().name().equalsIgnoreCase("WOODEN_AXE"))
                            player.getInventory().setItem(8, new ItemStack(Material.STONE_AXE));

                        this.cancel();
                    }
                    else
                        ii[0]++;
                }
                else
                    this.cancel();

            }
        }.runTaskTimer(Objects.requireNonNull(Bukkit.getServer().getPluginManager().getPlugin("Invictools")),  20, 20 * 5); // 20 TICKS IS 1 SECOND NOT 1 TICK
                            */
    }
}

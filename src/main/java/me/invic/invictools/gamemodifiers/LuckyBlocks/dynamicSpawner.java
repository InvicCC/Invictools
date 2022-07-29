package me.invic.invictools.gamemodifiers.LuckyBlocks;

import me.invic.invictools.util.disableStats;
import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.screamingsandals.bedwars.api.BedwarsAPI;

import java.io.File;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class dynamicSpawner
{
    public dynamicSpawner(String config, Player player, Location loc) // config set drop location, yml for each scenario sets amount, type, everything else
    {
        String world = player.getWorld().getName();

        Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("Invictools");
        File Folder = new File(plugin.getDataFolder(), "Spawner");
        File pFile = new File(Folder, config + ".yml");
        final FileConfiguration spawner = YamlConfiguration.loadConfiguration(pFile);

        int delay = spawner.getInt("delay",60);

        if(BedwarsAPI.getInstance().isPlayerPlayingAnyGame(player))
            if(disableStats.getGameType(BedwarsAPI.getInstance().getGameOfPlayer(player)).equalsIgnoreCase("bedfight"))
                delay = delay/2;

        String spawnerText = spawner.getString("text");

        final int[] itemSize = {0};
        final int[] EnchantSize = {0};

        while (true)
        {
            String temp = spawner.getString("items." + itemSize[0] + ".type");
            if (temp != null)
            {
                itemSize[0]++;
            }
            else
                break;
        }

        Random rand = new Random();
        final int[] i = {0};
        int finalItemSize = itemSize[0];
        int delayL = delay * 20;
        final boolean[] cancel = {false};
        new BukkitRunnable()
        {
            @Override
            public void run()
            {
                if (!player.getLocation().getWorld().getName().equalsIgnoreCase(world) || cancel[0])
                    this.cancel();
                else
                {
                    int randnum = rand.nextInt(finalItemSize);

                    String nextItem = spawner.getString("items." + randnum + ".type");
                    List<String> enchants = spawner.getStringList("items." + randnum + ".data.enchants"); // get item enchant names
                    List<String> enchantsLevels = spawner.getStringList("items." + randnum + ".data.levels"); // get item enchant levels
                    int nextItemAmount = spawner.getInt("items." + randnum + ".amount");

                    List<String> Lore = spawner.getStringList("items." + randnum + ".data.lore"); // get item lore
                    String name = spawner.getString("items." + randnum + ".data.name"); // get item name

                    EnchantSize[0] = enchants.size();
                    ItemStack drop = new ItemStack(Material.valueOf(nextItem), nextItemAmount);
                    ItemMeta meta = drop.getItemMeta();

                    if (Lore.size() != 0)
                        meta.setLore(Lore);
                    if (name != null)
                        meta.setDisplayName(name);

                    drop.setItemMeta(meta);

                    while (true) // add another one of these for potion data AND CUSTOM NAMES / LORE / NBT FOR OTHER INTERACTIONS, however the fuck that works
                    {
                        if (i[0] < EnchantSize[0] && EnchantSize[0] != 0)
                        {
                            drop.addUnsafeEnchantment(Objects.requireNonNull(Enchantment.getByKey(NamespacedKey.minecraft(enchants.get(i[0])))), Integer.parseInt(enchantsLevels.get(i[0])));
                            i[0]++;
                        }
                        else
                        {
                            i[0] = 0;
                            break;
                        }
                    }

                    player.getWorld().dropItemNaturally(loc, drop);
                }
            }
        }.runTaskTimer(Objects.requireNonNull(Bukkit.getServer().getPluginManager().getPlugin("Invictools")), delayL, delayL); // repeat every second so you can have a floating timer with named invis armor stand and just add variable that checks if its at delay to do item spawn

        Location ArmorStand1Loc = new Location(player.getLocation().getWorld(), loc.getX(), loc.getY() + .3, loc.getZ());
        ArmorStand as1 = (ArmorStand) player.getWorld().spawnEntity(ArmorStand1Loc, EntityType.ARMOR_STAND);
        ArmorStand as2 = (ArmorStand) player.getWorld().spawnEntity(loc, EntityType.ARMOR_STAND);

        as1.setGravity(false);
        as1.setCustomName(spawnerText);
        as1.setCustomNameVisible(true);
        as1.setVisible(false);

        as2.setGravity(false);
        as2.setCustomName(ChatColor.YELLOW + "Spawns in " + ChatColor.RED + delay + ChatColor.YELLOW + " seconds");
        as2.setCustomNameVisible(true);
        as2.setVisible(false);

        final int[] resettableDelay = {delay - 1};
        int finalDelay = delay;
        new BukkitRunnable()
        {
            @Override
            public void run()
            {
                if (!player.getLocation().getWorld().getName().equalsIgnoreCase(world))
                {
                    as1.remove();
                    as2.remove();
                    cancel[0] = true;
                    this.cancel();
                }

                if (resettableDelay[0] == -1)
                    resettableDelay[0] = finalDelay - 1;

                as2.setCustomName(ChatColor.YELLOW + "Spawns in " + ChatColor.RED + resettableDelay[0] + ChatColor.YELLOW + " seconds");
                resettableDelay[0]--;
            }
        }.runTaskTimer(Objects.requireNonNull(Bukkit.getServer().getPluginManager().getPlugin("Invictools")), 20L, 20L); // repeat every second so you can have a floating timer with named invis armor stand and just add variable that checks if its at delay to do item spawn

    }
}

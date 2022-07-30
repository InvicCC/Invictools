package cc.invic.invictools.gamemodifiers;

import cc.invic.invictools.util.TableToList;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.screamingsandals.bedwars.api.BedwarsAPI;
import org.screamingsandals.bedwars.api.game.ItemSpawnerType;

import java.io.File;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class ItemRain
{
    BedwarsAPI api = BedwarsAPI.getInstance();

    public static void ItemRainLootTable(String tableConfig, int delay, Player player)
    {
        new BukkitRunnable()
        {
            final World gameWorld = player.getWorld();
            List<ItemStack> table;

            @Override
            public void run()
            {
                if (player.getWorld() != gameWorld || !IsEnabled)
                    this.cancel();

                table = new TableToList().CHESTTABLE(tableConfig);
                new ItemRain(table, player);
            }
        }.runTaskTimer(Objects.requireNonNull(Bukkit.getServer().getPluginManager().getPlugin("Invictools")), delay, delay);
    }

    public static void ItemRainConfig(String tableConfig, int delay, Player player)
    {
        new BukkitRunnable()
        {
            final World gameWorld = player.getWorld();
            final Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("Invictools");
            final File Folder = new File(plugin.getDataFolder(), "LootTables");
            final File pFile = new File(Folder, tableConfig + ".yml");
            final FileConfiguration balls = YamlConfiguration.loadConfiguration(pFile);
            List<ItemStack> table;

            @Override
            public void run()
            {
                if (player.getWorld() != gameWorld || !IsEnabled)
                    this.cancel();

                table = (List<ItemStack>) balls.getList("rainfall");
                new ItemRain(table, player);
            }
        }.runTaskTimer(Objects.requireNonNull(Bukkit.getServer().getPluginManager().getPlugin("Invictools")), delay, delay);
    }

    public ItemRain(List<ItemStack> table, Player player)
    {
        int shift;
        switch (api.getGameOfPlayer(player).getName())
        {
            case "MonasteryV2":
                shift = 500;
                break;
            default:
                shift = 0;
        }

        final Random rand = new Random();
        int x;
        int z;

        for (ItemStack item : table)
        {
            int amount = item.getAmount();
            x = (rand.nextInt(200) - 100) + shift;
            z = (rand.nextInt(200) - 100) + shift;
            Location loc = new Location(player.getWorld(), x, 150, z);
            switch (item.getType().toString())
            {
                case "EMERALD":
                    ItemSpawnerType em = api.getItemSpawnerTypeByName("emerald");
                    item = (em.getStack());
                    break;
                case "DIAMOND":
                    ItemSpawnerType dia = api.getItemSpawnerTypeByName("diamond");
                    item = (dia.getStack());
                    break;
                case "GOLD_INGOT":
                    ItemSpawnerType gold = api.getItemSpawnerTypeByName("gold");
                    item = (gold.getStack());
                    break;
                case "IRON_INGOT":
                    ItemSpawnerType iron = api.getItemSpawnerTypeByName("iron");
                    item = (iron.getStack());
                    break;
            }
            item.setAmount(amount);
            player.getWorld().dropItemNaturally(loc, item);
        }
    }

    public static boolean IsEnabled = true;
}

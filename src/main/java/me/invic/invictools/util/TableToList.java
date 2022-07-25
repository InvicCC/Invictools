package me.invic.invictools.util;

import me.invic.invictools.commands.Commands;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.loot.LootContext;
import org.bukkit.loot.LootTable;
import org.bukkit.loot.LootTables;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.util.List;
import java.util.Random;

public class TableToList
{
    public List<ItemStack> CHESTTABLE(String tableConfig)
    {
        LootTable loot = LootTables.valueOf(tableConfig).getLootTable();
        try
        {
            return replaceCurrency((List<ItemStack>) loot.populateLoot(new Random(), new LootContext.Builder(Commands.MasterPlayer.getLocation()).build()));
        }
        catch (java.lang.IllegalArgumentException e)
        {
            System.out.println(ChatColor.RED + "WARNING: LootTable was invalid, defaulting to End City");
            return replaceCurrency((List<ItemStack>) LootTables.END_CITY_TREASURE.getLootTable().populateLoot(new Random(), new LootContext.Builder(Commands.MasterPlayer.getLocation()).build()));
        }
    }

    public List<ItemStack> CONFIGTABLE(String tableConfig)
    {
        Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("Invictools");
        File Folder = new File(plugin.getDataFolder(), "LootTables");
        File pFile = new File(Folder, tableConfig + ".yml");
        FileConfiguration balls = YamlConfiguration.loadConfiguration(pFile);

        return replaceCurrency((List<ItemStack>) balls.getList("rainfall"));
    }

    public List<ItemStack> replaceCurrency(List<ItemStack> table)
    {
        /* This should work but it doesnt lol
        BedwarsAPI api = BedwarsAPI.getInstance();
        for (ItemStack item : table)
        {
            int amount = item.getAmount();
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
        }

         */
        return table;
    }
}

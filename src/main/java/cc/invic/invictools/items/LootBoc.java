package cc.invic.invictools.items;

import cc.invic.invictools.util.TableToList;
import org.bukkit.Location;
import org.bukkit.block.Chest;
import org.bukkit.inventory.ItemStack;
import org.screamingsandals.bedwars.api.BedwarsAPI;
import org.screamingsandals.bedwars.api.game.ItemSpawnerType;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class LootBoc
{
    public static void fillBoc(Location loc)
    {
        BedwarsAPI api = BedwarsAPI.getInstance();
        lootTable.add("END_CITY_TREASURE");
        lootTable.add("ABANDONED_MINESHAFT");
        lootTable.add("BASTION_TREASURE");
        lootTable.add("BASTION_BRIDGE");
        lootConfig.add("items");
        lootConfig.add("elytra");
        lootConfig.add("util");
        lootConfig.add("defence");
        lootConfig.add("nothing0");
        lootConfig.add("nothing1");
        lootConfig.add("iron");
        lootConfig.add("gems");

        Chest chest = (Chest) loc.getBlock().getState();
        int po = chest.getInventory().getSize();
        Random rand = new Random();

        String table;
        List<ItemStack> loot;

        switch (rand.nextInt(2))
        {
            case 0:
                table = lootTable.get(rand.nextInt(lootTable.size()));
                loot = new TableToList().CHESTTABLE(table);
                break;
            case 1:
                table = lootConfig.get(rand.nextInt(lootTable.size()));
                loot = new TableToList().CONFIGTABLE(table);
                break;
            default:
                loot = new TableToList().CHESTTABLE("BASTION_BRIDGE");
        }

        for (ItemStack item : loot)
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
            chest.getInventory().setItem(rand.nextInt(po), item);
        }
    }

    public static List<String> lootConfig = new ArrayList<>();
    public static List<String> lootTable = new ArrayList<>();
}

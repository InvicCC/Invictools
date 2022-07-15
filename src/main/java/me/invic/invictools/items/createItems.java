package me.invic.invictools.items;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class createItems
{
    public ItemStack BOUNCE()
    {
        ItemStack block = new ItemStack(Material.LIME_DYE);
        ItemMeta meta = block.getItemMeta();

        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.translateAlternateColorCodes('&', "&7Flings you high into the air when right clicked")); //
        lore.add(ChatColor.translateAlternateColorCodes('&', "&7Good for saving yourself from the void!"));
        lore.add(ChatColor.translateAlternateColorCodes('&', "&7Also disables fall damage while used!"));

        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&a&lSelf-Launcher"));
        meta.setLore(lore);
        block.setItemMeta(meta);

        return block;
    }

    public ItemStack FIREBALL()
    {
        ItemStack block = new ItemStack(Material.FIRE_CHARGE);
        ItemMeta meta = block.getItemMeta();

        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.translateAlternateColorCodes('&', "&7Shoots a Fireball")); //

        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&c&lFireball"));
        meta.setLore(lore);
        block.setItemMeta(meta);

        return block;
    }

    public ItemStack MODBOW()
    {
        ItemStack block = new ItemStack(Material.BOW);
        ItemMeta meta = block.getItemMeta();

        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.translateAlternateColorCodes('&', "&7Multi-purposed Ranged Weapon")); //
        lore.add(ChatColor.translateAlternateColorCodes('&', "&7Left Click to Cycle Modes"));

        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&a&lMod Bow"));
        meta.setLore(lore);
        block.setItemMeta(meta);

        block.addUnsafeEnchantment(Enchantment.DURABILITY, 10);

        return block;
    }

    public ItemStack FIREBOW()
    {
        ItemStack block = new ItemStack(Material.BOW);
        ItemMeta meta = block.getItemMeta();

        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.translateAlternateColorCodes('&', "&7Does what it says, somehow."));

        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&e&lRideable Fireball Launcher"));
        meta.setLore(lore);
        block.setItemMeta(meta);

        block.addUnsafeEnchantment(Enchantment.ARROW_FIRE, 1);

        return block;
    }

    public ItemStack SNAP()
    {
        ItemStack block = new ItemStack(Material.ICE);
        ItemMeta meta = block.getItemMeta();

        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.translateAlternateColorCodes('&', "&7Turns every placed block within 20 meters into ice")); //
        lore.add(ChatColor.translateAlternateColorCodes('&', "&7Good for getting rid of hard to destroy blocks"));

        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&c&lSnap Freeze"));
        meta.setLore(lore);
        block.setItemMeta(meta);

        block.addUnsafeEnchantment(Enchantment.FROST_WALKER, 10);

        return block;
    }

    public ItemStack FIRESTICK()
    {
        ItemStack block = new ItemStack(Material.BLAZE_ROD);
        ItemMeta meta = block.getItemMeta();

        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.translateAlternateColorCodes('&', "&7It's like a magic toy stick, but slightly cooler"));

        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&e&lFire Stick"));
        meta.setLore(lore);
        block.setItemMeta(meta);

        return block;
    }

    public ItemStack DAREBONE()
    {
        ItemStack block = new ItemStack(Material.BONE);
        ItemMeta meta = block.getItemMeta();

        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.translateAlternateColorCodes('&', "&7Summons a legend"));
        lore.add(ChatColor.translateAlternateColorCodes('&', "&cLimited Lifespan!"));

        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&f&lDare Devil Bone"));
        meta.setLore(lore);
        block.setItemMeta(meta);

        return block;
    }

    public ItemStack RIDEBOW()
    {
        ItemStack block = new ItemStack(Material.BOW);
        ItemMeta meta = block.getItemMeta();

        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.translateAlternateColorCodes('&', "&7Fires Rideable Arrows"));

        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&7&lRideable Bow"));
        meta.setLore(lore);
        block.setItemMeta(meta);

        return block;
    }

    public ItemStack INFDAREBONE()
    {
        ItemStack block = new ItemStack(Material.BONE);
        ItemMeta meta = block.getItemMeta();

        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.translateAlternateColorCodes('&', "&7Summons a legend"));
        lore.add(ChatColor.translateAlternateColorCodes('&', "&7Doesnt despawn"));

        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&f&lDare Devil Bone"));
        meta.setLore(lore);
        block.setItemMeta(meta);

        return block;
    }


    public ItemStack BLIND()
    {
        ItemStack block = new ItemStack(Material.INK_SAC);
        ItemMeta meta = block.getItemMeta();

        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.translateAlternateColorCodes('&', "&7Blinds everyone within 20 blocks of you for 5 seconds")); //

        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&0&lBlooper"));
        meta.setLore(lore);
        block.setItemMeta(meta);

        return block;
    }

    public ItemStack ANVIL()
    {
        ItemStack block = new ItemStack(Material.PAPER);
        ItemMeta meta = block.getItemMeta();

        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.translateAlternateColorCodes('&', "&7Sends a barrage of anvils raining down where you're looking")); //

        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&7&lAnvil Rain"));
        meta.setLore(lore);
        block.setItemMeta(meta);

        return block;
    }

    public ItemStack TNT()
    {
        ItemStack block = new ItemStack(Material.PAPER);
        ItemMeta meta = block.getItemMeta();

        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.translateAlternateColorCodes('&', "&7Spawns lots of tnt where you're looking")); //

        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&c&lTnT Rain"));
        meta.setLore(lore);
        block.setItemMeta(meta);

        return block;
    }

    public ItemStack LOOTBOX()
    {
        ItemStack block = new ItemStack(Material.CHEST);
        ItemMeta meta = block.getItemMeta();

        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.translateAlternateColorCodes('&', "&7Contains loot!")); //

        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&e&lRandom Loot Box"));
        meta.setLore(lore);
        block.setItemMeta(meta);

        return block;
    }

    public ItemStack MOBS()
    {
        ItemStack block = new ItemStack(Material.PAPER);
        ItemMeta meta = block.getItemMeta();

        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.translateAlternateColorCodes('&', "&7Summons mobs where you're looking")); //

        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&c&lHostile Ambush"));
        meta.setLore(lore);
        block.setItemMeta(meta);

        return block;
    }

    public ItemStack WOOL()
    {
        ItemStack block = new ItemStack(Material.TNT);
        ItemMeta meta = block.getItemMeta();

        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.translateAlternateColorCodes('&', "&7Violently ignites all wool in a 20 block radius")); //

        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&c&lWool Detonator"));
        meta.setLore(lore);
        block.setItemMeta(meta);

        block.addUnsafeEnchantment(Enchantment.ARROW_DAMAGE, 10);

        return block;
    }

    public ItemStack getRandomItem()
    {
        List<String> items = getItemNames();
        Collections.shuffle(items);

        // exclusion from randomizer
        if (items.get(0).equalsIgnoreCase("infdarebone"))
            return getByName(items.get(1));
        else
            return getByName(items.get(0));
    }

    public ItemStack getByName(String item)
    {
        item = item.toLowerCase(Locale.ROOT);
        switch (item)
        {
            case "bounce":
                return BOUNCE();
            case "blind":
                return BLIND();
            case "anvil":
                return ANVIL();
            case "tnt":
                return TNT();
            case "mobs":
                return MOBS();
            case "wool":
                return WOOL();
            case "lootbox":
                return LOOTBOX();
            case "snap":
                return SNAP();
            case "firestick":
                return FIRESTICK();
            case "ridebow":
                return RIDEBOW();
            case "modbow":
                return MODBOW();
            case "firebow":
                return FIREBOW();
            case "fireball":
                return FIREBALL();
            case "infdarebone":
                return INFDAREBONE();
            case "darebone":
                return DAREBONE();
            default:
                return null;
        }
    }

    public static List<String> getItemNames()
    {
        List<String> items = new ArrayList<>();
        items.add("bounce");
        items.add("blind");
        items.add("darebone");
        items.add("infdarebone");
        items.add("anvil");
        items.add("tnt");
        items.add("mobs");
        items.add("wool");
        items.add("ridebow");
        items.add("lootbox");
        items.add("snap");
        items.add("firestick");
        items.add("modbow");
        items.add("firebow");
        items.add("fireball");
        return items;
    }
}

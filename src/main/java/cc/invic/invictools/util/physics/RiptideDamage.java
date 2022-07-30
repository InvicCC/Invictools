package cc.invic.invictools.util.physics;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRiptideEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

public class RiptideDamage implements Listener
{
    private static boolean checkWorlds = false;

    @EventHandler
    public void riptideUsage(PlayerRiptideEvent e)
    {
        ItemStack trident = e.getItem();
        int lvl = trident.getEnchantmentLevel(Enchantment.RIPTIDE);
        int maxDura = Material.TRIDENT.getMaxDurability();
        int change;
        switch (lvl)
        {
            case 1:
                change = 10;
                break;
            case 2:
                change = 10;
                break;
            case 3:
                change = 5;
                break;
            case 5:
                change = 5;
                break;
            case 10:
                change = 3;
                break;
            case 20:
                change = 3;
                break;
            default:
                change = 10;
        }
        int mathed = maxDura / change;

        ItemMeta meta = trident.getItemMeta();
        int damage = ((Damageable) meta).getDamage();

        if (mathed + damage >= maxDura)
        {
            trident.setAmount(trident.getAmount() - 1);
            e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.ENTITY_ITEM_BREAK, 1F, 1F);
        }

        ((Damageable) meta).setDamage(mathed + damage);
        trident.setItemMeta(meta);
    }

    public static void setWorldCheck(boolean bool)
    {
        if (bool)
            checkWorlds = true;
        if (!bool)
            checkWorlds = false;
    }
}

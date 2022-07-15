package me.invic.invictools.gamemodifiers;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.io.File;
import java.util.List;

public class Haunt
{
    public Haunt(Player player, String config)
    {
        player.setGameMode(GameMode.CREATIVE);
        player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 999999999, 0, false, false));
        player.getInventory().setItem(39, new ItemStack(Material.DRAGON_EGG));

        Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("Invictools");
        File Folder = new File(plugin.getDataFolder(), "Haunt");
        File pFile = new File(Folder, config + ".yml");
        FileConfiguration balls = YamlConfiguration.loadConfiguration(pFile);
        List<String> commands = balls.getStringList("haunts");

        for (String string : commands)
        {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), string + player.getName());
        }
    }
}

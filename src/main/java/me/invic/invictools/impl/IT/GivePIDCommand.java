package me.invic.invictools.impl.IT;

import me.invic.invictools.commandManagerLib.SubCommand;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.List;
import java.util.Map;

import static org.bukkit.Bukkit.getServer;

public class GivePIDCommand implements SubCommand
{
    @Override
    public String getName()
    {
        return "givePID";
    }

    @Override
    public String getDescription()
    {
        return null;
    }

    @Override
    public String getSyntax()
    {
        return "/it givePID";
    }

    @Override
    public String getPermission()
    {
        return "invic.invictools";
    }

    @Override
    public List<String> getTabCompletion(int index, String[] args)
    {
        return null;
    }

    @Override
    public void perform(CommandSender sender, String[] args)
    {
        Plugin Invictools = getServer().getPluginManager().getPlugin("Invictools");
        Player player = (Player) sender;
        FileConfiguration config = Invictools.getConfig();
        ItemStack item = (ItemStack) config.get("pid");
        final Map<Integer, ItemStack> map = player.getInventory().addItem(item);
        player.playSound(player.getLocation(), Sound.ENTITY_CHICKEN_EGG, 1, 1);
        for (final ItemStack i : map.values())
        {
            player.getWorld().dropItemNaturally(player.getLocation(), i);
        }
    }
}
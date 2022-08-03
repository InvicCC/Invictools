package me.invic.invictools.impl.IT;

import me.invic.invictools.commandManagerLib.SubCommand;
import me.invic.invictools.gamemodifiers.PotionEffects.EffectSometimes;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.List;

import static org.bukkit.Bukkit.getServer;

public class PrintitemdataCommand implements SubCommand
{
    @Override
    public String getName()
    {
        return "printitemdata";
    }

    @Override
    public String getDescription()
    {
        return null;
    }

    @Override
    public String getSyntax()
    {
        return "/it printitemdata";
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
        System.out.println(player.getInventory().getItem(40));
        FileConfiguration config = Invictools.getConfig();
        config.set("pid", player.getInventory().getItem(40));
        Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("Invictools");
        plugin.saveConfig();
        sender.sendMessage(ChatColor.AQUA + "Logged the ItemStack in your offhand in the console and config : " + player.getInventory().getItem(40).getType());
    }
}
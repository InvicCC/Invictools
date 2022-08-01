package me.invic.invictools.impl.IT;

import me.invic.invictools.commandManagerLib.SubCommand;
import me.invic.invictools.gamemodifiers.CloseElytra;
import me.invic.invictools.gamemodifiers.Manhunt.ManhuntMain;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

import static me.invic.invictools.commands.OldCommands.ProximityElytra;

public class ProximityElytraSingleCommand implements SubCommand
{
    @Override
    public String getName()
    {
        return "ProximityElytraSingle";
    }

    @Override
    public String getDescription()
    {
        return null;
    }

    @Override
    public String getSyntax()
    {
        return "/it ProximityElytraSingle";
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
        Player p = Bukkit.getPlayer(args[2]);

        ItemStack item = new ItemStack(Material.ELYTRA);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.YELLOW + "Proximity Elytra");
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.translateAlternateColorCodes('&', "&cStay near your teammates or you'll lose it!"));
        meta.setLore(lore);
        item.setItemMeta(meta);

        new CloseElytra(Double.parseDouble(args[1]), p, item);
        p.sendMessage(ChatColor.AQUA + "You will now receive an Elytra when near your teammates");
        ProximityElytra.put(p, true);
    }
}
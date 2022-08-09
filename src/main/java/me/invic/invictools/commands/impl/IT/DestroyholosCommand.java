package me.invic.invictools.commands.impl.IT;

import me.invic.invictools.commands.commandManagerLib.SubCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.List;

public class DestroyholosCommand implements SubCommand
{
    @Override
    public String getName()
    {
        return "destroyholos";
    }

    @Override
    public String getDescription()
    {
        return null;
    }

    @Override
    public String getSyntax()
    {
        return "/it destroyholos";
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
        Player p = (Player) sender;
        for (Entity e : p.getWorld().getEntitiesByClass(ArmorStand.class))
        {
            if (!e.hasMetadata("holo"))
            {
                e.remove();
            }
        }
        //  Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "minecraft:kill @e[type=minecraft:armor_stand]");
        sender.sendMessage(ChatColor.AQUA + "All non scoreboard holographic displays have been deleted!");
    }
}
package me.invic.invictools.commands;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class InvicSpecCommand implements CommandExecutor
{

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if (sender instanceof Player && !((Player) sender).getWorld().getName().equals("bwlobby"))
        {
            Location location = new Location(sender.getServer().getWorld("bwlobby"), 0, 60, 0);
            ((Player) sender).teleport(location);
            ((Player) sender).setGameMode(GameMode.SURVIVAL);
        }
        if (sender instanceof Player && ((Player) sender).getWorld().getName().equals("bwlobby"))
        {
            if (!sender.getServer().getPlayer("Invictable").getWorld().getName().equals("bwlobby"))
            {
                sender.sendMessage(ChatColor.AQUA + "Invic is in the lobby");
            }
            else
            {
                if (sender.getServer().getPlayer("Invictable") != null)
                {
                    ((Player) sender).setGameMode(GameMode.SPECTATOR);
                    ((Player) sender).teleport(sender.getServer().getPlayer("Invictable").getLocation());
                }
                else
                {
                    sender.sendMessage(ChatColor.AQUA + "Invic is offline");
                }
            }

        }
        return true;
    }
}

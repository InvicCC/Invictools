package cc.invic.invictools.impl;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.screamingsandals.bedwars.api.BedwarsAPI;

public class InvicSpecCommand implements CommandExecutor
{
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if (sender instanceof Player && ((Player) sender).getWorld().getName().equals("bwlobby") && sender.getServer().getPlayer("Invictable") != null)
        {
            if (sender.getServer().getPlayer("Invictable") != null)
            {
                if (sender.getServer().getPlayer("Invictable").getWorld().getName().equals("bwlobby"))
                {
                    sender.sendMessage(ChatColor.AQUA + "Invic is in the lobby");
                }
                else
                {
                        if(!BedwarsAPI.getInstance().isPlayerPlayingAnyGame(Bukkit.getPlayer("Invictable")))
                        {
                            ((Player) sender).teleport(sender.getServer().getPlayer("Invictable").getLocation());
                            ((Player) sender).setGameMode(GameMode.SPECTATOR);
                        }
                        else
                            sender.sendMessage(ChatColor.AQUA+"Invic is in a game. try /queue spec");
                }
            }
            else
            {
                sender.sendMessage(ChatColor.AQUA + "Invic is offline");
            }
        }
        return true;
    }
}

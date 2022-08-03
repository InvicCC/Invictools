package me.invic.invictools.impl.IT;

import me.invic.invictools.commandManagerLib.SubCommand;
import me.invic.invictools.gamemodifiers.Manhunt.ManhuntMain;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.Objects;

import static me.invic.invictools.commands.OldCommands.InfiniteTotems;

public class TotemsCommand implements SubCommand
{
    @Override
    public String getName()
    {
        return "totems";
    }

    @Override
    public String getDescription()
    {
        return null;
    }

    @Override
    public String getSyntax()
    {
        return "/it totems";
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
        if (args[1].equalsIgnoreCase("all"))
        {
            for (Player player : Bukkit.getOnlinePlayers())
            {
                InfiniteTotems.put(player, true);
                player.sendMessage(ChatColor.AQUA + "You are now nearly immortal");
                World world = player.getWorld();
                new BukkitRunnable() // kills at max 1 minute after match ends or player dies and leaves
                {
                    @Override
                    public void run()
                    {
                        if (player == null)
                        {
                            this.cancel();
                        }

                        if (player.getWorld() != world)
                        {
                            InfiniteTotems.remove(player);
                            player.sendMessage(ChatColor.AQUA + "Totem disabled");
                            this.cancel();
                        }
                    }
                }.runTaskTimer(Objects.requireNonNull(Bukkit.getServer().getPluginManager().getPlugin("Invictools")), 0L, 120L);
            }
        }
        else
        {
            Player player = Bukkit.getPlayer(args[1]);
            InfiniteTotems.put(Bukkit.getPlayer(args[1]), true);
            player.sendMessage(ChatColor.AQUA + "You are now nearly immortal");
            World world = player.getWorld();
            new BukkitRunnable() // kills at max 1 minute after match ends or player dies and leaves
            {
                @Override
                public void run()
                {
                    if (player == null)
                    {
                        this.cancel();
                    }

                    if (player.getWorld() != world)
                    {
                        InfiniteTotems.remove(player);
                        player.sendMessage(ChatColor.AQUA + "Totem disabled");
                        this.cancel();
                    }
                }
            }.runTaskTimer(Objects.requireNonNull(Bukkit.getServer().getPluginManager().getPlugin("Invictools")), 0L, 120L);
        }
    }
}
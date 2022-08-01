package me.invic.invictools.impl.IT;

import me.invic.invictools.commandManagerLib.SubCommand;
import me.invic.invictools.commands.OldCommands;
import me.invic.invictools.cosmetics.NormalKillHandler;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.Objects;

public class NormalKillPreview implements SubCommand
{
    @Override
    public String getName()
    {
        return "NormalKillPreview";
    }

    @Override
    public String getDescription()
    {
        return null;
    }

    @Override
    public String getSyntax()
    {
        return "/it NormalKillPreview";
    }

    @Override
    public String getPermission()
    {
        return "invic.all";
    }

    @Override
    public List<String> getTabCompletion(int index, String[] args)
    {
        return null;
    }

    @Override
    public void perform(CommandSender sender, String[] args)
    {
        if (args.length >= 2 && args[2].equalsIgnoreCase("cp"))
        {
            Player player = (Player) sender;
            Location oldloc = player.getLocation();
            Location tp = new Location(player.getLocation().getWorld(), 6.5, 126, 6.5, 134, -10);
            String effect = args[1];

            player.teleport(tp);
            Location loc = new Location(player.getLocation().getWorld(), 0.5, 129, 0.5);

            new BukkitRunnable()
            {
                @Override
                public void run()
                {
                    new NormalKillHandler().effectSwitch(player, player, loc, effect);
                }
            }.runTaskLater(OldCommands.Invictools, 20L);

            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.BOLD + "Hold crouch to cancel"));
            BukkitRunnable runnable2 = new BukkitRunnable()
            {
                final int cancelAt = 20 * 5;
                int i = 0;

                @Override
                public void run()
                {
                    i++;
                    if ((player.getLocation().getWorld() == oldloc.getWorld() && i == cancelAt) || player.isSneaking())
                    {
                        player.teleport(oldloc);
                        this.cancel();
                    }

                    if (i == cancelAt)
                        this.cancel();
                }
            };
            runnable2.runTaskTimer(Objects.requireNonNull(Bukkit.getServer().getPluginManager().getPlugin("Invictools")), 10, 1);
        }
    }
}
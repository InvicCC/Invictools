package me.invic.invictools.impl.IT;

import me.invic.invictools.commandManagerLib.SubCommand;
import me.invic.invictools.commands.OldCommands;
import me.invic.invictools.cosmetics.bedbreaks.BedBreaks;
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

public class BedBreakTestCommand implements SubCommand
{
    @Override
    public String getName()
    {
        return "BedBreakTest";
    }

    @Override
    public String getDescription()
    {
        return null;
    }

    @Override
    public String getSyntax()
    {
        return "/it BedBreakTest";
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
            Location tp = new Location(player.getLocation().getWorld(), 250.5, 128, 283.5, 180, 0);
            String effect = args[1];

            final int[] wait = new int[1];

            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.BOLD + "Hold crouch to cancel"));
            player.teleport(tp);
            Location loc = new Location(player.getLocation().getWorld(), 250.5, 128.5, 278.5);

            new BukkitRunnable()
            {
                @Override
                public void run()
                {
                    new BedBreaks().handle(loc, player, loc.getBlock().getType().toString(), false, effect);
                }
            }.runTaskLater(OldCommands.Invictools, 20L);

            BukkitRunnable runnable = new BukkitRunnable()
            {
                @Override
                public void run()
                {
                    switch (effect)
                    {
                        case "Lightning":
                            wait[0] = 50; // 2.5 sec
                            break;
                        case "Fireworks":
                            wait[0] = 80;
                            break;
                        case "Ranked":
                            wait[0] = 70;
                            break;
                        case "Tornado":
                            wait[0] = 40;
                            break;
                        case "Holo":
                            wait[0] = 60;
                            break;
                        default:
                            wait[0] = 50;
                    }

                    BukkitRunnable runnable2 = new BukkitRunnable()
                    {
                        final int cancelAt = wait[0];
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
                    runnable2.runTaskTimer(Objects.requireNonNull(Bukkit.getServer().getPluginManager().getPlugin("Invictools")), 30, 1);
                }
            };
            runnable.runTaskLater(Objects.requireNonNull(Bukkit.getServer().getPluginManager().getPlugin("Invictools")), 10L);
        }
    }
}
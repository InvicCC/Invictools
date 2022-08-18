package me.invic.invictools.commands;

import me.invic.invictools.cosmetics.VictoryDances.VictoryDanceHandler;
import me.invic.invictools.cosmetics.cage;
import me.invic.invictools.cosmetics.cageHandler;
import me.invic.invictools.cosmetics.statisticRequirments;
import me.invic.invictools.gamemodifiers.WardenSpawner;
import me.invic.invictools.util.fixes.LobbyInventoryFix;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class cageCommands implements CommandExecutor, TabExecutor
{
    static File Folder = new File(OldCommands.Invictools.getDataFolder(), "PlayerData");

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        boolean perms = false;
        if (sender instanceof Player)
        {
            if (sender.hasPermission("invic.invictools"))
            {
                perms = true;
            }
        } else
        {
            perms = true;
        }

        if(args.length == 2 && args[0].equalsIgnoreCase("select") && sender instanceof Player p)
        {
            selectCage(p, statisticRequirments.loadedCages.get(args[1]));
        }
        else if(args.length == 3 && args[0].equalsIgnoreCase("select") && perms)
        {
            selectCage(Bukkit.getPlayer(args[2]), statisticRequirments.loadedCages.get(args[1]));
        }
        else if(args.length == 2 && args[0].equalsIgnoreCase("preview") && sender instanceof Player p)
        {
            previewCage(p,statisticRequirments.loadedCages.get(args[1]));
        }
        else
            sender.sendMessage(ChatColor.RED+"Unknown Command");

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args)
    {
        boolean perms = false;
        if (sender instanceof Player)
        {
            if (sender.hasPermission("invic.invictools"))
            {
                perms = true;
            }
        } else
        {
            perms = true;
        }

        List<String> tabComplete = new ArrayList<>();

        if(args.length==0)
        {
            tabComplete.add("select");
            tabComplete.add("preview");
        }
        else if(args.length == 1 && args[0].equalsIgnoreCase("select") || args.length == 2 && args[0].equalsIgnoreCase("preview"))
        {
            tabComplete.addAll(statisticRequirments.loadedCages.keySet());
        }
        else if(args.length == 2 && args[0].equalsIgnoreCase("select"))
        {
            for (Player p:Bukkit.getOnlinePlayers())
            {
                tabComplete.add(p.getName());
            }
        }

        return tabComplete;
    }

    void selectCage(Player p, cage cage)
    {
        final File file = new File(Folder,p.getUniqueId()+".yml");
        final FileConfiguration pcfg = YamlConfiguration.loadConfiguration(file);
        if (cage.checkReq(p))
        {
            pcfg.set("cage",cage.getName());
            try
            {
                pcfg.save(file);
                p.sendMessage(ChatColor.AQUA+"Your cage has been set.");
            }
            catch (IOException e)
            {
                p.sendMessage(ChatColor.RED+"Error saving your cage");
                e.printStackTrace();
            }
        }
        else
        {
            p.sendMessage(ChatColor.RED+"You do not have this cage unlocked");
        }
    }

    void previewCage(Player p, cage cage)
    {
        Location old = p.getLocation();
        Location cageloc = new WardenSpawner().locationFromConfig(OldCommands.Invictools.getConfig().getString("CagePreview.Cage","bwlobby;291.5;140;288.5;180;0"));
        OfflinePlayer pl = p;
        p.teleport(new WardenSpawner().locationFromConfig(OldCommands.Invictools.getConfig().getString("CagePreview.Player","bwlobby;302.5;133;288.5;90;-24")));
        new cageHandler().buildCage(p
                ,cageloc
                ,cage);
        p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.BOLD + "Hold crouch to cancel"));
        BukkitRunnable runnable2 = new BukkitRunnable()
        {
            final int cancelAt = 10*20;
            int i = 0;

            @Override
            public void run()
            {
                i++;
                if ((p.getLocation().getWorld() == old.getWorld() && i == cancelAt) || p.isSneaking())
                {
                    p.teleport(old);
                    new cageHandler().destroyCage(pl,cageloc);
                    this.cancel();
                }

                if (i == cancelAt)
                {
                    new cageHandler().destroyCage(pl,cageloc);
                    this.cancel();
                }
            }
        };
        runnable2.runTaskTimer(OldCommands.Invictools, 10, 1);
    }
}

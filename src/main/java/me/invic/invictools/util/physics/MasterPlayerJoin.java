package me.invic.invictools.util.physics;

import me.invic.invictools.commands.OldCommands;
import me.invic.invictools.commands.toggleCommands;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class MasterPlayerJoin implements Listener
{
    @EventHandler
    public void WorldSwitch(PlayerChangedWorldEvent e) // update to dynamically grab valid worlds
    {
        probe();

        if (OldCommands.MasterPlayer instanceof Player && e.getPlayer().getName().equalsIgnoreCase(OldCommands.MasterPlayer.getName()))
        {
            if (e.getPlayer().getWorld().getName().equalsIgnoreCase("Tree"))
                new grabSandstone(e.getPlayer());
        }
    }

    @EventHandler
    public void Join(PlayerJoinEvent e)
    {
        /*
        Plugin plugin = Commands.Invictools;
        final FileConfiguration config = plugin.getConfig();
        String masterplayer = config.getString("masterplayer","Invictable");
        Commands.MasterPlayer = Bukkit.getPlayer(masterplayer);
         */
        probe();
    }

    @EventHandler
    public void leave(PlayerQuitEvent e)
    {
        new BukkitRunnable()
        {
            @Override
            public void run()
            {
                probe();
            }
        }.runTaskLater(OldCommands.Invictools, 20L);
    }

    public void probe()
    {
      //  if(Commands.MasterPlayer instanceof Player)

        try
        {
            OldCommands.MasterPlayer.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(" "));
        }
        catch (NullPointerException e)
        {
            if(!toggleCommands.isHosting && Bukkit.getOnlinePlayers().size() > 0)
            {
                Player next = Bukkit.getOnlinePlayers().iterator().next();
                FileConfiguration config = OldCommands.Invictools.getConfig();
                OldCommands.MasterPlayer = next;
                config.set("masterplayer", next.getName());
                OldCommands.Invictools.saveConfig();
              //  System.out.println("swapping null");
            }
        }

        {
            Player p = OldCommands.MasterPlayer;
            if(!p.isOnline())
            {
                if(!toggleCommands.isHosting && Bukkit.getOnlinePlayers().size() > 0)
                {
                    Player next = Bukkit.getOnlinePlayers().iterator().next();
                    FileConfiguration config = OldCommands.Invictools.getConfig();
                    OldCommands.MasterPlayer = next;
                    config.set("masterplayer", next.getName());
                    OldCommands.Invictools.saveConfig();
                 //   System.out.println("swapping !isonline");
                }
            }
        }
    }
}

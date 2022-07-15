package me.invic.invictools.cosmetics;

import java.io.File;
import java.io.IOException;

import me.invic.invictools.Invictools;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class ConfigHandler implements Listener
{
    private Invictools main;

    public ConfigHandler(Invictools main)
    {
        this.main = main;
    }

        @EventHandler
        public void onJoin(PlayerJoinEvent e)
        {
            final Player player = e.getPlayer();
          //  File pFile = new File(main.getDataFolder().getPath() + "\\PlayerData" + File.separator + player.getUniqueId() + ".yml");
            File Folder = new File(main.getDataFolder(), "PlayerData");
            File pFile = new File(Folder, player.getUniqueId() + ".yml");
            final FileConfiguration playerData = YamlConfiguration.loadConfiguration(pFile);
            if (!pFile.exists())
            {
                try
                {
                    pFile.createNewFile();
                    playerData.createSection("Username");
                    playerData.createSection("Displayname");
                    playerData.createSection("BedBreak");
                    playerData.createSection("ProjTrail");
                    playerData.createSection("FinalKill");
                    playerData.createSection("VictoryDance");
                    playerData.createSection("NormalKill");
                    playerData.createSection("Lobby1");
                    playerData.createSection("tracker");
                    playerData.set("Username", player.getName());
                    playerData.set("Displayname",player.getDisplayName());
                    playerData.set("BedBreak", "Fireworks");
                    playerData.set("ProjTrail", "Crit");
                    playerData.set("FinalKill", "Lightning");
                    playerData.set("VictoryDance", "Firestick");
                    playerData.set("NormalKill", "none");
                    playerData.set("Lobby1", "none");
                    playerData.set("tracker", 0);
                    playerData.save(pFile);
                    System.out.println("[Invictools] player config created for "+player.getName());
                }
                catch (IOException ex)
                {
                    player.sendMessage(ChatColor.YELLOW + "Error creating your playerfile! Try relogging, then tell an admin!");
                }
            }
            else
            {
                if(!playerData.getString("Username").equalsIgnoreCase(player.getName()))
                {
                    try
                    {
                        playerData.set("Username", player.getName());
                        playerData.save(pFile);
                    }
                    catch (IOException ex)
                    {
                        player.sendMessage(ChatColor.YELLOW + "Error updating your playerfile! Try relogging, then tell an admin!");
                    }
                }
            }
        }
}

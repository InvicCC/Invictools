package me.invic.invictools.util.npc;

import me.invic.invictools.commands.OldCommands;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.Objects;

public class BlazeNpc implements Listener
{
    Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("Invictools");
    final FileConfiguration Config = plugin.getConfig();
    List<String> messages = Config.getStringList("PlugMessages");


    @EventHandler
    public void watch(PlayerInteractEntityEvent e)
    {
        if (e.getHand().toString().equalsIgnoreCase("OFF_HAND"))
            return;

        if (e.getRightClicked() instanceof Illusioner && e.getPlayer().getWorld().getName().equalsIgnoreCase("bwlobby"))
        {
            Bukkit.dispatchCommand(e.getPlayer(), "it panel npcpanel");
        }
        else if (e.getRightClicked() instanceof Allay && e.getPlayer().getWorld().getName().equalsIgnoreCase("bwlobby"))
        {
            for (String message : messages)
            {
                e.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', message));
            }
        }
        else if (!e.getPlayer().getWorld().getName().equals("bwlobby") && OldCommands.noShop.get(e.getPlayer()) != null)
        {
            if (OldCommands.noShop.get(e.getPlayer()))
                e.setCancelled(true);
        }
    }

    public void spawnNPC(String type, boolean entity)
    {
        Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("Invictools");
        final FileConfiguration pluginConfig = plugin.getConfig();
        String npctitle = pluginConfig.getString(type + ".title");

        Location loc = new Location(Bukkit.getWorld("bwlobby"), pluginConfig.getDouble(type + ".x"), pluginConfig.getDouble(type + ".y"), pluginConfig.getDouble(type + ".z"), (float) pluginConfig.getDouble(type + ".yaw"), (float) pluginConfig.getDouble(type + ".pitch"));

        if (entity)
        {
            Illusioner blaze = (Illusioner) loc.getWorld().spawnEntity(loc, EntityType.ILLUSIONER);
            blaze.setCustomName(ChatColor.translateAlternateColorCodes('&', npctitle));
            blaze.setAI(false);
            blaze.setInvulnerable(true);
            blaze.setSilent(true);
            blaze.setRemoveWhenFarAway(false);
        }
        else
        {
            Allay blaze = (Allay) loc.getWorld().spawnEntity(loc.clone().add(0, 1, 0), EntityType.ALLAY);
            blaze.setCustomName(ChatColor.translateAlternateColorCodes('&', npctitle));
            blaze.setAI(false);
            blaze.setInvulnerable(true);
            blaze.setSilent(true);
        }

        if (entity)
        {
            ArmorStand as1 = (ArmorStand) Objects.requireNonNull(Bukkit.getWorld("bwlobby")).spawnEntity(loc.clone().add(0, .3, 0), EntityType.ARMOR_STAND);
            as1.setGravity(false);
            as1.setCustomName(ChatColor.translateAlternateColorCodes('&', " "));
            as1.setCustomNameVisible(true);
            as1.setVisible(false);
            int maxp = Bukkit.getMaxPlayers();
            new BukkitRunnable()
            {
                @Override
                public void run()
                {
                    int ingame = 0;

                    for (Player player : Bukkit.getOnlinePlayers())
                    {
                        if (!player.getWorld().getName().equals("bwlobby"))
                        {
                            ingame++;
                        }
                    }
                    as1.setCustomName(ChatColor.translateAlternateColorCodes('&', "&bPlayers in game: &f" + ingame + " / " + maxp));
                }
            }.runTaskTimer(Objects.requireNonNull(Bukkit.getServer().getPluginManager().getPlugin("Invictools")), 1L, 1L);
        }
        else
        {
            if(OldCommands.Invictools.getConfig().getBoolean("ShowTut",true))
                tutorial();

            ArmorStand as1 = (ArmorStand) Objects.requireNonNull(Bukkit.getWorld("bwlobby")).spawnEntity(loc.clone().add(.7, .15, -.7), EntityType.ARMOR_STAND);
            as1.setGravity(false);
            as1.setCustomName(ChatColor.translateAlternateColorCodes('&', "&bPlayers in game:"));
            as1.setCustomNameVisible(true);
            as1.setVisible(false);
            new BukkitRunnable()
            {
                int ticker = 0;

                @Override
                public void run()
                {

                    if (ticker == 0)
                    {
                        as1.setCustomName(ChatColor.translateAlternateColorCodes('&', "&b&l>>  &fWant to play in more games like these?  &b&l<<"));
                        ticker++;
                    }
                    else if (ticker == 1)
                    {
                        as1.setCustomName(ChatColor.translateAlternateColorCodes('&', "&b&l>>    &fWant to play in more games like these?    &b&l<<"));
                        ticker++;
                    }
                    else if (ticker == 2)
                    {
                        as1.setCustomName(ChatColor.translateAlternateColorCodes('&', "&b&l>>  &fWant to play in more games like these?  &b&l<<"));
                        ticker++;
                    }
                    else if (ticker == 3)
                    {
                        as1.setCustomName(ChatColor.translateAlternateColorCodes('&', "&b&l>>  &fSee these games played on YouTube!  &b&l<<"));
                        ticker++;
                    }
                    else if (ticker == 4)
                    {
                        as1.setCustomName(ChatColor.translateAlternateColorCodes('&', "&b&l>>    &fSee these games played on YouTube!    &b&l<<"));
                        ticker++;
                    }
                    else if (ticker == 5)
                    {
                        as1.setCustomName(ChatColor.translateAlternateColorCodes('&', "&b&l>>  &fSee these games played on YouTube!  &b&l<<"));
                        ticker = 0;
                    }
                }
            }.runTaskTimer(Objects.requireNonNull(Bukkit.getServer().getPluginManager().getPlugin("Invictools")), 1L, 60L);
        }
    }

    private void tutorial()
    {
        Location loc = new Location(Bukkit.getWorld("bwlobby"),254.5,126.5,254.5);
        ArmorStand as1 = (ArmorStand) Objects.requireNonNull(Bukkit.getWorld("bwlobby")).spawnEntity(loc.clone().add(0, .3, 0), EntityType.ARMOR_STAND);
        ArmorStand as2 = (ArmorStand) Objects.requireNonNull(Bukkit.getWorld("bwlobby")).spawnEntity(loc.clone().add(0, .6, 0), EntityType.ARMOR_STAND);
        ArmorStand as3 = (ArmorStand) Objects.requireNonNull(Bukkit.getWorld("bwlobby")).spawnEntity(loc.clone().add(0, .9, 0), EntityType.ARMOR_STAND);
        ArmorStand as4 = (ArmorStand) Objects.requireNonNull(Bukkit.getWorld("bwlobby")).spawnEntity(loc.clone().add(0, 1.2, 0), EntityType.ARMOR_STAND);
        ArmorStand as5 = (ArmorStand) Objects.requireNonNull(Bukkit.getWorld("bwlobby")).spawnEntity(loc.clone().add(0, 1.5, 0), EntityType.ARMOR_STAND);

        as1.setGravity(false);
        as1.setCustomNameVisible(true);
        as1.setVisible(false);
        as2.setGravity(false);
        as2.setCustomNameVisible(true);
        as2.setVisible(false);
        as3.setGravity(false);
        as3.setCustomNameVisible(true);
        as3.setVisible(false);
        as4.setGravity(false);
        as4.setCustomNameVisible(true);
        as4.setVisible(false);
        as5.setGravity(false);
        as5.setCustomNameVisible(true);
        as5.setVisible(false);

        as5.setCustomName(ChatColor.translateAlternateColorCodes('&', "&b&lHow to Play:"));
        as4.setCustomName(ChatColor.translateAlternateColorCodes('&', "&fCreate a party: (/party invite <name> /party accept)"));
        as3.setCustomName(ChatColor.translateAlternateColorCodes('&', "&fChose a mode with the compass."));
        as2.setCustomName(ChatColor.translateAlternateColorCodes('&', "&fChange team size with the anvil"));
        as1.setCustomName(ChatColor.translateAlternateColorCodes('&', "&fUse /party warp to rejoin your party to the game"));

    }
}

package me.invic.invictools.util.npc;

//import com.mojang.authlib.GameProfile;
//import com.mojang.authlib.properties.Property;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;

//import net.minecraft.server.v1_16_R3.*;
//import org.bukkit.craftbukkit.v1_16_R3.CraftServer;
//import org.bukkit.craftbukkit.v1_16_R3.CraftWorld;
//import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;

import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;

//import static net.minecraft.server.v1_16_R3.PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER;
    /*
public class SpawnPlugNPC implements Listener
{

    public static EntityPlayer npc3;

    public SpawnPlugNPC()
    {
      //  System.out.println("spawning");
        Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("Invictools");
        final FileConfiguration pluginConfig = plugin.getConfig();
        String skinname = pluginConfig.getString("npc2.skin");
        String npctitle = pluginConfig.getString("npc2.title");

        MinecraftServer nmsServer = ((CraftServer) Bukkit.getServer()).getServer();
        WorldServer nmsWorld = ((CraftWorld) Objects.requireNonNull(Bukkit.getWorld("bwlobby"))).getHandle(); // Change "world" to the world the NPC should be spawned in.
        GameProfile gameProfile = new GameProfile(UUID.randomUUID(), npctitle); // Change "playername" to the name the NPC should have, max 16 characters.
        EntityPlayer npc = new EntityPlayer(nmsServer, nmsWorld, gameProfile, new PlayerInteractManager(nmsWorld)); // This will be the EntityPlayer (NPC) we send with the sendNPCPacket method.

        npc.setLocation(pluginConfig.getDouble("npc2.x"), pluginConfig.getDouble("npc2.y"), pluginConfig.getDouble("npc2.z"), (float) pluginConfig.getDouble("npc2.yaw"), (float) pluginConfig.getDouble("npc2.pitch"));
        try
        {
            HttpsURLConnection connection = (HttpsURLConnection) new URL(String.format("https://api.ashcon.app/mojang/v2/user/%s", skinname)).openConnection();
            if (connection.getResponseCode() == HttpsURLConnection.HTTP_OK)
            {
                ArrayList<String> lines = new ArrayList<>();
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                reader.lines().forEach(lines::add);

                String reply = String.join(" ", lines);
                int indexOfValue = reply.indexOf("\"value\": \"");
                int indexOfSignature = reply.indexOf("\"signature\": \"");
                String skin = reply.substring(indexOfValue + 10, reply.indexOf("\"", indexOfValue + 10));
                String signature = reply.substring(indexOfSignature + 14, reply.indexOf("\"", indexOfSignature + 14));

                npc.getProfile().getProperties().put("textures", new Property("textures", skin, signature));
            } else
            {
                Bukkit.getConsoleSender().sendMessage("Connection could not be opened when fetching player skin (Response code " + connection.getResponseCode() + ", " + connection.getResponseMessage() + ")");
            }

        } catch (IOException e)
        {
            e.printStackTrace();
        }

        npc3 = npc;

        pluginConfig.set("npc2.ID", npc.getId());
        plugin.saveConfig();


        Location ArmorStand1Loc = new Location(Bukkit.getWorld("bwlobby"), pluginConfig.getDouble("npc2.x"), pluginConfig.getDouble("npc2.y")+.15, pluginConfig.getDouble("npc2.z"));
        ArmorStand as1 = (ArmorStand) Objects.requireNonNull(Bukkit.getWorld("bwlobby")).spawnEntity(ArmorStand1Loc, EntityType.ARMOR_STAND);

        as1.setGravity(false);
        as1.setCustomName(ChatColor.translateAlternateColorCodes('&', "&khaha hey"));
        as1.setCustomNameVisible(true);
        as1.setVisible(false);

        new BukkitRunnable()
        {
            int ticker = 0;
            @Override
            public void run()
            {

                if(ticker == 0)
                {
                    as1.setCustomName(ChatColor.translateAlternateColorCodes('&', "&b&l>>  &fWant to play in more games like these?  &b&l<<"));
                    ticker++;
                }
                else if(ticker == 1)
                {
                    as1.setCustomName(ChatColor.translateAlternateColorCodes('&', "&b&l>>    &fWant to play in more games like these?    &b&l<<"));
                    ticker++;
                }
                else if(ticker == 2)
                {
                    as1.setCustomName(ChatColor.translateAlternateColorCodes('&', "&b&l>>  &fWant to play in more games like these?  &b&l<<"));
                    ticker++;
                }
                else if(ticker == 3)
                {
                    as1.setCustomName(ChatColor.translateAlternateColorCodes('&', "&b&l>>  &fSee these games played on YouTube!  &b&l<<"));
                    ticker++;
                }
                else if(ticker == 4)
                {
                    as1.setCustomName(ChatColor.translateAlternateColorCodes('&', "&b&l>>    &fSee these games played on YouTube!    &b&l<<"));
                    ticker++;
                }
                else if(ticker == 5)
                {
                    as1.setCustomName(ChatColor.translateAlternateColorCodes('&', "&b&l>>  &fSee these games played on YouTube!  &b&l<<"));
                    ticker=0;
                }
            }
        }.runTaskTimer(Objects.requireNonNull(Bukkit.getServer().getPluginManager().getPlugin("Invictools")), 1L, 25L);

    }

    @EventHandler
    public void RenderNPC(PlayerChangedWorldEvent e)
    {
     //   System.out.println("spawning after world change");
        Player player = e.getPlayer();
        if(player.getWorld().getName().equalsIgnoreCase("bwlobby"))
        {
            PlayerConnection connection = ((CraftPlayer) player).getHandle().playerConnection;
            connection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, npc3)); // "Adds the player data for the client to use when spawning a player" - https://wiki.vg/Protocol#Spawn_Player
            connection.sendPacket(new PacketPlayOutNamedEntitySpawn(npc3)); // Spawns the NPC for the player client.
            connection.sendPacket(new PacketPlayOutEntityHeadRotation(npc3, (byte) (npc3.yaw * 256 / 360))); // Correct head rotation when spawned in player look direction.

            DataWatcher watcher = npc3.getDataWatcher();
            watcher.set(new DataWatcherObject<>(16, DataWatcherRegistry.a), (byte) 127);
            PacketPlayOutEntityMetadata packet = new PacketPlayOutEntityMetadata(npc3.getId(), watcher, true);
            ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);

            BukkitRunnable runnable = new BukkitRunnable() {
                @Override
                public void run()
                {
                    connection.sendPacket(new PacketPlayOutPlayerInfo(REMOVE_PLAYER, npc3));
                }
            };
            runnable.runTaskLater(Objects.requireNonNull(Bukkit.getServer().getPluginManager().getPlugin("Invictools")), 60);
        }
    }

    @EventHandler
    public void RenderNPConJoin(PlayerJoinEvent e)
    {
       // System.out.println("spawning on join");
        Player player = e.getPlayer();
        if(player.getWorld().getName().equalsIgnoreCase("bwlobby"))
        {
            PlayerConnection connection = ((CraftPlayer) player).getHandle().playerConnection;
            connection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, npc3)); // "Adds the player data for the client to use when spawning a player" - https://wiki.vg/Protocol#Spawn_Player
            connection.sendPacket(new PacketPlayOutNamedEntitySpawn(npc3)); // Spawns the NPC for the player client.
            connection.sendPacket(new PacketPlayOutEntityHeadRotation(npc3, (byte) (npc3.yaw * 256 / 360))); // Correct head rotation when spawned in player look direction.

            DataWatcher watcher = npc3.getDataWatcher();
            watcher.set(new DataWatcherObject<>(16, DataWatcherRegistry.a), (byte) 127);
            PacketPlayOutEntityMetadata packet = new PacketPlayOutEntityMetadata(npc3.getId(), watcher, true);
            ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);

            BukkitRunnable runnable = new BukkitRunnable()
            {
                @Override
                public void run()
                {
                    connection.sendPacket(new PacketPlayOutPlayerInfo(REMOVE_PLAYER, npc3));
                }
            };
            runnable.runTaskLater(Objects.requireNonNull(Bukkit.getServer().getPluginManager().getPlugin("Invictools")), 60);
        }
    }
}

 */
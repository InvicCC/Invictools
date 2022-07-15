package me.invic.invictools.util.npc;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.EnumWrappers;
import me.invic.invictools.Commands;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import scala.concurrent.impl.FutureConvertersImpl;

import java.util.Objects;

public class DetectClickOnNPC
{
    Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("Invictools");
    final FileConfiguration Config = plugin.getConfig();
    int ID = Config.getInt("npc.ID");

    public DetectClickOnNPC()
    {
        ProtocolManager manager = ProtocolLibrary.getProtocolManager();
        manager.addPacketListener(new PacketAdapter(Objects.requireNonNull(Bukkit.getServer().getPluginManager().getPlugin("Invictools")), ListenerPriority.NORMAL, PacketType.Play.Client.USE_ENTITY)
        {

            public void onPacketReceiving(PacketEvent e)
            {
                Player player = e.getPlayer();
                String playername = player.getName();
                PacketContainer packet = e.getPacket();
                int entityId = packet.getIntegers().read(0);
                if (ID == entityId)
                {
                    if (packet.getEntityUseActions().read(0) == EnumWrappers.EntityUseAction.ATTACK)
                        return;

                    if (packet.getHands().read(0) == null)
                        return;

                    if (packet.getHands().read(0) != EnumWrappers.Hand.MAIN_HAND)
                        return;

                    if (packet.getEntityUseActions().read(0) != EnumWrappers.EntityUseAction.INTERACT)
                        return;

                    new BukkitRunnable()
                    {
                        @Override
                        public void run()
                        {
                            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "commandpanel npcpanel " + playername);
                        }
                    }.runTask(plugin);
                }
                else if (Commands.noShop.get(e.getPlayer()) != null)
                {
                    if (Commands.noShop.get(e.getPlayer()))
                    {
                        if (packet.getEntityUseActions().read(0) == EnumWrappers.EntityUseAction.ATTACK)
                            return;
/*
                        if(packet.getHands().read(0) == null)
                            return;

                        if(packet.getHands().read(0) != EnumWrappers.Hand.MAIN_HAND)
                            return;

                        if(packet.getEntityUseActions().read(0) != EnumWrappers.EntityUseAction.INTERACT)
                            return;
                        */
                        // System.out.println("popped");
                        e.setCancelled(true);
                    }
                }
            }
        });
    }
}

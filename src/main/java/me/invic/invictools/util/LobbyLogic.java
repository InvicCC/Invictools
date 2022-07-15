package me.invic.invictools.util;

import com.mojang.authlib.minecraft.TelemetrySession;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.session.ClipboardHolder;
import com.sk89q.worldedit.world.block.BlockTypes;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.screamingsandals.bedwars.api.BedwarsAPI;
import org.screamingsandals.bedwars.api.events.BedwarsGameStartedEvent;
import org.screamingsandals.bedwars.api.events.BedwarsPlayerJoinEvent;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Vector;

public class LobbyLogic implements Listener
{
    public static HashMap<String, Region> lobbyRegion = new HashMap<>();

    @EventHandler
    public void LobbyStarted(BedwarsPlayerJoinEvent e)
    {
        if(e.getGame().getConnectedPlayers().size() == 0) // 1 player idfk why
        {
            double x = e.getGame().getLobbySpawn().getX();
            double y = e.getGame().getLobbySpawn().getY();
            double z = e.getGame().getLobbySpawn().getZ();

            createLobby(e.getGame().getName(),x,y,z,e.getGame().getLobbyWorld());
            startTicker(e);
        }
    }

    @EventHandler
    public void GameStarted(BedwarsGameStartedEvent e)
    {
        destroyLobby(e.getGame().getName(),e.getGame().getLobbyWorld());
    }

    public void createLobby(String gameName ,double x, double y, double z, World gameWorld)
    {
        Clipboard lobby = loadSchem(getGameLobby(gameName));
        try (EditSession editSession = WorldEdit.getInstance().newEditSession((new BukkitWorld(gameWorld))))
        {
            Operation operation = new ClipboardHolder(lobby)
                    .createPaste(editSession)
                    .to(BlockVector3.at(x,y,z))
                    .build();
            Operations.complete(operation);
            Region region = lobby.getRegion();
            double xchange = lobby.getOrigin().getX() - x;
            double ychange = lobby.getOrigin().getY() - y;
            double zchange = lobby.getOrigin().getZ() - z;
            region.shift(BlockVector3.at(-xchange,-ychange,-zchange));
            lobbyRegion.put(gameName,region);
        }
        catch (WorldEditException worldEditException)
        {
            worldEditException.printStackTrace();
        }
    }

    public void destroyLobby(String gameName, World lobbyWorld)
    {
        if (lobbyRegion.get(gameName) != null)
        {
            Region region = lobbyRegion.get(gameName);

            if(!getGameLobbyEffect(gameName).equalsIgnoreCase("none"))
            {
                Location loc = new Location(lobbyWorld, region.getCenter().getX(), region.getCenter().getY(), region.getCenter().getZ());
                lobbyEffects.effectHandler(getGameLobbyEffect(gameName),loc);
            }

            try (EditSession editSession = WorldEdit.getInstance().newEditSession((new BukkitWorld(lobbyWorld))))
            {
                editSession.setBlocks(region, BlockTypes.AIR.getDefaultState());
                lobbyRegion.remove(gameName);
            } catch (MaxChangedBlocksException ignored) { }
        }
    }

    public void startTicker(BedwarsPlayerJoinEvent e)
    {
        new BukkitRunnable()
        {
            @Override
            public void run()
            {
                if (e.getGame().getLobbyWorld().getPlayers().size() == 0 && lobbyRegion.get(e.getGame().getName()) != null)
                {
                    destroyLobby(e.getGame().getName(),e.getGame().getLobbyWorld());
                    this.cancel();
                }
            }
        }.runTaskTimer(Objects.requireNonNull(Bukkit.getServer().getPluginManager().getPlugin("Invictools")), 20*10, 20*30);
    }

    public String getGameLobby(String game)
    {
        FileConfiguration config = getMapConfiguration(game);
        return config.getString("Lobby.name");
    }

    public String getGameLobbyEffect(String game)
    {
        FileConfiguration config = getMapConfiguration(game);
        return config.getString("Lobby.effect");
    }

    public File getMapFile(String game)
    {
        Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("Invictools");
        File Folder = new File(plugin.getDataFolder(), "Maps");
        return new File(Folder, game + ".yml");
    }

    public FileConfiguration getMapConfiguration(String game)
    {
        Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("Invictools");
        File Folder = new File(plugin.getDataFolder(), "Maps");
        File pFile = new File(Folder, game + ".yml");
        return YamlConfiguration.loadConfiguration(pFile);
    }

    /**
     *
     * @param world use getMapFromConfiguration if possible because multiple games may be in the same world!
     */
    public FileConfiguration getMapConfigurationFromWorld(String world)
    {
        Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("Invictools");
        File Folder = new File(plugin.getDataFolder(), "Maps");
        File[] yamlFiles = Folder.listFiles();
        for (File file:yamlFiles)
        {
            FileConfiguration map = YamlConfiguration.loadConfiguration(file);
            if(map.getString("World") != null)
                if(map.getString("World").equalsIgnoreCase(world))
                    return map;
        }
        System.out.println("no match");
        return null;
    }

    public Clipboard loadSchem(String schem)
    {
        Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("Invictools");
        File Folder = new File(plugin.getDataFolder(), "Schems");
        File file = new File(Folder, schem + ".schem");

        ClipboardFormat format = ClipboardFormats.findByFile(file);
        try (ClipboardReader reader = format.getReader(new FileInputStream(file)))
        {
            return reader.read();
        }
        catch (Throwable e)
        {
            try (ClipboardReader reader = format.getReader(new FileInputStream(new File(Folder, "default.schem"))))
            {
                return reader.read();
            }
            catch (IOException e2)
            {
                e.printStackTrace();
                return null;
            }
        }
    }
}

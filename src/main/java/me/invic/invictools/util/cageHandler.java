package me.invic.invictools.util;

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
import me.invic.invictools.commands.OldCommands;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;

public class cageHandler
{
    public static HashMap<String, Region> activeCage = new HashMap<>();

    public void buildCage(Player p, Location loc)
    {
        Clipboard lobby = loadSchem(getCage(p));
        try (EditSession editSession = WorldEdit.getInstance().newEditSession((new BukkitWorld(loc.getWorld()))))
        {
            Operation operation = new ClipboardHolder(lobby)
                    .createPaste(editSession)
                    .to(BlockVector3.at(loc.getX(), loc.getY(), loc.getZ()))
                    .build();
            Operations.complete(operation);
            Region region = lobby.getRegion();
            double xchange = lobby.getOrigin().getX() - loc.getX();
            double ychange = lobby.getOrigin().getY() - loc.getY();
            double zchange = lobby.getOrigin().getZ() - loc.getZ();
            region.shift(BlockVector3.at(-xchange, -ychange, -zchange));
            activeCage.put(p.getName(), region);
        }
        catch (WorldEditException worldEditException)
        {
            worldEditException.printStackTrace();
        }
    }

    public void destroyCage(OfflinePlayer p, Location loc)
    {
        if (activeCage.get(p.getName()) != null)
        {
            Region region = activeCage.get(p.getName());

            try (EditSession editSession = WorldEdit.getInstance().newEditSession((new BukkitWorld(loc.getWorld()))))
            {
                editSession.setBlocks(region, BlockTypes.AIR.getDefaultState());
                activeCage.remove(p.getName());
            }
            catch (MaxChangedBlocksException ignored)
            {
            }
        }
    }

    public String getCage(Player p)
    {
        File Folder = new File(OldCommands.Invictools.getDataFolder(), "PlayerData");
        File file = new File(Folder, p.getUniqueId() + ".yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        return config.getString("cage","glass");
    }

    public Clipboard loadSchem(String schem)
    {
        File Folder = new File(OldCommands.Invictools.getDataFolder(), "Schems");
        File file = new File(Folder, schem + "_cage.schem");

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

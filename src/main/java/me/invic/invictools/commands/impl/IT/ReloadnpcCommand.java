package me.invic.invictools.commands.impl.IT;

import me.invic.invictools.commands.commandManagerLib.SubCommand;
import me.invic.invictools.util.npc.BlazeNpc;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Allay;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Illusioner;

import java.util.List;

public class ReloadnpcCommand implements SubCommand
{
    @Override
    public String getName()
    {
        return "reloadnpc";
    }

    @Override
    public String getDescription()
    {
        return null;
    }

    @Override
    public String getSyntax()
    {
        return "/it reloadnpc";
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
        for (Entity e : Bukkit.getWorld("bwlobby").getEntitiesByClass(ArmorStand.class))
        {
            if (!e.hasMetadata("holo"))
                e.remove();
        }
        for (Entity e : Bukkit.getWorld("bwlobby").getEntitiesByClass(Illusioner.class))
        {
            e.remove();
        }
        for (Entity e : Bukkit.getWorld("bwlobby").getEntitiesByClass(Allay.class))
        {
            e.remove();
        }
        new BlazeNpc().spawnNPC("npc", true);
        new BlazeNpc().spawnNPC("npc2", false);
    }
}
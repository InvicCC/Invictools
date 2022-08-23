package me.invic.invictools.util.gui.scenSelector;

import me.invic.invictools.commands.OldCommands;
import me.invic.invictools.gamemodes.Manhunt.ManhuntMain;
import me.invic.invictools.util.fixes.SafeOpCommand;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class scenarioQueue
{
    List<String> commands = new ArrayList<>();

    public scenarioQueue()
    {

    }

    public void debugPrint(Player p)
    {
        for(String s:commands)
        {
            p.sendMessage(s);
        }
    }

    public void print(List<Player> p)
    {
        List<String> cleaned = ManhuntMain.cleanSyntax(commands);
        if (cleaned.size() > 28)
        {
            new BukkitRunnable()
            {
                @Override
                public void run()
                {
                    for (String s : cleaned)
                    {
                        p.forEach((pl) -> pl.sendMessage(s));
                    }
                }
            }.runTaskLater(OldCommands.Invictools, 20L);
        }
    }

    public void addCommand(String s)
    {
        commands.add(s);
    }

    public void removeCommand(String s)
    {
        commands.remove(s);
    }

    int loadedCommands()
    {
        return commands.size();
    }

    void remove()
    {
        commands.clear();
    }

    void execute()
    {
        for(String s:commands)
        {
            if(!s.equalsIgnoreCase("null"))
            {
                try
                {
                    System.out.println("executing " + s);
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), s);
                } catch (Throwable e)
                {
                    System.out.println(s + " skipped");
                }
            }
        }
    }

    void execute(Player p)
    {
        for(String s:commands)
        {
            if(!s.equalsIgnoreCase("null"))
            {
                try
                {
                    System.out.println("executing " + s);
                    Bukkit.dispatchCommand(p, s);
                } catch (Throwable e)
                {
                    System.out.println(s + " skipped");
                }
            }
        }
    }

    public void executeOP(Player p)
    {
        for(String s:commands)
        {
            if(!s.equalsIgnoreCase("null"))
            {
                try
                {
                    System.out.println("executing " + s);
                    new SafeOpCommand(p, s);
                } catch (Throwable e)
                {
                    System.out.println(s + " skipped");
                }
            }
        }
    }
}

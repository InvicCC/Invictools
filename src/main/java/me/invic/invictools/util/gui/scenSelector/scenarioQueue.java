package me.invic.invictools.util.gui.scenSelector;

import me.invic.invictools.util.fixes.SafeOpCommand;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class scenarioQueue
{
    List<String> commands = new ArrayList<>();

    scenarioQueue()
    {

    }

    void print()
    {
        for(String s:commands)
        {
            System.out.println(s);
        }
    }

    void addCommand(String s)
    {
        commands.add(s);
    }

    void removeCommand(String s)
    {
        commands.add(s);
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
            try
            {
                System.out.println("executing "+s);
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(),s);
            }
            catch(Throwable e)
            {
                System.out.println(s+ " skipped");
            }
        }
    }

    void execute(Player p)
    {
        for(String s:commands)
        {
            try
            {
                System.out.println("executing "+s);
                Bukkit.dispatchCommand(p,s);
            }
            catch(Throwable e)
            {
                System.out.println(s+ " skipped");
            }
        }
    }

    void executeOP(Player p)
    {
        for(String s:commands)
        {
            try
            {
                System.out.println("executing "+s);
                new SafeOpCommand(p,s);
            }
            catch(Throwable e)
            {
                System.out.println(s+ " skipped");
            }
        }
    }

}

package me.invic.invictools.commands.impl.IT;

import me.invic.invictools.commands.commandManagerLib.SubCommand;
import me.invic.invictools.gamemodifiers.PotionEffects.EffectSometimes;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.List;

public class EffectSometimesCommand implements SubCommand
{
    @Override
    public String getName()
    {
        return "EffectSometimes";
    }

    @Override
    public String getDescription()
    {
        return null;
    }

    @Override
    public String getSyntax()
    {
        return "/it EffectSometimes";
    }

    @Override
    public String getPermission()
    {
        return "invic.invictools";
    }

    @Override
    public List<String> getTabCompletion(int index, String[] args)
    {
        return null;
    }

    @Override
    public void perform(CommandSender sender, String[] args)
    {
        if (args[1].equalsIgnoreCase("all"))
        {
            new EffectSometimes(true, Bukkit.getPlayer(args[7]), args[6], Integer.parseInt(args[2]), Integer.parseInt(args[3]), Integer.parseInt(args[4]), Integer.parseInt(args[5]));
        }
        else
        {
            new EffectSometimes(false, Bukkit.getPlayer(args[7]), args[6], Integer.parseInt(args[2]), Integer.parseInt(args[3]), Integer.parseInt(args[4]), Integer.parseInt(args[5]));
        }
        sender.sendMessage(ChatColor.AQUA + "Command went through but i didnt code this part yet so dont look");
    }
}
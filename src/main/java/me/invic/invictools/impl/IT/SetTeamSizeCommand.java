package me.invic.invictools.impl.IT;

import me.invic.invictools.commandManagerLib.SubCommand;
import me.invic.invictools.gamemodifiers.Manhunt.ManhuntMain;
import me.invic.invictools.gamemodifiers.PotionEffects.EffectSometimes;
import me.invic.invictools.util.fixes.ChangeTeamSize;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.List;

public class SetTeamSizeCommand implements SubCommand
{
    @Override
    public String getName()
    {
        return "SetTeamSize";
    }

    @Override
    public String getDescription()
    {
        return null;
    }

    @Override
    public String getSyntax()
    {
        return "/it SetTeamSize";
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
        if (args.length > 1)
        {
            if (args[1].equalsIgnoreCase("EveryArena"))
            {
                ChangeTeamSize.ChangeEveryArenaTeamSize(Integer.parseInt(args[2]));
            }
            else if (args[1].equalsIgnoreCase("SingleArena"))
            {
                if (args[2].equalsIgnoreCase("EveryTeam"))
                {
                    ChangeTeamSize.ChangeSingleArenaTeamSize(args[3], Integer.parseInt(args[4]));
                }
                else if (args[2].equalsIgnoreCase("SingleTeam"))
                {
                    ChangeTeamSize.ChangeSingleTeamSize(args[3], Integer.parseInt(args[4]), args[5]);
                }
            }
        }
        else if (args.length == 1)
        {
            ChangeTeamSize.printTeamSizes(sender);
        }
    }
}
package me.invic.invictools.items;

import me.invic.invictools.gamemodifiers.LuckyBlocks.createLuckyBlocks;
import me.invic.invictools.gamemodes.Manhunt.ManhuntMain;
import me.invic.invictools.util.GrabTeammates;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Map;

public class giveitem
{
    public static void giveItem(String who, String specify, String type, Player p)
    {
        if (who.equalsIgnoreCase("single"))
        {
            if (type.equalsIgnoreCase("lb"))
                makelb(specify, p);
            else if (type.equalsIgnoreCase("item"))
                makeitem(specify, p);
        }
        else if (who.equalsIgnoreCase("all"))
        {
            for (Player player : Bukkit.getOnlinePlayers())
            {
                if (player.getWorld().equals(p.getWorld()))
                {
                    if (type.equalsIgnoreCase("lb"))
                        makelb(specify, player);
                    else if (type.equalsIgnoreCase("item"))
                        makeitem(specify, player);
                }
            }
        }
        else if (who.equalsIgnoreCase("mteam") || who.equalsIgnoreCase("manhuntteam"))
        {
            for (Player player : ManhuntMain.ManhuntTeam.keySet())
            {
                if (ManhuntMain.ManhuntTeam.get(player).equalsIgnoreCase(ManhuntMain.ManhuntTeam.get(p)))
                {
                    if (type.equalsIgnoreCase("lb"))
                        makelb(specify, player);
                    else if (type.equalsIgnoreCase("item"))
                        makeitem(specify, player);
                }
            }
        }
        else if (who.equalsIgnoreCase("bteam") || who.equalsIgnoreCase("bedwarsteam"))
        {
            try
            {
                List<Player> teammates = GrabTeammates.getTeammates(p);
                for (Player player : teammates)
                {
                    if (type.equalsIgnoreCase("lb"))
                        makelb(specify, player);
                    else if (type.equalsIgnoreCase("item"))
                        makeitem(specify, player);
                }
            }
            catch (NullPointerException n)
            {
                if (type.equalsIgnoreCase("lb"))
                    makelb(specify, p);
                else if (type.equalsIgnoreCase("item"))
                    makeitem(specify, p);
            }
        }
    }

    static void makeitem(String item, Player player)
    {
        ItemStack lb;
        switch (item)
        {
            case "random":
                lb = new createItems().getRandomItem();
                break;
            default:
                lb = new createItems().getByName(item);
        }
        smartGive(lb, player);
    }

    static void makelb(String type, Player player)
    {
        ItemStack lb;
        switch (type)
        {
            case "random":
                lb = new createLuckyBlocks().getRandomBlock();
                break;
            default:
                lb = new createLuckyBlocks().getByName(type);
        }
        smartGive(lb, player);
    }

    public static void smartGive(ItemStack i, Player player)
    {
        final Map<Integer, ItemStack> map = player.getInventory().addItem(i);
        player.playSound(player.getLocation(), Sound.ENTITY_CHICKEN_EGG, 1, 1);
        for (final ItemStack item : map.values())
        {
            player.getWorld().dropItemNaturally(player.getLocation(), item);
        }
    }

}

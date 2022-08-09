package me.invic.invictools.commands.impl.IT;

import me.invic.invictools.commands.commandManagerLib.SubCommand;
import me.invic.invictools.gamemodifiers.LuckyBlocks.createLuckyBlocks;
import me.invic.invictools.gamemodifiers.giveItemRepeated;
import me.invic.invictools.items.createItems;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class RepeatedItemCommand implements SubCommand
{
    @Override
    public String getName()
    {
        return "repeatedItem";
    }

    @Override
    public String getDescription()
    {
        return null;
    }

    @Override
    public String getSyntax()
    {
        return "/it repeatedItem";
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
        if (args[5].equalsIgnoreCase("all"))
        {
            for (Player player : Bukkit.getOnlinePlayers())
            {
                int delay = Integer.parseInt(args[3]);
                String type = args[4];
                if (args[4].equalsIgnoreCase("specificlb"))
                {
                    ItemStack item = new createLuckyBlocks().getByName(args[1]);
                    item.setAmount(Integer.parseInt(args[2]));
                    type = "normal";

                    new giveItemRepeated(delay, item, player, type);
                    player.sendMessage((ChatColor.AQUA + "You will receive luckyblocks,  " + ChatColor.YELLOW + item.getAmount() + " " + args[1] + ChatColor.AQUA + " every " + ChatColor.YELLOW + delay + ChatColor.AQUA + " seconds."));

                }
                else if (args[4].equalsIgnoreCase("specificItem"))
                {
                    ItemStack item = new createItems().getByName(args[1]);
                    item.setAmount(Integer.parseInt(args[2]));
                    type = "normal";

                    new giveItemRepeated(delay, item, player, type);
                    player.sendMessage((ChatColor.AQUA + "You will receive " + ChatColor.YELLOW + item.getAmount() + " " + args[1] + ChatColor.AQUA + " every " + ChatColor.YELLOW + delay + ChatColor.AQUA + " seconds."));
                }
                else
                {
                    ItemStack item = new ItemStack(Objects.requireNonNull(Material.getMaterial(args[1])));
                    item.setAmount(Integer.parseInt(args[2]));

                    new giveItemRepeated(delay, item, player, type);
                    player.sendMessage((ChatColor.AQUA + "You will receive " + ChatColor.YELLOW + item.getAmount() + " " + item.getType().toString().toLowerCase(Locale.ROOT) + ChatColor.AQUA + " every " + ChatColor.YELLOW + delay + ChatColor.AQUA + " seconds."));
                }
            }
        }
    }
}
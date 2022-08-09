package me.invic.invictools.commands.impl.IT;

import me.invic.invictools.commands.commandManagerLib.SubCommand;
import me.invic.invictools.commands.OldCommands;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.screamingsandals.bedwars.api.BedwarsAPI;

import java.util.List;
import java.util.Objects;

import static me.invic.invictools.commands.OldCommands.Hauntable;

public class HauntCommand implements SubCommand
{
    @Override
    public String getName()
    {
        return "haunt";
    }

    @Override
    public String getDescription()
    {
        return null;
    }

    @Override
    public String getSyntax()
    {
        return "/it haunt";
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
        OldCommands.HauntConfig = args[2];
        if (args[1].equalsIgnoreCase("all"))
        {
            for (Player player : BedwarsAPI.getInstance().getGameOfPlayer((Player) sender).getConnectedPlayers())
            {
                player.sendMessage(ChatColor.AQUA + "You can now haunt players after you are final killed");
                //  if (MasterPlayer.getWorld().equals(player.getWorld()))
                {
                    Hauntable.put(player, true);
                    World world = player.getWorld();

                    new BukkitRunnable() // kills at max 1 minute after match ends or player dies and leaves
                    {
                        @Override
                        public void run()
                        {
                            if (player == null)
                            {
                                this.cancel();
                            }

                            if (player.getWorld() != world)
                            {
                                Hauntable.remove(player);
                                this.cancel();
                            }
                        }
                    }.runTaskTimer(Objects.requireNonNull(Bukkit.getServer().getPluginManager().getPlugin("Invictools")), 0L, 120L);
                }
            }
        }
        else
        {
            Player player = Bukkit.getPlayer(args[1]);
            player.sendMessage(ChatColor.AQUA + "You can now haunt players after you are final killed");
            //  if (MasterPlayer.getWorld().equals(player.getWorld()))
            {
                Hauntable.put(player, true);
                World world = player.getWorld();

                new BukkitRunnable() // kills at max 1 minute after match ends or player dies and leaves
                {
                    @Override
                    public void run()
                    {
                        if (player == null)
                        {
                            this.cancel();
                        }

                        if (player.getWorld() != world)
                        {
                            Hauntable.remove(player);
                            this.cancel();
                        }
                    }
                }.runTaskTimer(Objects.requireNonNull(Bukkit.getServer().getPluginManager().getPlugin("Invictools")), 0L, 120L);
            }
        }
    }
}
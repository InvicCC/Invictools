package me.invic.invictools.util.gui;

import me.invic.invictools.commands.OldCommands;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.screamingsandals.bedwars.api.BedwarsAPI;
import org.screamingsandals.bedwars.api.Team;
import org.screamingsandals.bedwars.api.events.BedwarsGameStartEvent;
import org.screamingsandals.bedwars.api.events.BedwarsPlayerJoinEvent;
import org.screamingsandals.bedwars.api.events.BedwarsPlayerLeaveEvent;
import org.screamingsandals.bedwars.api.game.Game;

import java.util.*;

public class TeamSelection implements Listener
{
    public static HashMap<Game, Inventory> TeamSelector = new HashMap<>();
    public static HashMap<String, Integer> teamSize = new HashMap<>();
    final static String panelName = ChatColor.translateAlternateColorCodes('&', "&8&lTEAM SELECTOR");
    final static String joinMsg = ChatColor.translateAlternateColorCodes('&', "&r&fClick to join this Team!");
    BedwarsAPI api = BedwarsAPI.getInstance();

    @EventHandler
    public void bwjoin(BedwarsPlayerJoinEvent e)
    {
        if (e.getGame().getConnectedPlayers().size() == 0)
        {
            if (TeamSelector.get(e.getGame()) == null)
            {
                createInventory(e.getGame());
                for (Team team : e.getGame().getAvailableTeams())
                {
                    teamSize.put(e.getGame().getName() + ";" + team.getName(), 0);
                }
            }
        }

        new BukkitRunnable()
        {
            @Override
            public void run()
            {
                new gameLobbyItems().giveItems(e.getPlayer());
            }
        }.runTaskLater(OldCommands.Invictools, 10L);
        // openInventory(e.getPlayer(),e.getGame(),false);
    }

    @EventHandler
    public void click(InventoryClickEvent e)
    {
        if (TeamSelector.containsValue(e.getClickedInventory()) && e.getCurrentItem() == null && e.getWhoClicked().isOp())
        {
            List<Integer> nums = new ArrayList<>();
            Game game = api.getGameOfPlayer((Player) e.getWhoClicked());
            for (int i = 0; i < game.getAvailableTeams().size(); i++)
            {
                nums.add(i);
            }
            Collections.shuffle(nums);
            int i = 0;
            int t = 0;
            while (i < game.getConnectedPlayers().size())
            {
                if (i == game.getAvailableTeams().size())
                {
                    i = 0;
                    Collections.shuffle(nums);
                }

                Player p = game.getConnectedPlayers().get(t);
                Team team = game.getAvailableTeams().get(nums.get(i));
                game.selectPlayerTeam(p, team);

                i++;
                t++;
            }
        }
        else if (TeamSelector.containsValue(e.getClickedInventory()) && e.getCurrentItem() != null)
        {
            e.setCancelled(true);
            updateItem(e.getCurrentItem(), (Player) e.getWhoClicked(), true);
        }
    }

    @EventHandler
    public void bwleave(BedwarsPlayerLeaveEvent e)
    {
        if (e.getGame().getGameWorld().getPlayers().size() == 1)
        {
            TeamSelector.remove(e.getGame());
        }
        else if(TeamSelector.get(e.getGame()) != null)
        {
            for (int i = 0; i < TeamSelector.get(e.getGame()).getSize(); i++)
            {
                if(TeamSelector.get(e.getGame()).getItem(i) != null && TeamSelector.get(e.getGame()).getItem(i).getItemMeta() != null)
                {
                    ItemStack other = TeamSelector.get(e.getGame()).getItem(i);
                    int a = loreContains(other.getItemMeta().getLore(), e.getPlayer().getDisplayName());
                    if (loreContains(other.getItemMeta().getLore(), e.getPlayer().getDisplayName()) != -1)
                    {
                        ItemMeta otherMeta = other.getItemMeta();
                        List<String> lore2 = otherMeta.getLore();
                        lore2.remove(a);
                        otherMeta.setLore(lore2);
                        String[] otherTeam = otherMeta.getDisplayName().split(" ");
                        other.setItemMeta(otherMeta);
                        if (other.getAmount() > 1)
                        {
                            otherMeta.setDisplayName(ChatColor.RESET + "" + teamColorToChatColor(e.getGame().getTeamFromName(otherTeam[1]).getColor().toString()) + " " + e.getGame().getTeamFromName(otherTeam[1]).getName() + " " + ChatColor.GRAY + "(" + ChatColor.WHITE + (other.getAmount() - 1) + ChatColor.GRAY + "/" + ChatColor.WHITE + e.getGame().getTeamFromName(otherTeam[1]).getMaxPlayers() + ChatColor.GRAY + ")");
                            other.setAmount(other.getAmount() - 1);
                            //    teamSize.put(game.getName()+";"+team[1],teamSize.get(game.getName()+";"+team[1])-1);
                        } else
                        {
                            otherMeta.setDisplayName(ChatColor.RESET + "" + teamColorToChatColor(e.getGame().getTeamFromName(otherTeam[1]).getColor().toString()) + " " + e.getGame().getTeamFromName(otherTeam[1]).getName() + " " + ChatColor.GRAY + "(" + ChatColor.WHITE + "0" + ChatColor.GRAY + "/" + ChatColor.WHITE + e.getGame().getTeamFromName(otherTeam[1]).getMaxPlayers() + ChatColor.GRAY + ")");
                            //  teamSize.put(game.getName()+";"+team[1],teamSize.get(game.getName()+";"+team[1])-1);
                        }
                        other.setItemMeta(otherMeta);
                        teamSize.put(e.getGame().getName() + ";" + otherTeam[1], teamSize.get(e.getGame().getName() + ";" + otherTeam[1]) - 1);
                    }
                }
            }
        }
    }

    @EventHandler
    public void bwstart(BedwarsGameStartEvent e)
    {
      //  teamSize.clear();
        TeamSelector.remove(e.getGame());
    }

    public void openInventory(Player p, Game game, boolean fast)
    {
        if (fast)
        {
            p.openInventory(TeamSelector.get(game));
        }
        else
        {
            new BukkitRunnable()
            {
                @Override
                public void run()
                {
                    p.openInventory(TeamSelector.get(game));
                }
            }.runTaskLater(OldCommands.Invictools, 20L);
        }
    }

    public void createInventory(Game game)
    {
        Inventory inventory = Bukkit.createInventory(null, nearest(game.getAvailableTeams().size()), panelName);
        for (int s = 0; s < game.getAvailableTeams().size(); s++)
        {
            ItemStack item = new ItemStack(Material.valueOf(game.getAvailableTeams().get(s).getColor().toString().toUpperCase(Locale.ROOT) + "_WOOL"));
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(ChatColor.RESET + "" + teamColorToChatColor(game.getAvailableTeams().get(s).getColor().toString()) + " " + game.getAvailableTeams().get(s).getName() + " " + ChatColor.GRAY + "(" + ChatColor.WHITE + "0" + ChatColor.GRAY + "/" + ChatColor.WHITE + game.getAvailableTeams().get(1).getMaxPlayers() + ChatColor.GRAY + ")");
            meta.setLore(Collections.singletonList(joinMsg));
            item.setItemMeta(meta);
            inventory.setItem(s, item);
        }
        TeamSelector.put(game, inventory);
    }

    public int nearest(int v)
    {
        for (int i = 1; i < 54; i++)
        {
            if (v <= 9 * i)
                return 9 * i;
        }
        return 54;
    }

    private void updateItem(ItemStack item, Player p, boolean chooseteam)
    {
        if (loreContains(item.getItemMeta().getLore(), p.getDisplayName()) != -1)
        {
            return;
        }

        Game game = api.getGameOfPlayer(p);
        String[] team = item.getItemMeta().getDisplayName().split(" ");

        if (teamSize.get(game.getName() + ";" + team[1]) != null)
        {
          //  System.out.println(teamSize.get(game.getName() + ";" + team[1]));
         //   System.out.println(game.getTeamFromName(team[1]).getMaxPlayers());
            if (teamSize.get(game.getName() + ";" + team[1]) == game.getTeamFromName(team[1]).getMaxPlayers())
            {
                return;
            }
        }

        if (chooseteam)
        {
            game.selectPlayerTeam(p, game.getTeamFromName(team[1]));
           // if(teamSize.containsKey(game.getName()))
          //  {
                teamSize.put(game.getName() + ";" + team[1], teamSize.get(game.getName() + ";" + team[1]) + 1);
         //   }
        }

        ItemMeta meta = item.getItemMeta();
        if (meta.getLore().size() > 1) // join line and a player
        {
            item.setAmount(item.getAmount() + 1);
        }

        meta.setDisplayName(ChatColor.RESET + "" + teamColorToChatColor(game.getTeamFromName(team[1]).getColor().toString()) + " " + game.getTeamFromName(team[1]).getName() + " " + ChatColor.GRAY + "(" + ChatColor.WHITE + item.getAmount() + ChatColor.GRAY + "/" + ChatColor.WHITE + game.getTeamFromName(team[1]).getMaxPlayers() + ChatColor.GRAY + ")");

        List<String> lore = meta.getLore();

        lore.add(p.getDisplayName());
        meta.setLore(lore);
        item.setItemMeta(meta);

        for (int i = 0; i < TeamSelector.get(game).getSize(); i++)
        {
            ItemStack other = TeamSelector.get(game).getItem(i);
            if (other != null && !other.getItemMeta().getDisplayName().equals(item.getItemMeta().getDisplayName()))
            {
                int a = loreContains(other.getItemMeta().getLore(), p.getDisplayName());
                if (loreContains(other.getItemMeta().getLore(), p.getDisplayName()) != -1)
                {
                    ItemMeta otherMeta = other.getItemMeta();
                    List<String> lore2 = otherMeta.getLore();
                    lore2.remove(a);
                    otherMeta.setLore(lore2);
                    String[] otherTeam = otherMeta.getDisplayName().split(" ");
                    other.setItemMeta(otherMeta);
                    if (other.getAmount() > 1)
                    {
                        otherMeta.setDisplayName(ChatColor.RESET + "" + teamColorToChatColor(game.getTeamFromName(otherTeam[1]).getColor().toString()) + " " + game.getTeamFromName(otherTeam[1]).getName() + " " + ChatColor.GRAY + "(" + ChatColor.WHITE + (other.getAmount() - 1) + ChatColor.GRAY + "/" + ChatColor.WHITE + game.getTeamFromName(otherTeam[1]).getMaxPlayers() + ChatColor.GRAY + ")");
                        other.setAmount(other.getAmount() - 1);
                        //    teamSize.put(game.getName()+";"+team[1],teamSize.get(game.getName()+";"+team[1])-1);
                    }
                    else
                    {
                        otherMeta.setDisplayName(ChatColor.RESET + "" + teamColorToChatColor(game.getTeamFromName(otherTeam[1]).getColor().toString()) + " " + game.getTeamFromName(otherTeam[1]).getName() + " " + ChatColor.GRAY + "(" + ChatColor.WHITE + "0" + ChatColor.GRAY + "/" + ChatColor.WHITE + game.getTeamFromName(otherTeam[1]).getMaxPlayers() + ChatColor.GRAY + ")");
                        //  teamSize.put(game.getName()+";"+team[1],teamSize.get(game.getName()+";"+team[1])-1);
                    }
                    other.setItemMeta(otherMeta);
                    teamSize.put(game.getName() + ";" + otherTeam[1], teamSize.get(game.getName() + ";" + otherTeam[1]) - 1);
                }
            }
        }
    }

    private int loreContains(List<String> c1, String c2)
    {
        if (c1 == null)
            return -1;

        for (int a = 0; a < c1.size(); a++)
        {
            String s = c1.get(a) + "§r";
            if (s.equalsIgnoreCase(c2))
                return a;
        }
        return -1;
    }

    public ChatColor teamColorToChatColor(String s)
    {
        switch (s.toUpperCase(Locale.ROOT))
        {
            case "LIGHT_GRAY":
                return ChatColor.GRAY;
            case "LIGHT_BLUE":
                return ChatColor.AQUA;
            case "LIME":
                return ChatColor.GREEN;
            case "PINK":
                return ChatColor.LIGHT_PURPLE;
            case "PURPLE":
                return ChatColor.DARK_PURPLE;
            case "ORANGE":
                return ChatColor.GOLD;
        }

        return ChatColor.valueOf(s);
    }

}

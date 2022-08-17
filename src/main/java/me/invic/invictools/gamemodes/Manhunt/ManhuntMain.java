package me.invic.invictools.gamemodes.Manhunt;

import me.invic.invictools.commands.OldCommands;
import me.invic.invictools.items.createItems;
import me.invic.invictools.gamemodifiers.LuckyBlocks.createLuckyBlocks;
import me.invic.invictools.util.fixes.SafeOpCommand;
import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.util.*;

public class ManhuntMain
{
    public static HashMap<Player, String> ManhuntTeam = new HashMap<>(); // Hunter BIG TEAM, Hunted SMALL TEAM aka CCs
    public static List<ItemStack> HunterItems = new ArrayList<>();
    public static List<String> HunterEffects = new ArrayList<>();
    public static List<ItemStack> HuntedItems = new ArrayList<>();
    public static List<String> HuntedEffects = new ArrayList<>();

    String Hunted = "Hunted";
    String Hunter = "Hunter";

    // grab loadout from config, start end game watcher, assign teams listing same way as teammate, force grab teammate when this runs

    public ManhuntMain(String loadout, Player hunted)
    {
        // only grabs teammates if this is a doubles game TEMP REMOVAL FOR QUIG EVENT
        //    if (Bukkit.getOnlinePlayers().size() > 9 && Bukkit.getOnlinePlayers().size() < 17)
        //       new GrabTeammates(Commands.MasterPlayer);

        //Loads teams, handles solos and doubles games
        setTeams(hunted);

        //creates loadout arraylists from config
        grabLoadout(loadout);

        //gets if proper manhunt or scuffed way to do lots of commands.
        boolean realmanhunt = getRealManhunt(loadout);

        //gives all players their loadouts and executes personal modifier commands
        giveLoadout(hunted);

        //wash chat with empty spaces to hide ugly command start syntaxes and grab a list of active modifiers that look clean so each team knows what they have.
        List<String> HunterModifiers = cleanSyntax(HunterEffects);
        List<String> HuntedModifiers = cleanSyntax(HuntedEffects);
        if (realmanhunt)
        {
            HunterModifiers.add(ChatColor.AQUA + "You are hunting the team of " + ChatColor.WHITE + hunted.getName());
            HunterModifiers.add(" ");
            HuntedModifiers.add(ChatColor.AQUA + "You are being hunted");
            HuntedModifiers.add(" ");
        }

        BukkitRunnable runnable = new BukkitRunnable()
        {
            @Override
            public void run()
            {
                ManhuntTeam.forEach((player, team) ->
                {
                    if (team.equalsIgnoreCase(Hunter))
                    {
                        for (String message : HunterModifiers)
                        {
                            player.sendMessage(message);
                        }
                    }
                    else if (team.equalsIgnoreCase(Hunted))
                    {
                        for (String message : HuntedModifiers)
                        {
                            player.sendMessage(message);
                        }
                    }
                });
            }
        };
        runnable.runTaskLater(Objects.requireNonNull(Bukkit.getServer().getPluginManager().getPlugin("Invictools")), 20);

        //watch for all of either teams elimination, notify hunters of their target team players, turn on compass, clear teammates at manhunt end. maybe force end game if possible

        //Temp array clearing after hunted leaves game world
        World GameWorld = hunted.getWorld();
        new BukkitRunnable()
        {
            @Override
            public void run()
            {
                if (hunted.getWorld() != GameWorld)
                {
                    ManhuntTeam.clear();

                    HuntedItems.clear();
                    HuntedEffects.clear();

                    HunterItems.clear();
                    HunterEffects.clear();

                    // System.out.println("Cleared");

                    this.cancel();
                }
            }
        }.runTaskTimer(Objects.requireNonNull(Bukkit.getServer().getPluginManager().getPlugin("Invictools")), 1, 120); // repeat every second so you can have a floating timer with named invis armor stand and just add variable that checks if its at delay to do item spawn
        // Audio notification of the game's start
        ManhuntTeam.forEach((player, s) -> player.playSound(player.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 1, 1));
    }

    private boolean getRealManhunt(String loadout)
    {
        Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("Invictools");
        File Folder = new File(plugin.getDataFolder(), "Manhunt");
        File pFile = new File(Folder, loadout + ".yml");
        //  System.out.println(plugin.getDataFolder() +loadout + ".yml");
        final FileConfiguration loadoutConfig = YamlConfiguration.loadConfiguration(pFile);
        return !loadoutConfig.getBoolean("fake");
    }

    private void grabLoadout(String loadout) //t
    {
        Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("Invictools");
        File Folder = new File(plugin.getDataFolder(), "Manhunt");
        File pFile = new File(Folder, loadout + ".yml");
        //  System.out.println(plugin.getDataFolder() +loadout + ".yml");
        final FileConfiguration loadoutConfig = YamlConfiguration.loadConfiguration(pFile);

        int HunterEffectSize = getModifierSize(Hunter, "effect", loadoutConfig);
        int HunterSpecialItemSize = getModifierSize(Hunter, "special", loadoutConfig);
        int HuntedEffectSize = getModifierSize(Hunted, "effect", loadoutConfig);
        int HuntedSpecialItemSize = getModifierSize(Hunted, "special", loadoutConfig);

        // Handles Normal Items
        @SuppressWarnings("unchecked")
        List<ItemStack> HunterNormalItems = (List<ItemStack>) loadoutConfig.get("Hunter.items");
        if (HunterNormalItems != null)
            HunterItems.addAll(HunterNormalItems);

        @SuppressWarnings("unchecked")
        List<ItemStack> HuntedNormalItems = (List<ItemStack>) loadoutConfig.get("Hunted.items");
        if (HuntedNormalItems != null)
            HuntedItems.addAll(HuntedNormalItems);

        // Handles Effect given by Commands
        for (int i = 0; i < HunterEffectSize; i++)
        {
            HunterEffects.add(loadoutConfig.getString("Hunter.effect." + i + ".name"));
        }
        for (int i = 0; i < HuntedEffectSize; i++)
        {
            HuntedEffects.add(loadoutConfig.getString("Hunted.effect." + i + ".name"));
        }

        // Handles Special Items
        for (int i = 0; i < HunterSpecialItemSize; i++)
        {
            String specialItem = loadoutConfig.getString("Hunter.special." + i + ".name"); // item / lb
            String specialType = loadoutConfig.getString("Hunter.special." + i + ".type"); // command parameter
            int specialAmount = loadoutConfig.getInt("Hunter.special." + i + ".amount"); // count
            if (specialItem.equals("item"))
            {
                ItemStack item = new createItems().getByName(specialType);
                item.setAmount(specialAmount);
                HunterItems.add(item);
            }
            else if (specialItem.equals("lb"))
            {
                ItemStack item = new createLuckyBlocks().getByName(specialType);
                item.setAmount(specialAmount);
                HunterItems.add(item);
            }
            else if (specialItem.equals("randomitem"))
            {
                ItemStack item = new createItems().getRandomItem();
                item.setAmount(specialAmount);
                HunterItems.add(item);
            }
            else if (specialItem.equals("randomlb"))
            {
                ItemStack item = new createLuckyBlocks().getRandomBlockWeighted();
                item.setAmount(specialAmount);
                HunterItems.add(item);
            }
        }
        for (int i = 0; i < HuntedSpecialItemSize; i++)
        {
            String specialItem = loadoutConfig.getString("Hunted.special." + i + ".name"); // item / lb
            String specialType = loadoutConfig.getString("Hunted.special." + i + ".type"); // specification for creation
            int specialAmount = loadoutConfig.getInt("Hunted.special." + i + ".amount"); // specification for creation
            if (specialItem.equals("item"))
            {
                ItemStack item = new createItems().getByName(specialType);
                item.setAmount(specialAmount);
                HuntedItems.add(item);
            }
            else if (specialItem.equals("lb"))
            {
                ItemStack item = new createLuckyBlocks().getByName(specialType);
                item.setAmount(specialAmount);
                HuntedItems.add(item);
            }
            else if (specialItem.equals("randomitem"))
            {
                ItemStack item = new createItems().getRandomItem();
                item.setAmount(specialAmount);
                HuntedItems.add(item);
            }
            else if (specialItem.equals("randomlb"))
            {
                ItemStack item = new createLuckyBlocks().getRandomBlockWeighted();
                item.setAmount(specialAmount);
                HuntedItems.add(item);
            }
        }
    }

    private void giveLoadout(Player hunted)
    {
        BukkitRunnable runnable = new BukkitRunnable()
        {
            @Override
            public void run()
            {
                for (Player player : Bukkit.getOnlinePlayers())
                {
                    if (player.getWorld().equals(hunted.getWorld()))
                    {
                        if (ManhuntTeam.get(player).equalsIgnoreCase(Hunted))
                        {
                            if (HuntedItems.size() != 0)
                            {
                                for (ItemStack item : HuntedItems)
                                {
                                    player.getInventory().addItem(item);
                                    player.playSound(player.getLocation(), Sound.ENTITY_CHICKEN_EGG, 1, 1);
                                }
                            }

                            if (HuntedEffects.size() != 0)
                            {
                                for (String huntedEffect : HuntedEffects)
                                {
                                    // String command = HuntedEffects.get(i);
                                    new SafeOpCommand(player, huntedEffect + player.getName());
                                /*    if(player.isOp())
                                    {
                                        Bukkit.dispatchCommand(player, command + player.getName());
                                    }
                                    else
                                    {
                                        player.setOp(true);
                                        Bukkit.dispatchCommand(player, command + player.getName());
                                        player.setOp(false);
                                    }
                                    System.out.println(command+player.getName());

                                 */
                                }
                            }
                        }
                        else if (ManhuntTeam.get(player).equalsIgnoreCase(Hunter))
                        {
                            if (HunterItems.size() != 0)
                            {
                                for (ItemStack item : HunterItems)
                                {
                                    player.getInventory().addItem(item);
                                    player.playSound(player.getLocation(), Sound.ENTITY_CHICKEN_EGG, 1, 1);
                                }
                            }

                            if (HunterEffects.size() != 0)
                            {
                                for (String hunterEffect : HunterEffects)
                                {
                                    //  String command = HunterEffects.get(i);
                                    new SafeOpCommand(player, hunterEffect + player.getName());
                                  /*  if(player.isOp())
                                    {
                                        Bukkit.dispatchCommand(player, command + player.getName());
                                    }
                                    else
                                    {
                                        player.setOp(true);
                                        Bukkit.dispatchCommand(player, command + player.getName());
                                        player.setOp(false);
                                    }
                                    System.out.println(command+player.getName());

                                   */
                                }
                            }
                        }
                    }
                }
            }
        };
        runnable.runTaskLater(Objects.requireNonNull(Bukkit.getServer().getPluginManager().getPlugin("Invictools")), 20);
    }

    private int getModifierSize(String team, String type, FileConfiguration config)
    {
        int itemSize = 0;

        while (true)
        {
            String temp = config.getString(team + "." + type + "." + itemSize);
            if (temp != null)
            {
                itemSize++;
            }
            else
                break;
        }
        return itemSize;
    }

    private void setTeams(Player hunted)
    {
        if (OldCommands.teammates.get(hunted) != null)
        {
            ManhuntTeam.put(OldCommands.teammates.get(hunted), Hunted);
            // Commands.teammates.get(hunted).sendMessage(ChatColor.AQUA + "You are about to be " + ChatColor.WHITE + Hunted);
        }
        ManhuntTeam.put(hunted, Hunted);
        //   hunted.sendMessage(ChatColor.AQUA + "You are about to be " + ChatColor.WHITE + Hunted);

        for (Player player : Bukkit.getOnlinePlayers())
        {
            if (player.getWorld() == hunted.getWorld() && player.getGameMode() != GameMode.SPECTATOR && player != hunted && ManhuntTeam.get(player) == null)
            {
                ManhuntTeam.put(player, Hunter);
            }
        }
    }

    static public List<String> cleanSyntax(List<String> effects)
    {
        List<String> list = new ArrayList<>();
      //  list.add(ChatColor.RED + "Stop using chat condensers you're ruining my formatting i spent so long making :(");
        for (int i = 0; i < 25; i++)
        {
            list.add(" ");
        }

        list.add(ChatColor.WHITE + "Your modifiers for this round:");
        list.add(" ");

        for (String command : effects)
        {
            String currentEffect;
            String[] commandSplit = command.split(" ");
            commandSplit[1] = commandSplit[1].toLowerCase(Locale.ROOT);
            switch (commandSplit[1])
            {
                case "alwaysbridge":
                case "alwaysbridgesingle":
                    currentEffect = ChatColor.AQUA + "  - Automatically bridge with wool in your hotbar (Hold sneak to disable)";
                    list.add(currentEffect);
                    break;
                case "giveitem":
                    currentEffect = ChatColor.AQUA + "  - Receive a " + TranslateCustomItem(commandSplit[3]) + ChatColor.AQUA + " at the start of the game.";
                    list.add(currentEffect);
                    break;
                case "deathitems":
                    if (commandSplit[4].equalsIgnoreCase("normal"))
                    {
                        currentEffect = ChatColor.AQUA + "  - Receive " + ChatColor.WHITE + commandSplit[3] + " " + removeUnderscores(commandSplit[2]).toLowerCase(Locale.ROOT) + "(s) " + ChatColor.AQUA + "after dying ";
                        list.add(currentEffect);
                        break;
                    }
                    else
                    {
                        currentEffect = ChatColor.AQUA + "  - Receive " + ChatColor.WHITE + commandSplit[3] + " " + TranslateCustomItem(commandSplit[2]) + ChatColor.AQUA + "(s) after dying ";
                        list.add(currentEffect);
                        break;
                    }
                case "disableshop":
                    currentEffect = ChatColor.RED + "  - You cannot use Item Shops this round!";
                    list.add(currentEffect);
                    break;
                case "preventdrop":
                    currentEffect = ChatColor.AQUA + "  - You cannot drop " + TranslateCustomItem(commandSplit[3]) + ChatColor.AQUA + " items during this round";
                    list.add(currentEffect);
                    break;
                case "closeteammateeffectsingle":
                    currentEffect = ChatColor.AQUA + "  - When near your teammate, receive " + ChatColor.WHITE + translatePotion(commandSplit[2]) + " " + commandSplit[3];
                    list.add(currentEffect);
                    break;
                case "creative":
                    currentEffect = ChatColor.AQUA + "  - Enter creative every " + ChatColor.WHITE + commandSplit[2] + ChatColor.AQUA + " seconds";
                    list.add(currentEffect);
                    break;
                case "creativeall":
                    currentEffect = ChatColor.AQUA + "  - Enter creative randomly ";
                    list.add(currentEffect);
                    break;
                case "single": // bw effects
                    if (commandSplit[3].equalsIgnoreCase("c") || commandSplit[3].equalsIgnoreCase("constant"))
                    {
                        currentEffect = ChatColor.AQUA + "  - Receive " + ChatColor.WHITE + translatePotion(commandSplit[2]) + " " + commandSplit[4];
                        list.add(currentEffect);
                    }
                    else if (commandSplit[3].equalsIgnoreCase("p") || commandSplit[3].equalsIgnoreCase("procedural"))
                    {
                        currentEffect = ChatColor.AQUA + "  - Receive " + ChatColor.WHITE + translatePotion(commandSplit[2]) + ChatColor.AQUA + " at an increasing level";
                        list.add(currentEffect);
                    }
                    break;
                case "randomeffectsingle":
                    currentEffect = ChatColor.AQUA + "  - Receive random effects every " + ChatColor.WHITE + commandSplit[3] + ChatColor.AQUA + " seconds ";
                    list.add(currentEffect);
                    break;
                case "sethealth":
                    currentEffect = ChatColor.AQUA + "  - You have " + ChatColor.WHITE + commandSplit[3] + ChatColor.AQUA + " health ";
                    list.add(currentEffect);
                    break;
                case "tnt":
                case "tntsingle":
                    currentEffect = ChatColor.RED + "  - Tnt spawns on you every " + ChatColor.WHITE + commandSplit[2] + ChatColor.AQUA + " seconds ";
                    list.add(currentEffect);
                    break;
                case "repeateditem":
                    if (commandSplit[5].equalsIgnoreCase("normal"))
                    {
                        currentEffect = ChatColor.AQUA + "  - Receive " + ChatColor.WHITE + commandSplit[3] + " " + removeUnderscores(commandSplit[2]).toLowerCase(Locale.ROOT) + "(s) " + ChatColor.AQUA + "every " + ChatColor.WHITE + commandSplit[4] + ChatColor.AQUA + " seconds ";
                        list.add(currentEffect);
                    }
                    else
                    {
                        currentEffect = ChatColor.AQUA + "  - Receive " + ChatColor.WHITE + commandSplit[3] + " " + TranslateCustomItem(commandSplit[2]) + "(s) " + ChatColor.AQUA + "every " + ChatColor.WHITE + commandSplit[4] + ChatColor.AQUA + " seconds ";
                        list.add(currentEffect);
                    }
                    break;
                case "killeffects":
                    currentEffect = ChatColor.AQUA + "  - Receive " + ChatColor.WHITE + translatePotion(commandSplit[2]) + ChatColor.AQUA + " after killing a player";
                    list.add(currentEffect);
                    break;
                case "killitems":
                    if (commandSplit[4].equalsIgnoreCase("normal"))
                    {
                        currentEffect = ChatColor.AQUA + "  - Receive " + ChatColor.WHITE + commandSplit[3] + " " + removeUnderscores(commandSplit[2]).toLowerCase(Locale.ROOT) + "(s) " + ChatColor.AQUA + "after killing a player ";
                        list.add(currentEffect);
                        break;
                    }
                    else
                    {
                        currentEffect = ChatColor.AQUA + "  - Receive " + ChatColor.WHITE + commandSplit[3] + " " + TranslateCustomItem(commandSplit[2]) + ChatColor.AQUA + "(s) after killing a player ";
                        list.add(currentEffect);
                        break;
                    }
                case "totems":
                    currentEffect = ChatColor.AQUA + "  - Instead of dying, you will activate a totem ";
                    list.add(currentEffect);
                    break;
                case "lucky":
                    currentEffect = ChatColor.AQUA + "  - Lucky Blocks will spawn from spawners, drop from players, and drop from beds ";
                    list.add(currentEffect);
                    break;
                case "nostats":
                    currentEffect = ChatColor.AQUA + "  - Stats will not track";
                    list.add(currentEffect);
                    break;
                case "nofall":
                    currentEffect = ChatColor.AQUA + "  - Fall damage is disabled";
                    list.add(currentEffect);
                    break;
                default:
                    currentEffect = command;
                    list.add(currentEffect);
                    break;
            }
        }

        list.add(" ");
        return list;
    }

    static String removeUnderscores(String text)
    {
        return text.replace("_", " ");
    }

    static String TranslateCustomItem(String text)
    {
        ItemStack item = new createItems().getByName(text);
        try
        {
            return item.getItemMeta().getDisplayName();
        }
        catch (NullPointerException e)
        {
            return "Lucky Block";
        }
    }

    static String translatePotion(String text)
    {
        text = removeUnderscores(text).toLowerCase(Locale.ROOT);
        //  text = text.substring(0, 1).toUpperCase() + text.substring(1);

        switch (text)
        {
            case "increase damage":
                text = "strength";
                return text;
            case "slow digging":
                text = "mining Fatigue";
                return text;
            case "damage resistance":
                text = "resistance";
                return text;
            case "confusion":
                text = "nausea";
                return text;
            case "fast digging":
                text = "haste";
                return text;
            case "slow":
                text = "slowness";
                return text;
            case "conduit power":
                text = "water enhancements";
                return text;
            case "heal":
                text = "instant health";
                return text;
            case "harm":
                text = "instant damage";
                return text;
        }
        return text;
    }
}



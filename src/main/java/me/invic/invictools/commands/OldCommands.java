package me.invic.invictools.commands;

import me.invic.invictools.cosmetics.Lobby1Handler;
import me.invic.invictools.cosmetics.VictoryDances.VictoryDanceHandler;
import me.invic.invictools.cosmetics.VictoryDances.VictoryDancePreview;
import me.invic.invictools.cosmetics.NormalKillHandler;
import me.invic.invictools.cosmetics.projtrail.ProjTrailHandler;
import me.invic.invictools.cosmetics.projtrail.ProjTrailPreview;
import me.invic.invictools.gamemodifiers.*;
import me.invic.invictools.items.createItems;
import me.invic.invictools.gamemodifiers.LuckyBlocks.createLuckyBlocks;
import me.invic.invictools.items.dareListener;
import me.invic.invictools.util.deathListener;
import me.invic.invictools.gamemodifiers.Manhunt.ManhuntMain;
import me.invic.invictools.gamemodifiers.PotionEffects.*;
import me.invic.invictools.items.giveitem;
import me.invic.invictools.util.*;
import me.invic.invictools.util.WorldBorder;
import me.invic.invictools.util.fixes.ChangeTeamSize;
import me.invic.invictools.util.fixes.LobbyInventoryFix;
import me.invic.invictools.util.npc.BlazeNpc;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.loot.LootTables;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.StringUtil;
import org.screamingsandals.bedwars.api.BedwarsAPI;
import org.screamingsandals.bedwars.api.game.Game;

import java.io.File;
import java.util.*;

import static me.invic.invictools.util.deathListener.addedNormalBlocks;
import static me.invic.invictools.util.deathListener.coalBlocks;
import static org.bukkit.Bukkit.*;

// firestick for game, item boxes, tnt rain,

public class OldCommands implements CommandExecutor, TabExecutor
{
    //plugins
    public static Plugin BWeffects = getServer().getPluginManager().getPlugin("Bweffects");
    public static Plugin Invictools = getServer().getPluginManager().getPlugin("Invictools");

    public static final String permissionsError = Invictools.getConfig().getString("Strings.NoPerm", "You do not have permission to run this command!");

    //global controls
    public static Player MasterPlayer = Bukkit.getPlayer("Invictable");
    public static HashMap<Player, Player> teammates = new HashMap<>();
    public static boolean LuckyBlocksEnabled = false;
    public static boolean FireStickEnabled = true;
    public static boolean StatsTrack = true;
    public static String HauntConfig = "normal";
    public static HashMap<Player, String> killEffects = new HashMap<>();
    public static HashMap<Player, Boolean> InfiniteTotems = new HashMap<>();
    public static HashMap<Player, ItemStack> killItems = new HashMap<>();
    public static HashMap<Player, ItemStack> deathItems = new HashMap<>();
    public static HashMap<Player, Boolean> Hauntable = new HashMap<>();
    public static HashMap<Player, Long> FireStickCooldown = new HashMap<>();
    public static HashMap<Player, Boolean> ProximityElytra = new HashMap<>();
    public static HashMap<Player, Boolean> noShop = new HashMap<>();
    public static boolean worldswap = false;

    //idiot
    public static List<String> worldstemp;
    public static List<String> gamestemp;
    public static double ytemp;
    public static List<String> blacklistedWorlds;

    //tnt
    int interval = 8; // seconds
    int fuse = 6; // seconds to explosion

    //random effect
    int intensityLimit = 4;
    int effecttime = 30;

    //idiot 2
    String name;
    String worldName;

    File Folder = new File(Invictools.getDataFolder(), "Panels");
    File[] yamlFiles = Folder.listFiles();

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args)
    {
        List<String> tabComplete = new ArrayList<>();
        if (args.length == 1)
        {
            //util
            //  tabComplete.add("Srestart");
            // tabComplete.add("SetArena");
            tabComplete.add("DestroyAddedBlocks");
            tabComplete.add("Pid");
            tabComplete.add("GivePID");
            tabComplete.add("Master");

            tabComplete.add("BedBreak");
            tabComplete.add("VictoryDance");
            tabComplete.add("VictoryDancePreview");
            tabComplete.add("FinalKill");
            tabComplete.add("NormalKill");
            tabComplete.add("NormalKillPreview");
            tabComplete.add("ProjTrail");
            tabComplete.add("Lobby1");

            tabComplete.add("GrabTeammates");
            tabComplete.add("ClearTeammates");
            tabComplete.add("DestroyHolos");
            tabComplete.add("ClearManhuntTeam");
            tabComplete.add("ForceManhuntTeam");
            tabComplete.add("ResetDeathCounter");
            tabComplete.add("ResetAttributes");
            //  tabComplete.add("Ranks");
            //  tabComplete.add("Debug");
            tabComplete.add("WorldBorder");
            //   tabComplete.add("SetTeamSize");
            //      tabComplete.add("Parkour");

            //      tabComplete.add("ToggleFireStick");
            //     tabComplete.add("ToggleVictory");

            tabComplete.add("jumping");
            tabComplete.add("repeatedcancel");
            tabComplete.add("preventdrop");
            tabComplete.add("reset");
            tabComplete.add("preventdrop");
            tabComplete.add("stats");
            tabComplete.add("panel");
            tabComplete.add("reloadnpc");
            tabComplete.add("shrieker");
            tabComplete.add("loadInventory");
            // tabComplete.add("leaderboard");

            //Global Game Modifiers
            tabComplete.add("tnt");
            tabComplete.add("LuckyBlocks");
            tabComplete.add("SetHealth");
            tabComplete.add("RandomEffect");
            tabComplete.add("Spawner");
            tabComplete.add("DamageTeammates");
            tabComplete.add("CloseTeammateEffect");
            tabComplete.add("AlwaysBridge");
            tabComplete.add("Manhunt");
            tabComplete.add("EffectSometimes");
            tabComplete.add("ProximityElytra");

            //Personal Game Modifiers
            tabComplete.add("Creative");
            tabComplete.add("RandomEffectSingle");
            tabComplete.add("CloseTeammateEffectSingle");
            tabComplete.add("AlwaysbridgeSingle");
            tabComplete.add("ProximityElytraSingle");
            tabComplete.add("disableshop");

            //Single or global game modifiers (uses all to apply to all players)
            tabComplete.add("RepeatedItem");
            tabComplete.add("KillEffects");
            tabComplete.add("KillItems");
            tabComplete.add("DeathItems");
            tabComplete.add("Totems");
            tabComplete.add("DeathAttribute");

            //Creation
            tabComplete.add("GiveItem");

            //Help
            //    tabComplete.add("Help");
        }
        else if (args.length == 2 && args[0].equalsIgnoreCase("reload"))
        {
            tabComplete.add("bweffects");
            tabComplete.add("invictools");
        }
        else if (args.length == 2 && args[0].equalsIgnoreCase("preventdrop"))
        {
            tabComplete.add("<item/normal> <itemname> <player>");
            tabComplete.add("<no args to clear>");
        }
        else if (args.length == 2 && args[0].equalsIgnoreCase("ProximityElytra") || args.length == 2 && args[0].equalsIgnoreCase("ProximityElytraSingle"))
        {
            tabComplete.add("<distance>");
            tabComplete.add("10");
        }
        else if (args.length == 2 && args[0].equalsIgnoreCase("disableshop"))
        {
            tabComplete.add("single");
            tabComplete.add("all");
        }
        else if (args.length == 2 && args[0].equalsIgnoreCase("shrieker"))
        {
            tabComplete.add("true");
            tabComplete.add("false");
        }
        else if (args.length == 3 && args[0].equalsIgnoreCase("shrieker"))
        {
            tabComplete.add("player");
            tabComplete.add("world");
        }
        else if (args.length == 2 && args[0].equalsIgnoreCase("jumping"))
        {
            tabComplete.add("x");
            tabComplete.add("y");
            tabComplete.add("op");
            tabComplete.add("range");
        }
        else if (args.length == 3 && args[0].equalsIgnoreCase("jumping"))
        {
            if (args[1].equalsIgnoreCase("range"))
            {
                tabComplete.add(String.valueOf(ExplosionsListener.range));
            }
            else if (args[1].equalsIgnoreCase("x"))
            {
                tabComplete.add(String.valueOf(ExplosionsListener.xzmultiplier));
            }
            else if (args[1].equalsIgnoreCase("y"))
            {
                tabComplete.add(String.valueOf(ExplosionsListener.ymultiplier));
            }
            else if (args[1].equalsIgnoreCase("op"))
            {
                tabComplete.add(String.valueOf(ExplosionsListener.op));
            }
        }
        else if (args.length == 3 && args[0].equalsIgnoreCase("ProximityElytraSingle"))
        {
            tabComplete.add("<Player>");
            for (Player player : Bukkit.getOnlinePlayers())
            {
                tabComplete.add(player.getName());
            }
        }
        else if (args.length >= 1 && args[0].equalsIgnoreCase("EffectSometimes"))
        {
            tabComplete.add("all 10 30 15 1 GLOWING Invictable");
            tabComplete.add("<all or one> <delay randomness> <delay min> <effect time> <effect level> <effect type> <player>");
        }
        else if (args.length == 2 && args[0].equalsIgnoreCase("SetTeamSize"))
        {
            tabComplete.add("EveryArena");
            tabComplete.add("SingleArena");
        }
        else if (args.length == 3 && args[0].equalsIgnoreCase("SetTeamSize") && args[1].equalsIgnoreCase("EveryArena"))
        {
            tabComplete.add("<team size>");
            tabComplete.add("2");
        }
        else if (args.length == 3 && args[0].equalsIgnoreCase("SetTeamSize") && args[1].equalsIgnoreCase("SingleArena"))
        {
            tabComplete.add("SingleTeam");
            tabComplete.add("EveryTeam");
        }
        else if (args.length == 4 && args[0].equalsIgnoreCase("SetTeamSize") && args[1].equalsIgnoreCase("SingleArena"))
        {
            FileConfiguration cfg = OldCommands.Invictools.getConfig();
            List<String> configs = cfg.getStringList("ArenaConversion");
            for (String s : configs)
            {
                String[] cutconfig = s.split("_");
                tabComplete.add(cutconfig[0]);
            }
        }
        else if (args.length == 5 && args[0].equalsIgnoreCase("SetTeamSize") && args[1].equalsIgnoreCase("SingleArena"))
        {
            tabComplete.add("<team size>");
            tabComplete.add("2");
        }
        else if (args.length == 6 && args[0].equalsIgnoreCase("SetTeamSize") && args[2].equalsIgnoreCase("SingleTeam"))
        {
            tabComplete.add("<team color>");
        }
        else if (args.length == 2 && args[0].equalsIgnoreCase("Manhunt"))
        {
            tabComplete.add("<config>");
            tabComplete.add("test");
            tabComplete.add("Scaffold");
            tabComplete.add("LuckyBlocks");
            tabComplete.add("100");
            tabComplete.add("spec1");
            tabComplete.add("spec2");
            tabComplete.add("weakshop");
            tabComplete.add("teamwork1");
        }
        else if (args.length == 3 && args[0].equalsIgnoreCase("Manhunt"))
        {
            tabComplete.add("random");
            for (Player player : Bukkit.getOnlinePlayers())
            {
                tabComplete.add(player.getName());
            }
        }
        else if (args.length >= 1 && args[0].equalsIgnoreCase("Totems"))
        {
            tabComplete.add("<all/player name>");
            for (Player player : Bukkit.getOnlinePlayers())
            {
                tabComplete.add(player.getName());
            }
            tabComplete.add("all");
        }
        else if (args.length >= 1 && args[0].equalsIgnoreCase("WorldBorder"))
        {
            tabComplete.add("250 50 240 0");
            tabComplete.add("InitialSize ReducedSize ReductionTime Center");
        }
        else if (args.length == 2 && args[0].equalsIgnoreCase("CloseTeammateEffectSingle") || (args.length == 2 && args[0].equalsIgnoreCase("CloseTeammateEffect")))
        {
            tabComplete.add("<effect>");
            tabComplete.add("FAST_DIGGING");
        }
        else if (args.length == 3 && args[0].equalsIgnoreCase("CloseTeammateEffectSingle") || (args.length == 3 && args[0].equalsIgnoreCase("CloseTeammateEffect")))
        {
            tabComplete.add("<player>");
            for (Player player : Bukkit.getOnlinePlayers())
            {
                tabComplete.add(player.getName());
            }
        }
        else if (args.length == 2 && args[0].equalsIgnoreCase("master"))
        {
            tabComplete.add("<master player>");
            for (Player player : Bukkit.getOnlinePlayers())
            {
                tabComplete.add(player.getName());
            }
        }
        else if (args.length == 2 && args[0].equalsIgnoreCase("spawner"))
        {
            tabComplete.add("oparmor");
            tabComplete.add("netherite");
            tabComplete.add("test");
            tabComplete.add("<config>");
        }
        else if (args.length == 2 && args[0].equalsIgnoreCase("giveitem"))
        {
            tabComplete.add("single");
            tabComplete.add("manhuntteam");
            tabComplete.add("bedwarsteam");
            tabComplete.add("all");
        }
        else if (args.length == 3 && args[0].equalsIgnoreCase("giveitem"))
        {
            tabComplete.addAll(createItems.getItemNames());
            tabComplete.add("good");
            tabComplete.add("op");
            tabComplete.add("bad");
            tabComplete.add("random");
        }
        else if (args.length == 4 && args[0].equalsIgnoreCase("giveitem"))
        {
            tabComplete.add("lb");
            tabComplete.add("item");
        }
        else if (args.length == 5 && args[0].equalsIgnoreCase("giveitem"))
        {
            tabComplete.add("<player>");
            for (Player player : Bukkit.getOnlinePlayers())
            {
                tabComplete.add(player.getName());
            }
        }
        else if (args.length == 3 && args[0].equalsIgnoreCase("spawner"))
        {
            tabComplete.add("mid");
            tabComplete.add("island");
            tabComplete.add("<mid or island location>");
        }
        else if (args.length == 2 && args[0].equalsIgnoreCase("DamageTeammates"))
        {
            tabComplete.add("7.5");
            tabComplete.add("<distance>");
        }
        else if (args.length == 3 && args[0].equalsIgnoreCase("DamageTeammates"))
        {
            tabComplete.add("15");
            tabComplete.add("<initial box size>");
        }
        else if (args.length == 4 && args[0].equalsIgnoreCase("DamageTeammates"))
        {
            tabComplete.add("2");
            tabComplete.add("<potion level>");
        }
        else if (args.length == 2 && args[0].equalsIgnoreCase("setarena"))
        {
            tabComplete.add("Oasis");
            tabComplete.add("MarsFours");
            tabComplete.add("Fusion");
            tabComplete.add("Monastery");
            tabComplete.add("MonasteryV2");
            tabComplete.add("Snowstorm");
            tabComplete.add("castle");
            tabComplete.add("Tree");
            tabComplete.add("Slipslide");
            tabComplete.add("Multiverse");
            tabComplete.add("none");
        }
        else if (args.length == 2 && args[0].equalsIgnoreCase("sethealth"))
        {
            tabComplete.add("all");
            tabComplete.add("one");
            tabComplete.add("reset");
            tabComplete.add("randomall");
            tabComplete.add("decrease");
            tabComplete.add("<every player, specified player, reset for all players, decrease over time, randomize>");
        }
        else if (args.length == 3 && args[0].equalsIgnoreCase("sethealth") && args[1].equalsIgnoreCase("all") || args.length == 3 && args[0].equalsIgnoreCase("sethealth") && args[1].equalsIgnoreCase("one"))
        {
            tabComplete.add("1");
            tabComplete.add("<health value>");
        }
        else if (args.length == 3 && args[0].equalsIgnoreCase("sethealth") && args[1].equalsIgnoreCase("randomall") || args[1].equalsIgnoreCase("decrease"))
        {
            tabComplete.add("40");
            tabComplete.add("<max health value>");
        }
        else if (args.length == 4 && args[0].equalsIgnoreCase("sethealth") && args[1].equalsIgnoreCase("one"))
        {
            tabComplete.add("Invictable");
            tabComplete.add("<playername>");
        }
        else if (args.length == 4 && args[0].equalsIgnoreCase("sethealth") && args[1].equalsIgnoreCase("randomall"))
        {
            tabComplete.add("30");
            tabComplete.add("<interval in seconds>");
        }
        else if (args.length == 4 && args[0].equalsIgnoreCase("sethealth") && args[1].equalsIgnoreCase("decrease"))
        {
            tabComplete.add("30");
            tabComplete.add("<interval in seconds>");
        }
        else if (args.length == 2 && args[0].equalsIgnoreCase("tnt") || args.length == 2 && args[0].equalsIgnoreCase("tntsingle"))
        {
            tabComplete.add(interval + " " + fuse);
            tabComplete.add("<seconds between tnt spawns> <fuse time in seconds");
        }
        else if (args.length >= 2 && args[0].equalsIgnoreCase("forceManhuntTeam"))
        {
            for (Player player : Bukkit.getOnlinePlayers())
            {
                tabComplete.add(player.getName());
            }
            tabComplete.add("forces player to be hunted");
        }
        else if (args.length == 2 && args[0].equalsIgnoreCase("DeathAttribute"))
        {
            tabComplete.add("all");
            for (Player player : Bukkit.getOnlinePlayers())
            {
                tabComplete.add(player.getName());
            }
        }
        else if (args.length == 3 && args[0].equalsIgnoreCase("DeathAttribute"))
        {
            for (Attribute a : Attribute.values())
            {
                tabComplete.add(a.toString());
            }
        }
        else if (args.length == 4 && args[0].equalsIgnoreCase("DeathAttribute"))
        {
            tabComplete.add("interval to increase or decrease attribute on death");
            tabComplete.add("2");
        }
        else if (args.length == 5 && args[0].equalsIgnoreCase("DeathAttribute"))
        {
            Player p = Bukkit.getOnlinePlayers().iterator().next();
            Double value = p.getAttribute(Attribute.valueOf(args[2])).getValue();
            tabComplete.add(String.valueOf(value));
        }
        else if (args.length == 4 && args[0].equalsIgnoreCase("DeathAttribute"))
        {
            tabComplete.add("interval to increase or decrease attribute on death");
            tabComplete.add("2");
        }
        else if (args.length == 2 && args[0].equalsIgnoreCase("teamworkeffect"))
        {
            tabComplete.add("<close to 0 effect> <amplifier> <further to 0 effect> <amplifier>");
            tabComplete.add("weakness 10 blindness 1");
        }
        else if (args.length == 2 && args[0].equalsIgnoreCase("panel"))
        {
            for (File file : yamlFiles)
            {
                String[] mapName = file.getName().split("\\.");
                tabComplete.add(mapName[0]);
            }
            tabComplete.add("reload");
        }
        else if (args.length == 3 && args[0].equalsIgnoreCase("creative"))
        {
            tabComplete.add("<Seconds in creative>");
            tabComplete.add("<10>");
        }
        else if (args.length == 4 && args[0].equalsIgnoreCase("creative"))
        {
            for (Player player : Bukkit.getOnlinePlayers())
            {
                tabComplete.add(player.getName());
            }
        }
        else if (args.length == 2 && args[0].equalsIgnoreCase("haunt"))
        {
            tabComplete.add("<all / player> <config>");
            for (Player player : Bukkit.getOnlinePlayers())
            {
                tabComplete.add(player.getName());
            }
        }
        else if (args.length == 2 && args[0].equalsIgnoreCase("InstantHaunt"))
        {
            tabComplete.add("<Player> <config>");
            tabComplete.add("DO NOT USE ON LIVING PLAYERS. SKIPS VALIDITY CHECKS.");
            for (Player player : Bukkit.getOnlinePlayers())
            {
                tabComplete.add(player.getName());
            }
        }
        else if (args.length == 2 && args[0].equalsIgnoreCase("leaderboard"))
        {
            tabComplete.add("reload");
            tabComplete.add("position");
        }
        else if (args.length == 3 && args[1].equalsIgnoreCase("reload"))
        {
            tabComplete.add("destroyedBeds");
            tabComplete.add("star");
            tabComplete.add("score");
            tabComplete.add("loses");
            tabComplete.add("deaths");
            tabComplete.add("wins");
            tabComplete.add("kills");
            tabComplete.add("finals");
            tabComplete.add("fkdr");
            tabComplete.add("kdr");
            tabComplete.add("wl");
        }
        else if (args.length == 2 && args[0].equalsIgnoreCase("itemrain"))
        {
            tabComplete.add("LootTable");
            tabComplete.add("Config");
        }
        else if (args.length == 2 && args[0].equalsIgnoreCase("itemrain"))
        {
            tabComplete.add("LootTable");
            tabComplete.add("Config");
        }
        else if (args.length == 3 && args[0].equalsIgnoreCase("itemrain") && args[1].equalsIgnoreCase("LootTable"))
        {
            tabComplete.add("<Loot Table>");
            for (LootTables table : LootTables.values())
            {
                tabComplete.add(table.toString());
            }
        }
        else if (args.length == 3 && args[0].equalsIgnoreCase("itemrain") && args[1].equalsIgnoreCase("Config"))
        {
            tabComplete.add("<Config>");
            tabComplete.add("table1");
        }
        else if (args.length == 4 && args[0].equalsIgnoreCase("itemrain"))
        {
            tabComplete.add("<delay in ticks>");
            tabComplete.add("20");
        }
        else if (args[0].equalsIgnoreCase("repeateditem"))
        {
            tabComplete.add("<item> <amount> <interval> <normal/random/specificitem/specificlb> <name/all>");
            tabComplete.add("GOLDEN_APPLE 1 30 normal Invictable");
            tabComplete.add("BOUNCE 1 45 specificItem Invictable");
            tabComplete.add("GOOD 1 45 specificLb Invictable");
            tabComplete.add("PAPER 2 30 random Invictable");
            tabComplete.add("SEA_LANTERN 2 30 random Invictable");
        }
        else if (args[0].equalsIgnoreCase("killitems"))
        {
            tabComplete.add("<item> <amount> <normal/lb/item> <name/all>");
            tabComplete.add("GOLDEN_APPLE 1 normal Invictable");
            tabComplete.add("GOOD 1 lb Invictable");
            tabComplete.add("BOUNCE 2 item Invictable");
        }
        else if (args[0].equalsIgnoreCase("deathitems"))
        {
            tabComplete.add("<item> <amount> <normal/lb/item> <name/all>");
            tabComplete.add("GOLDEN_APPLE 1 normal Invictable");
            tabComplete.add("GOOD 2 lb Invictable");
            tabComplete.add("FIRESTICK 1 item Invictable");
        }
        else if (args[0].equalsIgnoreCase("killeffects"))
        {
            tabComplete.add("<pottype> <level> <duration> <name/all>");
            tabComplete.add("INCREASE_DAMAGE 1 100 Invictable");
        }
        else if (args.length == 2 && args[0].equalsIgnoreCase("randomeffect") || args.length == 2 && args[0].equalsIgnoreCase("randomeffectsingle"))
        {
            tabComplete.add(intensityLimit + " " + effecttime);
            tabComplete.add("<effect amplifier limit> <time between effect changes");
        }
        else if (args.length == 2 && args[0].equalsIgnoreCase("bedbreak"))
        {
            tabComplete.add("Lightning");
            tabComplete.add("Ranked");
            tabComplete.add("Fireworks");
            tabComplete.add("Tornado");
            tabComplete.add("Holo");
            tabComplete.add("Enderman");
            tabComplete.add("Guardian");
        }
        else if (args.length == 2 && args[0].equalsIgnoreCase("VictoryDance"))
        {
            tabComplete.addAll(new VictoryDanceHandler().getEffects());
        }
        else if (args.length == 2 && args[0].equalsIgnoreCase("finalkill"))
        {
            tabComplete.add("Lightning");
            tabComplete.add("Ranked");
            tabComplete.add("Tornado");
            tabComplete.add("Firework");
            tabComplete.add("Head");
            tabComplete.add("Pres");
            tabComplete.add("Sonic");
        }
        else if (args.length == 2 && args[0].equalsIgnoreCase("normalkill"))
        {
            tabComplete.addAll(NormalKillHandler.NormalKillEffects().keySet());
        }
        else if (args.length == 2 && args[0].equalsIgnoreCase("lobby1"))
        {
            tabComplete.addAll(Lobby1Handler.Lobby1Effects().keySet());
        }
        else if (args.length == 2 && args[0].equalsIgnoreCase("projtrail"))
        {
            tabComplete.addAll(new ProjTrailHandler().getEffects());
        }
        else
        {
            for (Player player : Bukkit.getOnlinePlayers())
            {
                tabComplete.add(player.getName());
            }
        }

        if (sender.hasPermission("invic.invictools"))
        {
            return StringUtil.copyPartialMatches(args[0], tabComplete, new ArrayList<>());
        }
        else
        {
            return null;
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if(false)
        {}
//        if (args.length >= 2 && args[0].equalsIgnoreCase("BedBreak"))
//        {
//            if (args.length > 2 && sender.hasPermission("invic.invictools"))
//            {
//                Player player = Bukkit.getPlayer(args[2]);
//                assert player != null;
//                new BedBreakConfig(player, args[1], true);
//            }
//            else if (args.length == 2)
//            {
//                Player player = (Player) sender;
//                new BedBreakConfig(player, args[1], false);
//            }
//        }
//        else if (args.length >= 2 && args[0].equalsIgnoreCase("VictoryDance"))
//        {
//            if (args.length > 2 && sender.hasPermission("invic.invictools"))
//            {
//                Player player = Bukkit.getPlayer(args[2]);
//                assert player != null;
//                new VictoryDanceConfig(player, args[1], true);
//            }
//            else if (args.length == 2)
//            {
//                Player player = (Player) sender;
//                new VictoryDanceConfig(player, args[1], false);
//            }
//        }
//        else if (args.length >= 2 && args[0].equalsIgnoreCase("FinalKill"))
//        {
//            if (args.length > 2 && sender.hasPermission("invic.invictools"))
//            {
//                Player player = Bukkit.getPlayer(args[2]);
//                assert player != null;
//                new FinalKillConfig(player, args[1], true);
//            }
//            else if (args.length == 2)
//            {
//                Player player = (Player) sender;
//                new FinalKillConfig(player, args[1], false);
//            }
//        }
//        else if (args.length >= 2 && args[0].equalsIgnoreCase("NormalKill"))
//        {
//            if (args.length > 2 && sender.hasPermission("invic.invictools"))
//            {
//                Player player = Bukkit.getPlayer(args[2]);
//                assert player != null;
//                new NormalKillHandler().configHandler(player, args[1], true);
//            }
//            else if (args.length == 2)
//            {
//                Player player = (Player) sender;
//                new NormalKillHandler().configHandler(player, args[1], false);
//            }
//        }
//        else if (args.length >= 2 && args[0].equalsIgnoreCase("lobby1"))
//        {
//            if (args.length > 2 && sender.hasPermission("invic.invictools"))
//            {
//                Player player = Bukkit.getPlayer(args[2]);
//                assert player != null;
//                new Lobby1Handler().configHandler(player, args[1], true);
//            }
//            else if (args.length == 2)
//            {
//                Player player = (Player) sender;
//                new Lobby1Handler().configHandler(player, args[1], false);
//            }
//        }
//        else if (args.length >= 2 && args[0].equalsIgnoreCase("ProjTrail"))
//        {
//            if (args.length > 2 && sender.hasPermission("invic.invictools"))
//            {
//                Player player = Bukkit.getPlayer(args[2]);
//                assert player != null;
//                new ProjTrailConfig(player, args[1], true);
//            }
//            else if (args.length == 2)
//            {
//                Player player = (Player) sender;
//                new ProjTrailConfig(player, args[1], false);
//            }
//        }
//        else if (args.length == 1 && args[0].equalsIgnoreCase("ts"))
//        {
//            new TeamSelection().openInventory((Player) sender, BedwarsAPI.getInstance().getGameOfPlayer((Player) sender), true);
//            sender.sendMessage("Opened team selection");
//        }
//         if (args.length == 2 && args[0].equalsIgnoreCase("panel"))
//        {
//            if (!args[1].equals("reload"))
//                new panels().openInventory(args[1], (Player) sender);
//            else
//            {
//                new panels().loadPanels();
//                sender.sendMessage(ChatColor.AQUA + "Panels reloaded");
//            }
//        }
//        else if (args.length == 3 && args[0].equalsIgnoreCase("panel"))
//        {
//            new panels().openInventory(args[1], Bukkit.getPlayer(args[2]));
//        }
//        else if (args.length == 1 && args[0].equalsIgnoreCase("leaderboard"))
//        {
//            new leaderboard().printFormattedLeaderboard((Player) sender);
//        }
//        else if (args.length == 2 && args[0].equalsIgnoreCase("leaderboard") && args[1].equalsIgnoreCase("reload"))
//        {
//            new leaderboard().loadLeaderboard("Star");
//            new leaderboardHologram().createLeaderboard();
//        }
//        else if (args.length == 3 && args[0].equalsIgnoreCase("leaderboard") && args[1].equalsIgnoreCase("reload"))
//        {
//            new leaderboard().loadLeaderboard(args[2]);
//            new leaderboardHologram().createLeaderboard();
//        }
//        else if (args.length == 3 && args[0].equalsIgnoreCase("leaderboard") && args[1].equalsIgnoreCase("position"))
//        {
//            new leaderboard().givePosition(Bukkit.getOfflinePlayer(args[2]), (Player) sender);
//        }
//        else if (args.length == 2 && args[0].equalsIgnoreCase("leaderboard") && args[1].equalsIgnoreCase("position"))
//        {
//            new leaderboard().givePosition(Bukkit.getOfflinePlayer(UUID.fromString(sender.getName())), (Player) sender);
//        }
//        else if (args.length >= 1 && args[0].equalsIgnoreCase("createbfconfig"))
//        {
//            new bedfight().saveBedfightInventory(args[1], (Player) sender, true);
//            sender.sendMessage(ChatColor.AQUA + "Saved your inventory as bedfight loadout" + args[1]);
//        }
//        else if (args.length >= 1 && args[0].equalsIgnoreCase("rbfc"))
//        {
//            new bedfight().loadBedfightInventory(args[2], Bukkit.getPlayer(args[1]), true);
//            sender.sendMessage(ChatColor.AQUA + "reset bedfight inventory " + args[2] +" for " + args[1]);
//        }
//        else if (args.length >= 2 && args[0].equalsIgnoreCase("alljoin"))
//        {
//            if (BedwarsAPI.getInstance().isGameWithNameExists(args[1]))
//            {
//                for (Player player : Bukkit.getOnlinePlayers())
//                {
//                    if(!BedwarsAPI.getInstance().isPlayerPlayingAnyGame(player))
//                    {
//                        if(player.getWorld().getName().equalsIgnoreCase("bwlobby"))
//                            new LobbyInventoryFix().saveInventory(player);
//
//                        BedwarsAPI.getInstance().getGameByName(args[1]).joinToGame(player);
//                    }
//                }
//                sender.sendMessage(ChatColor.AQUA + "sending to game " + ChatColor.YELLOW + args[1]);
//            }
//            else
//            {
//                sender.sendMessage(ChatColor.RED + "Invalid Game: " + args[1]);
//            }
//        }
//        else if (args.length == 1 && args[0].equalsIgnoreCase("gamejoiner"))
//        {
//            Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("Invictools");
//            final FileConfiguration Config = plugin.getConfig();
//
//            String currentgame = Config.getString("loadedgame");
//            Player player = (Player) sender;
//
//            for (Player p : Bukkit.getOnlinePlayers()) //party catch
//            {
//                if (p.getWorld().getName().equalsIgnoreCase("bwlobby"))
//                    new LobbyInventoryFix().saveInventory(p);
//            }
//
//            if (currentgame.equalsIgnoreCase("none"))
//            {
//                player.sendMessage(ChatColor.RED + "No games are currently running! sorry :(");
//                player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_HURT, 2, 1);
//            }
//            else
//            {
//                BedwarsAPI.getInstance().getGameByName(currentgame).joinToGame(player);
//                //  new SafeOpCommand(player, "bw join " + currentgame);
//                new BukkitRunnable()
//                {
//                    @Override
//                    public void run()
//                    {
//                        if (!BedwarsAPI.getInstance().getGameByName(currentgame).getStatus().equals(GameStatus.WAITING))
//                            if (player.getWorld().equals(BedwarsAPI.getInstance().getGameByName(currentgame).getGameWorld()))
//                                player.setGameMode(GameMode.SPECTATOR);
//                        player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 2, 1);
//                    }
//                }.runTaskLater(Commands.Invictools, 5L);
//            }
//        }
//        else if (args.length == 1 && args[0].equalsIgnoreCase("tracker"))
//        {
//            Player player = (Player) sender;
//            Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("Invictools");
//            //   File pFile = new File(plugin.getDataFolder().getPath() + "\\PlayerData" + File.separator + player.getUniqueId() + ".yml");
//            File Folder = new File(plugin.getDataFolder(), "PlayerData");
//            File pFile = new File(Folder, player.getUniqueId() + ".yml");
//            final FileConfiguration playerData = YamlConfiguration.loadConfiguration(pFile);
//
//            int tracker = playerData.getInt("tracker");
//            tracker++;
//            playerData.set("tracker", tracker);
//            try
//            {
//                playerData.save(pFile);
//            }
//            catch (IOException e)
//            {
//                e.printStackTrace();
//            }
//        }
//        else if (args.length >= 2 && args[0].equalsIgnoreCase("FinalKillTest") && args[2].equalsIgnoreCase("cp"))
//        {
//            Player player = (Player) sender;
//            Location oldloc = player.getLocation();
//            Location tp = new Location(player.getLocation().getWorld(), 6.5, 126, 6.5, 134, -30);
//            String effect = args[1];
//
//            player.teleport(tp);
//            Location loc = new Location(player.getLocation().getWorld(), 0.5, 129, 0.5);
//
//            new BukkitRunnable()
//            {
//                @Override
//                public void run()
//                {
//                    new FinalKillHandler().effectSwitch(effect, player, player, loc);
//                }
//            }.runTaskLater(Commands.Invictools, 20L);
//
//            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.BOLD + "Hold crouch to cancel"));
//            BukkitRunnable runnable2 = new BukkitRunnable()
//            {
//                final int cancelAt = 20 * 5;
//                int i = 0;
//
//                @Override
//                public void run()
//                {
//                    i++;
//                    if ((player.getLocation().getWorld() == oldloc.getWorld() && i == cancelAt) || player.isSneaking())
//                    {
//                        player.teleport(oldloc);
//                        this.cancel();
//                    }
//
//                    if (i == cancelAt)
//                        this.cancel();
//                }
//            };
//            runnable2.runTaskTimer(Objects.requireNonNull(Bukkit.getServer().getPluginManager().getPlugin("Invictools")), 10, 1);
//        }
//        else if (args.length >= 2 && args[0].equalsIgnoreCase("NormalKillPreview") && args[2].equalsIgnoreCase("cp"))
//        {
//            Player player = (Player) sender;
//            Location oldloc = player.getLocation();
//            Location tp = new Location(player.getLocation().getWorld(), 6.5, 126, 6.5, 134, -10);
//            String effect = args[1];
//
//            player.teleport(tp);
//            Location loc = new Location(player.getLocation().getWorld(), 0.5, 129, 0.5);
//
//            new BukkitRunnable()
//            {
//                @Override
//                public void run()
//                {
//                    new NormalKillHandler().effectSwitch(player, player, loc, effect);
//                }
//            }.runTaskLater(Commands.Invictools, 20L);
//
//            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.BOLD + "Hold crouch to cancel"));
//            BukkitRunnable runnable2 = new BukkitRunnable()
//            {
//                final int cancelAt = 20 * 5;
//                int i = 0;
//
//                @Override
//                public void run()
//                {
//                    i++;
//                    if ((player.getLocation().getWorld() == oldloc.getWorld() && i == cancelAt) || player.isSneaking())
//                    {
//                        player.teleport(oldloc);
//                        this.cancel();
//                    }
//
//                    if (i == cancelAt)
//                        this.cancel();
//                }
//            };
//            runnable2.runTaskTimer(Objects.requireNonNull(Bukkit.getServer().getPluginManager().getPlugin("Invictools")), 10, 1);
//        }
//        if (args.length >= 2 && args[0].equalsIgnoreCase("BedBreakTest") && args[2].equalsIgnoreCase("cp"))
//        {
//            Player player = (Player) sender;
//            Location oldloc = player.getLocation();
//            Location tp = new Location(player.getLocation().getWorld(), 250.5, 128, 283.5, 180, 0);
//            String effect = args[1];
//
//            final int[] wait = new int[1];
//
//            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.BOLD + "Hold crouch to cancel"));
//            player.teleport(tp);
//            Location loc = new Location(player.getLocation().getWorld(), 250.5, 128.5, 278.5);
//
//            new BukkitRunnable()
//            {
//                @Override
//                public void run()
//                {
//                    new BedBreaks().handle(loc, player, loc.getBlock().getType().toString(), false, effect);
//                }
//            }.runTaskLater(Commands.Invictools, 20L);
//
//            BukkitRunnable runnable = new BukkitRunnable()
//            {
//                @Override
//                public void run()
//                {
//                    switch (effect)
//                    {
//                        case "Lightning":
//                            wait[0] = 50; // 2.5 sec
//                            break;
//                        case "Fireworks":
//                            wait[0] = 80;
//                            break;
//                        case "Ranked":
//                            wait[0] = 70;
//                            break;
//                        case "Tornado":
//                            wait[0] = 40;
//                            break;
//                        case "Holo":
//                            wait[0] = 60;
//                            break;
//                        default:
//                            wait[0] = 50;
//                    }
//
//                    BukkitRunnable runnable2 = new BukkitRunnable()
//                    {
//                        final int cancelAt = wait[0];
//                        int i = 0;
//
//                        @Override
//                        public void run()
//                        {
//                            i++;
//                            if ((player.getLocation().getWorld() == oldloc.getWorld() && i == cancelAt) || player.isSneaking())
//                            {
//                                player.teleport(oldloc);
//                                this.cancel();
//                            }
//
//                            if (i == cancelAt)
//                                this.cancel();
//                        }
//                    };
//                    runnable2.runTaskTimer(Objects.requireNonNull(Bukkit.getServer().getPluginManager().getPlugin("Invictools")), 30, 1);
//                }
//            };
//            runnable.runTaskLater(Objects.requireNonNull(Bukkit.getServer().getPluginManager().getPlugin("Invictools")), 10L);
//        }
//        else if (args.length >= 1 && args[0].equalsIgnoreCase("ranks"))
//        {
//            Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("Invictools");
//            final FileConfiguration Config = plugin.getConfig();
//            List<String> messages = Config.getStringList("RankMessages");
//            for (String message : messages)
//            {
//                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
//            }
//        }
//        else if (args.length >= 1 && args[0].equalsIgnoreCase("stats"))
//        {
//            if (StatsTrack)
//            {
//                StatsTrack = false;
//                sender.sendMessage(ChatColor.AQUA + "Stats should no longer track");
//            }
//            else
//            {
//                StatsTrack = true;
//                sender.sendMessage(ChatColor.AQUA + "Stats should now track");
//            }
//        }
//        else if (args.length >= 1 && args[0].equalsIgnoreCase("portals"))
//        {
//            if (worldswap)
//            {
//                worldswap = false;
//                sender.sendMessage(ChatColor.AQUA + "Portals Disabled");
//            }
//            else
//            {
//                worldswap = true;
//                sender.sendMessage(ChatColor.AQUA + "Portals Enabled");
//            }
//        }
//        else if (args.length >= 1 && args[0].equalsIgnoreCase("reloadnpc"))
//        {
//            for (Entity e : Bukkit.getWorld("bwlobby").getEntitiesByClass(ArmorStand.class))
//            {
//                if (!e.hasMetadata("holo"))
//                    e.remove();
//            }
//            for (Entity e : Bukkit.getWorld("bwlobby").getEntitiesByClass(Illusioner.class))
//            {
//                e.remove();
//            }
//            for (Entity e : Bukkit.getWorld("bwlobby").getEntitiesByClass(Allay.class))
//            {
//                e.remove();
//            }
//            new BlazeNpc().spawnNPC("npc", true);
//            new BlazeNpc().spawnNPC("npc2", false);
//        }
//        else if (sender.hasPermission("invic.firestick") && args[0].equalsIgnoreCase("firestick") && FireStickEnabled)
//        {
//            if (Bukkit.getPlayer(sender.getName()).getWorld().getName().equals("bwlobby"))
//                new FireStick((Player) sender);
//        }
//        else if (sender.hasPermission("invic.firestick") && args[0].equalsIgnoreCase("lobbydare") && FireStickEnabled)
//        {
//            Player p = (Player) sender;
//            if (Bukkit.getPlayer(sender.getName()).getWorld().getName().equals("bwlobby"))
//                new dareListener().handleItem(p.getLocation(), p, true, true);
//        }
//        else if (sender.hasPermission("invic.firestick") && args[0].equalsIgnoreCase("lobbydare") && !FireStickEnabled)
//        {
//            sender.sendMessage(ChatColor.GOLD + "Lobby Dare " + ChatColor.RED + "is currently disabled.");
//        }
//        else if (sender.hasPermission("invic.firestick") && args[0].equalsIgnoreCase("firestick") && !FireStickEnabled)
//        {
//            sender.sendMessage(ChatColor.GOLD + "FireSticks " + ChatColor.RED + "are currently disabled.");
//        }
//         if (args.length > 0 && args[0].equalsIgnoreCase("projpreview"))
//        {
//            Player p = (Player) sender;
//            if (p.getWorld().getName().equalsIgnoreCase("bwlobby"))
//            {
//                new ProjTrailPreview().handle(args[1], p);
//            }
//        }
//        else if (args.length > 0 && args[0].equalsIgnoreCase("VictoryDancePreview"))
//        {
//            Player p = (Player) sender;
//            if (p.getWorld().getName().equalsIgnoreCase("bwlobby"))
//            {
//                new VictoryDancePreview().handle(args[1], p);
//            }
//        }
//        else if (sender.hasPermission("invic.invictools"))
//        {
//            if (args.length > 0 && args[0].equalsIgnoreCase("destroyholos"))
//            {
//                Player p = (Player) sender;
//                for (Entity e : p.getWorld().getEntitiesByClass(ArmorStand.class))
//                {
//                    if (!e.hasMetadata("holo"))
//                    {
//                        e.remove();
//                    }
//                }
//                //  Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "minecraft:kill @e[type=minecraft:armor_stand]");
//                sender.sendMessage(ChatColor.AQUA + "All non scoreboard holographic displays have been deleted!");
//            }
//            else if (args.length >= 1 && args[0].equalsIgnoreCase("debug"))
//            {
//                debug(sender);
//            }
//            else if (args.length >= 1 && args[0].equalsIgnoreCase("forceManhuntTeam"))
//            {
//                ManhuntMain.ManhuntTeam.put(Bukkit.getPlayer(args[1]), "Hunted");
//                sender.sendMessage(ChatColor.YELLOW + " " + Bukkit.getPlayer(args[1]).getName() + ChatColor.AQUA + " will be hunted.");
//            }
//            else if (args.length >= 1 && args[0].equalsIgnoreCase("loadInventory"))
//            {
//                new LobbyInventoryFix().loadInventory(Bukkit.getPlayer(args[1]), Bukkit.getPlayer(args[2]));
//                sender.sendMessage(ChatColor.YELLOW + " " + Bukkit.getPlayer(args[1]).getName() + ChatColor.AQUA + "'s Inventory has been loaded");
//            }
//            else if (args.length >= 1 && args[0].equalsIgnoreCase("Parkour"))
//            {
//                new disableParkour();
//                sender.sendMessage(ChatColor.AQUA + "Toggling Parkour");
//            }
//            else if (args.length >= 1 && args[0].equalsIgnoreCase("clearManhuntTeam"))
//            {
//                ManhuntMain.ManhuntTeam.clear();
//                ManhuntMain.HuntedItems.clear();
//                ManhuntMain.HuntedEffects.clear();
//
//                ManhuntMain.HunterItems.clear();
//                ManhuntMain.HunterEffects.clear();
//                sender.sendMessage(ChatColor.AQUA + "Manhunt team pre-assignments and data has been cleared");
//            }
//            else if (args.length >= 1 && args[0].equalsIgnoreCase("ResetAttributes"))
//            {
//                AbtributesOnDeath.resetAttributes();
//                sender.sendMessage(ChatColor.AQUA + "All players active attributes modifiers have been reset");
//            }
//            else if (args.length >= 1 && args[0].equalsIgnoreCase("ResetDeathCounter"))
//            {
//                DeathCounter.resetCounter();
//                sender.sendMessage(ChatColor.AQUA + "Death counter reset");
//            }
//            else if (args.length >= 3 && args[0].equalsIgnoreCase("shrieker"))
//            {
//                if (args[2].equalsIgnoreCase("player") || args[2].equalsIgnoreCase("p"))
//                {
//                    new WardenSpawner().ShriekerFromWorld(Bukkit.getPlayer(args[3]).getWorld(), Boolean.parseBoolean(args[1]));
//                }
//                else
//                {
//                    new WardenSpawner().ShriekerFromWorld(Bukkit.getWorld(args[3]), Boolean.parseBoolean(args[1]));
//                }
//
//                // sender.sendMessage(ChatColor.AQUA + "Default world border closing on 0,0...");
//            }
//            else if (args.length == 1 && args[0].equalsIgnoreCase("worldborder"))
//            {
//                new WorldBorder(250, 50, 240, 0, ((Player) sender).getWorld(), true, (Player) sender);
//                sender.sendMessage(ChatColor.AQUA + "world border closing");
//            }
//            else if (args.length > 1 && args[0].equalsIgnoreCase("worldborder"))
//            {
//                new WorldBorder(Integer.parseInt(args[1]), Integer.parseInt(args[2]), Integer.parseInt(args[3]), Integer.parseInt(args[4]), ((Player) sender).getWorld(), false, (Player) sender);
//                sender.sendMessage(ChatColor.AQUA + "World border closing on x/z" + Integer.parseInt(args[4]));
//            }
//            else if (args.length == 1 && args[0].equalsIgnoreCase("grabteammates"))
//            {
//                new GrabTeammates((Player) sender);
//                //   sender.sendMessage(ChatColor.AQUA + "Grabbing teammates...");
//            }
//            else if (args.length == 1 && args[0].equalsIgnoreCase("clearteammates"))
//            {
//                teammates.clear();
//                sender.sendMessage(ChatColor.AQUA + "Teammates cleared...");
//            }
//            else if (args.length >= 1 && args[0].equalsIgnoreCase("disableshop"))
//            {
//                Player p = (Player) sender;
//                if (args[1].equalsIgnoreCase("single"))
//                {
//                    noShop.put(Bukkit.getPlayer(args[2]), true);
//                }
//                else if (args[1].equalsIgnoreCase("all"))
//                {
//                    for (Player o : Bukkit.getOnlinePlayers())
//                    {
//                        if (o.getWorld().equals(p.getWorld()) && o.getGameMode().equals(GameMode.SURVIVAL))
//                        {
//                            noShop.put(o, true);
//                        }
//                    }
//                }
//            }
//            else if (args.length >= 1 && args[0].equalsIgnoreCase("reset"))
//            {
//                if (sender instanceof Player)
//                {
//                    Player p = (Player) sender;
//                    deathListener.clearEverything(p.getWorld());
//                }
//                else
//                {
//                    deathListener.clearEverything(Bukkit.getWorld("bwlobby"));
//                }
//
//                sender.sendMessage(ChatColor.AQUA + "Scenarios mostly reset.");
//            }
//            else if (args.length >= 1 && args[0].equalsIgnoreCase("ProximityElytra"))
//            {
//                ItemStack item = new ItemStack(Material.ELYTRA);
//                ItemMeta meta = item.getItemMeta();
//                meta.setDisplayName(ChatColor.YELLOW + "Proximity Elytra");
//                List<String> lore = new ArrayList<>();
//                lore.add(ChatColor.translateAlternateColorCodes('&', "&cStay near your teammates or you'll lose it!"));
//                meta.setLore(lore);
//                item.setItemMeta(meta);
//
//                BedwarsAPI api = BedwarsAPI.getInstance();
//                for (Player p : api.getGameOfPlayer((Player) sender).getConnectedPlayers())
//                {
//                    System.out.println("stating elytra for " + p.getName());
//                    new CloseElytra(Double.parseDouble(args[1]), p, item);
//                    p.sendMessage(ChatColor.AQUA + "You will now receive an Elytra when near your teammates");
//                    ProximityElytra.put(p, true);
//                }
//            }
//            else if (args.length >= 1 && args[0].equalsIgnoreCase("ProximityElytraSingle"))
//            {
//                Player p = Bukkit.getPlayer(args[2]);
//
//                ItemStack item = new ItemStack(Material.ELYTRA);
//                ItemMeta meta = item.getItemMeta();
//                meta.setDisplayName(ChatColor.YELLOW + "Proximity Elytra");
//                List<String> lore = new ArrayList<>();
//                lore.add(ChatColor.translateAlternateColorCodes('&', "&cStay near your teammates or you'll lose it!"));
//                meta.setLore(lore);
//                item.setItemMeta(meta);
//
//                new CloseElytra(Double.parseDouble(args[1]), p, item);
//                p.sendMessage(ChatColor.AQUA + "You will now receive an Elytra when near your teammates");
//                ProximityElytra.put(p, true);
//            }
//            else if (args.length > 1 && args[0].equalsIgnoreCase("SetTeamSize"))
//            {
//                if (args[1].equalsIgnoreCase("EveryArena"))
//                {
//                    ChangeTeamSize.ChangeEveryArenaTeamSize(Integer.parseInt(args[2]));
//                }
//                else if (args[1].equalsIgnoreCase("SingleArena"))
//                {
//                    if (args[2].equalsIgnoreCase("EveryTeam"))
//                    {
//                        ChangeTeamSize.ChangeSingleArenaTeamSize(args[3], Integer.parseInt(args[4]));
//                    }
//                    else if (args[2].equalsIgnoreCase("SingleTeam"))
//                    {
//                        ChangeTeamSize.ChangeSingleTeamSize(args[3], Integer.parseInt(args[4]), args[5]);
//                    }
//                }
//            }
//            else if (args.length == 1 && args[0].equalsIgnoreCase("SetTeamSize"))
//            {
//                ChangeTeamSize.printTeamSizes(sender);
//            }
//            else if (args.length >= 1 && args[0].equalsIgnoreCase("EffectSometimes"))
//            {
//                if (args[1].equalsIgnoreCase("all"))
//                {
//                    new EffectSometimes(true, Bukkit.getPlayer(args[7]), args[6], Integer.parseInt(args[2]), Integer.parseInt(args[3]), Integer.parseInt(args[4]), Integer.parseInt(args[5]));
//                }
//                else
//                {
//                    new EffectSometimes(false, Bukkit.getPlayer(args[7]), args[6], Integer.parseInt(args[2]), Integer.parseInt(args[3]), Integer.parseInt(args[4]), Integer.parseInt(args[5]));
//                }
//                sender.sendMessage(ChatColor.AQUA + "Command went through but i didnt code this part yet so dont look");
//            }
//            else if (args.length == 1 && args[0].equalsIgnoreCase("printitemdata") || (args.length == 1 && args[0].equalsIgnoreCase("pid")))
//            {
//                Player player = (Player) sender;
//                System.out.println(player.getInventory().getItem(40));
//                FileConfiguration config = Invictools.getConfig();
//                config.set("pid", player.getInventory().getItem(40));
//                Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("Invictools");
//                plugin.saveConfig();
//                sender.sendMessage(ChatColor.AQUA + "Logged the ItemStack in your offhand in the console and config : " + player.getInventory().getItem(40).getType());
//            }
//            else if (args.length == 1 && args[0].equalsIgnoreCase("givePID"))
//            {
//                Player player = (Player) sender;
//                FileConfiguration config = Invictools.getConfig();
//                ItemStack item = (ItemStack) config.get("pid");
//                final Map<Integer, ItemStack> map = player.getInventory().addItem(item);
//                player.playSound(player.getLocation(), Sound.ENTITY_CHICKEN_EGG, 1, 1);
//                for (final ItemStack i : map.values())
//                {
//                    player.getWorld().dropItemNaturally(player.getLocation(), i);
//                }
//            }
//            else if (args.length == 1 && args[0].equalsIgnoreCase("sreload"))
//            {
//                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "minecraft:kill @e[type=minecraft:armor_stand]");
//                sender.sendMessage(ChatColor.AQUA + "Safe reloading... This command is pointless now you moron.");
//                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "reload");
//            }
//            else if (args.length == 1 && args[0].equalsIgnoreCase("srestart"))
//            {
//                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "minecraft:kill @e[type=minecraft:armor_stand]");
//                sender.sendMessage(ChatColor.AQUA + "Safe restarting... This command is pointless now you moron.");
//                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "restart");
//            }
//            else if (args.length == 1 && args[0].equalsIgnoreCase("ToggleFireStick"))
//            {
//                FireStickEnabled = !FireStickEnabled;
//
//                sender.sendMessage(ChatColor.AQUA + "Firestick Enabled is now " + ChatColor.YELLOW + FireStickEnabled);
//            }
//            else if (args.length == 1 && args[0].equalsIgnoreCase("ToggleVictory"))
//            {
//                VictoryDancePreview.VictoryPreviewEnabled = !VictoryDancePreview.VictoryPreviewEnabled;
//
//                sender.sendMessage(ChatColor.AQUA + "Victory Dance Preview Enabled is now " + ChatColor.YELLOW + VictoryDancePreview.VictoryPreviewEnabled);
//            }
//            else if (args.length >= 1 && args[0].equalsIgnoreCase("itemrain"))
//            {
//                if (args[1].equalsIgnoreCase("loottable"))
//                {
//                    ItemRain.ItemRainLootTable(args[2], Integer.parseInt(args[3]), (Player) sender);
//                }
//                else if (args[1].equalsIgnoreCase("config"))
//                {
//                    ItemRain.ItemRainConfig(args[2], Integer.parseInt(args[3]), (Player) sender);
//                }
//                ItemRain.IsEnabled = true;
//                sender.sendMessage(ChatColor.AQUA + "Raining items in the sky using LootTable " + ChatColor.YELLOW + args[2]);
//            }
//            else if (args.length >= 1 && args[0].equalsIgnoreCase("DeathAttribute"))
//            {
//                if (args[1].equalsIgnoreCase("all")) // it DeathAttribute all Attribute interval basevalue
//                {
//                    new AbtributesOnDeath().AbtributesOnDeathAll((Player) sender, args[2], Double.parseDouble(args[3]), Double.parseDouble(args[4]));
//                }
//                else if (args[1].equalsIgnoreCase("single")) // it DeathAttribute single Attribute interval basevalue player
//                {
//                    new AbtributesOnDeath().AttributesOnDeathSingular(getPlayer(args[5]), args[2], Double.parseDouble(args[3]), Double.parseDouble(args[4]));
//                }
//            }
//            else if (args.length >= 1 && args[0].equalsIgnoreCase("totems"))
//            {
//                if (args[1].equalsIgnoreCase("all"))
//                {
//                    for (Player player : Bukkit.getOnlinePlayers())
//                    {
//                        InfiniteTotems.put(player, true);
//                        player.sendMessage(ChatColor.AQUA + "You are now nearly immortal");
//                        World world = player.getWorld();
//                        new BukkitRunnable() // kills at max 1 minute after match ends or player dies and leaves
//                        {
//                            @Override
//                            public void run()
//                            {
//                                if (player == null)
//                                {
//                                    this.cancel();
//                                }
//
//                                if (player.getWorld() != world)
//                                {
//                                    InfiniteTotems.remove(player);
//                                    player.sendMessage(ChatColor.AQUA + "Totem disabled");
//                                    this.cancel();
//                                }
//                            }
//                        }.runTaskTimer(Objects.requireNonNull(Bukkit.getServer().getPluginManager().getPlugin("Invictools")), 0L, 120L);
//                    }
//                }
//                else
//                {
//                    Player player = Bukkit.getPlayer(args[1]);
//                    InfiniteTotems.put(Bukkit.getPlayer(args[1]), true);
//                    player.sendMessage(ChatColor.AQUA + "You are now nearly immortal");
//                    World world = player.getWorld();
//                    new BukkitRunnable() // kills at max 1 minute after match ends or player dies and leaves
//                    {
//                        @Override
//                        public void run()
//                        {
//                            if (player == null)
//                            {
//                                this.cancel();
//                            }
//
//                            if (player.getWorld() != world)
//                            {
//                                InfiniteTotems.remove(player);
//                                player.sendMessage(ChatColor.AQUA + "Totem disabled");
//                                this.cancel();
//                            }
//                        }
//                    }.runTaskTimer(Objects.requireNonNull(Bukkit.getServer().getPluginManager().getPlugin("Invictools")), 0L, 120L);
//                }
//            }
//            else if (args.length >= 1 && args[0].equalsIgnoreCase("haunt"))
//            {
//                HauntConfig = args[2];
//                if (args[1].equalsIgnoreCase("all"))
//                {
//                    for (Player player : BedwarsAPI.getInstance().getGameOfPlayer((Player) sender).getConnectedPlayers())
//                    {
//                        player.sendMessage(ChatColor.AQUA + "You can now haunt players after you are final killed");
//                        //  if (MasterPlayer.getWorld().equals(player.getWorld()))
//                        {
//                            Hauntable.put(player, true);
//                            World world = player.getWorld();
//
//                            new BukkitRunnable() // kills at max 1 minute after match ends or player dies and leaves
//                            {
//                                @Override
//                                public void run()
//                                {
//                                    if (player == null)
//                                    {
//                                        this.cancel();
//                                    }
//
//                                    if (player.getWorld() != world)
//                                    {
//                                        Hauntable.remove(player);
//                                        this.cancel();
//                                    }
//                                }
//                            }.runTaskTimer(Objects.requireNonNull(Bukkit.getServer().getPluginManager().getPlugin("Invictools")), 0L, 120L);
//                        }
//                    }
//                }
//                else
//                {
//                    Player player = Bukkit.getPlayer(args[1]);
//                    player.sendMessage(ChatColor.AQUA + "You can now haunt players after you are final killed");
//                    //  if (MasterPlayer.getWorld().equals(player.getWorld()))
//                    {
//                        Hauntable.put(player, true);
//                        World world = player.getWorld();
//
//                        new BukkitRunnable() // kills at max 1 minute after match ends or player dies and leaves
//                        {
//                            @Override
//                            public void run()
//                            {
//                                if (player == null)
//                                {
//                                    this.cancel();
//                                }
//
//                                if (player.getWorld() != world)
//                                {
//                                    Hauntable.remove(player);
//                                    this.cancel();
//                                }
//                            }
//                        }.runTaskTimer(Objects.requireNonNull(Bukkit.getServer().getPluginManager().getPlugin("Invictools")), 0L, 120L);
//                    }
//                }
//            }
//            else if (args.length >= 1 && args[0].equalsIgnoreCase("InstantHaunt"))
//            {
//                Player player = Bukkit.getPlayer(args[1]);
//                player.sendMessage(ChatColor.AQUA + "You can now haunt living players.");
//                new Haunt(player, args[2]);
//            }
//            else if (args.length == 1 && args[0].equalsIgnoreCase("luckyblocks"))
//            {
//                new scenarioCommands().luckyblockEnable((Player) sender, "normal");
//            }
//            else if (args.length > 1 && args[0].equalsIgnoreCase("repeatedItem"))
//            {
//                if (args[5].equalsIgnoreCase("all"))
//                {
//                    for (Player player : Bukkit.getOnlinePlayers())
//                    {
//                        int delay = Integer.parseInt(args[3]);
//                        String type = args[4];
//                        if (args[4].equalsIgnoreCase("specificlb"))
//                        {
//                            ItemStack item = new createLuckyBlocks().getByName(args[1]);
//                            item.setAmount(Integer.parseInt(args[2]));
//                            type = "normal";
//
//                            new giveItemRepeated(delay, item, player, type);
//                            player.sendMessage((ChatColor.AQUA + "You will receive luckyblocks,  " + ChatColor.YELLOW + item.getAmount() + " " + args[1] + ChatColor.AQUA + " every " + ChatColor.YELLOW + delay + ChatColor.AQUA + " seconds."));
//
//                        }
//                        else if (args[4].equalsIgnoreCase("specificItem"))
//                        {
//                            ItemStack item = new createItems().getByName(args[1]);
//                            item.setAmount(Integer.parseInt(args[2]));
//                            type = "normal";
//
//                            new giveItemRepeated(delay, item, player, type);
//                            player.sendMessage((ChatColor.AQUA + "You will receive " + ChatColor.YELLOW + item.getAmount() + " " + args[1] + ChatColor.AQUA + " every " + ChatColor.YELLOW + delay + ChatColor.AQUA + " seconds."));
//                        }
//                        else
//                        {
//                            ItemStack item = new ItemStack(Objects.requireNonNull(Material.getMaterial(args[1])));
//                            item.setAmount(Integer.parseInt(args[2]));
//
//                            new giveItemRepeated(delay, item, player, type);
//                            player.sendMessage((ChatColor.AQUA + "You will receive " + ChatColor.YELLOW + item.getAmount() + " " + item.getType().toString().toLowerCase(Locale.ROOT) + ChatColor.AQUA + " every " + ChatColor.YELLOW + delay + ChatColor.AQUA + " seconds."));
//                        }
//                    }
//                }
//                else
//                {
//                    int delay = Integer.parseInt(args[3]);
//                    Player player = Bukkit.getPlayer(args[5]);
//                    String type = args[4];
//                    if (args[4].equalsIgnoreCase("specificlb"))
//                    {
//                        ItemStack item = new createLuckyBlocks().getByName(args[1]);
//                        item.setAmount(Integer.parseInt(args[2]));
//                        type = "normal";
//
//                        new giveItemRepeated(delay, item, player, type);
//                        player.sendMessage((ChatColor.AQUA + "You will receive luckyblocks,  " + ChatColor.YELLOW + item.getAmount() + " " + args[1] + ChatColor.AQUA + " every " + ChatColor.YELLOW + delay + ChatColor.AQUA + " seconds."));
//
//                    }
//                    else if (args[4].equalsIgnoreCase("specificItem"))
//                    {
//                        ItemStack item = new createItems().getByName(args[1]);
//                        item.setAmount(Integer.parseInt(args[2]));
//                        type = "normal";
//
//                        new giveItemRepeated(delay, item, player, type);
//                        player.sendMessage((ChatColor.AQUA + "You will receive " + ChatColor.YELLOW + item.getAmount() + " " + args[1] + ChatColor.AQUA + " every " + ChatColor.YELLOW + delay + ChatColor.AQUA + " seconds."));
//                    }
//                    else
//                    {
//                        ItemStack item = new ItemStack(Objects.requireNonNull(Material.getMaterial(args[1])));
//                        item.setAmount(Integer.parseInt(args[2]));
//
//                        new giveItemRepeated(delay, item, player, type);
//                        player.sendMessage((ChatColor.AQUA + "You will receive " + ChatColor.YELLOW + item.getAmount() + " " + item.getType().toString().toLowerCase(Locale.ROOT) + ChatColor.AQUA + " every " + ChatColor.YELLOW + delay + ChatColor.AQUA + " seconds."));
//                    }
//                }
//            }
            else if (args.length > 1 && args[0].equalsIgnoreCase("KillEffects")) // it killeffects POTTYPE POTLEVEL DURATION PLAYERNAME
            {
                if (args[4].equals("all"))
                {
                    for (Player player : Bukkit.getOnlinePlayers())
                    {
                        killEffects.put(player, args[1] + "-" + args[2] + "-" + args[3]); // pot type, levles, duartion
                        World world = player.getWorld();
                        new BukkitRunnable()
                        {
                            @Override
                            public void run()
                            {
                                if (player.getWorld() != world)
                                {
                                    killEffects.remove(player); // pot type, levles, duartion
                                    this.cancel();
                                }
                            }
                        }.runTaskTimer(Objects.requireNonNull(Bukkit.getServer().getPluginManager().getPlugin("Invictools")), 0L, 120L);
                    }
                }
                else
                {
                    killEffects.put(Bukkit.getPlayer(args[4]), args[1] + "-" + args[2] + "-" + args[3]); // pot type, levles, duartion
                    World world = Bukkit.getPlayer(args[4]).getWorld();
                    new BukkitRunnable() // kills at max 1 minute after match ends or player dies and leaves
                    {
                        @Override
                        public void run()
                        {
                            if (Bukkit.getPlayer(args[4]).getWorld() != world)
                            {
                                killEffects.remove(Bukkit.getPlayer(args[4])); // pot type, levles, duartion
                                this.cancel();
                            }
                        }
                    }.runTaskTimer(Objects.requireNonNull(Bukkit.getServer().getPluginManager().getPlugin("Invictools")), 0L, 120L);
                }
            }
            else if (args.length > 1 && args[0].equalsIgnoreCase("KillItems")) // it killitems itemtype amount normal player
            {
                if (args[4].equals("all"))
                {
                    for (Player player : Bukkit.getOnlinePlayers())
                    {
                        if (args[3].equalsIgnoreCase("normal"))
                        {
                            ItemStack item = new ItemStack(Material.getMaterial(args[1]));
                            item.setAmount(Integer.parseInt(args[2]));
                            killItems.put(player, item);

                            World world = player.getWorld();
                            new BukkitRunnable()
                            {
                                @Override
                                public void run()
                                {
                                    if (player.getWorld() != world)
                                    {
                                        killItems.remove(player);
                                        this.cancel();
                                    }
                                }
                            }.runTaskTimer(Objects.requireNonNull(Bukkit.getServer().getPluginManager().getPlugin("Invictools")), 0L, 120L);
                        }
                        else if (args[3].equalsIgnoreCase("lb"))
                        {
                            ItemStack item = new createLuckyBlocks().getByName(args[1]);
                            item.setAmount(Integer.parseInt(args[2]));
                            killItems.put(player, item);

                            World world = player.getWorld();
                            new BukkitRunnable()
                            {
                                @Override
                                public void run()
                                {
                                    if (player.getWorld() != world)
                                    {
                                        killItems.remove(player);
                                        this.cancel();
                                    }
                                }
                            }.runTaskTimer(Objects.requireNonNull(Bukkit.getServer().getPluginManager().getPlugin("Invictools")), 0L, 120L);
                        }
                        else if (args[3].equalsIgnoreCase("item"))
                        {
                            ItemStack item = new createItems().getByName(args[1]);
                            item.setAmount(Integer.parseInt(args[2]));
                            killItems.put(player, item);

                            World world = player.getWorld();
                            new BukkitRunnable()
                            {
                                @Override
                                public void run()
                                {
                                    if (player.getWorld() != world)
                                    {
                                        killItems.remove(player);
                                        this.cancel();
                                    }
                                }
                            }.runTaskTimer(Objects.requireNonNull(Bukkit.getServer().getPluginManager().getPlugin("Invictools")), 0L, 120L);
                        }
                    }
                }
                else
                {
                    if (args[3].equalsIgnoreCase("normal"))
                    {
                        ItemStack item = new ItemStack(Material.getMaterial(args[1]));
                        item.setAmount(Integer.parseInt(args[2]));
                        killItems.put(Bukkit.getPlayer(args[4]), item);
                        //  System.out.println((Bukkit.getPlayer(args[4]);
                        //   System.out.println(Bukkit.getPlayer(String.valueOf(item)));

                        World world = Bukkit.getPlayer(args[4]).getWorld();
                        sender.sendMessage(ChatColor.AQUA + Bukkit.getPlayer(args[4]).getName() + item.getType() + " after every kill");
                        new BukkitRunnable()
                        {
                            @Override
                            public void run()
                            {
                                if (Bukkit.getPlayer(args[4]).getWorld() != world)
                                {
                                    killItems.remove(Bukkit.getPlayer(args[4]));
                                    this.cancel();
                                }
                            }
                        }.runTaskTimer(Objects.requireNonNull(Bukkit.getServer().getPluginManager().getPlugin("Invictools")), 0L, 120L);
                    }
                    else if (args[3].equalsIgnoreCase("lb"))
                    {
                        ItemStack item = new createLuckyBlocks().getByName(args[1]);
                        item.setAmount(Integer.parseInt(args[2]));
                        killItems.put(Bukkit.getPlayer(args[4]), item);

                        World world = Bukkit.getPlayer(args[4]).getWorld();
                        new BukkitRunnable()
                        {
                            @Override
                            public void run()
                            {
                                if (Bukkit.getPlayer(args[4]).getWorld() != world)
                                {
                                    killItems.remove(Bukkit.getPlayer(args[4]));
                                    this.cancel();
                                }
                            }
                        }.runTaskTimer(Objects.requireNonNull(Bukkit.getServer().getPluginManager().getPlugin("Invictools")), 0L, 120L);
                    }
                    else if (args[3].equalsIgnoreCase("item"))
                    {
                        ItemStack item = new createItems().getByName(args[1]);
                        item.setAmount(Integer.parseInt(args[2]));
                        killItems.put(Bukkit.getPlayer(args[4]), item);

                        World world = Bukkit.getPlayer(args[4]).getWorld();
                        new BukkitRunnable()
                        {
                            @Override
                            public void run()
                            {
                                if (Bukkit.getPlayer(args[4]).getWorld() != world)
                                {
                                    killItems.remove(Bukkit.getPlayer(args[4]));
                                    this.cancel();
                                }
                            }
                        }.runTaskTimer(Objects.requireNonNull(Bukkit.getServer().getPluginManager().getPlugin("Invictools")), 0L, 120L);
                    }
                }
            }
            else if (args.length > 1 && args[0].equalsIgnoreCase("DeathItems")) // it deathitems itemtype amount normal player
            {
                if (args[4].equals("all"))
                {
                    for (Player player : Bukkit.getOnlinePlayers())
                    {
                        if (args[3].equalsIgnoreCase("normal"))
                        {
                            ItemStack item = new ItemStack(Material.getMaterial(args[1]));
                            item.setAmount(Integer.parseInt(args[2]));
                            deathItems.put(player, item);

                            World world = player.getWorld();
                            new BukkitRunnable()
                            {
                                @Override
                                public void run()
                                {
                                    if (player.getWorld() != world)
                                    {
                                        deathItems.remove(player);
                                        this.cancel();
                                    }
                                }
                            }.runTaskTimer(Objects.requireNonNull(Bukkit.getServer().getPluginManager().getPlugin("Invictools")), 0L, 120L);
                        }
                        else if (args[3].equalsIgnoreCase("lb"))
                        {
                            ItemStack item = new createLuckyBlocks().getByName(args[1]);
                            item.setAmount(Integer.parseInt(args[2]));
                            deathItems.put(player, item);

                            World world = player.getWorld();
                            new BukkitRunnable()
                            {
                                @Override
                                public void run()
                                {
                                    if (player.getWorld() != world)
                                    {
                                        deathItems.remove(player);
                                        this.cancel();
                                    }
                                }
                            }.runTaskTimer(Objects.requireNonNull(Bukkit.getServer().getPluginManager().getPlugin("Invictools")), 0L, 120L);
                        }
                        else if (args[3].equalsIgnoreCase("item"))
                        {
                            ItemStack item = new createItems().getByName(args[1]);
                            item.setAmount(Integer.parseInt(args[2]));
                            deathItems.put(player, item);

                            World world = player.getWorld();
                            new BukkitRunnable()
                            {
                                @Override
                                public void run()
                                {
                                    if (player.getWorld() != world)
                                    {
                                        deathItems.remove(player);
                                        this.cancel();
                                    }
                                }
                            }.runTaskTimer(Objects.requireNonNull(Bukkit.getServer().getPluginManager().getPlugin("Invictools")), 0L, 120L);
                        }
                    }
                }
                else
                {
                    if (args[3].equalsIgnoreCase("normal"))
                    {
                        ItemStack item = new ItemStack(Material.getMaterial(args[1]));
                        item.setAmount(Integer.parseInt(args[2]));
                        deathItems.put(Bukkit.getPlayer(args[4]), item);
                        //  System.out.println((Bukkit.getPlayer(args[4]);
                        //   System.out.println(Bukkit.getPlayer(String.valueOf(item)));

                        World world = Bukkit.getPlayer(args[4]).getWorld();
                        sender.sendMessage(ChatColor.AQUA + Bukkit.getPlayer(args[4]).getName() + item.getType() + " after every kill");
                        new BukkitRunnable()
                        {
                            @Override
                            public void run()
                            {
                                if (Bukkit.getPlayer(args[4]).getWorld() != world)
                                {
                                    deathItems.remove(Bukkit.getPlayer(args[4]));
                                    this.cancel();
                                }
                            }
                        }.runTaskTimer(Objects.requireNonNull(Bukkit.getServer().getPluginManager().getPlugin("Invictools")), 0L, 120L);
                    }
                    else if (args[3].equalsIgnoreCase("lb"))
                    {
                        ItemStack item = new createLuckyBlocks().getByName(args[1]);
                        item.setAmount(Integer.parseInt(args[2]));
                        deathItems.put(Bukkit.getPlayer(args[4]), item);

                        World world = Bukkit.getPlayer(args[4]).getWorld();
                        new BukkitRunnable()
                        {
                            @Override
                            public void run()
                            {
                                if (Bukkit.getPlayer(args[4]).getWorld() != world)
                                {
                                    deathItems.remove(Bukkit.getPlayer(args[4]));
                                    this.cancel();
                                }
                            }
                        }.runTaskTimer(Objects.requireNonNull(Bukkit.getServer().getPluginManager().getPlugin("Invictools")), 0L, 120L);
                    }
                    else if (args[3].equalsIgnoreCase("item"))
                    {
                        ItemStack item = new createItems().getByName(args[1]);
                        item.setAmount(Integer.parseInt(args[2]));
                        deathItems.put(Bukkit.getPlayer(args[4]), item);

                        World world = Bukkit.getPlayer(args[4]).getWorld();
                        new BukkitRunnable()
                        {
                            @Override
                            public void run()
                            {
                                if (Bukkit.getPlayer(args[4]) != null)
                                {
                                    if (Bukkit.getPlayer(args[4]).getWorld() != world)
                                    {
                                        deathItems.remove(Bukkit.getPlayer(args[4]));
                                        this.cancel();
                                    }
                                }
                                else
                                {
                                    deathItems.remove(Bukkit.getPlayer(args[4]));
                                    this.cancel();
                                }
                            }
                        }.runTaskTimer(Objects.requireNonNull(Bukkit.getServer().getPluginManager().getPlugin("Invictools")), 0L, 120L);
                    }
                }
            }
            else if (args.length > 1 && args[0].equalsIgnoreCase("teamworkeffect"))
            {
                Player player = (Player) sender;
                World world = player.getWorld();

                String e1 = args[1];
                String e2 = args[3];
                int a1 = Integer.parseInt(args[2]);
                int a2 = Integer.parseInt(args[4]);

                boolean check = false;
                for (PotionEffectType p : PotionEffectType.values())
                {
                    if (e1.equalsIgnoreCase(p.getName()))
                    {
                        check = true;
                    }
                }
                if (!check)
                {
                    e1 = "LUCK";
                }
                check = false;

                for (PotionEffectType p : PotionEffectType.values())
                {
                    if (e2.equalsIgnoreCase(p.getName()))
                    {
                        check = true;
                    }
                }
                if (!check)
                {
                    e2 = "LUCK";
                }

                Game game = BedwarsAPI.getInstance().getGameOfPlayer((Player) sender);
                new TeamworkEffect().createEffect(e1, e2, game, a1, a2);
                sender.sendMessage(ChatColor.AQUA + "Teammate Based Effect Enabled");
                if (!check)
                {
                    sender.sendMessage(ChatColor.RED + "incorrectly spelled effect has been replaced.");
                }

                new BukkitRunnable()
                {
                    @Override
                    public void run()
                    {
                        if (world != player.getWorld())
                        {
                            TeamworkEffect.players1.clear();
                            TeamworkEffect.players2.clear();
                            TeamworkEffect.gameongoing = false;
                            // System.out.println("canceled");
                            this.cancel();
                        }
                    }
                }.runTaskTimer(Objects.requireNonNull(Bukkit.getServer().getPluginManager().getPlugin("Invictools")), 0L, 120L);
            }
            else if (args.length > 1 && args[0].equalsIgnoreCase("repeatedcancel"))
            {
                giveItemRepeated.repeatedCancel = Boolean.parseBoolean(args[1]);
                sender.sendMessage(ChatColor.AQUA + "GiveItem global cancel: " + ChatColor.YELLOW + giveItemRepeated.repeatedCancel);
            }
            else if (args.length > 1 && args[0].equalsIgnoreCase("master"))
            {
                Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("Invictools");
                FileConfiguration config = plugin.getConfig();
                MasterPlayer = Bukkit.getPlayer(args[1]);
                config.set("masterplayer", args[1]);
                plugin.saveConfig();
                sender.sendMessage(ChatColor.AQUA + "Master player is now " + ChatColor.YELLOW + args[1]);
            }
            else if (args.length == 1 && args[0].equalsIgnoreCase("master"))
            {
                Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("Invictools");
                FileConfiguration config = plugin.getConfig();
                MasterPlayer = Bukkit.getPlayer(config.getString("masterplayer"));
                sender.sendMessage(ChatColor.AQUA + "Master player is currently " + ChatColor.YELLOW + config.getString("masterplayer"));
            }
            else if (args.length == 1 && args[0].equalsIgnoreCase("alwaysbridge"))
            {
                Player player = (Player) sender;
                sender.sendMessage(ChatColor.AQUA + "Always bridging active in world " + ChatColor.YELLOW + player.getWorld().getName());
                for (Player p : Bukkit.getOnlinePlayers())
                {
                    if (player.getWorld().equals(p.getWorld()))
                    {
                        new AlwaysBridge(p);
                    }
                }
            }
            else if (args.length >= 1 && args[0].equalsIgnoreCase("alwaysbridgesingle"))
            {
                Player player = (Player) sender;
                player.sendMessage(ChatColor.AQUA + "Scaffold now active while you have wool! Hold crouch to fall.");
                new AlwaysBridge(player);
            }
            else if (args.length >= 1 && args[0].equalsIgnoreCase("closeTeammateEffect"))
            {
                Player player = (Player) sender;
                sender.sendMessage(ChatColor.AQUA + "giving effects when you're near your teammate");
                for (Player p : Bukkit.getOnlinePlayers())
                {
                    if (player.getWorld().equals(p.getWorld()))
                    {
                        new CloseEffectSpecific(p, p.getWorld().getName(), args[1], Integer.parseInt(args[2]));
                    }
                }
            }
            else if (args.length >= 1 && args[0].equalsIgnoreCase("closeTeammateEffectSingle"))
            {
                Player player = (Player) sender;
                sender.sendMessage(ChatColor.AQUA + "giving effects when you're near your teammate");
                new CloseEffectSpecific(getPlayer(args[3]), player.getWorld().getName(), args[1], Integer.parseInt(args[2]));
            }
            else if (args.length > 1 && args[0].equalsIgnoreCase("manhunt"))
            {
                if (args[2].equalsIgnoreCase("random"))
                {
                    Player player = Bukkit.getOnlinePlayers().stream().skip((int) (Bukkit.getOnlinePlayers().size() * Math.random())).findFirst().orElse(null);
                    new ManhuntMain(args[1], player);
                }
                else
                {
                    new ManhuntMain(args[1], Bukkit.getPlayer(args[2]));
                }
            }
            else if (args.length >= 1 && args[0].equalsIgnoreCase("jumping"))
            {
                if (args.length == 1)
                {
                    sender.sendMessage(ChatColor.AQUA + "Fireball and TnT jumping: ");
                    sender.sendMessage(ChatColor.AQUA + "range: " + ChatColor.YELLOW + ExplosionsListener.range);
                    sender.sendMessage(ChatColor.AQUA + "x: " + ChatColor.YELLOW + ExplosionsListener.xzmultiplier);
                    sender.sendMessage(ChatColor.AQUA + "y: " + ChatColor.YELLOW + ExplosionsListener.ymultiplier);
                    sender.sendMessage(ChatColor.AQUA + "op: " + ChatColor.YELLOW + ExplosionsListener.op);
                }

                if (args[1].equalsIgnoreCase("range"))
                {
                    ExplosionsListener.range = Double.parseDouble(args[2]);
                }
                else if (args[1].equalsIgnoreCase("x"))
                {
                    ExplosionsListener.xzmultiplier = Double.parseDouble(args[2]);
                }
                else if (args[1].equalsIgnoreCase("y"))
                {
                    ExplosionsListener.ymultiplier = Double.parseDouble(args[2]);
                }
                else if (args[1].equalsIgnoreCase("op"))
                {
                    ExplosionsListener.op = Boolean.parseBoolean(args[2]);
                }

                sender.sendMessage(ChatColor.YELLOW + args[1] + ChatColor.AQUA + " set to " + ChatColor.YELLOW + args[2]);
            }
            else if (args.length == 4 && args[0].equalsIgnoreCase("giveitem"))
            {
                Player player = (Player) sender; // who, specific, type, player
                giveitem.giveItem(args[1], args[2], args[3], player);
            }
            else if (args.length == 5 && args[0].equalsIgnoreCase("giveitem"))
            {
                giveitem.giveItem(args[1], args[2], args[3], getPlayer(args[4]));
            }
            else if (args.length >= 1 && args[0].equalsIgnoreCase("destroyaddedblocks"))
            {
                for (Block block : coalBlocks)
                {
                    block.setType(Material.AIR);
                }
                coalBlocks.clear();

                for (Block block : addedNormalBlocks)
                {
                    block.setType(Material.AIR);
                }
                addedNormalBlocks.clear();

                sender.sendMessage(ChatColor.AQUA + "All added blocks have been destroyed");
            }
            else if (args.length == 1 && args[0].equalsIgnoreCase("DamageTeammates"))
            {
                Player player = (Player) sender;
                new DamageTeammates(7.5, 15, player.getWorld().getName(), 2);
            }
            else if (args.length > 1 && args[0].equalsIgnoreCase("DamageTeammates"))
            {
                Player player = (Player) sender;
                double distance = Double.parseDouble(args[1]);
                double intialbox = Double.parseDouble(args[2]);
                int potlevel = Integer.parseInt(args[3]);
                new DamageTeammates(distance, intialbox, player.getWorld().getName(), potlevel);
            }
            else if (args.length == 2 && args[0].equalsIgnoreCase("setarena"))
            {
                Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("Invictools");
                final FileConfiguration Config = plugin.getConfig();
                String arena = args[1];
                if (arena != null)
                {
                    Config.set("loadedgame", arena);
                    plugin.saveConfig();
                    sender.sendMessage(ChatColor.AQUA + "Arena " + ChatColor.YELLOW + arena + ChatColor.AQUA + " has been loaded into the npc");
                }
                else
                {
                    sender.sendMessage(ChatColor.RED + "Arena name is not valid");
                }
            }
            else if (args.length == 1 && args[0].equalsIgnoreCase("setarena"))
            {
                Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("Invictools");
                final FileConfiguration Config = plugin.getConfig();

                Config.set("loadedgame", "none");
                plugin.saveConfig();
            }
            else if (args.length > 1 && args[0].equalsIgnoreCase("preventDrop"))
            {
                String type = args[1];
                String item = args[2];
                Player p = Bukkit.getPlayer(args[3]);
                if (type.equalsIgnoreCase("normal"))
                {
                    disableDrops.forbidDrop.put(p, item);
                }
                else if (type.equalsIgnoreCase("item"))
                {
                    ItemStack i = new createItems().getByName(item);
                    item = i.getItemMeta().getDisplayName();
                    disableDrops.forbidDrop.put(p, item);
                }
                sender.sendMessage(ChatColor.AQUA + "Forbidden Drop added: " + ChatColor.YELLOW + item + ChatColor.AQUA + " for " + ChatColor.YELLOW + args[3]);
            }
            else if (args.length == 1 && args[0].equalsIgnoreCase("preventDrop"))
            {
                disableDrops.forbidDrop.clear();
                sender.sendMessage(ChatColor.AQUA + "Forbidden Drops cleared");
            }
            else if (args.length > 0 && args[0].equalsIgnoreCase("SetHealth")) // sethealth all/one/random/decrease/reset, value, player/interval
            {
                if (args[1].equalsIgnoreCase("all"))
                {
                    new CustomHealth("all", (Player) sender, Integer.parseInt(args[2]), 1, ((Player) sender).getWorld().getName());
                    sender.sendMessage(ChatColor.AQUA + "Maximum Health is now set to " + ChatColor.YELLOW + Integer.parseInt(args[2]) + ChatColor.AQUA + " for " + ChatColor.YELLOW + args[1]);
                }
                else if (args[1].equalsIgnoreCase("one"))
                {
                    Player p = getPlayer(args[3]);
                    new CustomHealth("one", p, Integer.parseInt(args[2]), 1, ((Player) sender).getWorld().getName());
                    sender.sendMessage(ChatColor.AQUA + "Maximum Health is now set to " + ChatColor.YELLOW + Integer.parseInt(args[2]) + ChatColor.AQUA + " for " + ChatColor.YELLOW + args[3]);
                }
                else if (args[1].equalsIgnoreCase("reset"))
                {
                    new CustomHealth("reset", (Player) sender, 20, 1, ((Player) sender).getWorld().getName());
                    sender.sendMessage(ChatColor.AQUA + "Maximum Health has been reset to " + ChatColor.YELLOW + "20");
                }
                else if (args[1].equalsIgnoreCase("decrease"))
                {
                    new CustomHealth("decrease", (Player) sender, Integer.parseInt(args[2]), Integer.parseInt(args[3]), ((Player) sender).getWorld().getName());
                    sender.sendMessage(ChatColor.AQUA + "Maximum Health will start at " + ChatColor.YELLOW + Integer.parseInt(args[2]) + ChatColor.AQUA + " and decrease every " + ChatColor.YELLOW + Integer.parseInt(args[3]) + ChatColor.AQUA + " seconds");
                }
                else if (args[1].equalsIgnoreCase("randomall"))
                {
                    new CustomHealth("randomall", (Player) sender, Integer.parseInt(args[2]), Integer.parseInt(args[3]), ((Player) sender).getWorld().getName());
                    sender.sendMessage(ChatColor.AQUA + "Maximum Health will randomize with a max of " + ChatColor.YELLOW + Integer.parseInt(args[2]) + ChatColor.AQUA + " every " + ChatColor.YELLOW + Integer.parseInt(args[3]) + ChatColor.AQUA + " seconds");
                }
            }
            else if (args.length >= 2 && args[0].equalsIgnoreCase("spawner"))
            {
                String config = args[1];
                String midorisland = args[2];
                Player player = (Player) sender;

                Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("Invictools");
                //  File pFile = new File(plugin.getDataFolder().getPath() + "\\Spawner" + File.separator + config + ".yml");
                File Folder = new File(plugin.getDataFolder(), "Spawner");
                File pFile = new File(Folder, config + ".yml");

                final FileConfiguration spawner = YamlConfiguration.loadConfiguration(pFile);
                String test = spawner.getString("items.0.type");

                if (test != null)
                {
                    new ItemSpawner(config, player.getLocation().getWorld().getName(), player, midorisland);
                    sender.sendMessage(ChatColor.AQUA + "Custom spawner active in world " + ChatColor.YELLOW + player.getLocation().getWorld().getName() + ChatColor.AQUA + " using config " + ChatColor.YELLOW + config);
                }
                else
                {
                    sender.sendMessage(ChatColor.RED + "Requires a valid config.yml and valid spawner location in this world");
                }
            }
            else if (args.length == 1 && args[0].equalsIgnoreCase("spawner"))
            {
                sender.sendMessage(ChatColor.RED + "Requires a valid config.yml");
            }
            else if (args.length == 2 && args[0].equalsIgnoreCase("randomeffect"))
            {
                intensityLimit = Integer.parseInt(args[1]);

                for (Player player : Bukkit.getOnlinePlayers())
                {
                    worldName = player.getLocation().getWorld().getName();
                    new randomEffect(intensityLimit, worldName, player, effecttime);
                }
                sender.sendMessage(ChatColor.AQUA + "Applying random effect with intensity limit " + ChatColor.YELLOW + intensityLimit + ChatColor.AQUA + " in world " + ChatColor.YELLOW + worldName);
            }
            else if (args.length == 1 && args[0].equalsIgnoreCase("randomeffect"))
            {
                for (Player player : Bukkit.getOnlinePlayers())
                {
                    worldName = player.getLocation().getWorld().getName();
                    new randomEffect(intensityLimit, worldName, player, effecttime);
                }
                sender.sendMessage(ChatColor.AQUA + "Applying random effect with intensity limit " + ChatColor.YELLOW + intensityLimit + ChatColor.AQUA + " in world " + ChatColor.YELLOW + worldName);
            }
            else if (args.length == 3 && args[0].equalsIgnoreCase("randomeffect"))
            {
                intensityLimit = Integer.parseInt(args[1]);
                effecttime = Integer.parseInt(args[2]);

                for (Player player : Bukkit.getOnlinePlayers())
                {
                    worldName = player.getLocation().getWorld().getName();
                    new randomEffect(intensityLimit, worldName, player, effecttime);
                }
                sender.sendMessage(ChatColor.AQUA + "Applying random effect with intensity limit " + ChatColor.YELLOW + intensityLimit + ChatColor.AQUA + " in world " + ChatColor.YELLOW + worldName);
            }
            else if (args.length >= 3 && args[0].equalsIgnoreCase("randomeffectsingle"))
            {
                intensityLimit = Integer.parseInt(args[1]);
                effecttime = Integer.parseInt(args[2]);
                Player player = Bukkit.getPlayer(args[3]);
                worldName = player.getLocation().getWorld().getName();

                new randomEffect(intensityLimit, worldName, player, effecttime);
                sender.sendMessage(ChatColor.AQUA + "You will now receive random potion effects every " + ChatColor.YELLOW + effecttime);
            }
            else if (args.length >= 1 && args[0].equalsIgnoreCase("creative"))
            {
                Player player = (Player) sender;
                worldName = player.getLocation().getWorld().getName();

                name = args[3];
                int intervalBetween = Integer.parseInt(args[1]);
                int timeIn = Integer.parseInt(args[2]);
                new creative(intervalBetween, timeIn, name);
                sender.sendMessage(ChatColor.AQUA + "Entering creative mode for " + ChatColor.YELLOW + timeIn + ChatColor.AQUA + " seconds every " + ChatColor.YELLOW + intervalBetween + ChatColor.AQUA + " seconds in world " + ChatColor.YELLOW + worldName + ChatColor.AQUA + " for player " + ChatColor.YELLOW + name);
            }
            else if (args.length == 3 && args[0].equalsIgnoreCase("tnt"))
            {
                Player p = (Player) sender;
                name = sender.getName();
                worldName = p.getLocation().getWorld().getName();
                if (blacklistedWorlds.contains(worldName))
                {
                    sender.sendMessage(ChatColor.RED + "Cannot be activated in world " + ChatColor.YELLOW + worldName);
                }
                else
                {
                    interval = Integer.parseInt(args[1]);
                    fuse = Integer.parseInt(args[2]);
                    for (Player player : Bukkit.getOnlinePlayers())
                    {
                        new tnt(interval, worldName, fuse, player);
                    }
                    sender.sendMessage(ChatColor.AQUA + "Spawning tnt on players at an interval of " + ChatColor.YELLOW + interval + ChatColor.AQUA + " in world " + ChatColor.YELLOW + worldName);
                }
            }
            else if (args.length >= 4 && args[0].equalsIgnoreCase("tntsingle"))
            {
                Player p = (Player) sender;
                name = sender.getName();
                worldName = p.getLocation().getWorld().getName();
                if (blacklistedWorlds.contains(worldName))
                {
                    sender.sendMessage(ChatColor.RED + "Cannot be activated in world " + ChatColor.YELLOW + worldName);
                }
                else
                {
                    interval = Integer.parseInt(args[1]);
                    fuse = Integer.parseInt(args[2]);
                    new tnt(interval, worldName, fuse, Bukkit.getPlayer(args[3]));
                    sender.sendMessage(ChatColor.AQUA + "TnT will now spawn on top of you every " + ChatColor.YELLOW + interval + ChatColor.AQUA + " seconds ");
                }
            }
            else if (args.length > 0 && args[0].equalsIgnoreCase("reload"))
            {
                if (args.length > 1 && args[1].equalsIgnoreCase("invictools"))
                {
                    //   assert Invictools != null;
                    Invictools.reloadConfig();
                    sender.sendMessage(ChatColor.AQUA + "Invictools Config Reloaded");
                    //  Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "bweffects reload"); // executes below disable and enable from other plugin
                }
                else if (args.length > 1 && args[1].equalsIgnoreCase("bweffects"))
                {
                    assert BWeffects != null;
                    getServer().getPluginManager().disablePlugin(BWeffects);
                    getServer().getPluginManager().enablePlugin(BWeffects);
                    sender.sendMessage(ChatColor.AQUA + "BWeffects Reloaded");
                }
            }
            else
            {
                sender.sendMessage(" ");
                sender.sendMessage(ChatColor.AQUA + "Invic's tools. Activate modifiers in game world");
                /*
                sender.sendMessage(ChatColor.AQUA + "Creative: " + ChatColor.YELLOW + "/invictools creative [seconds in creative] [seconds before start] [player]");
                sender.sendMessage(ChatColor.AQUA + "Random effects: " + ChatColor.YELLOW + "/invictools randomeffect [intensity randomizer limit]");
                sender.sendMessage(ChatColor.AQUA + "Manually spawn lucky block: " + ChatColor.YELLOW + "/invictools givelb <type>");
                sender.sendMessage(ChatColor.AQUA + "Item Spawner: " + ChatColor.YELLOW + "/invictools spawner [config] [spawn location]");
                sender.sendMessage(ChatColor.AQUA + "TnT spawner: " + ChatColor.YELLOW + "/invictools tnt [seconds interval] [seconds fuse time]");
                sender.sendMessage(ChatColor.AQUA + "Health Modifications: " + ChatColor.YELLOW + "/invictools SetHealth [all,one,randomAll,decrease] <amount/max amount> <playername/interval>");
                sender.sendMessage(ChatColor.AQUA + "Set active arena for the NPC: " + ChatColor.YELLOW + "/invictools SetArena <arena/none>");
                sender.sendMessage(ChatColor.AQUA + "Set Bed Break effect: " + ChatColor.YELLOW + "/invictools BedBreak <effect> [player]");
                sender.sendMessage(ChatColor.AQUA + "Reload Invic plugins: " + ChatColor.YELLOW + "/invictools reload <bweffects/invictools>");
                sender.sendMessage(ChatColor.AQUA + "Reload or restart server safely: " + ChatColor.YELLOW + "/invictools sreload / srestart");
                sender.sendMessage(ChatColor.AQUA + "Set player who world specific features should trigger on: " + ChatColor.YELLOW + "/invictools master <playername>");
                sender.sendMessage(ChatColor.AQUA + "Constant or procedural effects: " + ChatColor.YELLOW + "/bweffects help");

                 */
                sender.sendMessage(ChatColor.AQUA + "refer to google docs for updated list. most commands tab complete");
            }

        return true;
    }

    public OldCommands(List<String> worlds, double sety, List<String> blacklist, List<String> games)
    {
        worldstemp = worlds;
        ytemp = sety;
        blacklistedWorlds = blacklist;
        gamestemp = games;
    }

    public static void debug(CommandSender sender)
    {
        sender.sendMessage(" ");
        sender.sendMessage(ChatColor.WHITE + "Status:");
        sender.sendMessage(ChatColor.YELLOW + "- Master Player: " + ChatColor.WHITE + MasterPlayer.getName());
        sender.sendMessage(ChatColor.YELLOW + "- Lucky Blocks Enabled: " + ChatColor.WHITE + LuckyBlocksEnabled);
        sender.sendMessage(ChatColor.YELLOW + "- FireStick Enabled: " + ChatColor.WHITE + FireStickEnabled);
        sender.sendMessage(ChatColor.YELLOW + "- Teammates Assignment: ");
        sender.sendMessage(ChatColor.YELLOW + "- Lobby: ");
        LobbyLogic.lobbyRegion.forEach((attribute, value) ->
                sender.sendMessage(ChatColor.WHITE + attribute + " " + value));
        sender.sendMessage(ChatColor.YELLOW + "- Attributes: ");
        AbtributesOnDeath.ActiveAttributes.forEach((attribute, value) ->
                sender.sendMessage(ChatColor.WHITE + attribute + " at value " + value));
        sender.sendMessage(ChatColor.YELLOW + "- ProximityElytra: ");
        ProximityElytra.forEach((p, bool) ->
                sender.sendMessage(ChatColor.WHITE + p.getName() + " has active elytra: " + bool));
        teammates.forEach((p, team) ->
                sender.sendMessage(ChatColor.WHITE + p.getName() + " and " + team.getName()));
        sender.sendMessage(ChatColor.YELLOW + "- killEffects: ");
        killEffects.forEach((p, effect) ->
                sender.sendMessage(ChatColor.WHITE + p.getName() + " has effect " + effect));
        sender.sendMessage(ChatColor.YELLOW + "- NoShop: ");
        noShop.forEach((p, effect) ->
                sender.sendMessage(ChatColor.WHITE + p.getName() + " has noshop " + effect));
        sender.sendMessage(ChatColor.YELLOW + "- killItems: ");
        killItems.forEach((p, effect) ->
                sender.sendMessage(ChatColor.WHITE + p.getName() + " has item " + effect));
        sender.sendMessage(ChatColor.YELLOW + "- deathitems: ");
        deathItems.forEach((p, effect) ->
                sender.sendMessage(ChatColor.WHITE + p.getName() + " has item " + effect));
        sender.sendMessage(ChatColor.YELLOW + "- Totems: ");
        InfiniteTotems.forEach((p, value) ->
                sender.sendMessage(ChatColor.WHITE + p.getName() + " has totems: " + value));
        sender.sendMessage(ChatColor.YELLOW + "- PreventDrops: ");
        disableDrops.forbidDrop.forEach((p, value) ->
                sender.sendMessage(ChatColor.WHITE + p.getName() + " cannot drop: " + value));
        sender.sendMessage(ChatColor.YELLOW + "- Hauntable: ");
        Hauntable.forEach((p, value) ->
                sender.sendMessage(ChatColor.WHITE + p.getName() + " will haunt: " + value));
        sender.sendMessage(ChatColor.YELLOW + "- Deaths: ");
        DeathCounter.deaths.forEach((p, value) ->
                sender.sendMessage(ChatColor.WHITE + p.getName() + " has this many deaths: " + value));
        sender.sendMessage(ChatColor.YELLOW + "- Manhunt: ");
        sender.sendMessage(ChatColor.YELLOW + "Teams: ");
        ManhuntMain.ManhuntTeam.forEach((p, team) ->
                sender.sendMessage(ChatColor.WHITE + p.getName() + " is on team " + team));
        sender.sendMessage(ChatColor.YELLOW + "Hunter items: ");
        ManhuntMain.HunterItems.forEach((item) ->
                sender.sendMessage(ChatColor.WHITE + " hunter items: " + item));
        sender.sendMessage(ChatColor.YELLOW + "Hunter effects: ");
        ManhuntMain.HunterEffects.forEach((item) ->
                sender.sendMessage(ChatColor.WHITE + " hunter effects: " + item));
        sender.sendMessage(ChatColor.YELLOW + "Hunted items: ");
        ManhuntMain.HuntedItems.forEach((item) ->
                sender.sendMessage(ChatColor.WHITE + " hunted items: " + item));
        sender.sendMessage(ChatColor.YELLOW + "Hunted effects: ");
        ManhuntMain.HuntedEffects.forEach((item) ->
                sender.sendMessage(ChatColor.WHITE + " hunted effects: " + item));
        sender.sendMessage(ChatColor.YELLOW + "Active random queue:");
        sender.sendMessage(ChatColor.YELLOW + "Bedwars: " + ChatColor.WHITE + queue.activeBedwarsGame.getName());
        sender.sendMessage(ChatColor.YELLOW + "Bedfight:" + ChatColor.WHITE + queue.activeBedfightGame.getName());
    }
}

package me.invic.invictools;

import me.invic.invictools.commands.commandManagerLib.MainCommand;
import me.invic.invictools.commands.*;
import me.invic.invictools.cosmetics.LobbyListener;
import me.invic.invictools.cosmetics.VictoryDances.VictoryDanceListener;
import me.invic.invictools.cosmetics.bedbreaks.BedBreaks;
import me.invic.invictools.cosmetics.ConfigHandler;
import me.invic.invictools.cosmetics.cage;
import me.invic.invictools.cosmetics.finalkills.FinalKillListener;
import me.invic.invictools.cosmetics.statisticRequirments;
import me.invic.invictools.econ.givePoints;
import me.invic.invictools.gamemodes.bf.bedfight;
import me.invic.invictools.gamemodes.bf.bedfightStatistics;
import me.invic.invictools.gamemodifiers.*;
import me.invic.invictools.util.ingame.LobbyLogic;
import me.invic.invictools.util.ingame.OlympusFires;
import me.invic.invictools.items.ItemListener;
import me.invic.invictools.items.ModBow;
import me.invic.invictools.items.dareListener;
import me.invic.invictools.util.*;
import me.invic.invictools.util.Leaderboards.*;
import me.invic.invictools.gamemodifiers.LuckyBlocks.luckyBlockBreakDetection;
import me.invic.invictools.gamemodifiers.PotionEffects.KillEffectListener;
import me.invic.invictools.gamemodifiers.PotionEffects.TeamworkEffect;
import me.invic.invictools.cosmetics.projtrail.ProjTrailListener;
//import me.invic.invictools.util.npc.DetectClickOnNPC;
//import me.invic.invictools.util.npc.DetectClickOnPlugNPC;
//import me.invic.invictools.util.npc.SpawnNPC;
//import me.invic.invictools.util.npc.SpawnPlugNPC;
import me.invic.invictools.util.fixes.*;
import me.invic.invictools.util.gui.TeamSelection;
import me.invic.invictools.util.gui.gameLobbyItems;
import me.invic.invictools.util.gui.panels;
import me.invic.invictools.util.ingame.SculkFires;
import me.invic.invictools.util.npc.BlazeNpc;
import me.invic.invictools.util.ingame.physics.CancelConcreteChange;
import me.invic.invictools.util.ingame.physics.CancelLampUpdates;
import me.invic.invictools.util.ingame.physics.MasterPlayerJoin;
import me.invic.invictools.util.ingame.physics.RiptideDamage;
import me.invic.invictools.util.gui.scenSelector.perGameScenSelHolder;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;


/*
        new BukkitRunnable()
        {
            @Override
            public void run()
            {

            }
        }.runTaskTimer(Commands.Invictools, 20L, 20L);

        new BukkitRunnable()
        {
            @Override
            public void run()
            {

            }
        }.runTaskLater(Commands.Invictools, 20L);

public class scenarioCommands implements TabExecutor, CommandExecutor
{
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args)
    {
        List<String> tabComplete = new ArrayList<>();
        if (sender instanceof Player)
        {
            if (!sender.hasPermission("invic.invictools"))
            {
                sender.sendMessage(Commands.permissionsError);
                return tabComplete;
            }
        }

        if(args.length == 1)
        {

        }

        return tabComplete;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if (sender instanceof Player)
        {
            if (!sender.hasPermission("invic.invictools"))
            {
                sender.sendMessage(Commands.permissionsError);
                return true;
            }
        }

        if(args.length==1 && args[0].equalsIgnoreCase(""))
        {

        }

        return true;
    }
}



        FileConfiguration config = Commands.Invictools.getConfig();

        1.8 protocal = 47

DEV LOG
2/15/2022
Added Haunt, Instant Haunt, Attempted AlwaysBridge Smoothness, Attempted to add fail safes to stop spawner, dynamic spawner, and give-repeated-item logic
3/22/2022
Added ProximityElytra, PreventElytraClick, SafeOpCommand method, better teammate grabbing methods via API (in GrabTeammates)
3/25/2022
Added Dynamic per arena lobbies, prevented concrete from solidifying, updated logic to disableSpectatorTeleport (slightly scuffed) small changes to various other logics, reformatted twinkle to physics
4/12/2022
Added LootBox item using luckyBlockBreak, LootBoc, and TableToList. Refined itemrain logic to convert materials to bedwars materials in TableToList so more usable elsewhere
5/20/2022
Revamped giveitem to work per manhunt team, bedwars team, specific player, all player. updated safeop, added different effect per teammate, revamped fireballs,
added repeateditem global toggle
5/27/2022
added Olympusfires control, added CloseElytra null check, fixed DisableShop, updated DamageTeammates to new geabteammates, added modbow name switch
started final kill attempts, tried to fix luckyblock teleporting and creative
6/2/2022
made firestick launch real fireball, updated wool detonator, added new final kills, made bedbreak/kill check not use api, added projectile trails
6/8/2022
added final kill tracking, reorganized classes, added more cosmetics.
6/19/2022
Fixed new lobby inventory bug, fixed spectator mode using whether lobby exists, adjusted boss lucky blocks, added DareDevil + dd items
made inventory save when moved around automatically. added leaderboard for every stat. prevented lobby mob despawning. previously added command panels in plugin
7/15/2022
changelog now implmented via github
TODO:
   rewrite every command to be grouped by what they are or just individual if used by normal players a lot. ex /lbpos instead of /it leaderboard position
   do not replace commands just write new ones that utilize the same code so manhunt configuration files and related still work but typing is cleaner

 */
public final class Invictools extends JavaPlugin
{
    List<String> worlds = new ArrayList<>();
    List<String> games = new ArrayList<>();
    List<String> Configs = new ArrayList<>();


    private static Invictools instance;
    private static MainCommand mainCommand;

    public Invictools()
    {
        instance = this;
    }

    private void registerCommandsAndEvents ()
    {
        // Register top level commands here, with the class you made in impl.commandmanagers
//        new ITMainCommand().registerMainCommand(this, "it");
    }

    public static MainCommand getMainCommand ()
    {
        return mainCommand;
    }

    public static Invictools getInstance ()
    {
        return instance;
    }

    public static Economy econ = null;

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }

        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }

    @Override
    public void onEnable()
    {
        if (!setupEconomy() ) {
            Logger.getLogger("Minecraft").severe(String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        registerCommandsAndEvents();

        this.saveDefaultConfig();
        FileConfiguration fileConfiguration = this.getConfig();
        File Folder = new File(OldCommands.Invictools.getDataFolder(), "Maps");
        File[] yamlFiles = Folder.listFiles();
        for (File file : yamlFiles)
        {
            FileConfiguration map = YamlConfiguration.loadConfiguration(file);
            worlds.add(map.getString("World"));
            String[] mapName = file.getName().split("\\.");
            Configs.add(mapName[0] + "_" + map.getString("Conversion"));
            games.add(mapName[0]);
        }
        List<String> blackListedWorlds = fileConfiguration.getStringList("blacklistedWorlds");
        double y = fileConfiguration.getDouble("ylevel");
        boolean UsePlugNPC = fileConfiguration.getBoolean("UsePlugNPC");

        // perms
        Bukkit.getPluginManager().addPermission(new Permission("nospectp.allowteleport"));
        Bukkit.getPluginManager().addPermission(new Permission("invic.invictools"));
        Bukkit.getPluginManager().addPermission(new Permission("invic.firestick"));
        Bukkit.getPluginManager().addPermission(new Permission("invic.all"));

        // proper
        Bukkit.getPluginManager().registerEvents(new disableSpectatorTeleport(), this); // teleport event
        Bukkit.getPluginManager().registerEvents(new BedBreaks(), this); // bed break cosm and lucky block on bedbreaks; block destroy
        Bukkit.getPluginManager().registerEvents(new ExplosionsListener(), this); // explosion event
        Bukkit.getPluginManager().registerEvents(new ItemListener(), this); // right click event
        Bukkit.getPluginManager().registerEvents(new Totem(), this); //on player damage
        Bukkit.getPluginManager().registerEvents(new CancelLampUpdates(), this); // cancels redstone lamps updating properly
        Bukkit.getPluginManager().registerEvents(new PreventElytraClick(), this); // Disables clicking Chestplate slot when ProximityElytra is active
        Bukkit.getPluginManager().registerEvents(new LobbyLogic(), this); // first player joins lobby and when game starts
        Bukkit.getPluginManager().registerEvents(new CancelConcreteChange(), this); // BlockFormEvent
        Bukkit.getPluginManager().registerEvents(new RiptideDamage(), this); // RiptideEvent
        Bukkit.getPluginManager().registerEvents(new TeamworkEffect(), this); // Teamwork specific game end clearing and bedwars death event
        Bukkit.getPluginManager().registerEvents(new ModBow(), this); // Entity Shoot Bow
        Bukkit.getPluginManager().registerEvents(new disableDrops(), this); // Player drop, bedwars game end
        Bukkit.getPluginManager().registerEvents(new OlympusFires(), this); // Bedwars bed break, bedwars start
        Bukkit.getPluginManager().registerEvents(new SculkFires(), this); // Bedwars bed break, bedwars start
        Bukkit.getPluginManager().registerEvents(new FinalKillListener(), this); // Bedwars bed break, bedwars start
        Bukkit.getPluginManager().registerEvents(new InvisFix(), this); //Entioty damage event maybe fix invis?
        Bukkit.getPluginManager().registerEvents(new ProjTrailListener(), this); //Toggle glide, arrow shot
        Bukkit.getPluginManager().registerEvents(new lobbyDestroyFix(), this); //block break
        Bukkit.getPluginManager().registerEvents(new WorldChangeFix(), this); //portal usage
        Bukkit.getPluginManager().registerEvents(new FireworkDamageFix(), this); //entity damage by dentity
        Bukkit.getPluginManager().registerEvents(new ArmorStandFix(), this); //armor stand manipulate
        Bukkit.getPluginManager().registerEvents(new SculkGameStart(), this); //sculk warden spawner
        Bukkit.getPluginManager().registerEvents(new panels(), this); //inventory click
        Bukkit.getPluginManager().registerEvents(new BlazeNpc(), this); //enity interact HAS NO SHOP
        Bukkit.getPluginManager().registerEvents(new dareListener(), this); //horse jump
        Bukkit.getPluginManager().registerEvents(new LobbyInventoryFix(), this); //world change
        Bukkit.getPluginManager().registerEvents(new leaderboardCycle(), this); //entity click
        Bukkit.getPluginManager().registerEvents(new VictoryDanceListener(), this); //bw player killed
        Bukkit.getPluginManager().registerEvents(new LobbyListener(), this); //join and world change
        Bukkit.getPluginManager().registerEvents(new TeamSelection(), this); //bwjoin invnetory click
        Bukkit.getPluginManager().registerEvents(new disableStats(), this); //bw stats bw start
        Bukkit.getPluginManager().registerEvents(new bedfight(), this); //bedfight gamemode
        Bukkit.getPluginManager().registerEvents(new voider(), this); //checks y level
        Bukkit.getPluginManager().registerEvents(new bedfightStatistics(), this); //does waht it says
        Bukkit.getPluginManager().registerEvents(new safeSizeChange(), this); //teamsize panel handle
        Bukkit.getPluginManager().registerEvents(new queue(), this); //bedwars yeah u know what it is come on man
        Bukkit.getPluginManager().registerEvents(new Protocol47Fix(), this); //new potion effect aka slow falling 1.8 fix, version reminder, sound fix
        Bukkit.getPluginManager().registerEvents(new stuckOnDeathFix(), this); //glitched death fix
        Bukkit.getPluginManager().registerEvents(new perGameJumpingListener(), this); //create fireball jumping info per game
        Bukkit.getPluginManager().registerEvents(new gameLobbyItems(), this); //game lobby items clicks
        Bukkit.getPluginManager().registerEvents(new perGameScenSelHolder(), this); //bw join leave start for scen selector execution
        Bukkit.getPluginManager().registerEvents(new givePoints(), this); //bw start economy setup
        Bukkit.getPluginManager().registerEvents(new tempCombatSwap(), this); //entity damage by entity

        // nearly proper
        Bukkit.getPluginManager().registerEvents(new luckyBlockBreakDetection(), this); // lucky block place and break detection

        // death
        Bukkit.getPluginManager().registerEvents(new FixSpectatoring(), this); // death event
        Bukkit.getPluginManager().registerEvents(new KillEffectListener(), this); // death event
        Bukkit.getPluginManager().registerEvents(new DeathCounter(), this); // bw death and end game event

        // changed world & join server
        Bukkit.getPluginManager().registerEvents(new ConfigHandler(this), this); // join event
        Bukkit.getPluginManager().registerEvents(new AbtributesOnDeath(), this); // join event
        Bukkit.getPluginManager().registerEvents(new MasterPlayerJoin(), this); // changed world event

        // HAS LOTS OF GAME ENDING RESETS
        Bukkit.getPluginManager().registerEvents(new deathListener(), this); // bw death, lucky blocks on death, clear data on bedwars games end, falling blocks lb grabber, bounce fall damage cancel

        // Game start and end logger
        Bukkit.getPluginManager().registerEvents(new GameLogger(), this);

        // commands
        this.getCommand("invictools").setExecutor(new OldCommands(worlds, y, blackListedWorlds, games));
        this.getCommand("it").setExecutor(new OldCommands(worlds, y, blackListedWorlds, games));

        this.getCommand("toggle").setExecutor(new toggleCommands());
        this.getCommand("lb").setExecutor(new leaderboardCommands());
        this.getCommand("teamsize").setExecutor(new teamSizeCommands());
        this.getCommand("utility").setExecutor(new utilityCommands());
        this.getCommand("queue").setExecutor(new joinCommands());
        this.getCommand("scen").setExecutor(new scenarioCommands());
        this.getCommand("invictaview").setExecutor(new InvicSpecCommand());
        this.getCommand("coins").setExecutor(new econCommands());
        this.getCommand("cage").setExecutor(new cageCommands());

        if(Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI"))
        {
            new PAPIexpansion().register();
        }

        // to run after server loads
        BukkitRunnable runnable = new BukkitRunnable()
        {
            @Override
            public void run()
            {
                deathListener.clearEverything(Bukkit.getWorld("bwlobby"));
                new panels().loadPanels();
                new BlazeNpc().spawnNPC("npc", true);
                new leaderboard().loadLeaderboard("Star");
                new leaderboardHologram().createLeaderboard();
                new BedfightLeaderboard().loadBFLeaderboard("score");
                new BedfightLeaderboardHologram().createBFLeaderboard();
                if (UsePlugNPC)
                {
                    new BlazeNpc().spawnNPC("npc2", false);
                }
                for (String config : Configs)
                {
                    ChangeTeamSize.createLists(config);
                }

                queue.activeBedfightGame = new queue().getRandomGame("Bedfight");
                queue.activeBedwarsGame = new queue().getRandomGame("normal");

                final FileConfiguration config = OldCommands.Invictools.getConfig();
                String masterplayer = config.getString("masterplayer","Invictable");
                OldCommands.MasterPlayer = Bukkit.getPlayer(masterplayer);

                new perGameScenSelHolder();
                new givePoints();
                new statisticRequirments();

            }
        };
        runnable.runTaskLater(this, 1L);
    }

    @Override
    public void onDisable()
    {
        deathListener.clearEverything(Bukkit.getWorld("bwlobby"));
    }
}

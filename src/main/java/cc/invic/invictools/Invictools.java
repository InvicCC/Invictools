package cc.invic.invictools;

import cc.invic.invictools.cosmetics.ConfigHandler;
import cc.invic.invictools.cosmetics.LobbyListener;
import cc.invic.invictools.cosmetics.VictoryDances.VictoryDanceListener;
import cc.invic.invictools.cosmetics.bedbreaks.BedBreaks;
import cc.invic.invictools.cosmetics.finalkills.FinalKillListener;
import cc.invic.invictools.cosmetics.projtrail.ProjTrailListener;
import cc.invic.invictools.gamemodes.bedfight;
import cc.invic.invictools.gamemodes.bedfightStatistics;
import cc.invic.invictools.gamemodifiers.AbtributesOnDeath;
import cc.invic.invictools.gamemodifiers.LuckyBlocks.luckyBlockBreakDetection;
import cc.invic.invictools.gamemodifiers.PotionEffects.KillEffectListener;
import cc.invic.invictools.gamemodifiers.PotionEffects.TeamworkEffect;
import cc.invic.invictools.gamemodifiers.SculkGameStart;
import cc.invic.invictools.gamemodifiers.Totem;
import cc.invic.invictools.impl.*;
import cc.invic.invictools.items.ItemListener;
import cc.invic.invictools.items.ModBow;
import cc.invic.invictools.items.dareListener;
import cc.invic.invictools.util.*;
import cc.invic.invictools.util.Leaderboards.*;
import cc.invic.invictools.util.fixes.*;
import cc.invic.invictools.util.npc.BlazeNpc;
import cc.invic.invictools.util.physics.CancelConcreteChange;
import cc.invic.invictools.util.physics.CancelLampUpdates;
import cc.invic.invictools.util.physics.MasterPlayerJoin;
import cc.invic.invictools.util.physics.RiptideDamage;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.bukkit.Bukkit.getPluginManager;

public final class Invictools extends JavaPlugin
{
    List<String> worlds = new ArrayList<>();
    List<String> games = new ArrayList<>();
    List<String> Configs = new ArrayList<>();

    @Override
    public void onEnable()
    {
        this.saveDefaultConfig();
        FileConfiguration fileConfiguration = this.getConfig();
        File Folder = new File(Commands.Invictools.getDataFolder(), "Maps");
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
        getPluginManager().addPermission(new Permission("nospectp.allowteleport"));
        getPluginManager().addPermission(new Permission("invic.invictools"));
        getPluginManager().addPermission(new Permission("invic.firestick"));

        // proper
        getPluginManager().registerEvents(new disableSpectatorTeleport(), this); // teleport event
        getPluginManager().registerEvents(new BedBreaks(), this); // bed break cosmetics and lucky block on bedbreaks; block destroy
        getPluginManager().registerEvents(new ExplosionsListener(), this); // explosion event
        getPluginManager().registerEvents(new ItemListener(), this); // right click event
        getPluginManager().registerEvents(new Totem(), this); //on player damage
        getPluginManager().registerEvents(new CancelLampUpdates(), this); // cancels redstone lamps updating properly
        getPluginManager().registerEvents(new PreventElytraClick(), this); // Disables clicking Chestplate slot when ProximityElytra is active
        getPluginManager().registerEvents(new LobbyLogic(), this); // first player joins lobby and when game starts
        getPluginManager().registerEvents(new CancelConcreteChange(), this); // BlockFormEvent
        getPluginManager().registerEvents(new RiptideDamage(), this); // RiptideEvent
        getPluginManager().registerEvents(new TeamworkEffect(), this); // Teamwork specific game end clearing and bedwars death event
        getPluginManager().registerEvents(new ModBow(), this); // Entity Shoot Bow
        getPluginManager().registerEvents(new disableDrops(), this); // Player drop, bedwars game end
        getPluginManager().registerEvents(new OlympusFires(), this); // Bedwars bed break, bedwars start
        getPluginManager().registerEvents(new SculkFires(), this); // Bedwars bed break, bedwars start
        getPluginManager().registerEvents(new FinalKillListener(), this); // Bedwars bed break, bedwars start
        getPluginManager().registerEvents(new InvisFix(), this); //Entioty damage event maybe fix invis?
        getPluginManager().registerEvents(new ProjTrailListener(), this); //Toggle glide, arrow shot
        getPluginManager().registerEvents(new lobbyDestroyFix(), this); //block break
        getPluginManager().registerEvents(new WorldChangeFix(), this); //portal usage
        getPluginManager().registerEvents(new FireworkDamageFix(), this); //entity damage by dentity
        getPluginManager().registerEvents(new ArmorStandFix(), this); //armor stand manipulate
        getPluginManager().registerEvents(new SculkGameStart(), this); //sculk warden spawner
        getPluginManager().registerEvents(new panels(), this); //inventory click
        getPluginManager().registerEvents(new BlazeNpc(), this); //enity interact HAS NO SHOP
        getPluginManager().registerEvents(new dareListener(), this); //horse jump
        getPluginManager().registerEvents(new LobbyInventoryFix(), this); //world change
        getPluginManager().registerEvents(new leaderboardCycle(), this); //entity click
        getPluginManager().registerEvents(new VictoryDanceListener(), this); //bw player killed
        getPluginManager().registerEvents(new LobbyListener(), this); //join and world change
        getPluginManager().registerEvents(new TeamSelection(), this); //bwjoin invnetory click
        getPluginManager().registerEvents(new disableStats(), this); //bw stats bw start
        getPluginManager().registerEvents(new bedfight(), this); //bedfight gamemode
        getPluginManager().registerEvents(new voider(), this); //checks y level
        getPluginManager().registerEvents(new bedfightStatistics(), this); //does waht it says
        getPluginManager().registerEvents(new safeSizeChange(), this); //teamsize panel handle
        getPluginManager().registerEvents(new queue(), this); //bedwars yeah u know what it is come on man
        getPluginManager().registerEvents(new Protocol47Fix(), this); //new potion effect aka slow falling 1.8 fix, version reminder,
        getPluginManager().registerEvents(new stuckOnDeathFix(), this); //new potion effect aka slow falling 1.8 fix, version reminder,

        // nearly proper
        getPluginManager().registerEvents(new luckyBlockBreakDetection(), this); // lucky block place and break detection

        // death
        getPluginManager().registerEvents(new FixSpectatoring(), this); // death event
        getPluginManager().registerEvents(new KillEffectListener(), this); // death event
        getPluginManager().registerEvents(new DeathCounter(), this); // bw death and end game event

        // changed world & join server
        getPluginManager().registerEvents(new ConfigHandler(this), this); // join event
        getPluginManager().registerEvents(new AbtributesOnDeath(), this); // join event
        getPluginManager().registerEvents(new MasterPlayerJoin(), this); // changed world event

        // HAS LOTS OF GAME ENDING RESETS
        getPluginManager().registerEvents(new deathListener(), this); // bw death, lucky blocks on death, clear data on bedwars games end, falling blocks lb grabber, bounce fall damage cancel

        // Game start and end logger
        getPluginManager().registerEvents(new GameLogger(), this);

        // commands
        getCommand("invictools").setExecutor(new Commands(worlds, y, blackListedWorlds, games));
        getCommand("it").setExecutor(new Commands(worlds, y, blackListedWorlds, games));
        getCommand("toggle").setExecutor(new toggleCommands());
        getCommand("lb").setExecutor(new leaderboardCommands());
        getCommand("teamsize").setExecutor(new teamSizeCommands());
        getCommand("utility").setExecutor(new utilityCommands());
        getCommand("queue").setExecutor(new joinCommands());
        getCommand("scen").setExecutor(new scenarioCommands());
        getCommand("invictaview").setExecutor(new InvicSpecCommand());

        if(getPluginManager().isPluginEnabled("PlaceholderAPI"))
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

                final FileConfiguration config = Commands.Invictools.getConfig();
                String masterplayer = config.getString("masterplayer","Invictable");
                Commands.MasterPlayer = Bukkit.getPlayer(masterplayer);

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

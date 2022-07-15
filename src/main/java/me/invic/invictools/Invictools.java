package me.invic.invictools;

import me.invic.invictools.cosmetics.Lobby1Handler;
import me.invic.invictools.cosmetics.LobbyListener;
import me.invic.invictools.cosmetics.VictoryDances.VictoryDanceListener;
import me.invic.invictools.cosmetics.bedbreaks.BedBreaks;
import me.invic.invictools.cosmetics.ConfigHandler;
import me.invic.invictools.cosmetics.finalkills.FinalKillListener;
import me.invic.invictools.gamemodes.bedfight;
import me.invic.invictools.gamemodifiers.*;
import me.invic.invictools.items.ItemListener;
import me.invic.invictools.items.ModBow;
import me.invic.invictools.items.dareListener;
import me.invic.invictools.util.deathListener;
import me.invic.invictools.gamemodifiers.LuckyBlocks.luckyBlockBreakDetection;
import me.invic.invictools.gamemodifiers.PotionEffects.KillEffectListener;
import me.invic.invictools.gamemodifiers.PotionEffects.TeamworkEffect;
import me.invic.invictools.cosmetics.projtrail.ProjTrailListener;
import me.invic.invictools.util.*;
import me.invic.invictools.util.fixes.*;
//import me.invic.invictools.util.npc.DetectClickOnNPC;
//import me.invic.invictools.util.npc.DetectClickOnPlugNPC;
//import me.invic.invictools.util.npc.SpawnNPC;
//import me.invic.invictools.util.npc.SpawnPlugNPC;
import me.invic.invictools.util.npc.BlazeNpc;
import me.invic.invictools.util.physics.CancelConcreteChange;
import me.invic.invictools.util.physics.CancelLampUpdates;
import me.invic.invictools.util.physics.MasterPlayerJoin;
import me.invic.invictools.util.physics.RiptideDamage;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import scala.util.matching.Regex;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


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

        FileConfiguration config = Commands.Invictools.getConfig();

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
TODO: bedfight leaderboard / full statistics tracking,
  scenario selector that works for normal players when no mod / opped players are in game but special players always,
   via version checks to notify players / fix slow falling,
   game size changer avliable to all players when no games are running (because it runs /bw reload to work) but toggeable with command,
   second option in game join npc for normal players to select any map ,
   rewrite every command to be grouped by what they are or just individual if used by normal players a lot. ex /lbpos instead of /it leaderboard position
   do not replace commands just write new ones that utilize the same code so manhunt configuration files and related still work but typing is cleaner

 */
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
        Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("Invictools");
        File Folder = new File(plugin.getDataFolder(), "Maps");
        File[] yamlFiles = Folder.listFiles();
        for (File file:yamlFiles)
        {
            FileConfiguration map = YamlConfiguration.loadConfiguration(file);
            worlds.add(map.getString("World"));
            String[] mapName = file.getName().split("\\.");
            Configs.add(mapName[0]+"_"+map.getString("Conversion"));
            games.add(mapName[0]);
        }
        List<String> blackListedWorlds = fileConfiguration.getStringList("blacklistedWorlds");
        double y = fileConfiguration.getDouble("ylevel");
        boolean UsePlugNPC = fileConfiguration.getBoolean("UsePlugNPC");

        // perms
        Bukkit.getPluginManager().addPermission(new Permission("nospectp.allowteleport"));
        Bukkit.getPluginManager().addPermission(new Permission("invic.invictools"));
        Bukkit.getPluginManager().addPermission(new Permission("invic.firestick"));

        // proper
        Bukkit.getPluginManager().registerEvents(new disableSpectatorTeleport(), this); // teleport event
        Bukkit.getPluginManager().registerEvents(new BedBreaks(), this); // bed break cosmetics and lucky block on bedbreaks; block destroy
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

        // nearly proper
        Bukkit.getPluginManager().registerEvents(new luckyBlockBreakDetection(), this); // lucky block place and break detection
        //Bukkit.getPluginManager().registerEvents(new keepitems(), this); // move event is disabled, cancels firework bedbreak damage used to be here.

        // death
        Bukkit.getPluginManager().registerEvents(new FixSpectatoring(), this); // death event
        Bukkit.getPluginManager().registerEvents(new KillEffectListener(), this); // death event
        Bukkit.getPluginManager().registerEvents(new DeathCounter(), this); // bw death and end game event

        // changed world & join server
      //  Bukkit.getPluginManager().registerEvents(new SpawnNPC(), this); // changed world and join
      //  if(UsePlugNPC)
      //      Bukkit.getPluginManager().registerEvents(new SpawnPlugNPC(), this); // changed world and join
        Bukkit.getPluginManager().registerEvents(new ConfigHandler(this), this); // join event
        Bukkit.getPluginManager().registerEvents(new AbtributesOnDeath(), this); // join event
        Bukkit.getPluginManager().registerEvents(new MasterPlayerJoin(), this); // changed world event

        // HAS LOTS OF GAME ENDING RESETS
        Bukkit.getPluginManager().registerEvents(new deathListener(), this); // bw death, lucky blocks on death, clear data on bedwars games end, falling blocks lb grabber, bounce fall damage cancel

        // commands
        this.getCommand("invictools").setExecutor(new Commands(worlds, y, blackListedWorlds,games));
        this.getCommand("it").setExecutor(new Commands(worlds, y, blackListedWorlds,games));

        // to run after server loads
        BukkitRunnable runnable = new BukkitRunnable()
        {
            @Override
            public void run()
            {
                deathListener.clearEverything(Bukkit.getWorld("bwlobby"));
                new voider(worlds, y);
                new panels().loadPanels();
               // new SpawnNPC();
               // new DetectClickOnNPC();
                new BlazeNpc().spawnNPC("npc",true);
                new leaderboard().loadLeaderboard("Star");
                new leaderboardHologram().createLeaderboard();
                if(UsePlugNPC)
                {
                    //new SpawnPlugNPC();
                    //new DetectClickOnPlugNPC();
                    new BlazeNpc().spawnNPC("npc2",false);
                }
                for (String config:Configs)
                {
                    ChangeTeamSize.createLists(config);
                }
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

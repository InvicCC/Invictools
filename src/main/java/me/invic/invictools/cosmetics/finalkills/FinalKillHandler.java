package me.invic.invictools.cosmetics.finalkills;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import me.invic.invictools.cosmetics.bedbreaks.TornadoBedBreak;
import me.invic.invictools.cosmetics.projtrail.ProjTrailHandler;
import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.screamingsandals.bedwars.api.BedwarsAPI;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class FinalKillHandler
{
    public void grabEffect(Player effectOwner, Player killed, Location loc)
    {
        Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("Invictools");
        File Folder = new File(plugin.getDataFolder(), "PlayerData");
        File pFile = new File(Folder, effectOwner.getUniqueId() + ".yml");
        FileConfiguration balls = YamlConfiguration.loadConfiguration(pFile);
        String effect = balls.getString("FinalKill","Lightning");
        if (balls.getString("KillEffect") != null)
        {
            if (!balls.getString("KillEffect").equalsIgnoreCase("deprecated"))
            {
                balls.set("FinalKill", "Lightning");
                balls.set("KillEffect", "deprecated");
                try
                {
                    balls.save(pFile);
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }

        effectOwner.playSound(loc, Sound.BLOCK_RESPAWN_ANCHOR_CHARGE, 1, 350);
        effectSwitch(effect, effectOwner, killed, loc);
    }

    public void effectSwitch(String s, Player player, Player killed, Location loc)
    {
        switch (s.toLowerCase(Locale.ROOT))
        {
            case "lightning":
                Lightning(loc);
                break;
            case "firework":
                Firework(loc, player);
                break;
            case "ranked":
                Ranked(loc);
                break;
            case "head":
                head(loc, killed, player);
                break;
            case "pres":
                prestigeBarrage(loc, player);
                break;
            case "tornado":
                new TornadoBedBreak(loc);
                break;
            case "sonic":
                SonicBeam(loc);
                break;
            case "shatter":
                shatter(loc, player);
                break;
            default:
                Firework(loc, player);
        }
    }

    private void shatter(Location loc, Player p)
    {
        //  Random rand = new Random();
        //   loc.getWorld().playEffect(loc.clone().add(rand.nextDouble()-.5,rand.nextDouble()-.5,rand.nextDouble()-.5), Effect.STEP_SOUND, new ProjTrailHandler().PrestigeMaterial(p));
        loc.getWorld().playEffect(loc.clone().add(-.4, 0, 0), Effect.STEP_SOUND, new ProjTrailHandler().PrestigeMaterial(p));
        loc.getWorld().playEffect(loc.clone().add(.3, .4, 0), Effect.STEP_SOUND, new ProjTrailHandler().PrestigeMaterial(p));
        loc.getWorld().playEffect(loc.clone().add(.4, .2, -.4), Effect.STEP_SOUND, new ProjTrailHandler().PrestigeMaterial(p));
        loc.getWorld().playEffect(loc.clone().add(-.4, .3, .3), Effect.STEP_SOUND, new ProjTrailHandler().PrestigeMaterial(p));
        loc.getWorld().playEffect(loc.clone().add(0, .5, .4), Effect.STEP_SOUND, new ProjTrailHandler().PrestigeMaterial(p));
        loc.getWorld().playEffect(loc.clone().add(0, .7, -.4), Effect.STEP_SOUND, new ProjTrailHandler().PrestigeMaterial(p));
        //    loc.getWorld().spawnParticle(Particle.ITEM_CRACK, loc.add(0.5,0.5,0.5), 1, 1, 0.1, 0.1, 0.1, Material.LIGHT_BLUE_STAINED_GLASS.createBlockData());
    }

    private void SonicBeam(Location loc)
    {
        loc.getWorld().playSound(loc, Sound.ENTITY_WARDEN_SONIC_BOOM, 1, 1);
        loc.getWorld().playSound(loc, Sound.ENTITY_WARDEN_HEARTBEAT, 1, 1);
        Arrow arrow = (Arrow) loc.getWorld().spawnEntity(loc, EntityType.ARROW);
        arrow.setInvulnerable(true);
        arrow.setVelocity(new Vector(0, 1.5, 0));
        new ProjTrailHandler().effectSwitch("sculk", Bukkit.getOnlinePlayers().iterator().next(), arrow);

        new BukkitRunnable()
        {
            @Override
            public void run()
            {
                Location loc = arrow.getLocation();
                arrow.remove();

                new BukkitRunnable()
                {
                    @Override
                    public void run()
                    {
                        loc.getWorld().playSound(loc, Sound.ENTITY_WARDEN_DEATH, 20, 1);
                        loc.getWorld().playSound(loc, Sound.ENTITY_GENERIC_EXPLODE, 20, 1);
                        arrow.getWorld().spawnParticle(Particle.EXPLOSION_HUGE, arrow.getLocation(), 1);
                    }
                }.runTaskLater(Objects.requireNonNull(Bukkit.getServer().getPluginManager().getPlugin("Invictools")), 10L);
            }
        }.runTaskLater(Objects.requireNonNull(Bukkit.getServer().getPluginManager().getPlugin("Invictools")), 25L);
    }

    private void Lightning(Location loc)
    {
        loc.getWorld().strikeLightningEffect(loc);
    }

    private void Firework(Location loc, Player player)
    {
        BedwarsAPI api = BedwarsAPI.getInstance();
        Firework fw = (Firework) loc.getWorld().spawnEntity(loc, EntityType.FIREWORK);
        FireworkMeta fwm = fw.getFireworkMeta();

        fwm.setPower(0);
        if (api.isPlayerPlayingAnyGame(player) && api.getGameOfPlayer(player).getTeamOfPlayer(player) != null)
        {
            Color color = teamToColor(api.getGameOfPlayer(player).getTeamOfPlayer(player).getColor().toString());
            fwm.addEffect(FireworkEffect.builder().withColor(color).flicker(true).trail(true).build());
        }
        fwm.addEffect(FireworkEffect.builder().withColor(Color.WHITE).trail(true).flicker(true).build());

        fw.setMetadata("nodamage", new FixedMetadataValue(Objects.requireNonNull(Bukkit.getServer().getPluginManager().getPlugin("Invictools")), true));
        fw.setFireworkMeta(fwm);
        fw.detonate();

        //  new ProjTrailHandler().grabEffect(player,fw);
    }

    private void Ranked(Location loc)
    {
        loc.getWorld().spawnParticle(Particle.HEART, loc, 10, 2, 3, 2);
        loc.getWorld().spawnParticle(Particle.VILLAGER_HAPPY, loc, 20, 2, 3, 2);
        loc.getWorld().spawnParticle(Particle.NOTE, loc, 30, 2, 3, 2);
        loc.getWorld().playSound(loc, Sound.BLOCK_CONDUIT_DEACTIVATE, 2.0F, 1.0F);
    }


    // streemersm prestiege proj trail spawned on random arrows with random velocity
    private void prestigeBarrage(Location loc, Player p)
    {
        final int[] i = {0};
        new BukkitRunnable()
        {
            @Override
            public void run()
            {
                if (i[0] == 5)
                    i[0]++;

                if (i[0] == 0)
                    spawnFireworks(loc, 0, p);
                else if (i[0] < 6)
                {
                    spawnFireworks(loc, 1, p);
                }
                else
                {
                    spawnFireworks(loc, 2, p);
                    this.cancel();
                }
                i[0]++;
            }
        }.runTaskTimer(Objects.requireNonNull(Bukkit.getServer().getPluginManager().getPlugin("Invictools")), 0, 2);
    }

    private void head(Location loc, Player killed, Player p)
    {
        dohead(new Vector(0, 1.4, 0), loc, killed, p);
    }

    private void dohead(Vector v, Location loc, Player killed, Player p)
    {

        // ArmorStand as1 = (ArmorStand) loc.getWorld().spawnEntity(new Location(loc.getWorld(),loc.getX(),loc.getY() +100,loc.getZ()), EntityType.ARMOR_STAND);
        ArmorStand as1 = (ArmorStand) loc.getWorld().spawnEntity(loc, EntityType.ARMOR_STAND);
        as1.setVisible(false);
        as1.getEquipment().setHelmet(getCustomSkull(killed.getName()));

        as1.setVelocity(v);
        p.playSound(as1.getLocation(), Sound.ENTITY_FIREWORK_ROCKET_LAUNCH, 1, 1);
        new ProjTrailHandler().pres(as1, p);
        new BukkitRunnable()
        {
            @Override
            public void run()
            {
                if (cancel)
                {
                    cancel = false;
                    this.cancel();
                }

                as1.getLocation().getWorld().spawnParticle(Particle.CRIT, as1.getLocation().clone().add(0, 1, 0), 5, 0, 0, 0, .1);
            }
        }.runTaskTimer(Objects.requireNonNull(Bukkit.getServer().getPluginManager().getPlugin("Invictools")), 1L, 3L);

        BukkitRunnable runnable2 = new BukkitRunnable()
        {
            @Override
            public void run()
            {
                as1.getLocation().clone().getWorld().playSound(as1.getLocation().clone(), Sound.ENTITY_GENERIC_EXPLODE, 20, 1);
                as1.getWorld().spawnParticle(Particle.EXPLOSION_HUGE, as1.getLocation(), 1);
                as1.remove();
                cancel = true;
                //  Firework(as1.getLocation().clone().add(0, 1, 0), p);
            }
        };
        runnable2.runTaskLater(Objects.requireNonNull(Bukkit.getServer().getPluginManager().getPlugin("Invictools")), 17);
        //       }
        //   };
        // runnable.runTaskLater(Objects.requireNonNull(Bukkit.getServer().getPluginManager().getPlugin("Invictools")), 1);
    }

    boolean cancel = false;

    public ItemStack getCustomSkull(String base64)
    {

        ItemStack head = new ItemStack(Material.PLAYER_HEAD);
        if (base64.isEmpty()) return head;

        SkullMeta skullMeta = (SkullMeta) head.getItemMeta();
        //   skullMeta.setOwningPlayer(Bukkit.getPlayer(base64));
        GameProfile profile = new GameProfile(UUID.randomUUID(), null);

        profile.getProperties().put("textures", new Property("textures", getHeadValue(base64)));

        try
        {
            Method mtd = skullMeta.getClass().getDeclaredMethod("setProfile", GameProfile.class);
            mtd.setAccessible(true);
            mtd.invoke(skullMeta, profile);
        }
        catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException ex)
        {
            ex.printStackTrace();
        }

        head.setItemMeta(skullMeta);
        return head;
    }

    /*
    private static ItemStack getHead(String value)
    {
        ItemStack skull = new ItemStack(Material.PLAYER_HEAD);
        //UUID hashAsId = new UUID(value.hashCode(), value.hashCode());
        UUID hashAsId = Commands.MasterPlayer.getUniqueId();
        return Bukkit.getUnsafe().modifyItemStack(skull,
                "{SkullOwner:{Id:\"" + hashAsId + "\",Properties:{textures:[{Value:\"" + value + "\"}]}}}"
        );
    }

     */
    public Color teamToColor(String teamColor)
    {
        if (teamColor == null)
            return Color.BLACK;

        switch (teamColor)
        {
            case "LIGHT_BLUE":
                return Color.AQUA;
            case "BLUE":
                return Color.NAVY;
            case "GRAY":
            case "LIGHT_GRAY":
                return Color.GRAY;
            case "RED":
                return Color.RED;
            case "WHITE":
                return Color.SILVER;
            case "YELLOW":
                return Color.YELLOW;
            case "PINK":
                return Color.FUCHSIA;
            case "ORANGE":
                return Color.ORANGE;
            case "LIME":
                return Color.LIME;
            default:
                return Color.BLACK;
        }
    }

    static String getHeadValue(String name)
    {
        try
        {
            String result = getURLContent("https://api.mojang.com/users/profiles/minecraft/" + name);
            Gson g = new Gson();
            JsonObject obj = g.fromJson(result, JsonObject.class);
            String uid = obj.get("id").toString().replace("\"", "");
            String signature = getURLContent("https://sessionserver.mojang.com/session/minecraft/profile/" + uid);
            obj = g.fromJson(signature, JsonObject.class);
            String value = obj.getAsJsonArray("properties").get(0).getAsJsonObject().get("value").getAsString();
            String decoded = new String(Base64.getDecoder().decode(value));
            obj = g.fromJson(decoded, JsonObject.class);
            String skinURL = obj.getAsJsonObject("textures").getAsJsonObject("SKIN").get("url").getAsString();
            byte[] skinByte = ("{\"textures\":{\"SKIN\":{\"url\":\"" + skinURL + "\"}}}").getBytes();
            return new String(Base64.getEncoder().encode(skinByte));
        }
        catch (Exception ignored)
        {
        }
        return null;
    }

    private static String getURLContent(String urlStr)
    {
        URL url;
        BufferedReader in = null;
        StringBuilder sb = new StringBuilder();
        try
        {
            url = new URL(urlStr);
            in = new BufferedReader(new InputStreamReader(url.openStream(), StandardCharsets.UTF_8));
            String str;
            while ((str = in.readLine()) != null)
            {
                sb.append(str);
            }
        }
        catch (Exception ignored)
        {
        }
        finally
        {
            try
            {
                if (in != null)
                {
                    in.close();
                }
            }
            catch (IOException ignored)
            {
            }
        }
        return sb.toString();
    }

    public void spawnFireworks(Location loc, int pos, Player p)
    {
        if (pos == 1)
            loc.add(0, 5, 0);
        if (pos == 2)
            loc.add(0, 7, 0);

        Firework fw = (Firework) loc.getWorld().spawnEntity(loc, EntityType.FIREWORK);
        FireworkMeta fwm = fw.getFireworkMeta();

        fwm.setPower(0);

        if (pos == 1)
            fwm.addEffect(FireworkEffect.builder().withColor(new ProjTrailHandler().presColor(p)).flicker(true).trail(true).build());

        if (pos == 0 || pos == 1)
            fwm.addEffect(FireworkEffect.builder().withColor(Color.BLACK).trail(true).flicker(true).build());

        if (pos == 2)
        {
            //    fwm.addEffect(FireworkEffect.builder().withColor(Color.BLACK).flicker(false).with(FireworkEffect.Type.BALL_LARGE).trail(true).build());
            fwm.addEffect(FireworkEffect.builder().withColor(new ProjTrailHandler().presColor(p)).flicker(true).with(FireworkEffect.Type.STAR).trail(true).build());
            //  fwm.addEffect(FireworkEffect.builder().withColor(Color.WHITE).flicker(false).with(FireworkEffect.Type.STAR).trail(true).build());
        }

        fw.setMetadata("nodamage", new FixedMetadataValue(Objects.requireNonNull(Bukkit.getServer().getPluginManager().getPlugin("Invictools")), true));
        fw.setFireworkMeta(fwm);
        fw.detonate();
    }
}

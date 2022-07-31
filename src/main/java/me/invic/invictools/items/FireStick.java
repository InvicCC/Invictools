package me.invic.invictools.items;

import me.invic.invictools.commands.OldCommands;
import org.bukkit.Sound;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;

public class FireStick
{
    int cooldown = 1; // seconds

    public FireStick(Player p)
    {
        if (OldCommands.FireStickCooldown.containsKey(p))
        {
            long secondsLeft = ((OldCommands.FireStickCooldown.get(p) / 1000) + cooldown) - (System.currentTimeMillis() / 1000);
            if (secondsLeft <= 0)
                doFireStick(p);
        }
        else
            doFireStick(p);
    }

    public void doFireStick(Player p)
    {
        p.playSound(p.getLocation(), Sound.ENTITY_BLAZE_SHOOT, 1, 1);
        Fireball ball = p.launchProjectile(Fireball.class);
        //  new ProjTrailHandler().grabEffect(p,ball);
        ball.setShooter(p);
        ball.setYield(3);
        ball.setVelocity((p.getLocation().getDirection().multiply(1.5)));
        OldCommands.FireStickCooldown.put(p, System.currentTimeMillis());
    }
}

package me.invic.invictools.cosmetics.projtrail;

import me.invic.invictools.cosmetics.VictoryDances.VictoryDanceHandler;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityToggleGlideEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;

public class ProjTrailListener implements Listener
{
    @EventHandler
    public void Arrow(ProjectileLaunchEvent e)
    {
        if (!(e.getEntity().getShooter() instanceof Player))
            return;

        if (e.isCancelled())
            return;

        new ProjTrailHandler().grabEffect((Player) e.getEntity().getShooter(), e.getEntity());


    }

    @EventHandler
    public void Elytra(EntityToggleGlideEvent e)
    {
        if (e.getEntity().getType() != EntityType.PLAYER)
            return;

        if (e.isCancelled())
            return;

        if (e.isGliding())
            new ProjTrailHandler().grabEffect((Player) e.getEntity(), e.getEntity());
    }
}

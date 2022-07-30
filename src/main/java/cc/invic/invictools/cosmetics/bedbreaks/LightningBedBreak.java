package cc.invic.invictools.cosmetics.bedbreaks;

import org.bukkit.Location;

public class LightningBedBreak
{
    public LightningBedBreak(Location loc)
    {
        loc.getWorld().strikeLightningEffect(loc);
    }
}

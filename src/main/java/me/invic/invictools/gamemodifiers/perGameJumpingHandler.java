package me.invic.invictools.gamemodifiers;

import me.invic.invictools.commands.OldCommands;
import org.bukkit.configuration.file.FileConfiguration;
import org.screamingsandals.bedwars.api.game.Game;

import java.util.HashMap;

public class perGameJumpingHandler
{
    FileConfiguration pluginConfig = OldCommands.Invictools.getConfig();

    double x;
    double y;
    double range;
    boolean op;

    public perGameJumpingHandler()
    {
        makeDefault();
    }

    public void setX(double nx)
    {
        x = nx;
    }
    public void setY(double ny)
    {
        y = ny;
    }
    public void setRange(double nr)
    {
        range = nr;
    }
    public void setOP(boolean nop)
    {
        op = nop;
    }

    public double getX()
    {
        return x;
    }
    public double getY()
    {
        return y;
    }
    public double getRange()
    {
        return range;
    }
    public boolean getOP()
    {
        return op;
    }

    public void makeDefault()
    {
        range = pluginConfig.getDouble("jump.fireball.range",3.5);
        x = pluginConfig.getDouble("jump.fireball.xzmultiplier",0.4);
        y = pluginConfig.getDouble("jump.fireball.ymultiplier",0.6);
        op = pluginConfig.getBoolean("jump.fireball.op",false);
    }

}

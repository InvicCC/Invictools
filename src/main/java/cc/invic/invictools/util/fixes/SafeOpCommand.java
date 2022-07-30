package cc.invic.invictools.util.fixes;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class SafeOpCommand
{
    public SafeOpCommand(Player player, String com)
    {
        if (player.isOp())
        {
            Bukkit.dispatchCommand(player, com);
        }
        else
        {
            try
            {
                player.setOp(true);
                Bukkit.dispatchCommand(player, com);
                player.setOp(false);
            }
            catch (Throwable e)
            {
                player.setOp(false);
                System.out.println("Invictools: OP command failed: " + com);
            }
        }
    }
}

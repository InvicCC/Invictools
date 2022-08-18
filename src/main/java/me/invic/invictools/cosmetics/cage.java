package me.invic.invictools.cosmetics;

import org.bukkit.entity.Player;
import java.util.HashMap;

public class cage
{
    private HashMap<String, Integer> requirements = new HashMap<>();
    private String name;

    public cage(String n)
    {
        name = n;
    }

    public void addReq(String type, Integer amount)
    {
        requirements.put(type,amount);
    }

    public String getName()
    {
        return name;
    }

    //char at 1 != 8
    // on resest load hashmap of every cage
    // when player logs in add their player and cage in config to hashmap after checking req
    // check reqs before cage loading too
    public boolean checkReq(Player p)
    {
        if(requirements.isEmpty())
            return true;

        for (String s:requirements.keySet())
        {
            int a = statisticRequirments.getStatistic(s,p);
            if(s.equalsIgnoreCase("bfpos") || s.equalsIgnoreCase("bwpos"))
            {
                if(a <= requirements.get(s) && a != -1)
                    return true;
            }
            else
            {
                if(a >= requirements.get(s) && a != -1)
                    return true;
            }
        }
        return false;
    }

}

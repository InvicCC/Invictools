package me.invic.invictools.gamemodifiers;

import org.screamingsandals.bedwars.api.game.Game;

import java.util.HashMap;

public class gamemodeData
{
    private static HashMap<Game,String> luckyBlockMode = new HashMap<>();

    public String getLuckyBlockMode(Game game)
    {
        return luckyBlockMode.getOrDefault(game, "none");
    }

    /**
     *
     * @param game game whose mode is being set
     * @param mode normal, 47 (1.8 only), none (lucky blocked disabled).
     */
    public void setLuckyBlockMode(Game game,String mode)
    {
        luckyBlockMode.put(game,mode);
    }
}

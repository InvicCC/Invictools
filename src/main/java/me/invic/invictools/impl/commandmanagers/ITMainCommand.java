package me.invic.invictools.impl.commandmanagers;

import me.invic.invictools.commandManagerLib.MainCommand;
import me.invic.invictools.commandManagerLib.argumentMatchers.ContainingAllCharsOfStringArgumentMatcher;
import me.invic.invictools.impl.IT.*;
import org.bukkit.ChatColor;

public class ITMainCommand extends MainCommand
{
    public ITMainCommand ()
    {
        super(ChatColor.AQUA + "You don't have permission to do that.", new ContainingAllCharsOfStringArgumentMatcher());
    }

    @Override
    protected void registerSubCommands ()
    {
        subCommands.add(new HelpCommand());
        subCommands.add(new FirestickCommand());
        subCommands.add(new BedBreakCommand());
        subCommands.add(new VictoryDanceCommand());
        subCommands.add(new FinalKillCommand());
        subCommands.add(new NormalKillCommand());
        subCommands.add(new Lobby1Command());
        subCommands.add(new ProjTrailCommand());
        subCommands.add(new TsCommand());
        subCommands.add(new PanelCommand());
        subCommands.add(new CreatebfconfigCommand());
        subCommands.add(new RbfcCommand());
        subCommands.add(new GamejoinerCommand());
        subCommands.add(new TrackerCommand());
        subCommands.add(new FinalKillTestCommand());
        subCommands.add(new NormalKillCommand());
        subCommands.add(new BedBreakTestCommand());
        subCommands.add(new RanksCommand());
        subCommands.add(new StatsCommand());
        subCommands.add(new PortalsCommand());
        subCommands.add(new ReloadnpcCommand());
        subCommands.add(new LobbydareCommand());
        subCommands.add(new ProjpreviewCommand());
        subCommands.add(new VictoryDancePreviewCommand());
        subCommands.add(new DestroyholosCommand());
        subCommands.add(new DebugCommand());
        subCommands.add(new ForceManhuntTeamCommand());
        subCommands.add(new LoadInventoryCommand());
        subCommands.add(new ParkourCommand());
        subCommands.add(new ClearManhuntTeamCommand());
        subCommands.add(new ResetAttributesCommand());
        subCommands.add(new ResetDeathCounterCommand());
        subCommands.add(new ShriekerCommand());
        subCommands.add(new WorldborderCommand());
        subCommands.add(new GrabteammatesCommand());
        subCommands.add(new ClearteammatesCommand());
        subCommands.add(new DisableshopCommand());
        subCommands.add(new ResetCommand());
        subCommands.add(new ProximityElytraCommand());
        subCommands.add(new ProximityElytraSingleCommand());
        subCommands.add(new SetTeamSizeCommand());
        subCommands.add(new EffectSometimesCommand());
        subCommands.add(new PrintitemdataCommand());
        subCommands.add(new PidCommand());
        subCommands.add(new GivePIDCommand());
        subCommands.add(new ToggleFireStickCommand());
        subCommands.add(new ToggleVictoryCommand());
        subCommands.add(new ItemrainCommand());
        subCommands.add(new DeathAttributeCommand());
        subCommands.add(new TotemsCommand());
        subCommands.add(new HauntCommand());
        subCommands.add(new InstantHauntCommand());
        subCommands.add(new LuckyblocksCommand());
        subCommands.add(new RepeatedItemCommand());
    }
}
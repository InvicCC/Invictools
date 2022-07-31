package me.invic.invictools.impl;

import me.invic.invictools.commandManagerLib.MainCommand;
import me.invic.invictools.commandManagerLib.argumentMatchers.ContainingAllCharsOfStringArgumentMatcher;

public class ITMainCommand extends MainCommand
{
    public ITMainCommand ()
    {
        super("§cYou don't have permission to do that.", new ContainingAllCharsOfStringArgumentMatcher());
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
    }
}
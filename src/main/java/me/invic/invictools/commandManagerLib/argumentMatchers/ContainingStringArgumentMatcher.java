package me.invic.invictools.commandManagerLib.argumentMatchers;

import me.invic.invictools.commandManagerLib.ArgumentMatcher;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Filters to leave all the strings that containing the argument string.
 * Example: kill, kick, looking | ki -> kick, kill, looking
 */
public class ContainingStringArgumentMatcher implements ArgumentMatcher
{
    @Override
    public List<String> filter (List<String> tabCompletions, String argument)
    {
        return tabCompletions.stream().filter(tabCompletion -> tabCompletion.contains(argument)).collect(Collectors.toList());
    }
}
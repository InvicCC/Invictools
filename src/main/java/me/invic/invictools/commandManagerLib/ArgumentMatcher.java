package me.invic.invictools.commandManagerLib;

import java.util.List;

public interface ArgumentMatcher
{
    /**
     * Filters tabCompletions based on argument string.
     * @param tabCompletions The tabCompletions to filter.
     * @param argument The argument string.
     * @return The filtered tabCompletions.
     */
    List<String> filter (List<String> tabCompletions, String argument);
}
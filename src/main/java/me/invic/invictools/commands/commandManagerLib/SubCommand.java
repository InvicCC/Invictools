package me.invic.invictools.commands.commandManagerLib;

import org.bukkit.command.CommandSender;

import java.util.List;

public interface SubCommand
{
    /**
     * Returns name of this subCommand. (used in /mainCommand subCommandName)
     * @return The name.
     */
    String getName ();

    /**
     * Returns description of this subCommand.
     * @return The description.
     */
    String getDescription ();

    /**
     * Returns the syntax of this subCommand. (ex. /troll swap &lt;player1&gt; &lt;player2&gt;)
     * @return The syntax.
     */
    String getSyntax ();

    /**
     * Returns permission required to run this subCommand.
     * @return The permission.
     */
    String getPermission ();

    /**
     * Returns the list of words to tab-complete on an index starting after the subCommand. (ex. /troll swap 0 1 | numbers representing the index of tabComplete)
     * @param index The index to get tab-complete for.
     * @param args All the args in the command, including the currently tab-completed one.
     * @return The list of words to tab-complete.
     */
    List<String> getTabCompletion (int index, String[] args);

    /**
     * Performs the command.
     * @param sender Sender, who has invoked the command. (either Player or Console)
     * @param args The arguments after this subCommand.
     */
    void perform (CommandSender sender, String[] args);
}
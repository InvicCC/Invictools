package me.invic.invictools.commands.commandManagerLib;


import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabExecutor;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;
import java.util.stream.Collectors;

public abstract class MainCommand implements TabExecutor
{
    protected final Set<SubCommand> subCommands = new HashSet<>();

    protected final String noPermMessage;
    protected final ArgumentMatcher argumentMatcher;

    public MainCommand (String noPermissionMessage, ArgumentMatcher argumentMatcher)
    {
        this.noPermMessage = ChatColor.translateAlternateColorCodes('&', noPermissionMessage);
        this.argumentMatcher = argumentMatcher;

        registerSubCommands();
    }

    @Override
    public boolean onCommand (CommandSender sender, Command command, String label, String[] args)
    {
        /* Send sender a help if he doesn't use any subCommand and has permission for the help subCommand */
        if (args.length == 0)
        {
            SubCommand helpSC = getHelpSubCommand();

            if (helpSC != null && sender.hasPermission(helpSC.getPermission()))
            {
                helpSC.perform(sender, args);
                return true;
            }
            return false;
        }

        /* Gets the subcommand by the name in first argument. Or help, if the subCommand doesn't exist. */
        SubCommand subCommand = subCommands.stream().filter(sc -> sc.getName().equalsIgnoreCase(args[0])).findAny().orElse(getHelpSubCommand());

        if (subCommand == null)
            return false;

        /* Execute command if the player has permission, send no-permission message otherwise. */
        if (sender.hasPermission(subCommand.getPermission()))
            subCommand.perform(sender, Arrays.copyOfRange(args, 1, args.length));
        else
            sender.sendMessage(noPermMessage);

        return true;
    }

    @Override
    public List<String> onTabComplete (CommandSender sender, Command command, String alias, String[] args)
    {
        /* Return if there is nothing to tab complete. */
        if (args.length == 0)
            return null;

        /* If it's the first argument, that means that a subCommands need to be tab completed. */
        if (args.length == 1)
        {
            List<String> subCommandsTC = subCommands.stream().filter(sc -> sender.hasPermission(sc.getPermission())).map(SubCommand::getName).collect(Collectors.toList());
            return getMatchingStrings(subCommandsTC, args[args.length - 1], argumentMatcher);
        }

        /* Gets the subcommand by the name in first argument. */
        SubCommand subCommand = subCommands.stream().filter(sc -> sc.getName().equalsIgnoreCase(args[0])).findAny().orElse(null);

        if (subCommand == null)
            return null;

        /* Gets the tabCompletion from the subCommand. */
        List<String> subCommandTB = subCommand.getTabCompletion(args.length - 2, args);

        return getMatchingStrings(subCommandTB, args[args.length - 1], argumentMatcher);
    }

    /**
     * Returns a new list of tabCompletions based on unfinished argument filtered by selected ArgumentMatcher.
     * @param tabCompletions The source tabCompletions.
     * @param arg The argument string.
     * @param argumentMatcher The ArgumentMather.
     * @return A list of new tabCompletions.
     */
    private static List<String> getMatchingStrings (List<String> tabCompletions, String arg, ArgumentMatcher argumentMatcher)
    {
        if (tabCompletions == null || arg == null)
            return null;

        List<String> result = argumentMatcher.filter(tabCompletions, arg);

        Collections.sort(result);

        return result;
    }

    /**
     * Registers the command.
     * @param main Main calss of your plugin.
     * @param cmdName Name of the command to register. !!! Has to be the same as one in the plugin.yml !!!
     */
    public void registerMainCommand (JavaPlugin main, String cmdName)
    {
        PluginCommand cmd = main.getCommand(cmdName);

        cmd.setExecutor(this);
        cmd.setTabCompleter(this);
        cmd.setPermissionMessage(noPermMessage);
    }

    /**
     * Adds all subCommands to the subCommands set.
     */
    protected abstract void registerSubCommands ();

    /**
     * Returns the help subcommand from subCommands set. By default returns subCommand named "help".
     * @return the help subCommand.
     */
    protected SubCommand getHelpSubCommand()
    {
        /* Return subCommand named "help". */
        return subCommands.stream().filter(sc -> sc.getName().equalsIgnoreCase("help")).findAny().orElse(null);
    }

    /**
     * Returns a copy of set of subCommands used in this MainCommand.
     * @return The set of subCommands of this MainCommand.
     */
    public Set<SubCommand> getSubCommands ()
    {
        return new HashSet<>(subCommands);
    }
}
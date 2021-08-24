package de.framedev.essentialsmini.managers;

/*
 * de.framedev.essentialsmin.managers
 * ===================================================
 * This File was Created by FrameDev
 * Please do not change anything without my consent!
 * ===================================================
 * This Class was created at 17.09.2020 20:54
 */

import de.framedev.essentialsmini.main.Main;
import lombok.NonNull;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.List;

public abstract class CommandBase implements CommandExecutor, TabCompleter {

    private final Main plugin;
    private String cmdName;
    private String[] cmdNames;

    /**
     * Register an Command
     *
     * @param cmdName  the CommandName for registering
     * @param executor the Executor who executes the Command
     */
    public void setup(@NonNull String cmdName, @NonNull CommandExecutor executor) {
        plugin.getCommands().put(cmdName, executor);
    }

    /**
     * Register an TabCompleter
     *
     * @param cmdName      the CommandName for registering
     * @param tabCompleter the TabCompleter who used for the Command
     */
    public void setupTabCompleter(@NonNull String cmdName, @NonNull TabCompleter tabCompleter) {
        plugin.getTabCompleters().put(cmdName, tabCompleter);
    }

    public CommandBase(Main plugin) {
        this.plugin = plugin;
        this.cmdName = null;
    }

    public CommandBase(Main plugin, @NonNull String cmdName) {
        this.plugin = plugin;
        this.cmdName = cmdName;
    }

    public CommandBase(Main plugin, @NonNull String cmdName, CommandExecutor executor) {
        this.plugin = plugin;
        this.cmdName = cmdName;
        setup(executor);
    }

    public CommandBase(Main plugin, CommandExecutor executor, @NonNull String... cmdNames) {
        this.plugin = plugin;
        this.cmdNames = cmdNames;
        for (String cmd : cmdNames) {
            setup(cmd, executor);
        }
    }

    public Main getPlugin() {
        return plugin;
    }

    /**
     * Register a Command
     *
     * @param executor the Executor who executes the Command
     */
    public void setup(@NonNull CommandExecutor executor) {
        if (cmdName == null) return;
        plugin.getCommands().put(cmdName, executor);
    }

    /**
     * Register an TabCompleter
     *
     * @param tabCompleter the TabCompleter who used for the Command
     */
    public void setupTabCompleter(@NonNull TabCompleter tabCompleter) {
        if (cmdName == null) return;
        plugin.getTabCompleters().put(cmdName, tabCompleter);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        return null;
    }
}

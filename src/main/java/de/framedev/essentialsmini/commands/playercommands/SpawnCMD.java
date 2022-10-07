/**
 * Dies ist ein Plugin von FrameDev
 * Bitte nichts §ndern, @Copyright by FrameDev
 */
package de.framedev.essentialsmini.commands.playercommands;

import de.framedev.essentialsmini.main.Main;
import de.framedev.essentialsmini.managers.LocationsManager;
import de.framedev.essentialsmini.utils.AdminBroadCast;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * @author DHZoc
 */
public class SpawnCMD implements CommandExecutor {

    private final Main plugin;

    public SpawnCMD(Main plugin) {
        this.plugin = plugin;
        plugin.getCommands().put("spawn", this);
        plugin.getCommands().put("setspawn", this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("setspawn")) {
            if (sender.hasPermission("essentialsmini.setspawn")) {
                if (sender instanceof Player) {
                    new LocationsManager("spawn").setLocation(((Player) sender).getLocation());
                    sender.sendMessage(plugin.getPrefix() + "§6Spawn §aset!");
                } else {
                    sender.sendMessage(plugin.getPrefix() + plugin.getOnlyPlayer());
                }
            } else {
                sender.sendMessage(plugin.getPrefix() + plugin.getNoPerms());
                new AdminBroadCast("setspawn","§cNo Permissions!", sender);
            }
        }
        if (command.getName().equalsIgnoreCase("spawn")) {
            if(sender.hasPermission(plugin.getPermissionName() + "spawn")) {
                if (sender instanceof Player) {
                    try {
                        ((Player) sender).teleport(new LocationsManager("spawn").getLocation());
                        sender.sendMessage(plugin.getPrefix() + "§aTeleport to Spawn!");
                    } catch (IllegalArgumentException ignored) {
                        ((Player) sender).teleport(((Player) sender).getWorld().getSpawnLocation());
                    }
                } else {
                    sender.sendMessage(plugin.getPrefix() + plugin.getOnlyPlayer());
                }
            } else {
                sender.sendMessage(plugin.getPrefix() + plugin.getNoPerms());
            }
        }
        return false;
    }

}

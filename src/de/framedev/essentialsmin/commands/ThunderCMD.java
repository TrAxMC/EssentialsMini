package de.framedev.essentialsmin.commands;


/*
 * de.framedev.essentialsmin.commands
 * ===================================================
 * This File was Created by FrameDev
 * Please do not change anything without my consent!
 * ===================================================
 * This Class was created at 23.09.2020 19:19
 */

import de.framedev.essentialsmin.main.Main;
import de.framedev.essentialsmin.managers.CommandBase;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

public class ThunderCMD extends CommandBase implements CommandExecutor {

    private final Main plugin;

    public ThunderCMD(Main plugin) {
        super(plugin);
        this.plugin = plugin;
        setup("lightningstrike",this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(args.length == 0) {
            if(sender instanceof Player) {
                Player player = (Player) sender;
                if(player.hasPermission(new Permission(plugin.getPermissionName() + "lightningstrike", PermissionDefault.OP))) {
                    player.getWorld().strikeLightning(player.getLocation());
                    player.sendMessage(plugin.getPrefix() + "§6Blitz!");
                } else {
                    player.sendMessage(plugin.getPrefix() + plugin.getNOPERMS());
                }
            } else {
                sender.sendMessage(plugin.getPrefix() + plugin.getOnlyPlayer());
            }
        } else if(args.length == 1) {
            Player target = Bukkit.getPlayer(args[0]);
            if(target != null) {
                if (sender.hasPermission(new Permission(plugin.getPermissionName() + "lightningstrike.others", PermissionDefault.OP))) {
                    target.getWorld().strikeLightning(target.getLocation());
                    sender.sendMessage(plugin.getPrefix() + "§6Blitz! §a" + target.getName() + "§c!");
                } else {
                    sender.sendMessage(plugin.getPrefix() + plugin.getNOPERMS());
                }
            } else {
                sender.sendMessage(plugin.getPrefix() + "§cDieser Spieler existiert nicht! §6" + args[0]);
            }
        }
        return false;
    }
}

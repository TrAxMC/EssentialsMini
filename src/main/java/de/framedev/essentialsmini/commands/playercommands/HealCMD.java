package de.framedev.essentialsmini.commands.playercommands;


/*
 * ===================================================
 * This File was Created by FrameDev
 * Please do not change anything without my consent!
 * ===================================================
 * This Class was created at 18.08.2020 19:30
 */

import de.framedev.essentialsmini.main.Main;
import de.framedev.essentialsmini.managers.CommandBase;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HealCMD extends CommandBase {

    private final Main plugin;

    public HealCMD(Main plugin) {
        super(plugin);
        this.plugin = plugin;
        setup("heal", this);
        setupTabCompleter("heal", this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            if (sender instanceof Player) {
                if (sender.hasPermission("essentialsmini.heal")) {
                    Player player = (Player) sender;
                    player.setHealth(20);
                    player.setFireTicks(0);
                    player.setFoodLevel(20);
                    String heal = plugin.getCustomMessagesConfig().getString("Heal.Self");
                    if(heal.contains("&"))
                        heal = heal.replace('&','§');
                    player.sendMessage(plugin.getPrefix() + heal);
                    return true;
                } else {
                    sender.sendMessage(plugin.getPrefix() + plugin.getNOPERMS());
                    return true;
                }
            } else {
                sender.sendMessage(plugin.getPrefix() + plugin.getOnlyPlayer());
                return true;
            }
        } else if (args.length == 1) {
            if (args[0].equalsIgnoreCase("**")) {
                if (sender.hasPermission("essentialsmini.heal.others")) {
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        if (player != null) {
                            player.setHealth(20);
                            player.setFireTicks(0);
                            player.setFoodLevel(20);
                            if (!Main.getSilent().contains(sender.getName())) {
                                String heal = plugin.getCustomMessagesConfig().getString("Heal.Self");
                                if (heal.contains("&"))
                                    heal = heal.replace('&', '§');
                                player.sendMessage(plugin.getPrefix() + heal);
                            }
                            String healOther = plugin.getCustomMessagesConfig().getString("Heal.Other");
                            if(healOther.contains("&"))
                                healOther = healOther.replace('&', '§');
                            if(healOther.contains("%Player%"))
                                healOther = healOther.replace("%Player%", player.getName());
                            sender.sendMessage(plugin.getPrefix() + healOther);
                        }
                    }
                } else {
                    sender.sendMessage(plugin.getPrefix() + plugin.getNOPERMS());
                }
                return true;
            } else {
                if (sender.hasPermission("essentialsmini.heal.others")) {
                    Player player = Bukkit.getPlayer(args[0]);
                    if (player != null) {
                        player.setHealth(20);
                        player.setFireTicks(0);
                        player.setFoodLevel(20);
                        if (!Main.getSilent().contains(sender.getName())) {
                            String heal = plugin.getCustomMessagesConfig().getString("Heal.Self");
                            if (heal.contains("&"))
                                heal = heal.replace('&', '§');
                            player.sendMessage(plugin.getPrefix() + heal);
                        }
                        String healOther = plugin.getCustomMessagesConfig().getString("Heal.Other");
                        if(healOther.contains("&"))
                            healOther = healOther.replace('&', '§');
                        if(healOther.contains("%Player%"))
                            healOther = healOther.replace("%Player%", player.getName());
                        sender.sendMessage(plugin.getPrefix() + healOther);
                    } else {
                        sender.sendMessage(plugin.getPrefix() + plugin.getVariables().getPlayerNameNotOnline(args[0]));
                    }
                } else {
                    sender.sendMessage(plugin.getPrefix() + plugin.getNOPERMS());
                }
                return true;
            }
        } else {
            sender.sendMessage(plugin.getPrefix() + plugin.getWrongArgs("/heal §coder §6/heal <PlayerName>"));
            return true;
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1) {
            if (sender.hasPermission(plugin.getPermissionName() + "heal.others")) {
                ArrayList<String> players = new ArrayList<>();
                ArrayList<String> empty = new ArrayList<>();
                players.add("**");
                for (Player player : Bukkit.getOnlinePlayers()) {
                    players.add(player.getName());
                }
                for (String s : players) {
                    if (s.toLowerCase().startsWith(args[0].toLowerCase())) {
                        empty.add(s);
                    }
                }
                Collections.sort(empty);
                return empty;
            }
        }
        return super.onTabComplete(sender, command, label, args);
    }
}

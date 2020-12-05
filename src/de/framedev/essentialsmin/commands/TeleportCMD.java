/*
 * Dies ist ein Plugin von FrameDev
 * Bitte nichts ändern, @Copyright by FrameDev
 */
package de.framedev.essentialsmin.commands;

import de.framedev.essentialsmin.main.Main;
import de.framedev.essentialsmin.managers.CommandBase;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.*;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Objects;

/**
 * @author DHZoc
 */
public class TeleportCMD implements CommandExecutor {

    private final Main plugin;

    public TeleportCMD(Main plugin) {
        this.plugin = plugin;
        plugin.getCommands().put("tpa", this);
        plugin.getCommands().put("tpaaccept", this);
        plugin.getCommands().put("tpadeny", this);
        plugin.getCommands().put("tphereall", this);
        plugin.getCommands().put("tpahere", this);
        plugin.getCommands().put("tpahereaccept", this);
        plugin.getCommands().put("tpaheredeny", this);
    }

    private final HashMap<Player, Player> tpRequest = new HashMap<>();
    private final HashMap<Player, Player> tpHereRequest = new HashMap<>();

    @Override
    public boolean onCommand(@Nonnull CommandSender sender, Command command, @Nonnull String label, @Nonnull String[] args) {
        if (command.getName().equalsIgnoreCase("tpa")) {
            if (args.length == 1) {
                if (sender instanceof Player) {
                    Player target = Bukkit.getPlayer(args[0]);
                    if (target != sender) {
                        if (target != null) {
                            tpRequest.put(target, (Player) sender);
                            String send = plugin.getCustomMessagesConfig().getString("TpaMessages.TeleportSend");
                            send = send.replace('&','§');
                            if(send.contains("%Target%")) {
                                send = send.replace("%Target%",target.getName());
                            }
                            sender.sendMessage(plugin.getPrefix() + send);
                            String got = plugin.getCustomMessagesConfig().getString("TpaMessages.TeleportGot");
                            got = got.replace('&','§');
                            if(got.contains("%Player%")) {
                                got = got.replace("%Player%",sender.getName());
                            }
                            target.sendMessage(plugin.getPrefix() + got);
                            BaseComponent baseComponent = new TextComponent();
                            baseComponent.addExtra("§6[Accept]");
                            baseComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,"/tpaaccept " + sender.getName()));
                            baseComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,new ComponentBuilder("§aAccept Tpa Request!").create()));
                            BaseComponent ablehnen = new TextComponent();
                            ablehnen.addExtra("§c[Deny]");
                            ablehnen.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,"/tpadeny " + sender.getName()));
                            ablehnen.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,new ComponentBuilder("§cDeny Tpa Request!").create()));
                            target.spigot().sendMessage(baseComponent);
                            target.spigot().sendMessage(ablehnen);
                        } else {
                            sender.sendMessage(plugin.getPrefix() + plugin.getCustomMessagesConfig().getString("PlayerNotOnline"));
                        }
                    } else {
                        sender.sendMessage(plugin.getPrefix() + "§cYou cannot send to your self a Tpa Request!");
                    }
                }
            } else {
                sender.sendMessage(plugin.getPrefix() + plugin.getWrongArgs("/tpa <PlayerName>"));
            }
        }
        if (command.getName().equalsIgnoreCase("tpaaccept")) {
            if (sender instanceof Player) {
                if (tpRequest.containsKey(sender)) {
                    tpRequest.get(sender).teleport(((Player) sender).getLocation());
                    String targetMessage = plugin.getCustomMessagesConfig().getString("TpaMessages.TargetMessage");
                    targetMessage = targetMessage.replace('&','§');
                    if(targetMessage.contains("%Target%"))
                        targetMessage = targetMessage.replace("%Target%",tpRequest.get(sender).getName());
                    sender.sendMessage(plugin.getPrefix() + targetMessage);
                    String teleportTo = plugin.getCustomMessagesConfig().getString("TpaMessages.TeleportToPlayer");
                    teleportTo = teleportTo.replace('&','§');
                    if(teleportTo.contains("%Player%")) {
                        teleportTo = teleportTo.replace("%Player%",sender.getName());
                    }
                    tpRequest.get(sender).sendMessage(plugin.getPrefix() + teleportTo);
                    tpRequest.remove(sender);
                } else {
                    sender.sendMessage(plugin.getPrefix() + "§cDu hast keine Anfrage bekommen!");
                }
            }
        }
        if (command.getName().equalsIgnoreCase("tpadeny")) {
            if (sender instanceof Player) {
                if (tpRequest.containsKey(sender)) {
                    sender.sendMessage(plugin.getPrefix() + "§aDu hast die Telportier Anfrage abgelehnt!");
                    tpRequest.get(sender)
                            .sendMessage("§6" + sender.getName() + " §chat deine Teleportier Anfrage abgelehnt!");
                    tpRequest.remove(sender);
                }
            }
        }
        if (command.getName().equalsIgnoreCase("tpahere")) {
            if (sender instanceof Player) {
                Player target = Bukkit.getPlayer(args[0]);
                    Player player = (Player) sender;
                    if (target != null) {
                        tpHereRequest.put(target, player);
                        target.sendMessage(plugin.getPrefix() + "§6" + player.getName() + " §aMöchte dich zu ihm Telportieren!");
                        player.sendMessage(plugin.getPrefix() + "§aDieser Spieler möchtest du zu dir Teleportieren §6" + target.getName());
                    } else {
                        player.sendMessage(plugin.getPrefix() + "§cDieser Spieler ist nicht Online!");
                    }
            }
        }
        if (command.getName().equalsIgnoreCase("tpaheredeny")) {

        }
        if (command.getName().equalsIgnoreCase("tpahereaccept")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                if (!tpHereRequest.isEmpty() && tpHereRequest.containsKey(player)) {
                    player.teleport(tpHereRequest.get(player).getLocation());
                    tpHereRequest.remove(player);
                } else {
                    sender.sendMessage(plugin.getPrefix() + "§cDu hast keine Anfrage bekommen!");
                }
            }
        }
        if (command.getName().equalsIgnoreCase("tphereall")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                if (player.hasPermission("essentialsmini.tphereall")) {
                    Bukkit.getOnlinePlayers().forEach(players -> players.teleport(player.getLocation()));
                } else {
                    player.sendMessage(plugin.getPrefix() + plugin.getNOPERMS());
                }
            } else {
                sender.sendMessage(plugin.getPrefix() + plugin.getOnlyPlayer());
            }
        }
        return false;
    }
}

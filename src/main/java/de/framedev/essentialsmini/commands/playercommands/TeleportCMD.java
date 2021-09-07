/*
 * Dies ist ein Plugin von FrameDev
 * Bitte nichts ändern, @Copyright by FrameDev
 */
package de.framedev.essentialsmini.commands.playercommands;

import de.framedev.essentialsmini.main.Main;
import de.framedev.essentialsmini.utils.TextUtils;
import de.framedev.essentialsmini.utils.Variables;
import lombok.NonNull;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

/**
 * @author DHZoc
 */
public class TeleportCMD implements CommandExecutor, Listener {

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
        plugin.getCommands().put("tptoggle", this);
        plugin.getListeners().add(this);
    }

    private final HashMap<Player, Player> tpRequest = new HashMap<>();
    private final HashMap<Player, Player> tpHereRequest = new HashMap<>();
    private final ArrayList<Player> tpToggle = new ArrayList<>();
    private final ArrayList<Player> queue = new ArrayList<>();

    @Override
    public boolean onCommand(@NonNull CommandSender sender, Command command, @NonNull String label, @NonNull String[] args) {
        if (command.getName().equalsIgnoreCase("tptoggle")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                if (player.hasPermission(plugin.getPermissionName() + "tptoggle")) {
                    if (tpToggle.contains(player)) {
                        tpToggle.remove(player);
                        player.sendMessage(plugin.getPrefix() + "§aPlayers can now Teleport to you or send you a Tpa Request!");
                        return true;
                    } else {
                        tpToggle.add(player);
                        player.sendMessage(plugin.getPrefix() + "§6Players §ccan no more Teleporting to you or Send a Tpa Request");
                        return true;
                    }
                } else {
                    player.sendMessage(plugin.getPrefix() + plugin.getNOPERMS());
                }
            } else {
                sender.sendMessage(plugin.getPrefix() + plugin.getOnlyPlayer());
            }
        }
        if (command.getName().equalsIgnoreCase("tpa")) {
            if (args.length == 1) {
                if (sender instanceof Player) {
                    Player target = Bukkit.getPlayer(args[0]);
                    if (target != sender) {
                        if (target != null) {
                            if (!tpToggle.contains(target)) {
                                tpRequest.put(target, (Player) sender);
                                String send = plugin.getCustomMessagesConfig().getString("TpaMessages.TeleportSend");
                                send = send.replace('&', '§');
                                if (send.contains("%Target%")) {
                                    send = send.replace("%Target%", target.getName());
                                }
                                sender.sendMessage(plugin.getPrefix() + send);
                                String got = plugin.getCustomMessagesConfig().getString("TpaMessages.TeleportGot");
                                got = got.replace('&', '§');
                                if (got.contains("%Player%")) {
                                    got = got.replace("%Player%", sender.getName());
                                }
                                target.sendMessage(plugin.getPrefix() + got);
                                BaseComponent baseComponent = new TextComponent();
                                baseComponent.addExtra("§6[Accept]");
                                baseComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tpaaccept " + sender.getName()));
                                baseComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("§aAccept Tpa Request!")));
                                BaseComponent ablehnen = new TextComponent();
                                ablehnen.addExtra("§c[Deny]");
                                ablehnen.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tpadeny " + sender.getName()));
                                ablehnen.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("§cDeny Tpa Request!")));
                                target.spigot().sendMessage(baseComponent);
                                target.spigot().sendMessage(ablehnen);
                            } else if (sender.hasPermission(plugin.getPermissionName() + "tptoggle.bypass")) {
                                tpRequest.put(target, (Player) sender);
                                String send = plugin.getCustomMessagesConfig().getString("TpaMessages.TeleportSend");
                                send = send.replace('&', '§');
                                if (send.contains("%Target%")) {
                                    send = send.replace("%Target%", target.getName());
                                }
                                sender.sendMessage(plugin.getPrefix() + send);
                                String got = plugin.getCustomMessagesConfig().getString("TpaMessages.TeleportGot");
                                got = got.replace('&', '§');
                                if (got.contains("%Player%")) {
                                    got = got.replace("%Player%", sender.getName());
                                }
                                target.sendMessage(plugin.getPrefix() + got);
                                BaseComponent baseComponent = new TextComponent();
                                baseComponent.addExtra("§6[Accept]");
                                baseComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tpaaccept " + sender.getName()));
                                baseComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("§aAccept Tpa Request!")));
                                BaseComponent ablehnen = new TextComponent();
                                ablehnen.addExtra("§c[Deny]");
                                ablehnen.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tpadeny " + sender.getName()));
                                ablehnen.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("§cDeny Tpa Request!")));
                                target.spigot().sendMessage(baseComponent);
                                target.spigot().sendMessage(ablehnen);
                            } else {
                                sender.sendMessage(plugin.getPrefix() + "§cThis Player doesn't accept Teleport!");
                            }
                        } else {
                            sender.sendMessage(plugin.getPrefix() + plugin.getVariables().getPlayerNameNotOnline(args[0]));
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
                Player player = (Player) sender;
                if (tpRequest.containsKey(player)) {
                    if (plugin.getConfig().getBoolean("TeleportInOtherWorld")) {
                        queue.add(tpRequest.get(player));
                        int delay = plugin.getConfig().getInt("TeleportDelay");
                        String tpDelay = plugin.getCustomMessagesConfig().getString("TpaMessages.Delay");
                        tpDelay = new TextUtils().replaceAndToParagraph(tpDelay);
                        tpDelay = new TextUtils().replaceObject(tpDelay, "%Time%", delay + "");
                        tpRequest.get(player).sendMessage(plugin.getPrefix() + tpDelay);
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                if (queue.contains(tpRequest.get(player))) {
                                    tpRequest.get(player).teleport(player);
                                    queue.remove(tpRequest.get(player));
                                }
                            }
                        }.runTaskLater(plugin, 20 * delay);
                    } else {
                        if (player.getWorld().getName().equalsIgnoreCase(tpRequest.get(player).getWorld().getName())) {
                            tpRequest.get(player).teleport(player);
                        } else {
                            player.sendMessage(plugin.getPrefix() + "§aThe Player §6" + tpRequest.get(player).getName() + " §cis not in the same World!");
                            tpRequest.remove(player);
                            return true;
                        }
                    }
                    if (queue.contains(player)) {
                        String targetMessage = plugin.getCustomMessagesConfig().getString("TpaMessages.TargetMessage");
                        targetMessage = targetMessage.replace('&', '§');
                        if (targetMessage.contains("%Target%"))
                            targetMessage = targetMessage.replace("%Target%", tpRequest.get(sender).getName());
                        sender.sendMessage(plugin.getPrefix() + targetMessage);
                        String teleportTo = plugin.getCustomMessagesConfig().getString("TpaMessages.TeleportToPlayer");
                        teleportTo = teleportTo.replace('&', '§');
                        if (teleportTo.contains("%Player%")) {
                            teleportTo = teleportTo.replace("%Player%", sender.getName());
                        }
                        tpRequest.get(sender).sendMessage(plugin.getPrefix() + teleportTo);
                        tpRequest.remove(sender);
                    }
                } else {
                    String message = plugin.getCustomMessagesConfig().getString(Variables.TPMESSAGES + ".NoRequest");
                    message = new TextUtils().replaceAndToParagraph(message);
                    sender.sendMessage(plugin.getPrefix() + message);
                }
            }
        }
        if (command.getName().equalsIgnoreCase("tpadeny")) {
            if (sender instanceof Player) {
                if (tpRequest.containsKey(sender)) {
                    String deny = plugin.getCustomMessagesConfig().getString(Variables.TPMESSAGES + ".TpaDeny");
                    deny = new TextUtils().replaceAndToParagraph(deny);
                    sender.sendMessage(plugin.getPrefix() + deny);
                    String other = plugin.getCustomMessagesConfig().getString(Variables.TPMESSAGES + ".TpaDenyTarget");
                    other = new TextUtils().replaceAndToParagraph(other);
                    other = new TextUtils().replaceObject(other, "%Player%", sender.getName());
                    tpRequest.get(sender)
                            .sendMessage(other);
                    tpRequest.remove(sender);
                }
            }
        }
        if (command.getName().equalsIgnoreCase("tpahere")) {
            if (sender instanceof Player) {
                Player target = Bukkit.getPlayer(args[0]);
                Player player = (Player) sender;
                if (target != null) {
                    if (!tpToggle.contains(target)) {
                        tpHereRequest.put(target, player);
                        String other = plugin.getCustomMessagesConfig().getString(Variables.TPMESSAGES + ".TpaHereTarget");
                        other = new TextUtils().replaceAndToParagraph(other);
                        other = new TextUtils().replaceObject(other, "%Player%", player.getName());
                        target.sendMessage(plugin.getPrefix() + other);
                        String self = plugin.getCustomMessagesConfig().getString(Variables.TPMESSAGES + ".TpaHere");
                        self = new TextUtils().replaceAndToParagraph(self);
                        self = new TextUtils().replaceObject(self, "%Player%", target.getName());
                        player.sendMessage(plugin.getPrefix() + self);
                        BaseComponent baseComponent = new TextComponent();
                        baseComponent.addExtra("§6[Accept]");
                        baseComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tpahereaccept " + sender.getName()));
                        baseComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("§aAccept Tpa Request!")));
                        BaseComponent ablehnen = new TextComponent();
                        ablehnen.addExtra("§c[Deny]");
                        ablehnen.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tpaheredeny " + sender.getName()));
                        ablehnen.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("§cDeny TpaHere Request!")));
                        target.spigot().sendMessage(baseComponent);
                        target.spigot().sendMessage(ablehnen);
                    } else if (player.hasPermission(plugin.getPermissionName() + "tptoggle.bypass")) {
                        tpHereRequest.put(target, player);
                        String other = plugin.getCustomMessagesConfig().getString(Variables.TPMESSAGES + ".TpaHereTarget");
                        other = new TextUtils().replaceAndToParagraph(other);
                        other = new TextUtils().replaceObject(other, "%Player%", player.getName());
                        target.sendMessage(plugin.getPrefix() + other);
                        String self = plugin.getCustomMessagesConfig().getString(Variables.TPMESSAGES + ".TpaHere");
                        self = new TextUtils().replaceAndToParagraph(self);
                        self = new TextUtils().replaceObject(self, "%Player%", target.getName());
                        player.sendMessage(plugin.getPrefix() + self);
                        BaseComponent baseComponent = new TextComponent();
                        baseComponent.addExtra("§6[Accept]");
                        baseComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tpahereaccept " + sender.getName()));
                        baseComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("§aAccept TpaHere Request!")));
                        BaseComponent ablehnen = new TextComponent();
                        ablehnen.addExtra("§c[Deny]");
                        ablehnen.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tpaheredeny " + sender.getName()));
                        ablehnen.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("§cDeny TpaHere Request!")));
                        target.spigot().sendMessage(baseComponent);
                        target.spigot().sendMessage(ablehnen);
                    } else {
                        sender.sendMessage(plugin.getPrefix() + "§cThis Player doesn't accept Teleport!");
                    }
                } else {
                    player.sendMessage(plugin.getPrefix() + plugin.getVariables().getPlayerNameNotOnline(args[0]));
                }
            }
        }
        if (command.getName().equalsIgnoreCase("tpaheredeny")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                if (!tpHereRequest.isEmpty() && tpHereRequest.containsKey(player)) {
                    String deny = plugin.getCustomMessagesConfig().getString("TpaMessages.TpaHereDeny");
                    deny = new TextUtils().replaceAndToParagraph(deny);
                    player.sendMessage(plugin.getPrefix() + deny);
                    String other = plugin.getCustomMessagesConfig().getString("TpaMessages.TpaHereDenyTarget");
                    other = new TextUtils().replaceAndToParagraph(other);
                    other = new TextUtils().replaceObject(other, "%Player%", player.getName());
                    tpHereRequest.get(player).sendMessage(plugin.getPrefix() + other);
                    tpHereRequest.remove(player);
                } else {
                    String message = plugin.getCustomMessagesConfig().getString(Variables.TPMESSAGES + ".NoRequest");
                    message = new TextUtils().replaceAndToParagraph(message);
                    sender.sendMessage(plugin.getPrefix() + message);
                }
            }
        }
        if (command.getName().equalsIgnoreCase("tpahereaccept")) {
            if (sender instanceof Player) {
                final Player[] player = new Player[1];
                player[0] = (Player) sender;
                if (!tpHereRequest.isEmpty() && tpHereRequest.containsKey(player[0])) {
                    if (plugin.getConfig().getBoolean("TeleportInOtherWorld")) {
                        queue.add(player[0]);
                        int delay = plugin.getConfig().getInt("TeleportDelay");
                        String tpDelay = plugin.getCustomMessagesConfig().getString("TpaMessages.Delay");
                        tpDelay = new TextUtils().replaceAndToParagraph(tpDelay);
                        tpDelay = new TextUtils().replaceObject(tpDelay, "%Time%", delay + "");
                        player[0].sendMessage(plugin.getPrefix() + tpDelay);
                        Player target = tpHereRequest.get(player[0]);
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                if (queue.contains(player[0])) {
                                    player[0].teleport(target.getLocation());
                                    queue.remove(player[0]);
                                }
                            }
                        }.runTaskLater(plugin, 20 * 3);
                    } else {
                        if (tpHereRequest.get(player[0]).getWorld().getName().equalsIgnoreCase(player[0].getWorld().getName())) {
                            player[0].teleport(tpHereRequest.get(player[0]).getLocation());
                        } else {
                            player[0].sendMessage(plugin.getPrefix() + "§aThe Player §6" + tpHereRequest.get(player[0]).getName() + " §cis not in the same World!");
                            tpHereRequest.remove(player[0]);
                            return true;
                        }
                    }
                    tpHereRequest.remove(player[0]);
                } else {
                    String message = plugin.getCustomMessagesConfig().getString(Variables.TPMESSAGES + ".NoRequest");
                    message = new TextUtils().replaceAndToParagraph(message);
                    sender.sendMessage(plugin.getPrefix() + message);
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

    @EventHandler
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        if (!plugin.getConfig().getBoolean("TeleportInOtherWorld")) {
            Player player = event.getPlayer();
            for (Player player1 : Bukkit.getOnlinePlayers()) {
                if (Objects.equals(event.getTo(), player1.getLocation())) {
                    if (!player1.getWorld().getName().equalsIgnoreCase(player.getWorld().getName())) {
                        player.sendMessage(plugin.getPrefix() + "§6" + player1.getName() + " §cis not in the same World!");
                        event.setCancelled(true);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        if (queue.contains(event.getPlayer())) {
            int movX = event.getFrom().getBlockX() - event.getTo().getBlockX();
            int movZ = event.getFrom().getBlockZ() - event.getTo().getBlockZ();
            if (Math.abs(movX) > 0 || Math.abs(movZ) > 0) {
                queue.remove(event.getPlayer());
                String message = plugin.getCustomMessagesConfig().getString(Variables.TPMESSAGES + ".Denied");
                message = new TextUtils().replaceAndToParagraph(message);
                event.getPlayer().sendMessage(plugin.getPrefix() + message);
            }
        }
    }
}

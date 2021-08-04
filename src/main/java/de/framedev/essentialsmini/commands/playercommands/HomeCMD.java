/**
 * Dies ist ein Plugin von FrameDev
 * Bitte nichts §ndern, @Copyright by FrameDev
 */
package de.framedev.essentialsmini.commands.playercommands;

import de.framedev.essentialsmini.main.Main;
import de.framedev.essentialsmini.managers.CommandBase;
import de.framedev.essentialsmini.managers.LocationsManager;
import de.framedev.essentialsmini.utils.ReplaceCharConfig;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author DHZoc
 */
public class HomeCMD extends CommandBase implements CommandExecutor, TabCompleter {

    private final Main plugin;

    public HomeCMD(Main plugin) {
        super(plugin);
        this.plugin = plugin;
        if (plugin.isHomeTP()) {
            setup("home", this);
            setup("sethome", this);
            setup("delhome", this);
            setup("getlocationfromstring", this);
            setup("delotherhome", this);
            plugin.getTabCompleters().put("home", this);
            plugin.getTabCompleters().put("delhome", this);
            this.locationsManager = new LocationsManager();
        }
    }

    private LocationsManager locationsManager;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            if (command.getName().equalsIgnoreCase("sethome")) {
                if (sender instanceof Player) {
                    if(sender.hasPermission("essentialsmini.sethome")) {
                        new LocationsManager(sender.getName() + ".home.home").setLocation(((Player) sender).getLocation());
                        String homeSet = plugin.getCustomMessagesConfig().getString("HomeSet");
                        if (homeSet.contains("&"))
                            homeSet = homeSet.replace('&', '§');
                        sender.sendMessage(plugin.getPrefix() + homeSet);
                    } else {
                        sender.sendMessage(plugin.getPrefix() + plugin.getNOPERMS());
                    }
                } else {
                    sender.sendMessage(plugin.getPrefix() + plugin.getOnlyPlayer());
                }
            }
            if (command.getName().equalsIgnoreCase("home")) {
                try {
                    if (sender instanceof Player) {
                        if (sender.hasPermission(plugin.getPermissionName() + "home")) {
                            ((Player) sender).teleport(new LocationsManager(sender.getName() + ".home.home").getLocation());
                            String homeTeleport = plugin.getCustomMessagesConfig().getString("HomeTeleport");
                            if (homeTeleport.contains("&"))
                                homeTeleport = homeTeleport.replace('&', '§');
                            sender.sendMessage(plugin.getPrefix() + homeTeleport);
                            homes.clear();
                        } else {
                            sender.sendMessage(plugin.getPrefix() + plugin.getNOPERMS());
                        }

                    } else {
                        sender.sendMessage(plugin.getPrefix() + plugin.getOnlyPlayer());
                    }
                } catch (IllegalArgumentException x) {
                    String homeExist = plugin.getCustomMessagesConfig().getString("HomeNotExist");
                    homeExist = ReplaceCharConfig.replaceParagraph(homeExist);
                    sender.sendMessage(plugin.getPrefix() + homeExist);
                    sender.sendMessage(plugin.getPrefix() + "§aHome setzen?");
                    BaseComponent baseComponent = new TextComponent();
                    baseComponent.addExtra("§6[Yes]");
                    baseComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/sethome"));
                    baseComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("§aSet Home!")));
                    sender.spigot().sendMessage(baseComponent);
                }
            }
            if (command.getName().equalsIgnoreCase("delhome")) {
                try {
                    if (sender instanceof Player) {
                        this.locationsManager = new LocationsManager(sender.getName() + ".home.home");
                        locationsManager.getCfg().set(sender.getName() + ".home.home", " ");
                        locationsManager.saveCfg();
                        String homeDeleted = plugin.getCustomMessagesConfig().getString("HomeDelete");
                        if (homeDeleted.contains("&"))
                            homeDeleted = ReplaceCharConfig.replaceParagraph(homeDeleted);
                        sender.sendMessage(plugin.getPrefix() + homeDeleted);
                        homes.clear();
                    } else {
                        sender.sendMessage(plugin.getPrefix() + plugin.getOnlyPlayer());
                    }
                } catch (IllegalArgumentException ex) {
                    String homeExist = plugin.getCustomMessagesConfig().getString("HomeNotExist");
                    homeExist = ReplaceCharConfig.replaceParagraph(homeExist);
                    sender.sendMessage(plugin.getPrefix() + homeExist);
                }
            }
        } else if (args.length == 1) {
            if (command.getName().equalsIgnoreCase("sethome")) {
                if (sender instanceof Player) {
                    if(sender.hasPermission("essentialsmini.sethome")) {
                        Map<String, String> limitedHomepermissions = plugin.getLimitedHomesPermission();
                        String name = args[0].toLowerCase();
                        if (plugin.getConfig().getBoolean("Limited")) {
                            ArrayList<String> limited = new ArrayList<>();
                            if (sender.hasPermission(new Permission(limitedHomepermissions.get("Default"), PermissionDefault.OP))) {
                                if (!sender.isOp()) {
                                    if (new LocationsManager().getCfg().contains(sender.getName() + ".home")) {
                                        ConfigurationSection cs = new LocationsManager().getCfg().getConfigurationSection(sender.getName() + ".home");
                                        if (cs != null) {
                                            for (String s : cs.getKeys(false)) {
                                                if (new LocationsManager().getCfg().get(sender.getName() + ".home." + s) != null) {
                                                    if (!s.equalsIgnoreCase("home")) {
                                                        if (!new LocationsManager().getCfg().get(sender.getName() + ".home." + s).equals(" ")) {
                                                            limited.add(s);
                                                        }
                                                    }
                                                }
                                            }
                                            if (limited.size() < plugin.getConfig().getInt("LimitedHomes.Default")) {
                                                if (!new LocationsManager(sender.getName() + ".home." + name).getCfg().contains(sender.getName() + ".home." + name) || new LocationsManager().getCfg().getString(sender.getName() + ".home." + name).equalsIgnoreCase(" ")) {
                                                    new LocationsManager(sender.getName() + ".home." + name)
                                                            .setLocation(((Player) sender).getLocation());
                                                    String homeSet = plugin.getCustomMessagesConfig().getString("HomeSetOther");
                                                    homeSet = ReplaceCharConfig.replaceParagraph(homeSet);
                                                    homeSet = ReplaceCharConfig.replaceObjectWithData(homeSet, "%Name%", name);
                                                    sender.sendMessage(plugin.getPrefix() + homeSet);
                                                    homes.clear();
                                                    limited.clear();
                                                } else {
                                                    String exist = plugin.getCustomMessagesConfig().getString("HomeExist");
                                                    exist = ReplaceCharConfig.replaceParagraph(exist);
                                                    sender.sendMessage(plugin.getPrefix() + exist);
                                                    limited.clear();
                                                    homes.clear();
                                                }
                                            } else if (limited.size() == plugin.getConfig().getInt("LimitedHomes.Default") || limited.size() > plugin.getConfig().getInt("LimitedHomes.Default")) {
                                                String notSet = plugin.getCustomMessagesConfig().getString("NoMoreHomesSet");
                                                notSet = ReplaceCharConfig.replaceParagraph(notSet);
                                                sender.sendMessage(plugin.getPrefix() + notSet);
                                                limited.clear();
                                                homes.clear();
                                            }
                                        } else {
                                            new LocationsManager(sender.getName() + ".home." + name)
                                                    .setLocation(((Player) sender).getLocation());
                                            String homeSet = plugin.getCustomMessagesConfig().getString("HomeSetOther");
                                            homeSet = ReplaceCharConfig.replaceParagraph(homeSet);
                                            homeSet = ReplaceCharConfig.replaceObjectWithData(homeSet, "%Name%", name);
                                            sender.sendMessage(plugin.getPrefix() + homeSet);
                                            homes.clear();
                                        }
                                    } else {
                                        new LocationsManager(sender.getName() + ".home." + name)
                                                .setLocation(((Player) sender).getLocation());
                                        String homeSet = plugin.getCustomMessagesConfig().getString("HomeSetOther");
                                        homeSet = ReplaceCharConfig.replaceParagraph(homeSet);
                                        homeSet = ReplaceCharConfig.replaceObjectWithData(homeSet, "%Name%", name);
                                        sender.sendMessage(plugin.getPrefix() + homeSet);
                                        homes.clear();
                                    }
                                } else {
                                    if (new LocationsManager().getCfg().contains(sender.getName() + ".home")) {
                                        ConfigurationSection cs = new LocationsManager().getCfg().getConfigurationSection(sender.getName() + ".home");
                                        if (cs != null) {
                                            for (String s : cs.getKeys(false)) {
                                                if (new LocationsManager().getCfg().get(sender.getName() + ".home." + s) != null) {
                                                    if (!new LocationsManager().getCfg().get(sender.getName() + ".home." + s).equals(" ")) {
                                                        if (!s.equalsIgnoreCase("home")) {
                                                            limited.add(s);
                                                        }
                                                    }
                                                }
                                            }
                                            if (limited.size() < plugin.getConfig().getInt("LimitedHomes.Admin")) {
                                                if (!new LocationsManager(sender.getName() + ".home." + name).getCfg().contains(sender.getName() + ".home." + name) || new LocationsManager().getCfg().getString(sender.getName() + ".home." + name).equalsIgnoreCase(" ")) {
                                                    new LocationsManager(sender.getName() + ".home." + name)
                                                            .setLocation(((Player) sender).getLocation());
                                                    String homeSet = plugin.getCustomMessagesConfig().getString("HomeSetOther");
                                                    homeSet = ReplaceCharConfig.replaceParagraph(homeSet);
                                                    homeSet = ReplaceCharConfig.replaceObjectWithData(homeSet, "%Name%", name);
                                                    sender.sendMessage(plugin.getPrefix() + homeSet);
                                                    homes.clear();
                                                    limited.clear();
                                                } else {
                                                    String exist = plugin.getCustomMessagesConfig().getString("HomeExist");
                                                    exist = ReplaceCharConfig.replaceParagraph(exist);
                                                    sender.sendMessage(plugin.getPrefix() + exist);
                                                    homes.clear();
                                                    limited.clear();
                                                }
                                            } else if (limited.size() == plugin.getConfig().getInt("LimitedHomes.Admin") || limited.size() >= plugin.getConfig().getInt("LimitedHomes.Admin")) {
                                                String notSet = plugin.getCustomMessagesConfig().getString("NoMoreHomesSet");
                                                notSet = ReplaceCharConfig.replaceParagraph(notSet);
                                                sender.sendMessage(plugin.getPrefix() + notSet);
                                                homes.clear();
                                                limited.clear();
                                            }
                                        } else {
                                            new LocationsManager(sender.getName() + ".home." + name)
                                                    .setLocation(((Player) sender).getLocation());
                                            String homeSet = plugin.getCustomMessagesConfig().getString("HomeSetOther");
                                            homeSet = ReplaceCharConfig.replaceParagraph(homeSet);
                                            homeSet = ReplaceCharConfig.replaceObjectWithData(homeSet, "%Name%", name);
                                            sender.sendMessage(plugin.getPrefix() + homeSet);
                                            homes.clear();
                                        }
                                    } else {
                                        new LocationsManager(sender.getName() + ".home." + name)
                                                .setLocation(((Player) sender).getLocation());
                                        String homeSet = plugin.getCustomMessagesConfig().getString("HomeSetOther");
                                        homeSet = ReplaceCharConfig.replaceParagraph(homeSet);
                                        homeSet = ReplaceCharConfig.replaceObjectWithData(homeSet, "%Name%", name);
                                        sender.sendMessage(plugin.getPrefix() + homeSet);
                                        homes.clear();
                                    }
                                }
                            } else if (sender.hasPermission(new Permission(limitedHomepermissions.get("Medium"), PermissionDefault.OP))) {
                                if (!sender.isOp()) {
                                    if (new LocationsManager().getCfg().contains(sender.getName() + ".home")) {
                                        ConfigurationSection cs = new LocationsManager().getCfg().getConfigurationSection(sender.getName() + ".home");
                                        if (cs != null) {
                                            for (String s : cs.getKeys(false)) {
                                                if (new LocationsManager().getCfg().get(sender.getName() + ".home." + s) != null) {
                                                    if (!new LocationsManager().getCfg().get(sender.getName() + ".home." + s).equals(" ")) {
                                                        if (!s.equalsIgnoreCase("home")) {
                                                            limited.add(s);
                                                        }
                                                    }
                                                }
                                            }
                                            if (limited.size() < plugin.getConfig().getInt("LimitedHomes.Medium")) {
                                                if (!new LocationsManager(sender.getName() + ".home." + name).getCfg().contains(sender.getName() + ".home." + name) || new LocationsManager().getCfg().getString(sender.getName() + ".home." + name).equalsIgnoreCase(" ")) {
                                                    new LocationsManager(sender.getName() + ".home." + name)
                                                            .setLocation(((Player) sender).getLocation());
                                                    String homeSet = plugin.getCustomMessagesConfig().getString("HomeSetOther");
                                                    homeSet = ReplaceCharConfig.replaceParagraph(homeSet);
                                                    homeSet = ReplaceCharConfig.replaceObjectWithData(homeSet, "%Name%", name);
                                                    sender.sendMessage(plugin.getPrefix() + homeSet);
                                                    homes.clear();
                                                    limited.clear();
                                                } else {
                                                    String exist = plugin.getCustomMessagesConfig().getString("HomeExist");
                                                    exist = ReplaceCharConfig.replaceParagraph(exist);
                                                    sender.sendMessage(plugin.getPrefix() + exist);
                                                    limited.clear();
                                                    homes.clear();
                                                }
                                            } else if (limited.size() == plugin.getConfig().getInt("LimitedHomes.Medium") || limited.size() >= plugin.getConfig().getInt("LimitedHomes.Medium")) {
                                                String notSet = plugin.getCustomMessagesConfig().getString("NoMoreHomesSet");
                                                notSet = ReplaceCharConfig.replaceParagraph(notSet);
                                                sender.sendMessage(plugin.getPrefix() + notSet);
                                                limited.clear();
                                                homes.clear();
                                            }
                                        } else {
                                            new LocationsManager(sender.getName() + ".home." + name)
                                                    .setLocation(((Player) sender).getLocation());
                                            String homeSet = plugin.getCustomMessagesConfig().getString("HomeSetOther");
                                            homeSet = ReplaceCharConfig.replaceParagraph(homeSet);
                                            homeSet = ReplaceCharConfig.replaceObjectWithData(homeSet, "%Name%", name);
                                            sender.sendMessage(plugin.getPrefix() + homeSet);
                                            homes.clear();
                                        }
                                    } else {
                                        new LocationsManager(sender.getName() + ".home." + name)
                                                .setLocation(((Player) sender).getLocation());
                                        String homeSet = plugin.getCustomMessagesConfig().getString("HomeSetOther");
                                        homeSet = ReplaceCharConfig.replaceParagraph(homeSet);
                                        homeSet = ReplaceCharConfig.replaceObjectWithData(homeSet, "%Name%", name);
                                        sender.sendMessage(plugin.getPrefix() + homeSet);
                                        homes.clear();
                                    }
                                } else {
                                    if (new LocationsManager().getCfg().contains(sender.getName() + ".home")) {
                                        ConfigurationSection cs = new LocationsManager().getCfg().getConfigurationSection(sender.getName() + ".home");
                                        if (cs != null) {
                                            for (String s : cs.getKeys(false)) {
                                                if (new LocationsManager().getCfg().get(sender.getName() + ".home." + s) != null) {
                                                    if (!new LocationsManager().getCfg().get(sender.getName() + ".home." + s).equals(" ")) {
                                                        if (!s.equalsIgnoreCase("home")) {
                                                            limited.add(s);
                                                        }
                                                    }
                                                }
                                            }
                                            if (limited.size() < plugin.getConfig().getInt("LimitedHomes.Admin")) {
                                                if (!new LocationsManager(sender.getName() + ".home." + name).getCfg().contains(sender.getName() + ".home." + name) || new LocationsManager().getCfg().getString(sender.getName() + ".home." + name).equalsIgnoreCase(" ")) {
                                                    new LocationsManager(sender.getName() + ".home." + name)
                                                            .setLocation(((Player) sender).getLocation());
                                                    String homeSet = plugin.getCustomMessagesConfig().getString("HomeSetOther");
                                                    homeSet = ReplaceCharConfig.replaceParagraph(homeSet);
                                                    homeSet = ReplaceCharConfig.replaceObjectWithData(homeSet, "%Name%", name);
                                                    sender.sendMessage(plugin.getPrefix() + homeSet);
                                                    homes.clear();
                                                    limited.clear();
                                                } else {
                                                    String exist = plugin.getCustomMessagesConfig().getString("HomeExist");
                                                    exist = ReplaceCharConfig.replaceParagraph(exist);
                                                    sender.sendMessage(plugin.getPrefix() + exist);
                                                    homes.clear();
                                                    limited.clear();
                                                }
                                            } else if (limited.size() == plugin.getConfig().getInt("LimitedHomes.Admin") || limited.size() >= plugin.getConfig().getInt("LimitedHomes.Admin")) {
                                                String notSet = plugin.getCustomMessagesConfig().getString("NoMoreHomesSet");
                                                notSet = ReplaceCharConfig.replaceParagraph(notSet);
                                                sender.sendMessage(plugin.getPrefix() + notSet);
                                                homes.clear();
                                                limited.clear();
                                            }
                                        } else {
                                            new LocationsManager(sender.getName() + ".home." + name)
                                                    .setLocation(((Player) sender).getLocation());
                                            String homeSet = plugin.getCustomMessagesConfig().getString("HomeSetOther");
                                            homeSet = ReplaceCharConfig.replaceParagraph(homeSet);
                                            homeSet = ReplaceCharConfig.replaceObjectWithData(homeSet, "%Name%", name);
                                            sender.sendMessage(plugin.getPrefix() + homeSet);
                                            homes.clear();
                                        }
                                    } else {
                                        new LocationsManager(sender.getName() + ".home." + name)
                                                .setLocation(((Player) sender).getLocation());
                                        String homeSet = plugin.getCustomMessagesConfig().getString("HomeSetOther");
                                        homeSet = ReplaceCharConfig.replaceParagraph(homeSet);
                                        homeSet = ReplaceCharConfig.replaceObjectWithData(homeSet, "%Name%", name);
                                        sender.sendMessage(plugin.getPrefix() + homeSet);
                                        homes.clear();
                                    }
                                }
                            } else if (sender.hasPermission(new Permission(limitedHomepermissions.get("Admin"), PermissionDefault.OP))) {
                                if (new LocationsManager().getCfg().contains(sender.getName() + ".home")) {
                                    ConfigurationSection cs = new LocationsManager().getCfg().getConfigurationSection(sender.getName() + ".home");
                                    if (cs != null) {
                                        for (String s : cs.getKeys(false)) {
                                            if (new LocationsManager().getCfg().get(sender.getName() + ".home." + s) != null) {
                                                if (!new LocationsManager().getCfg().get(sender.getName() + ".home." + s).equals(" ")) {
                                                    if (!s.equalsIgnoreCase("home")) {
                                                        limited.add(s);
                                                    }
                                                }
                                            }
                                        }
                                        if (limited.size() < plugin.getConfig().getInt("LimitedHomes.Admin")) {
                                            if (!new LocationsManager(sender.getName() + ".home." + name).getCfg().contains(sender.getName() + ".home." + name) || new LocationsManager().getCfg().getString(sender.getName() + ".home." + name).equalsIgnoreCase(" ")) {
                                                new LocationsManager(sender.getName() + ".home." + name)
                                                        .setLocation(((Player) sender).getLocation());
                                                String homeSet = plugin.getCustomMessagesConfig().getString("HomeSetOther");
                                                homeSet = ReplaceCharConfig.replaceParagraph(homeSet);
                                                homeSet = ReplaceCharConfig.replaceObjectWithData(homeSet, "%Name%", name);
                                                sender.sendMessage(plugin.getPrefix() + homeSet);
                                                homes.clear();
                                                limited.clear();
                                            } else {
                                                String exist = plugin.getCustomMessagesConfig().getString("HomeExist");
                                                exist = ReplaceCharConfig.replaceParagraph(exist);
                                                sender.sendMessage(plugin.getPrefix() + exist);
                                                homes.clear();
                                                limited.clear();
                                            }
                                        } else if (limited.size() == plugin.getConfig().getInt("LimitedHomes.Admin") || limited.size() >= plugin.getConfig().getInt("LimitedHomes.Admin")) {
                                            String notSet = plugin.getCustomMessagesConfig().getString("NoMoreHomesSet");
                                            notSet = ReplaceCharConfig.replaceParagraph(notSet);
                                            sender.sendMessage(plugin.getPrefix() + notSet);
                                            homes.clear();
                                            limited.clear();
                                        }
                                    } else {
                                        new LocationsManager(sender.getName() + ".home." + name)
                                                .setLocation(((Player) sender).getLocation());
                                        String homeSet = plugin.getCustomMessagesConfig().getString("HomeSetOther");
                                        homeSet = ReplaceCharConfig.replaceParagraph(homeSet);
                                        homeSet = ReplaceCharConfig.replaceObjectWithData(homeSet, "%Name%", name);
                                        sender.sendMessage(plugin.getPrefix() + homeSet);
                                        homes.clear();
                                    }
                                } else {
                                    new LocationsManager(sender.getName() + ".home." + name)
                                            .setLocation(((Player) sender).getLocation());
                                    String homeSet = plugin.getCustomMessagesConfig().getString("HomeSetOther");
                                    homeSet = ReplaceCharConfig.replaceParagraph(homeSet);
                                    homeSet = ReplaceCharConfig.replaceObjectWithData(homeSet, "%Name%", name);
                                    sender.sendMessage(plugin.getPrefix() + homeSet);
                                    homes.clear();
                                }
                            }
                        } else {
                            if (!new LocationsManager(sender.getName() + ".home." + name).getCfg().contains(sender.getName() + ".home." + name) || new LocationsManager().getCfg().getString(sender.getName() + ".home." + name).equalsIgnoreCase(" ")) {
                                new LocationsManager(sender.getName() + ".home." + name)
                                        .setLocation(((Player) sender).getLocation());
                                String homeSet = plugin.getCustomMessagesConfig().getString("HomeSetOther");
                                homeSet = ReplaceCharConfig.replaceParagraph(homeSet);
                                homeSet = ReplaceCharConfig.replaceObjectWithData(homeSet, "%Name%", name);
                                sender.sendMessage(plugin.getPrefix() + homeSet);
                                homes.clear();
                            } else {
                                String exist = plugin.getCustomMessagesConfig().getString("HomeExist");
                                exist = ReplaceCharConfig.replaceParagraph(exist);
                                sender.sendMessage(plugin.getPrefix() + exist);
                            }
                        }
                    } else {
                        sender.sendMessage(plugin.getPrefix() + plugin.getNOPERMS());
                    }
                } else {
                    sender.sendMessage(plugin.getPrefix() + plugin.getOnlyPlayer());
                }
            }
            if (command.getName().equalsIgnoreCase("home")) {
                if (sender instanceof Player) {
                    String name = args[0].toLowerCase();
                    try {
                        if (sender.hasPermission(plugin.getPermissionName() + "home")) {
                            ((Player) sender)
                                    .teleport(new LocationsManager(sender.getName() + ".home." + name).getLocation());
                            String homeTeleport = plugin.getCustomMessagesConfig().getString("HomeTeleportOther");
                            homeTeleport = ReplaceCharConfig.replaceParagraph(homeTeleport);
                            homeTeleport = ReplaceCharConfig.replaceObjectWithData(homeTeleport, "%Name%", name);
                            sender.sendMessage(plugin.getPrefix() + homeTeleport);
                            homes.clear();
                        } else {
                            sender.sendMessage(plugin.getPrefix() + plugin.getNOPERMS());
                        }
                    } catch (IllegalArgumentException ex) {
                        String homeExist = plugin.getCustomMessagesConfig().getString("HomeNotExist");
                        homeExist = ReplaceCharConfig.replaceParagraph(homeExist);
                        sender.sendMessage(plugin.getPrefix() + homeExist);
                        sender.sendMessage(plugin.getPrefix() + "§aHome setzen?");
                        BaseComponent baseComponent = new TextComponent();
                        baseComponent.addExtra("§6[Yes]");
                        baseComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/sethome " + name));
                        baseComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("§aSet " + name + " Home!")));
                        sender.spigot().sendMessage(baseComponent);
                    }
                } else {
                    sender.sendMessage(plugin.getPrefix() + plugin.getOnlyPlayer());
                }
            }
            if (command.getName().equalsIgnoreCase("delhome")) {
                if (sender instanceof Player) {
                    String name = args[0].toLowerCase();
                    if (plugin.getConfig().getBoolean("JsonFormat")) {
                        if (new LocationsManager().getLocation(sender.getName() + ".home." + name) != null && new LocationsManager().existsHome(sender.getName() + ".home." + name)) {
                            new LocationsManager().removeLocation(sender.getName() + ".home." + name);
                            String homeDelete = plugin.getCustomMessagesConfig().getString("HomeDeleteOther");
                            homeDelete = ReplaceCharConfig.replaceParagraph(homeDelete);
                            homeDelete = ReplaceCharConfig.replaceObjectWithData(homeDelete, "%Name%", name);
                            sender.sendMessage(plugin.getPrefix() + homeDelete);
                            homes.clear();
                        } else {
                            String homeExist = plugin.getCustomMessagesConfig().getString("HomeNotExist");
                            homeExist = ReplaceCharConfig.replaceParagraph(homeExist);
                            sender.sendMessage(plugin.getPrefix() + homeExist);
                        }
                    } else {
                        try {
                            this.locationsManager = new LocationsManager(sender.getName() + ".home." + name);
                            locationsManager.getCfg().set(sender.getName() + ".home." + name, " ");
                            locationsManager.saveCfg();
                            String homeDelete = plugin.getCustomMessagesConfig().getString("HomeDeleteOther");
                            homeDelete = ReplaceCharConfig.replaceParagraph(homeDelete);
                            homeDelete = ReplaceCharConfig.replaceObjectWithData(homeDelete, "%Name%", name);
                            sender.sendMessage(plugin.getPrefix() + homeDelete);
                            homes.clear();
                        } catch (IllegalArgumentException ex) {
                            String homeExist = plugin.getCustomMessagesConfig().getString("HomeNotExist");
                            homeExist = ReplaceCharConfig.replaceParagraph(homeExist);
                            sender.sendMessage(plugin.getPrefix() + homeExist);
                        }
                    }
                } else {
                    sender.sendMessage(plugin.getPrefix() + plugin.getOnlyPlayer());
                }
            }
            if (command.getName().equalsIgnoreCase("getlocationfromstring")) {
                if (sender instanceof Player) {
                    if (sender.hasPermission("essentialsmini.getlocation")) {
                        String loc = args[0];
                        ((Player) sender).teleport(new LocationsManager().locationFromString(loc));
                    } else {
                        sender.sendMessage(plugin.getPrefix() + "§cKeine Permissions!");
                    }
                } else {
                    sender.sendMessage(plugin.getPrefix() + plugin.getOnlyPlayer());
                }
            }
        } else if (args.length == 2) {
            if (command.getName().equalsIgnoreCase("delotherhome")) {
                if (sender.hasPermission(new Permission("essentialsmini.deletehome.others", PermissionDefault.OP))) {
                    OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);
                    String name = args[0];
                    if (plugin.getConfig().getBoolean("JsonFormat")) {
                        if (new LocationsManager().getLocation(target.getName() + ".home." + name) != null && new LocationsManager().existsHome(target.getName() + ".home." + name)) {
                            new LocationsManager().removeLocation(target.getName() + ".home." + name);
                            sender.sendMessage(plugin.getPrefix() + "§aDen Home von §6" + target.getName() + " §amit dem Namen §6" + name + " §awurde §centfernt!");
                            homes.clear();
                        } else {
                            sender.sendMessage(plugin.getPrefix() + "§cDieses Home wurde noch nicht gesetzt!");
                            sender.sendMessage(plugin.getPrefix() + "§cOder wurde schon entfernt!");
                        }
                    } else {
                        try {
                            if (!new LocationsManager().getCfg().getString(target.getName() + ".home." + name).equalsIgnoreCase(" ")) {
                                this.locationsManager = new LocationsManager(target.getName() + ".home." + name);
                                locationsManager.getCfg().set(target.getName() + ".home." + name, " ");
                                locationsManager.saveCfg();
                                sender.sendMessage(plugin.getPrefix() + "§aDen Home von §6" + target.getName() + " §amit dem Namen §6" + name + " §awurde §centfernt!");
                                homes.clear();
                            }
                        } catch (IllegalArgumentException ex) {
                            sender.sendMessage(plugin.getPrefix() + "§cDieses Home wurde noch nicht gesetzt!");
                            sender.sendMessage(plugin.getPrefix() + "§cOder wurde schon entfernt!");
                        }
                    }
                } else {
                    sender.sendMessage(plugin.getPrefix() + plugin.getNOPERMS());
                }
            }
        } else {
            sender.sendMessage(plugin.getPrefix() + plugin.getWrongArgs("/sethome §coder §6/sethome <Name>"));
            sender.sendMessage(plugin.getPrefix() + plugin.getWrongArgs("/home §coder §6/home <Name>"));
        }
        return false;
    }

    ArrayList<String> homes = new ArrayList<>();

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("home")) {
            ArrayList<String> homes = new ArrayList<>();
            if (args.length == 1) {
                /*if (plugin.getConfig().getBoolean("JsonFormat")) {
                    List<LocationsManager.LocationJson> locs = new LocationsManager().getLocations();
                    ArrayList<String> empty = new ArrayList<>();
                    locs.forEach(loc -> {
                        empty.add(loc.getLocationName());
                    });

                    for (String s : empty) {
                        if (s.toLowerCase().startsWith(args[0].toLowerCase())) {
                            s = s.replace(sender.getName() + ".home.","");
                            homes.add(s);
                        }
                    }
                    Collections.sort(homes);
                    return homes;
                } else {*/
                if (new LocationsManager().getCfg().contains(sender.getName() + ".home")) {
                    ConfigurationSection cs = new LocationsManager().getCfg().getConfigurationSection(sender.getName() + ".home");
                    if (cs != null) {
                        for (String s : cs.getKeys(false)) {
                            if (new LocationsManager().getCfg().get(sender.getName() + ".home." + s) != null) {
                                if (!new LocationsManager().getCfg().get(sender.getName() + ".home." + s).equals(" ")) {
                                    if (s.toLowerCase().startsWith(args[0].toLowerCase())) {
                                        if (!s.equalsIgnoreCase("home")) {
                                            homes.add(s);
                                        }
                                    }
                                }
                            }
                        }
                        Collections.sort(homes);
                        return homes;
                    }
                }
            }
        }
        if (command.getName().equalsIgnoreCase("delhome")) {
            if (args.length == 1) {
                /*if (plugin.getConfig().getBoolean("JsonFormat")) {
                    List<LocationsManager.LocationJson> locs = new LocationsManager().getLocations();
                    ArrayList<String> empty = new ArrayList<>();
                    locs.forEach(loc -> {
                        empty.add(loc.getLocationName());
                    });

                    for (String s : empty) {
                        if (s.toLowerCase().startsWith(args[0].toLowerCase())) {
                            homes.add(s);
                        }
                    }
                    Collections.sort(homes);
                    return homes;
                } else {*/
                if (new LocationsManager().getCfg().contains(sender.getName() + ".home")) {
                    ConfigurationSection cs = new LocationsManager().getCfg().getConfigurationSection(sender.getName() + ".home");
                    if (cs != null) {
                        for (String s : cs.getKeys(false)) {
                            if (new LocationsManager().getCfg().get(sender.getName() + ".home." + s) != null) {
                                if (!new LocationsManager().getCfg().get(sender.getName() + ".home." + s).equals(" ")) {
                                    if (s.toLowerCase().startsWith(args[0].toLowerCase())) {
                                        homes.add(s);
                                    }
                                }
                            }
                        }
                        Collections.sort(homes);
                        return homes;
                    }
                }
            }
        }
        return null;
    }
}
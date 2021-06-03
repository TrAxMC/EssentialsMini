package de.framedev.essentialsmini.commands.playercommands;

import de.framedev.essentialsmini.main.Main;
import de.framedev.essentialsmini.managers.BanMuteManager;
import de.framedev.essentialsmini.managers.CommandBase;
import de.framedev.essentialsmini.utils.DateUnit;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * This Plugin was Created by FrameDev
 * Package : de.framedev.essentialsmini.commands.playercommands
 * ClassName TempBanCMD
 * Date: 15.05.21
 * Project: EssentialsMini
 * Copyrighted by FrameDev
 */

public class TempBanCMD extends CommandBase {
    public TempBanCMD(Main plugin) {
        super(plugin, "tempban");
        setup(this);
        setupTabCompleter(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("tempban")) {
            if (sender.hasPermission(getPlugin().getPermissionName() + "tempban")) {
                if (args.length == 4) {
                    OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
                    Ban grund = Ban.valueOf(args[1].toUpperCase());
                    DateUnit unit = DateUnit.valueOf(args[3].toUpperCase());
                    long value = Long.parseLong(args[2]);
                    long current = System.currentTimeMillis();
                    long millis = value * unit.getToSec() * 1000;
                    long newValue = current + millis;
                    Date date = new Date(newValue);
                    if(getPlugin().isMysql() || getPlugin().isSQL()) {
                        new BanMuteManager().setTempBan(target, grund, new SimpleDateFormat("dd.MM.yyyy | HH:mm:ss").format(date));
                    } else {
                        Bukkit.getServer().getBanList(BanList.Type.NAME).addBan(Objects.requireNonNull(target.getName()), "§aYou are Banned. Reason:§c " + grund.getReason(), date, "true");
                    }
                    if (target.isOnline())
                        ((Player) target).kickPlayer("§bBan while §c" + grund.getReason() + "§b for §a" + value + " " + unit.getOutput() + "!");
                    return true;
                } else {
                    sender.sendMessage(getPlugin().getPrefix() + getPlugin().getWrongArgs("/tempban <Player> <Reason> <time> <SEC or MIN or DAY or WEEK or MONTH or YEAR>"));
                    return true;
                }
            }
        }
        if(cmd.getName().equalsIgnoreCase("removetempban")) {
            OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
            if (sender.hasPermission(getPlugin().getPermissionName() + "tempban")) {
                if(getPlugin().isMysql() || getPlugin().isSQL()) {
                    new BanMuteManager().removeTempBan(target);
                    Bukkit.getServer().getBanList(BanList.Type.NAME).pardon(target.getName());
                } else {
                    Bukkit.getServer().getBanList(BanList.Type.NAME).pardon(target.getName());
                }
            }
        }
        return super.onCommand(sender, cmd, label, args);
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 2) {
            List<String> reasons = new ArrayList<>();
            Arrays.asList(Ban.values()).forEach(ban -> reasons.add(ban.name()));
            ArrayList<String> empt = new ArrayList<>();
            for (String s : reasons) {
                if (s.toLowerCase().startsWith(args[1].toLowerCase())) {
                    empt.add(s);
                }
            }
            Collections.sort(empt);
            return empt;
        }
        if (args.length == 3) {
            return new ArrayList<String>(Collections.singletonList("Time"));
        }
        if (args.length == 4) {
            ArrayList<String> dateFormat = new ArrayList<>();
            Arrays.asList(DateUnit.values()).forEach(dateUnit -> dateFormat.add(dateUnit.name()));
            ArrayList<String> empty = new ArrayList<>();
            for (String s : dateFormat) {
                if (s.toLowerCase().startsWith(args[3].toLowerCase())) {
                    empty.add(s);
                }
            }
            Collections.sort(empty);
            return empty;
        }
        return super.onTabComplete(sender, command, label, args);
    }

    public enum Ban {

        CLIENT_MODIFICATIONS("client modifications"),
        BUG_USING("exploit bugs"),
        FORBIDDEN_SKIN("forbidden skin/name"),
        DESTROY_BUILDINGS("destroy other buildings"),
        TROLLING("trolling"),
        TEAMING("teaming"),
        GRIEFING("griefing"),
        OFFENSIVE_INAPPROPROATE_BUILDING("Offensive / inappropriate building");

        private final String reason;

        Ban(String reason) {
            this.reason = reason;
        }

        public String getReason() {
            return reason;
        }
    }
}

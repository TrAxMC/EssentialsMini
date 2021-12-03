package de.framedev.essentialsmini.listeners;

import de.framedev.essentialsmini.main.Main;
import de.framedev.essentialsmini.managers.BanFile;
import de.framedev.essentialsmini.managers.BanMuteManager;
import de.framedev.essentialsmini.managers.ListenerBase;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

import java.text.ParseException;
import java.text.SimpleDateFormat;


public class BanListener extends ListenerBase {

    public BanListener(Main plugin) {
        super(plugin);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onJoin(AsyncPlayerPreLoginEvent e) {
        if (getPlugin().isMysql() || getPlugin().isSQL()) {
            if (!new BanMuteManager().isExpiredTempBan(Bukkit.getOfflinePlayer(e.getUniqueId()))) {
                final String[] reason = new String[1];
                if (new BanMuteManager().getTempBan(Bukkit.getOfflinePlayer(e.getUniqueId())) != null) {
                    new BanMuteManager().getTempBan(Bukkit.getOfflinePlayer(e.getUniqueId())).forEach((s, s2) -> {
                        try {
                            Bukkit.getServer().getBanList(BanList.Type.NAME).addBan(e.getName(), "§aYou are Banned. Reason:§c " + s2, new SimpleDateFormat("dd.MM.yyyy | HH:mm:ss").parse(s), "true");
                            reason[0] = "§aYou are Banned. Reason:§c " + s2 + " §aExpired at §6: " + s;
                        } catch (ParseException parseException) {
                            parseException.printStackTrace();
                        }
                    });
                    e.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_BANNED);
                    e.setKickMessage(reason[0]);
                }
            } else {
                new BanMuteManager().removeTempBan(Bukkit.getOfflinePlayer(e.getUniqueId()));
                e.setLoginResult(AsyncPlayerPreLoginEvent.Result.ALLOWED);
            }
        }
        if (getPlugin().isMysql() || getPlugin().isSQL()) {
            if (new BanMuteManager().isPermaBan(Bukkit.getOfflinePlayer(e.getUniqueId()))) {
                e.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_OTHER);
                e.setKickMessage(ChatColor.RED + "You are Banned while " + ChatColor.GOLD + new BanMuteManager().getPermaBanReason(Bukkit.getOfflinePlayer(e.getUniqueId())));
            }
        } else {
            if (BanFile.cfg.getBoolean(e.getName() + ".isBanned")) {
                e.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_OTHER);
                e.setKickMessage(ChatColor.RED + "You are Banned while " + ChatColor.GOLD + BanFile.cfg.getString(e.getName() + ".reason"));
            }
        }
    }
}

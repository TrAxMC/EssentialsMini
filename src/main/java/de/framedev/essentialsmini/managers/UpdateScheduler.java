package de.framedev.essentialsmini.managers;


/*
 * ===================================================
 * This File was Created by FrameDev
 * Please do not change anything without my consent!
 * ===================================================
 * This Class was created at 15.08.2020 13:48
 */

import de.framedev.essentialsmini.main.Main;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

public class UpdateScheduler implements Runnable {

    @Override
    public void run() {
        boolean[] s = {true, true, true};
        new BukkitRunnable() {
            @Override
            public void run() {
                if (Main.getInstance().getConfig().getBoolean("LocationsBackup")) {
                    new LocationsManager().saveBackup();
                    //Main.getInstance().savePlayerHomes();
                    if (Main.getInstance().getConfig().getBoolean("LocationsBackupMessage")) {
                        Bukkit.getConsoleSender().sendMessage(Main.getInstance().getPrefix() + "§a" + new LocationsManager().getFileBackup().getName() + " §6LocationBackup gespeichert!");
                        Bukkit.getConsoleSender().sendMessage(Main.getInstance().getPrefix() + "§aDas Backup befindet sich in §6" + new LocationsManager().getFileBackup().getPath());
                    }
                } else {
                    s[0] = false;
                }
                if (Main.getInstance().getConfig().getBoolean("PlayerInfoSave")) {
                    Main.getInstance().getCfgLossHashMap().forEach((player, playerManagerCfgLoss) -> {
                        if (playerManagerCfgLoss.getName().equalsIgnoreCase(player.getName())) {
                            playerManagerCfgLoss.savePlayerManager();
                        }
                    });
                } else {
                    s[1] = false;
                }
                if (Main.getInstance().getConfig().getBoolean("BackupMessages")) {
                    Bukkit.getConsoleSender().sendMessage(Main.getInstance().getPrefix() + "§6User Data §aSaved!");
                } else {
                    s[2] = false;
                }
                if (!s[0] && !s[1] && !s[2]) {
                    cancel();
                }
            }
        }.runTaskTimerAsynchronously(Main.getInstance(), 0, 20L * 60 * Main.getInstance().getConfig().getInt("BackupTime"));
    }
}

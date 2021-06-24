package de.framedev.essentialsmini.utils;

import de.framedev.essentialsmini.api.MySQLManager;
import de.framedev.essentialsmini.api.VaultAPI;
import de.framedev.essentialsmini.main.Main;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.ServicePriority;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class VaultManager {

    private final Economy eco;

    public VaultManager(Main plugin) {
        File filedata = new File(Main.getInstance().getDataFolder() + "/money", "eco.yml");
        FileConfiguration cfgdata = YamlConfiguration.loadConfiguration(filedata);
        if (!filedata.exists()) {
            filedata.getParentFile().mkdirs();
            try {
                filedata.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (Bukkit.getServer().getOnlineMode()) {
            if (!cfgdata.contains("accounts")) {
                ArrayList<String> accounts = new ArrayList<>();
                accounts.add("14555508-6819-4434-aa6a-e5ce1509ea35");
                cfgdata.set("accounts", accounts);
                try {
                    cfgdata.save(filedata);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
            if (!cfgdata.contains("accounts")) {
                ArrayList<String> accounts = new ArrayList<>();
                accounts.add("sambakuchen");
                cfgdata.set("accounts", accounts);
                try {
                    cfgdata.save(filedata);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        plugin.getServer().getServicesManager().register(Economy.class, new VaultAPI(), plugin, ServicePriority.High);
        eco = new VaultAPI();
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (!eco.hasAccount(p.getName()))
                eco.createPlayerAccount(p.getName());
        }
    }

    File file = new File(Main.getInstance().getDataFolder() + "/money", "eco.yml");
    FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);

    /**
     * Add a User to the Bank
     *
     * @param bankName the Bank name
     * @param player   the OfflinePlayer
     */
    public void addBankMember(String bankName, OfflinePlayer player) {
        if (Main.getInstance().isMysql() || Main.getInstance().isSQL()) {
            new MySQLManager().addBankMember(bankName, player);
        } else if (Main.getInstance().isMongoDB()) {
            List<String> users = (List<String>) Main.getInstance().getBackendManager().getObject("bankname", bankName, "bankmembers", "essentialsmini_data");
            if (!users.contains(player.getName()))
                users.add(player.getName());
            Main.getInstance().getBackendManager().updateUser(player, "bankname", bankName, "essentialsmini_data");
            Main.getInstance().getBackendManager().updateUser(player, "bankmembers", users, "essentialsmini_data");
            Main.getInstance().getBackendManager().updataData("bankname", bankName, "bankmembers", users, "essentialsmini_data");
        } else {
            try {
                cfg.load(file);
            } catch (IOException | InvalidConfigurationException e) {
                e.printStackTrace();
            }
            if (!cfg.contains("Banks." + bankName + ".members")) {
                List<String> players = new ArrayList<>();
                if (!players.contains(player.getName()))
                    players.add(player.getName());
                cfg.set("Banks." + bankName + ".members", players);
            } else {
                List<String> players = cfg.getStringList("Banks." + bankName + ".members");
                if (!players.contains(player.getName()))
                    players.add(player.getName());
                cfg.set("Banks." + bankName + ".members", players);
            }
            try {
                cfg.save(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Removing a User from the Bank
     *
     * @param bankName the BankName
     * @param player   the OfflinePlayer
     */
    public void removeBankMember(String bankName, OfflinePlayer player) {
        if (Main.getInstance().isMysql() || Main.getInstance().isSQL()) {
            new MySQLManager().removeBankMember(bankName, player);
        } else if (Main.getInstance().isMongoDB()) {
            List<String> users = (List<String>) Main.getInstance().getBackendManager().getObject("bankname", bankName, "bankmembers", "essentialsmini_data");
            if (users.contains(player.getName()))
                users.remove(player.getName());
            Main.getInstance().getBackendManager().updateUser(player, "bankname", "", "essentialsmini_data");
            Main.getInstance().getBackendManager().updateUser(player, "bankmembers", users, "essentialsmini_data");
            Main.getInstance().getBackendManager().updataData("bankname", bankName, "bankmembers", users, "essentialsmini_data");
        } else {
            try {
                cfg.load(file);
            } catch (IOException | InvalidConfigurationException e) {
                e.printStackTrace();
            }
            if (!cfg.contains("Banks." + bankName + ".members")) {
            } else {
                List<String> players = cfg.getStringList("Banks." + bankName + ".members");
                if (players.contains(player.getName()))
                    players.remove(player.getName());
                cfg.set("Banks." + bankName + ".members", players);
            }
            try {
                cfg.save(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Return all BankMembers if the Bank exists and have BankMembers!
     *
     * @param bankName the BankName
     * @return all BankMembers from the Bank
     */
    public List<String> getBankMembers(String bankName) {
        if (Main.getInstance().isMysql() || Main.getInstance().isSQL()) {
            return new MySQLManager().getBankMembers(bankName);
        } else if (Main.getInstance().isMongoDB()) {
            return (List<String>) Main.getInstance().getBackendManager().getObject("bankname", bankName, "bankmembers", "essentialsmini_data");
        } else {
            return cfg.getStringList("Banks." + bankName + ".members");
        }
    }


    public Economy getEconomy() {
        return eco;
    }


    public Economy getEco() {
        return eco;
    }
}

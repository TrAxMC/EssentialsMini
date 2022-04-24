package de.framedev.essentialsmini.api;

import de.framedev.essentialsmini.main.Main;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

/**
 * Not in use!
 * / This Plugin was Created by FrameDev
 * / Package : de.framedev.essentialsmini.api
 * / ClassName BankAPI
 * / Date: 27.06.21
 * / Project: EssentialsMini
 * / Copyrighted by FrameDev
 */

public class BankAPI {

    File file = new File(Main.getInstance().getDataFolder() + "/money", "bankaccounts.yml");
    FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
    private static BankAPI instance;

    public BankAPI() {
        instance = this;
    }

    public static BankAPI getInstance() {
        return instance;
    }

    private void save() {
        try {
            cfg.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean createBankAccount(String player, String bankName) {
        return cfg.contains("banks." + player + "." + bankName);
    }

    public double getBankBalance(String player, String bankName) {
        if (hasBankAccount(player))
            return cfg.getDouble("banks." + player + "." + bankName + ".balance");
        return 0d;
    }

    public boolean depositMoney(String player, String bankName, double amount) {
        if (!hasBankAccount(player))
            return false;
        double balance = getBankBalance(player, bankName);
        balance += amount;
        return setBankBalance(player, bankName, balance);
    }

    public boolean withdrawMoney(String player, String bankName, double amount) {
        if (!hasBankAccount(player))
            return false;
        double balance = getBankBalance(player, bankName);
        balance -= amount;
        return setBankBalance(player, bankName, balance);
    }

    public boolean setBankBalance(String player, String bankName, double money) {
        if (!hasBankAccount(player))
            return false;
        cfg.set("banks." + player + "." + bankName + ".balance", money);
        save();
        return true;
    }

    public boolean hasBankAccount(String player) {
        return cfg.contains("banks." + player) && cfg.get("banks." + player) != null;
    }

    public String getBankName(String player) {
        if (!hasBankAccount(player)) return null;
        return cfg.getString("banks." + player);
    }
}

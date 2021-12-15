package de.framedev.essentialsmini.managers;

import de.framedev.essentialsmini.commands.playercommands.*;
import de.framedev.essentialsmini.commands.servercommands.*;
import de.framedev.essentialsmini.commands.worldcommands.DayNightCMD;
import de.framedev.essentialsmini.commands.worldcommands.LightningStrikeCMD;
import de.framedev.essentialsmini.commands.worldcommands.SunRainThunderCMD;
import de.framedev.essentialsmini.commands.worldcommands.WorldTPCMD;
import de.framedev.essentialsmini.listeners.*;
import de.framedev.essentialsmini.main.Main;
import org.bukkit.command.CommandExecutor;

import java.util.Map;
import java.util.Objects;

public class RegisterManager {

    private final Main plugin;

    // BackupCMD Var
    private BackUpCMD backup;

    // MuteCMD Var
    private MuteCMD muteCMD;

    /**
     * Constructor of RegisterManager
     * Register all Events and Commands
     *
     * @param plugin the Main Plugin
     */
    public RegisterManager(Main plugin) {
        this.plugin = plugin;
        registerCommands();
        registerListeners();
        registerTabCompleters();
    }

    /**
     * Register all TabCompleters
     */
    private void registerTabCompleters() {
        plugin.getTabCompleters().forEach((key, value) -> Objects.requireNonNull(plugin.getCommand(key)).setTabCompleter(value));
    }

    /**
     * Register all Listeners
     */
    private void registerListeners() {
        new DisallowCommands(plugin);
        new SleepListener(plugin);
        new PlayerListeners(plugin);
        new BanListener(plugin);
        new WarpSigns(plugin);
        plugin.getListeners().add(new SkinChanger());
        plugin.getListeners().forEach(listener -> plugin.getServer().getPluginManager().registerEvents(listener, plugin));
    }

    /**
     * Register all Commands
     */
    private void registerCommands() {
        new EssentialsMiniCMD(plugin);
        new SpawnCMD(plugin);
        if (plugin.getConfig().getBoolean("HomeTP")) {
            new HomeCMD(plugin);
        }
        new TeleportCMD(plugin);
        new FlyCMD(plugin);
        new InvseeCMD(plugin);
        new BackCMD(plugin);
        new GameModeCMD(plugin);
        new VanishCMD(plugin);
        new WarpCMD(plugin);
        new OnlinePlayerListCMD(plugin);
        new DayNightCMD(plugin);
        new BackpackCMD(plugin);
        new SleepCMD(plugin);
        new PlayerDataCMD(plugin);
        new ItemCMD(plugin);
        new KillCMD(plugin);
        new PlayerHeadsCMD(plugin);
        new MessageCMD(plugin);
        new EnchantCMD(plugin);
        new WorkbenchCMD(plugin);
        new SunRainThunderCMD(plugin);
        new RenameItemCMD(plugin);
        new ShowLocationCMD(plugin);
        new RepairCMD(plugin);
        new HealCMD(plugin);
        new FeedCMD(plugin);
        new TrashInventory(plugin);
        new RestartCMD(plugin);
        new GenerateKeyCMD(plugin);
        new KitCMD(plugin);
        new FuckCMD(plugin);
        new LagCMD(plugin);
        if (plugin.getConfig().getBoolean("SaveInventory")) {
            new SaveInventoryCMD(plugin);
            SaveInventoryCMD.restore();
        }
        new ShowCraftingCMD(plugin);
        new SignItemCMD(plugin);
        new WorldTPCMD(plugin);
        new GodCMD(plugin);
        new SummonCMD(plugin);
        // new SetHealthCMD(plugin);
        new SpeedCMD(plugin);
        new LightningStrikeCMD(plugin);
        //new RegisterCMD(plugin);
        new ClearChatCMD(plugin);
        this.backup = new BackUpCMD(plugin);
        if (plugin.getConfig().getBoolean("Economy.Activate")) {
            new EcoCMDs(plugin);
            new BankCMD(plugin);
            new MoneySignListeners(plugin);
        }
        if (plugin.getConfig().getBoolean("AFK.Boolean"))
            new AFKCMD(plugin);
        new SilentCMD(plugin);
        new FlySpeedCMD(plugin);
        this.muteCMD = new MuteCMD(plugin);
        new TempBanCMD(plugin);
        new BanCMD(plugin);
        new UnBanCMD(plugin);
        new BookCMD(plugin);
        new FireWorkCMD(plugin);
        new GlobalMuteCMD(plugin);
        new ExperienceCMD(plugin);
        new NickCMD(plugin);
        for (Map.Entry<String, CommandExecutor> commands : plugin.getCommands().entrySet()) {
            if (commands.getKey() == null) continue;
            if (commands.getValue() == null) continue;
            if (plugin.getCommand(commands.getKey()) == null) continue;
            Objects.requireNonNull(plugin.getCommand(commands.getKey())).setExecutor(commands.getValue());
        }
    }

    public MuteCMD getMuteCMD() {
        return muteCMD;
    }

    public BackUpCMD getBackupCMD() {
        return backup;
    }
}

package de.framedev.essentialsmini.commands.playercommands;

import de.framedev.essentialsmini.main.Main;
import de.framedev.essentialsmini.managers.CommandBase;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

/**
 * / This Plugin was Created by FrameDev
 * / Package : de.framedev.essentialsmini.commands.playercommands
 * / ClassName GlobalMuteCMD
 * / Date: 06.09.21
 * / Project: EssentialsMini
 * / Copyrighted by FrameDev
 */

public class GlobalMuteCMD extends CommandBase {

    private final Main plugin;

    public GlobalMuteCMD(Main plugin) {
        super(plugin, "globalmute");
        this.plugin = plugin;
    }

    public static boolean isGlobalMute() {
        return Main.getInstance().getSettingsCfg().getBoolean("GlobalMute");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("globalmute")) {
            if (!sender.hasPermission("essentialsmini.globalmute")) {
                sender.sendMessage(plugin.getPrefix() + plugin.getNOPERMS());
                return true;
            }

            boolean globalMute = plugin.getSettingsCfg().getBoolean("GlobalMute");

            if (globalMute) {
                plugin.getSettingsCfg().set("GlobalMute", false);
                plugin.saveSettings();
                sender.sendMessage(plugin.getPrefix() + "§aGlobal Mute Deaktiviert!");
            } else {
                plugin.getSettingsCfg().set("GlobalMute", true);
                plugin.saveSettings();
                sender.sendMessage(plugin.getPrefix() + "§aGlobal Mute Aktiviert!");
            }
            return true;
        }
        return super.onCommand(sender, command, label, args);
    }
}

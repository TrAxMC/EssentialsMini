package de.framedev.essentialsmin.commands.servercommands;

import de.framedev.essentialsmin.main.Main;
import de.framedev.essentialsmin.managers.CommandBase;
import de.framedev.essentialsmin.utils.TextUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import lombok.NonNull;

/**
 * This Plugin was Created by FrameDev
 * Package : de.framedev.essentialsmin.commands
 * Date: 24.10.2020
 * Project: EssentialsMini
 * Copyrighted by FrameDev
 */
public class ClearChatCMD extends CommandBase {

    private final Main plugin;

    public ClearChatCMD(Main plugin) {
        super(plugin);
        this.plugin = plugin;
        setup("chatclear",this);
    }

    @Override
    public boolean onCommand(CommandSender sender, @NonNull Command command, @NonNull String label, @NonNull String[] args) {
        if(sender.hasPermission(plugin.getPermissionName() + "chatclear")) {
            clearChat(sender);
        } else {
            sender.sendMessage(plugin.getPrefix() + plugin.getNOPERMS());
        }
        return super.onCommand(sender, command, label, args);
    }

    public void clearChat(CommandSender sender) {
        for(int i = 0; i <= 500; i++) {
            Bukkit.broadcastMessage(" ");
        }
        String message = plugin.getCustomMessagesConfig().getString("ChatClear");
        if (message != null) {
            message = new TextUtils().replaceAndToParagraph(message);
            message = new TextUtils().replaceObject(message,"%Player%",sender.getName());
        }
        Bukkit.broadcastMessage(plugin.getPrefix() + message);
    }
}
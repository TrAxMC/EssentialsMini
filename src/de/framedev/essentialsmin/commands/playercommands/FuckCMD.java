package de.framedev.essentialsmin.commands.playercommands;

import de.framedev.essentialsmin.main.Main;
import de.framedev.essentialsmin.managers.CommandBase;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;

import java.util.ArrayList;
import java.util.List;

/**
 * This Plugin was Created by FrameDev
 * Package : de.framedev.essentialsmin.commands
 * Date: 01.11.2020
 * Project: EssentialsMini
 * Copyrighted by FrameDev
 */
public class FuckCMD extends CommandBase {

    private final Main plugin;

    public FuckCMD(Main plugin) {
        super(plugin);
        this.plugin = plugin;
        setup("fuck", this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender.hasPermission(plugin.getPermissionName() + "fuck")) {
            if (args.length == 0) {
                if (sender instanceof Player) {
                    Player player = (Player) sender;
                    for (int i = 0; i < 12; i++) {
                        Firework firework = (Firework) player.getWorld().spawnEntity(player.getLocation(), EntityType.FIREWORK);
                        FireworkMeta fireworkMeta = firework.getFireworkMeta();
                        fireworkMeta.setPower(3);
                        FireworkEffect fireworkEffect = FireworkEffect.builder()
                                .flicker(true)
                                .trail(true)
                                .withColor(Color.AQUA, Color.BLUE, Color.RED, Color.YELLOW)
                                .with(FireworkEffect.Type.BALL_LARGE)
                                .withFade(Color.FUCHSIA, Color.PURPLE, Color.NAVY).build();
                        FireworkEffect fireworkEffect1 = FireworkEffect.builder()
                                .flicker(true)
                                .trail(true)
                                .withColor(Color.MAROON, Color.OLIVE, Color.LIME, Color.GREEN)
                                .with(FireworkEffect.Type.BURST)
                                .withFade(Color.AQUA, Color.NAVY, Color.TEAL).build();
                        List<FireworkEffect> effects = new ArrayList<>();
                        effects.add(fireworkEffect);
                        effects.add(fireworkEffect1);
                        fireworkMeta.addEffects(effects);
                        firework.setFireworkMeta(fireworkMeta);
                    }
                } else {
                    sender.sendMessage(plugin.getPrefix() + plugin.getNOPERMS());
                }
            } else if (args.length == 1) {
                Player target = Bukkit.getPlayer(args[0]);
                if (target != null) {
                    for (int i = 0; i < 32; i++) {
                        Firework firework = (Firework) target.getWorld().spawnEntity(target.getLocation(), EntityType.FIREWORK);
                        FireworkMeta fireworkMeta = firework.getFireworkMeta();
                        fireworkMeta.setPower(3);
                        FireworkEffect fireworkEffect = FireworkEffect.builder()
                                .flicker(true)
                                .trail(true)
                                .withColor(Color.AQUA, Color.BLUE, Color.RED, Color.YELLOW)
                                .with(FireworkEffect.Type.BALL_LARGE)
                                .withFade(Color.FUCHSIA, Color.PURPLE, Color.NAVY).build();
                        FireworkEffect fireworkEffect1 = FireworkEffect.builder()
                                .flicker(true)
                                .trail(true)
                                .withColor(Color.MAROON, Color.OLIVE, Color.LIME, Color.GREEN)
                                .with(FireworkEffect.Type.BURST)
                                .withFade(Color.AQUA, Color.NAVY, Color.TEAL).build();
                        List<FireworkEffect> effects = new ArrayList<>();
                        effects.add(fireworkEffect);
                        effects.add(fireworkEffect1);
                        fireworkMeta.addEffects(effects);
                        firework.setFireworkMeta(fireworkMeta);
                    }
                    target.sendMessage(plugin.getPrefix() + "§aYou got Fucked!");
                } else {
                    sender.sendMessage(plugin.getPrefix() + plugin.getVariables().getPlayerNameNotOnline(args[0]));
                }
            }
        } else {
            sender.sendMessage(plugin.getPrefix() + plugin.getNOPERMS());
        }
        return false;
    }
}

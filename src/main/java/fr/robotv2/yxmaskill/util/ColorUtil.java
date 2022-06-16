package fr.robotv2.yxmaskill.util;

import fr.robotv2.yxmaskill.player.GamePlayer;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class ColorUtil {

    public static String colorize(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public static void sendMessage(CommandSender sender, String message) {
        sender.sendMessage(colorize(message));
    }

    public static void sendMessage(GamePlayer gamePlayer, String message) {
        ColorUtil.sendMessage(gamePlayer.getPlayer(), message);
    }
}

package me.m0dii.venturacalendar.base.utils;

import me.m0dii.venturacalendar.VenturaCalendar;
import me.m0dii.venturacalendar.game.config.Messages;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

public class Messenger {
    private static final VenturaCalendar plugin = VenturaCalendar.getInstance();

    public static void send(CommandSender sender, String msg) {
        sender.sendMessage(Utils.format(msg));
    }

    public static void send(CommandSender sender, Messages msg) {
        sender.sendMessage(Utils.format(plugin.getBaseConfig().getMessage(msg)));
    }

    public static void log(Level level, String msg) {
        switch (level) {
            case INFO -> plugin.getLogger().info(msg);
            case WARN -> plugin.getLogger().warning(msg);
            case ERROR -> plugin.getLogger().severe(msg);
            case DEBUG -> {
                if (VenturaCalendar.debug) {
                    Bukkit.getConsoleSender().sendMessage("&3[&bVenturaCalendar Debug&3] &b" + msg);
                }
            }
            default -> {
            }
        }
    }

    public static void log(Level level, Exception ex) {
        if (!VenturaCalendar.debug) {
            return;
        }

        if (level == Level.DEBUG) {
            ex.printStackTrace();
        }
    }

    public enum Level {
        INFO,
        WARN,
        ERROR,
        DEBUG
    }
}

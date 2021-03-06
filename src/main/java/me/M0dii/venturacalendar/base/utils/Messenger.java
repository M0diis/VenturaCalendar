package me.m0dii.venturacalendar.base.utils;

import me.m0dii.venturacalendar.VenturaCalendar;
import me.m0dii.venturacalendar.game.config.Messages;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

public class Messenger
{
    public enum Level
    {
        INFO,
        WARN,
        ERROR,
        DEBUG
    }
    
    private static final VenturaCalendar plugin = VenturaCalendar.getInstance();
    
    public static void send(CommandSender sender, String msg)
    {
        sender.sendMessage(Utils.format(msg));
    }
    
    public static void send(CommandSender sender, Messages msg)
    {
        sender.sendMessage(Utils.format(plugin.getBaseConfig().getMessage(msg)));
    }
    
    public static void log(Level level, String msg)
    {
        switch(level)
        {
            case INFO -> plugin.getLogger().info(msg);
            case WARN -> plugin.getLogger().warning(msg);
            case ERROR -> plugin.getLogger().severe(msg);
            case DEBUG -> Bukkit.getConsoleSender().sendMessage("[DEBUG] " + msg);
        }
    }
    
    public static void log(Level level, Exception ex)
    {
        if(!VenturaCalendar.debug)
        {
            return;
        }
        
        if(level == Level.DEBUG)
        {
            ex.printStackTrace();
        }
    }
}

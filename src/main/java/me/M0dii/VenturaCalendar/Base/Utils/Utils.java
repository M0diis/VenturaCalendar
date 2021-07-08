package me.M0dii.VenturaCalendar.Base.Utils;

import me.M0dii.VenturaCalendar.Base.DateUtils.Date;
import me.M0dii.VenturaCalendar.Base.DateUtils.DateUtils;
import me.M0dii.VenturaCalendar.Base.DateUtils.TimeSystem;
import me.M0dii.VenturaCalendar.Game.Config.Messages;
import me.M0dii.VenturaCalendar.VenturaCalendar;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.HashMap;

public class Utils
{
    private static final HashMap<Messages, String> messages =
            VenturaCalendar.getCConfig().getMessages();
    
    public static String format(String text)
    {
        if(text == null || text.isEmpty())
            return "";
        
        return ChatColor.translateAlternateColorCodes('&', text);
    }
    
    public static void sendFormat(CommandSender sender, String msg)
    {
        sender.sendMessage(format(msg));
    }
    
    public static void sendMsg(CommandSender sender, Messages msg)
    {
        sender.sendMessage(messages.get(msg));
    }
    
    public static String replacePlaceholder(String message, Date date)
    {
        DateUtils du = VenturaCalendar.getDateUtils();
        
        date = new Date(date);
        TimeSystem timeSystem = new TimeSystem(date.getTimeSystem());
        date = du.addZero(date);
        
        message = message
                .replaceAll("%tick%", String.valueOf(date.getTick()))
                .replaceAll("%second%", String.valueOf(date.getSecond()))
                .replaceAll("%minute%", String.valueOf(date.getMinute()))
                .replaceAll("%hour%", String.valueOf(date.getHour()))
                .replaceAll("%day%", String.valueOf(date.getDay()))
                .replaceAll("%week%", String.valueOf(date.getWeek()))
                .replaceAll("%month%", String.valueOf(date.getMonth()))
                .replaceAll("%year%", String.valueOf(date.getYear()))
                .replaceAll("%era%", String.valueOf(date.getEra()));
        
        date = du.removeZero(date);
        long dayOfWeek = du.getDayOfWeek(date);
        
        message = message
                .replaceAll("%dayName%", timeSystem.getDayNames().get((int) dayOfWeek))
                .replaceAll("%monthName%", timeSystem.getMonthNames().get((int) date.getMonth()))
                .replaceAll("%eraName%", timeSystem.getEraNames().get((int) date.getEra()));
        
        return message;
    }
}
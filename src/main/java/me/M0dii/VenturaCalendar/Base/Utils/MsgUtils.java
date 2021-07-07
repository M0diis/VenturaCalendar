package me.M0dii.VenturaCalendar.Base.Utils;

import me.M0dii.VenturaCalendar.Game.Config.Messages;
import me.M0dii.VenturaCalendar.VenturaCalendar;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.HashMap;

public class MsgUtils
{
    private static final HashMap<Messages, String> messages =
            VenturaCalendar.getCommandConfig().getMessages();
    
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
}

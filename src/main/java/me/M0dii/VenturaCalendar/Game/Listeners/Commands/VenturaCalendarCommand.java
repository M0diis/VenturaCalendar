package me.M0dii.VenturaCalendar.Game.Listeners.Commands;

import me.M0dii.VenturaCalendar.Base.Utils.Utils;
import me.M0dii.VenturaCalendar.Game.Config.Messages;
import me.M0dii.VenturaCalendar.VenturaCalendar;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.HashMap;

public class VenturaCalendarCommand
{
	HashMap<Messages, String> msgs = VenturaCalendar.getCConfig().getMessages();
	
	public VenturaCalendarCommand(CommandSender sender, Command command,
								  String label, String[] args)
	{
		if(args.length == 1)
		{
			if(args[0].equalsIgnoreCase("reload"))
			{
				if(sender.hasPermission("venturacalendar.command.reload"))
				{
					VenturaCalendar.getTimeConfig().reloadConfig();
					VenturaCalendar.getCalendarConfig().reloadConfig();
					VenturaCalendar.getCConfig().reloadConfig();
					
					Utils.sendMsg(sender, Messages.CONFIG_RELOADED);
				}
				else Utils.sendMsg(sender, Messages.NO_PERMISSION);
			}
			else Utils.sendMsg(sender, Messages.UNKNOWN_COMMAND);
		}
		else Utils.sendMsg(sender, Messages.UNKNOWN_COMMAND);
	}

}

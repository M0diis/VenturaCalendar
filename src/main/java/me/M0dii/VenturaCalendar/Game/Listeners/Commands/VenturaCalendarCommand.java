package me.M0dii.VenturaCalendar.Game.Listeners.Commands;

import me.M0dii.VenturaCalendar.Game.Config.Messages;
import me.M0dii.VenturaCalendar.VenturaCalendar;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.HashMap;

public class VenturaCalendarCommand
{
	HashMap<Messages, String> msgs = VenturaCalendar.getCommandConfig().getMessages();
	
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
					VenturaCalendar.getCommandConfig().reloadConfig();
					
					sender.sendMessage(msgs.get(Messages.CONFIG_RELOADED));
				}
				else sender.sendMessage(msgs.get(Messages.NO_PERMISSION));
			}
			else sender.sendMessage(msgs.get(Messages.UNKNOWN_COMMAND));
		}
		else sender.sendMessage(msgs.get(Messages.UNKNOWN_COMMAND));
	}

}

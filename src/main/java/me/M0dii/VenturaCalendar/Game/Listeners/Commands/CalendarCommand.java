package me.M0dii.VenturaCalendar.Game.Listeners.Commands;

import java.util.HashMap;

import me.M0dii.VenturaCalendar.VenturaCalendar;
import me.M0dii.VenturaCalendar.Base.DateUtils.Date;
import me.M0dii.VenturaCalendar.Base.DateUtils.TimeSystem;
import me.M0dii.VenturaCalendar.Game.Config.Messages;
import me.M0dii.VenturaCalendar.Game.GUI.Calendar;
import me.M0dii.VenturaCalendar.Game.GUI.StorageUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CalendarCommand
{
	StorageUtils storageUtils = VenturaCalendar.getStorageUtils();
	
	HashMap<Messages, String> errors = VenturaCalendar.getCommandConfig().getMessages();
	
	public CalendarCommand(CommandSender sender, Command command, String label, String[] args)
	{
		if(sender instanceof Player)
		{
			Player player = (Player) sender;
			HashMap<String, TimeSystem> timeSystems = VenturaCalendar.getTimeConfig().getTimeSystems();
			
			if(args.length == 0)
			{
				if(player.hasPermission("venturacalendar.calendar.default"))
				{
					if(timeSystems.containsKey("default"))
					{
						TimeSystem timeSystem = timeSystems.get("default");
						
						Date date = VenturaCalendar.getDateCalculator().dateFromTicks(player.getWorld().getFullTime(), timeSystem);
						Date creationDate = VenturaCalendar.getDateCalculator().dateFromTicks(player.getWorld().getFullTime(), timeSystem);
						
						Calendar calendar = new Calendar(date, creationDate);
						storageUtils.storageCalendar(player, calendar);
						
						player.openInventory(calendar.getInventory());
					}
					else
					{
						player.sendMessage(errors.get(Messages.UNKNOWN_TIMESYSTEM));
					}
				}
				else
				{
					player.sendMessage(errors.get(Messages.NO_PERMISSION));
				}
				
				return;
			}

			if(args.length == 1)
			{
				if(player.hasPermission("venturacalendar.calendar.timesystem"))
				{
					String timeSystemName = args[0];
					
					if(timeSystems.containsKey(timeSystemName))
					{
						TimeSystem timeSystem = timeSystems.get(timeSystemName);
						Date date = VenturaCalendar.getDateCalculator().dateFromTicks(player.getWorld().getFullTime(), timeSystem);
						Date creationDate = VenturaCalendar.getDateCalculator().dateFromTicks(player.getWorld().getFullTime(), timeSystem);

						Calendar calendar = new Calendar(date, creationDate);
						storageUtils.storageCalendar(player, calendar);
						
						player.openInventory(calendar.getInventory());
					}
					else
					{
						player.sendMessage(errors.get(Messages.UNKNOWN_TIMESYSTEM));
					}
				}
				else
				{
					player.sendMessage(errors.get(Messages.NO_PERMISSION));
				}
			}
			else
			{
				player.sendMessage(errors.get(Messages.UNKNOWN_COMMAND));
			}
			
		}
		else
		{
			sender.sendMessage(errors.get(Messages.NOT_PLAYER));
		}
		
	}

}

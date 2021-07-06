package me.M0dii.VenturaCalendar.Game.Listeners.Commands;

import java.util.HashMap;

import me.M0dii.VenturaCalendar.VenturaCalendar;
import me.M0dii.VenturaCalendar.Base.DateUtils.Date;
import me.M0dii.VenturaCalendar.Base.DateUtils.TimeSystem;
import me.M0dii.VenturaCalendar.Game.Config.Messages;
import me.M0dii.VenturaCalendar.Game.GUI.Calendar;
import me.M0dii.VenturaCalendar.Game.GUI.StorageUtils;
import org.bukkit.Bukkit;
import org.bukkit.World;
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
			Player pl = (Player) sender;
			HashMap<String, TimeSystem> timeSystems = VenturaCalendar.getTimeConfig().getTimeSystems();
			
			if(args.length == 0)
			{
				if(pl.hasPermission("venturacalendar.calendar.default"))
				{
					if(timeSystems.containsKey("default"))
					{
						TimeSystem timeSystem = timeSystems.get("default");
						
						World world = Bukkit.getWorld(timeSystem.getWorldName());
						
						if(world != null)
						{
							Date date = VenturaCalendar.getDateCalculator().dateFromTicks(world.getFullTime(), timeSystem);
							Date creationDate = VenturaCalendar.getDateCalculator().dateFromTicks(world.getFullTime(), timeSystem);
							
							Calendar calendar = new Calendar(date, creationDate);
							storageUtils.storageCalendar(pl, calendar);
							
							pl.openInventory(calendar.getInventory());
						}
						else VenturaCalendar.instance.getLogger().warning("World " + timeSystem.getWorldName() + " was not " +
								"found.");
					} else pl.sendMessage(errors.get(Messages.UNKNOWN_TIMESYSTEM));
				} else pl.sendMessage(errors.get(Messages.NO_PERMISSION));
				
				return;
			}

			if(args.length == 1)
			{
				if(pl.hasPermission("venturacalendar.calendar.timesystem"))
				{
					String timeSystemName = args[0];
					
					if(timeSystems.containsKey(timeSystemName))
					{
						TimeSystem timeSystem = timeSystems.get(timeSystemName);
						
						World world = Bukkit.getWorld(timeSystem.getWorldName());
						
						if(world != null)
						{
							Date date = VenturaCalendar.getDateCalculator().dateFromTicks(world.getFullTime(), timeSystem);
							Date creationDate = VenturaCalendar.getDateCalculator().dateFromTicks(world.getFullTime(), timeSystem);
							
							Calendar calendar = new Calendar(date, creationDate);
							storageUtils.storageCalendar(pl, calendar);
							
							pl.openInventory(calendar.getInventory());
						}
					}
					else pl.sendMessage(errors.get(Messages.UNKNOWN_TIMESYSTEM));
				} else pl.sendMessage(errors.get(Messages.NO_PERMISSION));
			} else pl.sendMessage(errors.get(Messages.UNKNOWN_COMMAND));
		} else sender.sendMessage(errors.get(Messages.NOT_PLAYER));
	}
}

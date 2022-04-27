package me.m0dii.venturacalendar.game.listeners.commands;

import me.m0dii.venturacalendar.base.events.CalendarOpenEvent;
import me.m0dii.venturacalendar.base.utils.Messenger;
import me.m0dii.venturacalendar.VenturaCalendar;
import me.m0dii.venturacalendar.base.dateutils.Date;
import me.m0dii.venturacalendar.base.dateutils.TimeSystem;
import me.m0dii.venturacalendar.game.config.Messages;
import me.m0dii.venturacalendar.game.gui.Calendar;
import me.m0dii.venturacalendar.game.gui.StorageUtils;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CalendarCommand
{
	public CalendarCommand(CommandSender sender, Command command,
						   String label, String[] args, VenturaCalendar plugin)
	{
		StorageUtils storageUtils = plugin.getStorageUtils();
		
		if(sender instanceof Player)
		{
			Player pl = (Player) sender;
			
			TimeSystem timeSystem = plugin.getTimeConfig().getTimeSystem();
			
			if(args.length == 0)
			{
				if(!pl.hasPermission("venturacalendar.calendar"))
				{
					Messenger.send(pl, Messages.NO_PERMISSION);
					
					return;
				}
				
				String worldName = timeSystem.getWorldName();
				World world = Bukkit.getWorld(worldName);
				
				if(worldName.equalsIgnoreCase("current"))
				{
					world = pl.getWorld();
				}
				
				if(timeSystem.isRealTime())
				{
					Date date = plugin.getDateCalculator().fromMillis(timeSystem);
					Date creationDate = plugin.getDateCalculator().fromMillis(timeSystem);
					
					Calendar calendar = new Calendar(date, creationDate, plugin);
					storageUtils.storeCalendar(pl, calendar);
					
					pl.openInventory(calendar.getInventory());
					
					Bukkit.getPluginManager().callEvent(new CalendarOpenEvent(calendar, calendar.getInventory(), pl));
				}
				else if(world != null)
				{
					Date date = plugin.getDateCalculator().fromTicks(world.getFullTime(), timeSystem);
					Date creationDate = plugin.getDateCalculator().fromTicks(world.getFullTime(), timeSystem);
					
					Calendar calendar = new Calendar(date, creationDate, plugin);
					storageUtils.storeCalendar(pl, calendar);
					
					pl.openInventory(calendar.getInventory());
					
					Bukkit.getPluginManager().callEvent(new CalendarOpenEvent(calendar, calendar.getInventory(), pl));
				}
				else
				{
					Messenger.log(Messenger.Level.WARN, "World " + timeSystem.getWorldName() + " was not found.");
				}
				
				return;
			}

			Messenger.send(pl, Messages.UNKNOWN_COMMAND);
		}
		else
		{
			Messenger.send(sender, Messages.NOT_PLAYER);
		}
	}
}

package me.M0dii.venturacalendar.game.listeners.Commands;

import java.util.HashMap;

import me.M0dii.venturacalendar.base.utils.Utils;
import me.M0dii.venturacalendar.VenturaCalendar;
import me.M0dii.venturacalendar.base.dateutils.Date;
import me.M0dii.venturacalendar.base.dateutils.TimeSystem;
import me.M0dii.venturacalendar.game.config.Messages;
import me.M0dii.venturacalendar.game.gui.Calendar;
import me.M0dii.venturacalendar.game.gui.StorageUtils;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CalendarCommand
{
	final StorageUtils storageUtils;
	
	public CalendarCommand(CommandSender sender, Command command,
						   String label, String[] args, VenturaCalendar plugin)
	{
		storageUtils = plugin.getStorageUtils();
		
		if(sender instanceof Player pl)
		{
			HashMap<String, TimeSystem> timeSystems = plugin.getTimeConfig().getTimeSystems();
			
			if(args.length == 0)
			{
				if(!pl.hasPermission("venturacalendar.calendar.default"))
				{
					Utils.sendMsg(pl, Messages.NO_PERMISSION);
					
					return;
				}
				
				if(timeSystems.containsKey("default"))
				{
					TimeSystem timeSystem = timeSystems.get("default");
					
					String wname = timeSystem.getWorldName();
					World world = Bukkit.getWorld(wname);
					
					if(wname.equalsIgnoreCase("current"))
						world = pl.getWorld();
					
					if(timeSystem.isRealTime())
					{
						Date date = plugin.getDateCalculator().fromMillis(timeSystem);
						Date creationDate = plugin.getDateCalculator().fromMillis(timeSystem);
						
						Calendar calendar = new Calendar(date, creationDate, plugin);
						storageUtils.storeCalendar(pl, calendar);
						
						pl.openInventory(calendar.getInventory());
					}
					else if(world != null)
					{
						Date date = plugin.getDateCalculator().fromTicks(world.getFullTime(), timeSystem);
						Date creationDate = plugin.getDateCalculator().fromTicks(world.getFullTime(), timeSystem);
						
						Calendar calendar = new Calendar(date, creationDate, plugin);
						storageUtils.storeCalendar(pl, calendar);
						
						pl.openInventory(calendar.getInventory());
					}
					else plugin.getLogger().warning("World " + timeSystem.getWorldName() + " was not " +
							"found.");
				} else Utils.sendMsg(pl, Messages.UNKNOWN_TIMESYSTEM);
				
				return;
			}

			if(args.length == 1)
			{
				if(!pl.hasPermission("venturacalendar.calendar.timesystem"))
				{
					Utils.sendMsg(pl, Messages.NO_PERMISSION);
					
					return;
				}
				
				String tsName = args[0];
				
				if(timeSystems.containsKey(tsName))
				{
					TimeSystem timeSystem = timeSystems.get(tsName);
					
					String wname = timeSystem.getWorldName();
					World world = Bukkit.getWorld(wname);
					
					if(wname.equalsIgnoreCase("current"))
						world = pl.getWorld();
					
					if(timeSystem.isRealTime())
					{
						Date date = plugin.getDateCalculator().fromMillis(timeSystem);
						Date creationDate = plugin.getDateCalculator().fromMillis(timeSystem);
						
						Calendar calendar = new Calendar(date, creationDate, plugin);
						storageUtils.storeCalendar(pl, calendar);
						
						pl.openInventory(calendar.getInventory());
					}
					else if(world != null)
					{
						Date date = plugin.getDateCalculator().fromTicks(world.getFullTime(), timeSystem);
						Date creationDate = plugin.getDateCalculator().fromTicks(world.getFullTime(), timeSystem);
						
						Calendar calendar = new Calendar(date, creationDate, plugin);
						storageUtils.storeCalendar(pl, calendar);
						
						pl.openInventory(calendar.getInventory());
					}
				} else Utils.sendMsg(pl, Messages.UNKNOWN_TIMESYSTEM);
			} else Utils.sendMsg(pl, Messages.UNKNOWN_COMMAND);
		} else Utils.sendMsg(sender, Messages.NOT_PLAYER);
	}
}

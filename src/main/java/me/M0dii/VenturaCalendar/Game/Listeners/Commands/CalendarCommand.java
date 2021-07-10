package me.M0dii.VenturaCalendar.Game.Listeners.Commands;

import java.util.HashMap;

import me.M0dii.VenturaCalendar.Base.Utils.Utils;
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
	
	public CalendarCommand(CommandSender sender, Command command,
						   String label, String[] args)
	{
		if(sender instanceof Player)
		{
			Player pl = (Player) sender;
			HashMap<String, TimeSystem> timeSystems = VenturaCalendar.getTimeConfig().getTimeSystems();
			
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
					
					if(world != null)
					{
						Date date = VenturaCalendar.getDateCalculator().fromTicks(world.getFullTime(), timeSystem);
						Date creationDate = VenturaCalendar.getDateCalculator().fromTicks(world.getFullTime(), timeSystem);
						
						Calendar calendar = new Calendar(date, creationDate);
						storageUtils.storeCalendar(pl, calendar);
						
						pl.openInventory(calendar.getInventory());
					}
					else VenturaCalendar.instance.getLogger().warning("World " + timeSystem.getWorldName() + " was not " +
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
					
					if(world != null)
					{
						Date date = VenturaCalendar.getDateCalculator().fromTicks(world.getFullTime(), timeSystem);
						Date creationDate = VenturaCalendar.getDateCalculator().fromTicks(world.getFullTime(), timeSystem);
						
						Calendar calendar = new Calendar(date, creationDate);
						storageUtils.storeCalendar(pl, calendar);
						
						pl.openInventory(calendar.getInventory());
					}
				} else Utils.sendMsg(pl, Messages.UNKNOWN_TIMESYSTEM);
			} else Utils.sendMsg(pl, Messages.UNKNOWN_COMMAND);
		} else Utils.sendMsg(sender, Messages.NOT_PLAYER);
	}
}

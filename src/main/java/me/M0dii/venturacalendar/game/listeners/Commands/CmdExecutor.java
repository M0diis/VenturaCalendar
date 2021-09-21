package me.M0dii.venturacalendar.game.listeners.Commands;

import me.M0dii.venturacalendar.base.dateutils.TimeSystem;
import me.M0dii.venturacalendar.VenturaCalendar;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class CmdExecutor implements CommandExecutor, TabCompleter
{
	final HashMap<String, TimeSystem> timeSystems;
	
	final VenturaCalendar plugin;
	
	public CmdExecutor(VenturaCalendar plugin)
	{
		this.plugin = plugin;
		
		this.timeSystems = plugin.getTimeConfig().getTimeSystems();
	}
	
	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command,
							 @NotNull String label, @NotNull String[] args)
	{
		if(command.getName().equalsIgnoreCase("calendar"))
			new CalendarCommand(sender, command, label, args, plugin);

		if(command.getName().equalsIgnoreCase("venturacalendar"))
			new VenturaCalendarCommand(sender, command, label, args, plugin);

		return true;
	}
	
	@Override
	public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command,
												@NotNull String alias, @NotNull String[] args)
	{
		List<String> completes = new ArrayList<>();
		
		String name = command.getName().toLowerCase(Locale.ROOT);
		
		if(name.equals("venturacalendar"))
		{
			if(args.length == 1)
			{
				completes.add("reload");
				completes.add("set");
				
				completes.add("add");
				completes.add("subtract");
			}
			
			if(args.length == 2)
			{
				if(args[1].equalsIgnoreCase("set"))
					completes.add("startyear");
				else
				{
					completes.add("seconds");
					completes.add("minutes");
					completes.add("hours");
					completes.add("days");
					completes.add("weeks");
				}
			}
			
			if(args.length == 3)
			{
				if(args[1].equalsIgnoreCase("set"))
					timeSystems.forEach((key, value) -> completes.add(value.getName()));
			}
		}
		
		if(name.equals("calendar"))
		{
			if(sender.hasPermission("venturacalendar.command.timesystem"))
				timeSystems.forEach((key, value) -> completes.add(value.getName()));
		}
		
		return completes;
	}
}

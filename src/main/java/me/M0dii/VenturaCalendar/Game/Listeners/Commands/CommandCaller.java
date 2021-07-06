package me.M0dii.VenturaCalendar.Game.Listeners.Commands;

import me.M0dii.VenturaCalendar.Base.DateUtils.TimeSystem;
import me.M0dii.VenturaCalendar.VenturaCalendar;
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

public class CommandCaller implements CommandExecutor, TabCompleter
{
	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command,
							 @NotNull String label, @NotNull String[] args)
	{
		if(command.getName().equalsIgnoreCase("calendar"))
			new CalendarCommand(sender, command, label, args);

		if(command.getName().equalsIgnoreCase("venturacalendar"))
			new VenturaCalendarCommand(sender, command, label, args);

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
			completes.add("reload");
		}
		
		if(name.equals("calendar"))
		{
			HashMap<String, TimeSystem> timeSystems = VenturaCalendar.getTimeConfig().getTimeSystems();
			
			timeSystems.forEach((key, value) -> completes.add(value.getName()));
		}
		
		return completes;
	}
}

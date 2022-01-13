package me.m0dii.venturacalendar.game.listeners.Commands;

import me.m0dii.venturacalendar.base.dateutils.Month;
import me.m0dii.venturacalendar.base.dateutils.TimeSystem;
import me.m0dii.venturacalendar.base.utils.Messenger;
import me.m0dii.venturacalendar.game.config.Messages;
import me.m0dii.venturacalendar.VenturaCalendar;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class VenturaCalendarCommand
{
	public VenturaCalendarCommand(CommandSender sender, Command command,
								  String label, String[] args, VenturaCalendar plugin)
	{
		if(args.length == 1)
		{
			if(args[0].equalsIgnoreCase("reload"))
			{
				if(sender.hasPermission("venturacalendar.command.reload"))
				{
					plugin.getTimeConfig().reloadConfig();
					plugin.getCalendarConfig().reloadConfig();
					plugin.getBaseConfig().reloadConfig();
					plugin.getEventConfig().reloadConfig();
					
					Messenger.send(sender, Messages.CONFIG_RELOADED);
					
					return;
				}
				else Messenger.send(sender, Messages.NO_PERMISSION);
			}
			else Messenger.send(sender, Messages.UNKNOWN_COMMAND);
		}
		
		if(args.length >= 3 && sender instanceof Player p)
		{
			if(!sender.hasPermission("venturacalendar.command.changetime"))
			{
				Messenger.send(sender, Messages.NO_PERMISSION);
				
				return;
			}
			
			if(args[0].equalsIgnoreCase("set"))
			{
				if(alias(args[1], "startyear, startingyear") && args.length == 4)
				{
					try
					{
						int startYear = Integer.parseInt(args[3]);
						
						plugin.getTimeConfig().set("time-systems." + args[2] + ".year.zero", startYear);
						
						plugin.getTimeConfig().reloadConfig();
						plugin.getCalendarConfig().reloadConfig();
						plugin.getBaseConfig().reloadConfig();
						
						Messenger.send(p, "&aSuccessfully set starting year to " + startYear);
					}
					catch(NumberFormatException ex)
					{
						Messenger.send(p, "&aIllegal number format.");
						Messenger.log(Messenger.Level.DEBUG, ex);
					}
				}
				
				if(alias(args[1], "date") && args.length == 4)
				{
					try
					{
						String[] values = args[3].split("/");
						
						String year = values[0];
						String month = values[1];
						String day = values[2];

						TimeSystem ts = plugin.getTimeConfig().getTimeSystems().get(args[2]);
						
						int maxMonth = ts.getMonths().size();
						
						int m = 1;
						
						try { m = Integer.parseInt(month); }
						catch(NumberFormatException ex)
						{
							Messenger.send(p, "&cIllegal month number format.");
							Messenger.log(Messenger.Level.DEBUG, ex);
						}
						
						if(m > maxMonth || m < 0)
						{
							Messenger.send(p, "&cSpecified month does not exist.");
						
							return;
						}
						
						Month mn = ts.getMonths().get(m);
						
						int maxDays = (int)mn.getDays();
						
						int d = 1;
						
						try { d = Integer.parseInt(day); }
						catch(NumberFormatException ex) {
							Messenger.send(p, "&cIllegal day number format.");
						}
						
						if(d> maxDays || d <= 0)
						{
							Messenger.send(p, "&cSpecified day in month " + mn.getName() + " does not exist.");
							
							return;
						}
						
						plugin.getTimeConfig().set("time-systems." + args[2] + ".starting-year", year);
						plugin.getTimeConfig().set("time-systems." + args[2] + ".month-offset", m);
						plugin.getTimeConfig().set("time-systems." + args[2] + ".day-offset", d - 1) ;
						
						Messenger.send(p, "&aDate has been set to " + year + "/" + month + "/" + day);
						
						p.getWorld().setFullTime(0);

						plugin.getTimeConfig().reloadConfig();
						plugin.getCalendarConfig().reloadConfig();
						plugin.getBaseConfig().reloadConfig();
						
					}
					catch(NumberFormatException ex)
					{
						Messenger.send(p, "&aIllegal number format.");
						Messenger.log(Messenger.Level.DEBUG, ex);
					}
				}
			}
			
			if(alias(args[0], "add, subtract"))
			{
				boolean sub = args[0].equalsIgnoreCase("subtract");
				
				if(args[1].equalsIgnoreCase("seconds"))
					fastForwards(args[2], p, 20L, " second.", " hours.", sub);
				
				if(args[1].equalsIgnoreCase("minutes"))
					fastForwards(args[2], p, 1200L, " second.", " hours.", sub);
				
				if(args[1].equalsIgnoreCase("hours"))
					fastForwards(args[2], p, 1000L, " hour.", " hours.", sub);
				
				if(args[1].equalsIgnoreCase("days"))
					fastForwards(args[2], p, 24000L, " day.", " days.", sub);
				
				if(args[1].equalsIgnoreCase("weeks"))
					fastForwards(args[2], p, 168000L, " week.", " weeks.", sub);
			}
		}
		else Messenger.send(sender, Messages.UNKNOWN_COMMAND);
	}
	
	private void fastForwards(String amt, Player p, long oneAmount, String s, String s2, boolean subtract)
	{
		try
		{
			amt = amt.replaceAll("\\D", "");
			
			if(amt.isEmpty())
				amt = "0";
			
			if(subtract)
				amt = '-' + amt;
			
			int amount = Integer.parseInt(amt);
		
			long total = oneAmount * amount;
			
			World w = p.getWorld();
			
			if(w.getFullTime() + total > 0)
			{
				w.setFullTime(w.getFullTime() + total);
				
				Messenger.send(p, "&aFast-forwarded the time by " + amount + (amt.charAt(0) == '1' ? s : s2));
			}
			else Messenger.send(p, "&aWorld time can not go below 0 days.");
		}
		catch(NumberFormatException ex)
		{
			Messenger.send(p, "&cProvided amount is not valid.");
		}
	}
	
	private boolean alias(String cmd, String names)
	{
		String[] split = names.split(", ");
		
		for(String s : split)
		{
			if(cmd.equalsIgnoreCase(s))
				return true;
		}
		
		return false;
	}
}

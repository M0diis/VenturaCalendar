package me.M0dii.VenturaCalendar.Game.Listeners.Commands;

import me.M0dii.VenturaCalendar.Base.Utils.Utils;
import me.M0dii.VenturaCalendar.Game.Config.Messages;
import me.M0dii.VenturaCalendar.VenturaCalendar;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class VenturaCalendarCommand
{
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
		
		if(args.length == 3 && sender instanceof Player)
		{
			Player p = (Player)sender;
			
			if(!sender.hasPermission("venturacalendar.command.changetime"))
			{
				Utils.sendMsg(sender, Messages.NO_PERMISSION);
				
				return;
			}
			
			if(args[0].equalsIgnoreCase("add") || args[0].equalsIgnoreCase("subtract"))
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
		else Utils.sendMsg(sender, Messages.UNKNOWN_COMMAND);
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
				
				Utils.sendFormat(p, "&aFast-forwarded the time by " + amount + (amt.charAt(0) == '1' ? s : s2));
			}
			else Utils.sendFormat(p, "&aWorld time can not go below 0 days.");
		}
		catch(NumberFormatException ex)
		{
			Utils.sendFormat(p, "&cProvided amount is not valid.");
		}
	}
	
}

package me.M0dii.VenturaCalendar.Game.Listeners.Inventory;

import me.M0dii.VenturaCalendar.Base.DateUtils.FromTo;
import me.M0dii.VenturaCalendar.Base.ItemUtils.ItemProperties;
import me.M0dii.VenturaCalendar.Base.ItemUtils.Items;
import me.M0dii.VenturaCalendar.Base.Utils.Utils;
import me.M0dii.VenturaCalendar.Game.Config.CommandConfig;
import me.M0dii.VenturaCalendar.Game.Config.Messages;
import me.M0dii.VenturaCalendar.Game.GUI.Calendar;
import me.M0dii.VenturaCalendar.Game.GUI.InventoryProperties;
import me.M0dii.VenturaCalendar.Game.GUI.Storage;
import me.M0dii.VenturaCalendar.VenturaCalendar;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public class Click
{
	public Click(InventoryClickEvent e)
	{
		Player player = (Player) e.getWhoClicked();
		
		Inventory inv = e.getClickedInventory();
		ItemStack item = e.getCurrentItem();
		
		if(VenturaCalendar.storages.containsKey(player))
		{
			e.setCancelled(true);
			
			Storage storage = VenturaCalendar.storages.get(player);
		}
		
		if(inv != null && inv.getHolder() instanceof Calendar)
		{
			e.setCancelled(true);
			
			CommandConfig cc = VenturaCalendar.getCConfig();
			
			HashMap<String, FromTo> redeemableMonths = cc.getRedeemableMonths();
			
			Calendar cal = (Calendar)inv.getHolder();
			
			if(cal.getDate() != null && !cal.getDate().getTimeSystem().getName()
					.equalsIgnoreCase(cc.getString("rewards.timesystem")))
				return;
			
			if(cal.getDate() != null && cc.redeemWhitelistEnabled())
			{
				FromTo fromTo = redeemableMonths.get(cal.getDate().getMonthName());
				
				if(fromTo != null)
				{
					int from = fromTo.getFrom();
					int to = fromTo.getTo();
					
					long day = cal.getDate().getDay();
					
					if(!(day >= from && day <= to))
						return;
				}
			}
			
			HashMap<Items, HashMap<ItemProperties, Object>> itemProperties =
					(HashMap<Items, HashMap<ItemProperties, Object>>)
					VenturaCalendar.getCalendarConfig().getCalendarProperties()
					.get(InventoryProperties.ITEMS);
			
			HashMap<ItemProperties, Object> today = itemProperties.get(Items.TODAY);
			
			Material m = (Material)today.get(ItemProperties.MATERIAL);
			
			if(item != null && m.equals(item.getType()))
			{
				if(cc.getBoolean("rewards.enabled"))
				{
					if(VenturaCalendar.redeem(player.getUniqueId()))
					{
						for(String cmd : cc.getListString("rewards.commands"))
							sendCommand((Player)e.getWhoClicked(), (Player)e.getWhoClicked(), cmd);
					}
					else Utils.sendMsg(e.getWhoClicked(), Messages.REDEEMED);
				}
			}
		}
	}
	
	private void sendCommand(Player sender, Player placeholderHolder, String cmd)
	{
		cmd = PlaceholderAPI.setPlaceholders(placeholderHolder, cmd)
				.replace("%sender_name%", sender.getName());
		
		if(cmd.startsWith("["))
		{
			String sendAs = cmd.substring(cmd.indexOf("["), cmd.indexOf("]") + 2);
			
			cmd = cmd.substring(cmd.indexOf("]") + 2);
			
			if(sendAs.equalsIgnoreCase("[PLAYER] "))
				Bukkit.dispatchCommand(sender, cmd);
			else if(sendAs.equalsIgnoreCase("[CONSOLE] "))
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(),
						cmd.replace("[CONSOLE] ", ""));
		}
		else Bukkit.dispatchCommand(sender, cmd);
	}

}

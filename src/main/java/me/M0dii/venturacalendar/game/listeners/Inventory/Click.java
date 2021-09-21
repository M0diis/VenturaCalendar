package me.M0dii.venturacalendar.game.listeners.Inventory;

import me.M0dii.venturacalendar.base.dateutils.FromTo;
import me.M0dii.venturacalendar.base.itemutils.ItemProperties;
import me.M0dii.venturacalendar.base.itemutils.Items;
import me.M0dii.venturacalendar.base.utils.Utils;
import me.M0dii.venturacalendar.game.config.BaseConfig;
import me.M0dii.venturacalendar.game.config.Messages;
import me.M0dii.venturacalendar.game.gui.Calendar;
import me.M0dii.venturacalendar.game.gui.InventoryProperties;
import me.M0dii.venturacalendar.game.gui.Storage;
import me.M0dii.venturacalendar.VenturaCalendar;
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
	public Click(InventoryClickEvent e, VenturaCalendar plugin)
	{
		Player player = (Player) e.getWhoClicked();
		
		Inventory inv = e.getClickedInventory();
		ItemStack item = e.getCurrentItem();
		
		if(VenturaCalendar.storages.containsKey(player))
		{
			e.setCancelled(true);
			
			Storage storage = VenturaCalendar.storages.get(player);
		}
		
		if(inv != null && inv.getHolder() instanceof Calendar cal)
		{
			e.setCancelled(true);
			
			BaseConfig cc = plugin.getBaseConfig();
			
			HashMap<String, FromTo> redeemableMonths = cc.getRedeemableMonths();
			
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
					
					long day = cal.getDate().getDay() + 1;

					if(!(day >= from && day <= to))
						return;
				}
				
				if(fromTo == null)
					return;
			}
			
			HashMap<Items, HashMap<ItemProperties, Object>> itemProperties =
					(HashMap<Items, HashMap<ItemProperties, Object>>)
					plugin.getCalendarConfig().getCalendarProperties(false)
					.get(InventoryProperties.ITEMS);
			
			HashMap<ItemProperties, Object> today = itemProperties.get(Items.TODAY);
			
			Material m = (Material)today.get(ItemProperties.MATERIAL);
			
			if(item != null && m.equals(item.getType()))
			{
				if(cc.getBoolean("rewards.enabled"))
				{
					if(plugin.redeem(player.getUniqueId()))
					{
						for(String cmd : cc.getListString("rewards.commands"))
							sendCommand((Player)e.getWhoClicked(), cmd);
					}
					else Utils.sendMsg(e.getWhoClicked(), Messages.REDEEMED);
				}
			}
		}
	}
	
	private void sendCommand(Player player, String cmd)
	{
		cmd = PlaceholderAPI.setPlaceholders(player, cmd)
				.replaceAll("%([pP]layer|[pP]layer[nN]ame|[pP]layer_[nN]ame)%", player.getName());
		
		if(cmd.startsWith("["))
		{
			String sendAs = cmd.substring(cmd.indexOf("["), cmd.indexOf("]") + 2);
			
			cmd = cmd.substring(cmd.indexOf("]") + 2);
			
			if(sendAs.equalsIgnoreCase("[PLAYER] "))
				Bukkit.dispatchCommand(player, cmd);
			else if(sendAs.equalsIgnoreCase("[CONSOLE] "))
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(),
						cmd.replace("[CONSOLE] ", ""));
		}
		else Bukkit.dispatchCommand(player, cmd);
	}

}

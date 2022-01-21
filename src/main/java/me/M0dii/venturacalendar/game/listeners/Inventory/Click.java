package me.m0dii.venturacalendar.game.listeners.Inventory;

import me.m0dii.venturacalendar.base.dateutils.FromTo;
import me.m0dii.venturacalendar.base.itemutils.ItemProperties;
import me.m0dii.venturacalendar.base.itemutils.Items;
import me.m0dii.venturacalendar.base.utils.Messenger;
import me.m0dii.venturacalendar.base.utils.Utils;
import me.m0dii.venturacalendar.game.config.BaseConfig;
import me.m0dii.venturacalendar.game.config.Messages;
import me.m0dii.venturacalendar.game.gui.Calendar;
import me.m0dii.venturacalendar.game.gui.InventoryProperties;
import me.m0dii.venturacalendar.game.gui.Storage;
import me.m0dii.venturacalendar.VenturaCalendar;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public class Click
{
	private final VenturaCalendar plugin;
	
	public Click(InventoryClickEvent e, VenturaCalendar plugin)
	{
		this.plugin = plugin;
		
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
			
			if(cal.getDate() != null)
				return;

			if(cal.getDate() != null && cc.redeemWhitelistEnabled())
			{
				FromTo fromTo = redeemableMonths.get(cal.getDate().getMonthName());
				
				if(fromTo == null)
					return;
				
				long day = cal.getDate().getDay() + 1;
				
				if(!fromTo.includes((int)day))
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
							Utils.sendCommand((Player)e.getWhoClicked(), cmd);
					}
					else Messenger.send(e.getWhoClicked(), Messages.REDEEMED);
				}
			}
		}
	}
	


}

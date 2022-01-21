package me.m0dii.venturacalendar.game.listeners.Inventory;

import me.m0dii.venturacalendar.VenturaCalendar;
import me.m0dii.venturacalendar.base.events.CalendarClickEvent;
import me.m0dii.venturacalendar.game.gui.Calendar;
import me.m0dii.venturacalendar.game.gui.Storage;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

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
		
		if(inv == null || !(inv.getHolder() instanceof Calendar cal))
		{
			return;
		}
		
		e.setCancelled(true);
		
		Bukkit.getServer().getPluginManager().callEvent(new CalendarClickEvent(cal, player, item));
	}
}

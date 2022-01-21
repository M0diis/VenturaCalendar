package me.m0dii.venturacalendar.game.listeners.inventory;

import me.m0dii.venturacalendar.VenturaCalendar;
import me.m0dii.venturacalendar.base.events.CalendarClickEvent;
import me.m0dii.venturacalendar.base.events.CalendarCloseEvent;
import me.m0dii.venturacalendar.game.gui.Calendar;
import me.m0dii.venturacalendar.game.gui.Storage;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public final class InventoryListener implements Listener
{
	@EventHandler
	public void onInventoryClick(InventoryClickEvent e)
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
		
		Bukkit.getPluginManager().callEvent(new CalendarClickEvent(cal, inv, player, item));
	}
	
	@EventHandler
	public void onInventoryClose(InventoryCloseEvent e)
	{
		Inventory inventory = e.getInventory();
		
		if(e.getPlayer() instanceof Player p)
		{
			if(inventory instanceof Calendar cal)
			{
				Bukkit.getPluginManager().callEvent(new CalendarCloseEvent(cal, inventory, p));
			}
		}
	}
}

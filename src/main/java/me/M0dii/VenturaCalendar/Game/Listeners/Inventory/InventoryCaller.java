package me.M0dii.VenturaCalendar.Game.Listeners.Inventory;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

public class InventoryCaller implements Listener
{
	@EventHandler
	public void onInventoryClick(InventoryClickEvent event)
	{
		new Click(event);
	}
	
	@EventHandler
	public void onInventoryClose(InventoryCloseEvent event)
	{
		new Close(event);
	}
}

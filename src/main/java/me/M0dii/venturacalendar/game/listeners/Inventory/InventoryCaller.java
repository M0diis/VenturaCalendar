package me.m0dii.venturacalendar.game.listeners.Inventory;

import me.m0dii.venturacalendar.VenturaCalendar;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

public final class InventoryCaller implements Listener
{
	private final VenturaCalendar plugin;
	
	public InventoryCaller(VenturaCalendar plugin)
	{
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onInventoryClick(InventoryClickEvent e)
	{
		new Click(e);
	}
	
	@EventHandler
	public void onInventoryClose(InventoryCloseEvent e)
	{
		new Close(e, plugin);
	}
}

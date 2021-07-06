package me.M0dii.VenturaCalendar.Game.Listeners.Inventory;

import me.M0dii.VenturaCalendar.VenturaCalendar;
import me.M0dii.VenturaCalendar.Game.GUI.Storage;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class Click
{
	public Click(InventoryClickEvent event)
	{
		Player player = (Player) event.getWhoClicked();
		
		Inventory inventory = event.getClickedInventory();
		ItemStack item = event.getCurrentItem();
		
		if(VenturaCalendar.storages.containsKey(player))
		{
			event.setCancelled(true);
			
			Storage storage = VenturaCalendar.storages.get(player);
		}
	}

}

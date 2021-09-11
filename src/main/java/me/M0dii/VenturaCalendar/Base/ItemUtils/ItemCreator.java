package me.M0dii.VenturaCalendar.Base.ItemUtils;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class ItemCreator
{
	ItemStack item;

	public ItemCreator(Material material,  int amount, String name, List<String> lore)
	{
		if(material == null)
			material = Material.WHITE_STAINED_GLASS_PANE;
		
		item = new ItemStack(material, amount);
		
		ItemMeta itemMeta = item.getItemMeta();
		
		itemMeta.setDisplayName(name);
		
		itemMeta.setLore(lore);
		
		item.setItemMeta(itemMeta);
	}
	
	public ItemStack getItem()
	{
		return item;
	}

}

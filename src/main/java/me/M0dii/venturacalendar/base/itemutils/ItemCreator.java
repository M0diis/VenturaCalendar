package me.m0dii.venturacalendar.base.itemutils;

import com.cryptomorin.xseries.XMaterial;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.List;
import java.util.UUID;

@Getter
public class ItemCreator {
    private final ItemStack item;

    public ItemCreator(Material material, int amount, String name, List<String> lore) {
        if (material == null) {
            material = XMaterial.WHITE_STAINED_GLASS_PANE.parseMaterial();
        }

        if (material == null) {
            material = Material.GLASS_PANE;
        }

        item = new ItemStack(material, amount);

        ItemMeta itemMeta = item.getItemMeta();

        itemMeta.setDisplayName(name);

        itemMeta.setLore(lore);

        item.setItemMeta(itemMeta);
    }

    public ItemCreator(Material material, int amount, String name, List<String> lore, String skullOwner) {
        if (material == null) {
            material = XMaterial.WHITE_STAINED_GLASS_PANE.parseMaterial();
        }

        if (material == null) {
            material = Material.GLASS_PANE;
        }

        item = new ItemStack(material, amount);

        ItemMeta itemMeta = item.getItemMeta();

        itemMeta.setDisplayName(name);

        itemMeta.setLore(lore);

        item.setItemMeta(itemMeta);

        if (material.equals(XMaterial.PLAYER_HEAD.parseMaterial())) {
            SkullMeta skullMeta = (SkullMeta) itemMeta;

            UUID uuid = UUID.fromString(skullOwner);

            skullMeta.setOwningPlayer(Bukkit.getOfflinePlayer(uuid));

            item.setItemMeta(skullMeta);
        }
    }

}

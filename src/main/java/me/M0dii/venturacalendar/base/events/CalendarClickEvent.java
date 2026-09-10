package me.m0dii.venturacalendar.base.events;

import lombok.Getter;
import me.m0dii.venturacalendar.game.gui.Calendar;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

@Getter
public class CalendarClickEvent extends Event implements Cancellable {
    private static final HandlerList HANDLERS_LIST = new HandlerList();
    private final Inventory inventory;
    private final Calendar calendar;
    private final ItemStack item;
    private final Player player;
    private final InventoryClickEvent inventoryClickEvent;
    private final int slot;
    private boolean isCancelled;

    public CalendarClickEvent(Calendar cal, Inventory inventory, Player p, ItemStack item) {
        this(cal, null, inventory, p, item);
    }

    public CalendarClickEvent(Calendar cal,
                              InventoryClickEvent event,
                              Inventory inventory,
                              Player p,
                              ItemStack item) {
        this.calendar = cal;
        this.player = p;
        this.inventory = inventory;
        this.item = item;
        this.inventoryClickEvent = event;
        this.slot = event == null ? -1 : event.getSlot();
    }

    public static HandlerList getHandlerList() {
        return HANDLERS_LIST;
    }

    @Override
    public boolean isCancelled() {
        return isCancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.isCancelled = cancel;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS_LIST;
    }

}

package me.m0dii.venturacalendar.base.events;

import me.m0dii.venturacalendar.game.gui.RealTimeCalendar;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class RealTimeCalendarClickEvent extends Event implements Cancellable {
    private static final HandlerList HANDLERS_LIST = new HandlerList();
    private boolean isCancelled;

    private final Inventory inv;
    private final RealTimeCalendar calendar;
    private final ItemStack item;
    private final InventoryClickEvent inventoryClickEvent;
    private final Player player;

    public RealTimeCalendarClickEvent(RealTimeCalendar cal, InventoryClickEvent event, Inventory inv, Player p, ItemStack item) {
        this.calendar = cal;
        this.inventoryClickEvent = event;
        this.player = p;
        this.inv = inv;
        this.item = item;
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

    public RealTimeCalendar getCalendar() {
        return calendar;
    }

    public Player getPlayer() {
        return player;
    }

    public ItemStack getItem() {
        return item;
    }

    public Inventory getInventory() {
        return inv;
    }

    public InventoryClickEvent getInventoryClickEvent() {
        return inventoryClickEvent;
    }
}

package me.m0dii.venturacalendar.base.events;

import lombok.Getter;
import me.m0dii.venturacalendar.base.dateutils.MonthEvent;
import me.m0dii.venturacalendar.base.dateutils.TimeSystem;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

@Getter
public class MonthEventDayEvent extends Event implements Cancellable {
    private static final HandlerList HANDLERS_LIST = new HandlerList();
    private final TimeSystem timeSystem;
    private final MonthEvent monthEvent;
    private boolean isCancelled;

    public MonthEventDayEvent(TimeSystem timeSystem, MonthEvent monthEvent) {
        this.timeSystem = timeSystem;
        this.monthEvent = monthEvent;
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

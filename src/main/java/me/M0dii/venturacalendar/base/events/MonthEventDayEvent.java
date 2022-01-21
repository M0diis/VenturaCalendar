package me.m0dii.venturacalendar.base.events;

import me.m0dii.venturacalendar.base.dateutils.MonthEvent;
import me.m0dii.venturacalendar.base.dateutils.TimeSystem;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class MonthEventDayEvent extends Event implements Cancellable
{
    private static final HandlerList HANDLERS_LIST = new HandlerList();
    private boolean isCancelled;
    
    private final TimeSystem ts;
    private final MonthEvent event;
    
    public MonthEventDayEvent(TimeSystem ts, MonthEvent event)
    {
        this.ts = ts;
        this.event = event;
    }
    
    public static HandlerList getHandlerList()
    {
        return HANDLERS_LIST;
    }
    
    @Override
    public boolean isCancelled()
    {
        return isCancelled;
    }
    
    @Override
    public void setCancelled(boolean cancel)
    {
        this.isCancelled = cancel;
    }
    
    @Override
    public @NotNull HandlerList getHandlers()
    {
        return HANDLERS_LIST;
    }
    
    public TimeSystem getTimeSystem()
    {
        return ts;
    }
    
    public MonthEvent getMonthEvent()
    {
        return event;
    }
}

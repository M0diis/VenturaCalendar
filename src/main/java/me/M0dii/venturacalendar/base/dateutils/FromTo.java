package me.M0dii.venturacalendar.base.dateutils;

public final class FromTo
{
    private final int from;
    private final int to;
    
    public FromTo(int from, int to)
    {
        this.from = from;
        this.to = to;
    }
    public int getTo()
    {
        return this.to;
    }
    
    public int getFrom()
    {
        return this.from;
    }
}

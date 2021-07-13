package me.M0dii.VenturaCalendar.Base.DateUtils;

public class FromTo
{
    private final int from;
    private final int to;
    
    public int getTo()
    {
        return this.to;
    }
    
    public int getFrom()
    {
        return this.from;
    }
    
    public FromTo(int from, int to)
    {
        this.from = from;
        this.to = to;
    }
}

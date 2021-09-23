package me.M0dii.venturacalendar.base.dateutils;

public final class FromTo
{
    private final int from;
    private final int to;
    
    public FromTo(Object fromTo)
    {
        String[] fromToString = String.valueOf(fromTo).split("-");
    
        from = Integer.parseInt(fromToString[0]);
        to = Integer.parseInt(fromToString[1]);
    }
    
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
    
    public boolean includes(int number)
    {
        return this.from <= number && number <= this.to;
    }
}

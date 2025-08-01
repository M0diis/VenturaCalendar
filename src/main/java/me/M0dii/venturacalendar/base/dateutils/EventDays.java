package me.m0dii.venturacalendar.base.dateutils;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public final class EventDays {
    private int from = -1;
    private int to = -1;

    private List<Integer> days = new ArrayList<>();

    public EventDays(List<Integer> days) {
        this.days = days;
    }

    public EventDays(int from, int to) {
        this.from = from;
        this.to = to;
    }

    public boolean includes(int number) {
        return this.from <= number && number <= this.to || this.days.contains(number);
    }
}

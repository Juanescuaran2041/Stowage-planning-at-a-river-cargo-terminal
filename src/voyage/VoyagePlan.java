package voyage;

import java.time.LocalDate;

public class VoyagePlan {
    private final String tugboatName;
    private final int hours;
    private final int minutes;
    private final long days;
    private final LocalDate arrival;

    public VoyagePlan(String tugboatName, int hours, int minutes, long days, LocalDate arrival) {
        this.tugboatName = tugboatName;
        this.hours = hours;
        this.minutes = minutes;
        this.days = days;
        this.arrival = arrival;
    }

    public String getTugboatName() {
        return tugboatName;
    }

    public int getHours() {
        return hours;
    }

    public int getMinutes() {
        return minutes;
    }

    public long getDays() {
        return days;
    }

    public LocalDate getArrival() {
        return arrival;
    }
}

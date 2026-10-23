package voyage;

import java.time.LocalDate;

public abstract class Voyage {
    private static final double DISTANCE_KM = 580;

    private final Tugboat tugboat;

    public Voyage(Tugboat tugboat) {
        this.tugboat = tugboat;
    }

    public abstract String regimeName();

    public abstract double speedFactor();

    public abstract double navigationHoursPerDay();

    public VoyagePlan plan(double totalWeight, LocalDate departure) {
        if (totalWeight > tugboat.maxWeight()) {
            throw new IllegalArgumentException(String.format(
                    "Tugboat %s cannot push %.2f t (max %.0f t)",
                    tugboat.getName(), totalWeight, tugboat.maxWeight()));
        }

        double totalHours = DISTANCE_KM / (tugboat.speed() * speedFactor());
        long days = (long) Math.ceil(totalHours / navigationHoursPerDay());
        LocalDate arrival = departure.plusDays(days);

        // Round to the minute first so minutes can never reach 60
        long totalMinutes = Math.round(totalHours * 60);
        int hours = (int) (totalMinutes / 60);
        int minutes = (int) (totalMinutes % 60);

        return new VoyagePlan(tugboat.getName(), hours, minutes, days, arrival);
    }
}

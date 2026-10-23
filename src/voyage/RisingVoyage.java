package voyage;

public class RisingVoyage extends Voyage {
    public RisingVoyage(Tugboat tugboat) {
        super(tugboat);
    }

    @Override
    public String regimeName() {
        return "CRECIENTE";
    }

    @Override
    public double speedFactor() {
        return 0.85;
    }

    @Override
    public double navigationHoursPerDay() {
        return 24;
    }
}

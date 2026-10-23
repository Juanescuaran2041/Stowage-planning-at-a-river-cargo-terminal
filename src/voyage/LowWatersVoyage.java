package voyage;

public class LowWatersVoyage extends Voyage {
    public LowWatersVoyage(Tugboat tugboat) {
        super(tugboat);
    }

    @Override
    public String regimeName() {
        return "AGUAS_BAJAS";
    }

    @Override
    public double speedFactor() {
        return 0.75;
    }

    @Override
    public double navigationHoursPerDay() {
        return 12;
    }
}

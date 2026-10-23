package voyage;

public class NormalWatersVoyage extends Voyage {
    public NormalWatersVoyage(Tugboat tugboat) {
        super(tugboat);
    }

    @Override
    public String regimeName() {
        return "AGUAS_NORMALES";
    }

    @Override
    public double speedFactor() {
        return 1.00;
    }

    @Override
    public double navigationHoursPerDay() {
        return 24;
    }
}

package voyage;

public class Rio3200Tugboat implements Tugboat {
    @Override
    public String getName() {
        return "RIO-3200";
    }

    @Override
    public double speed() {
        return 13;
    }

    @Override
    public double maxWeight() {
        return 5000;
    }
}

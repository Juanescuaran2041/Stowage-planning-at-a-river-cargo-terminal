package voyage;

public class Rio1800Tugboat implements Tugboat {

    @Override
    public String getName() {
        return "RIO-1800";
    }

    @Override
    public double speed() {
        return 10;
    }

    @Override
    public double maxWeight() {
        return 2000;
    }
}

package thirdparty;

public class LimnimeterRH100 {
    private double levelMeters;

    public LimnimeterRH100(double levelMeters) {
        this.levelMeters = levelMeters;
    }

    public double LevelMeters() {
        return levelMeters;
    }
}

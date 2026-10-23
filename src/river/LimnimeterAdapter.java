package river;

import thirdparty.LimnimeterRH100;

public class LimnimeterAdapter implements RiverLevelMeter {
    private final LimnimeterRH100 limnimeter;

    public LimnimeterAdapter(LimnimeterRH100 limnimeter) {
        this.limnimeter = limnimeter;
    }

    @Override
    public double levelMeters() {
        return limnimeter.LevelMeters();
    }
}

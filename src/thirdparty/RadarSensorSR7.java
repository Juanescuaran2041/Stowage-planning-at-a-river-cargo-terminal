package thirdparty;

public class RadarSensorSR7 {
    private String txt;

    public RadarSensorSR7(String txt) {
        this.txt = txt;
    }

    public String reading() {
        return txt;
    }
}

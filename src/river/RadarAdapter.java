package river;

import thirdparty.RadarSensorSR7;

public class RadarAdapter implements RiverLevelMeter {
    private final RadarSensorSR7 radarSensor;

    public RadarAdapter(RadarSensorSR7 radarSensor) {
        this.radarSensor = radarSensor;
    }

    @Override
    public double levelMeters() {
        // Expected format: NIVEL_CM=145;ESTADO=OK
        String[] parts = radarSensor.reading().split(";");
        if (parts.length != 2) {
            throw new IllegalStateException("Unexpected radar reading format");
        }

        String levelCm = valueOf(parts[0]);
        String status = valueOf(parts[1]);

        // Check the status before using the level
        if (status.equals("FALLA")) {
            throw new IllegalStateException("Radar sensor failure");
        }

        try {
            return Double.parseDouble(levelCm) / 100;
        } catch (NumberFormatException e) {
            throw new IllegalStateException("Radar level is not numeric");
        }
    }

    // "KEY=value" -> "value"
    private String valueOf(String pair) {
        String[] keyValue = pair.split("=");
        if (keyValue.length != 2) {
            throw new IllegalStateException("Unexpected radar reading format");
        }
        return keyValue[1].trim();
    }
}

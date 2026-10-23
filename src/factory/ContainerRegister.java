package factory;

import models.ChargeUnity;
import models.ContainerUnit;

public class ContainerRegister extends ManifestRegister {

    @Override
    protected ChargeUnity createUnit(String line) {
        String[] parts = line.split(";");
        if (parts.length != 5 || !parts[0].trim().equals("CNT")) {
            throw new IllegalArgumentException("error no coincide la sigla de registro");
        }
        String registration = parts[1].trim();
        String seal = parts[parts.length - 1].trim();

        int feet;
        try {
            feet = Integer.parseInt(parts[2].trim());
        }
        catch (NumberFormatException e) {
            throw new IllegalArgumentException("error no numerico de feet");
        }
        double netWeight;
        try {
            netWeight = Double.parseDouble(parts[3].trim());
        }catch (NumberFormatException e) {
            throw new IllegalArgumentException("carga neta no numerica");
        }
        return new ContainerUnit(registration, feet, netWeight, seal);
    }
}

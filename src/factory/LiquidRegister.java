package factory;

import models.ChargeUnity;
import models.LiquidUnit;

public class LiquidRegister extends ManifestRegister {

    @Override
    protected ChargeUnity createUnit(String line) {
        String[] args = line.split(";");

        if (args.length != 4 || !args[0].trim().equals("LIQ")) {
            throw new IllegalArgumentException("error la sigla no es LIQ");
        }

        String product =  args[1].trim();
        double liters;
        double density;

        try {
            liters = Double.parseDouble(args[2].trim());
            density = Double.parseDouble(args[3].trim());
        }catch (NumberFormatException e){
            throw new IllegalArgumentException("error no numerico liters y density");
        }

        return new LiquidUnit(product, liters, density);
    }
}

package factory;

import models.BulkUnit;
import models.ChargeUnity;

public class BulkRegister extends ManifestRegister{

    @Override
    protected ChargeUnity createUnit(String line) {
        String [] args = line.split(";");
        if (args.length !=4 || !args[0].equals("GRA")) {
            throw new IllegalArgumentException("error sigla no coincide");
        }
        String product = args[1].trim();

        double tons;
        try {
            tons = Double.parseDouble(args[2].trim());
        }
        catch (NumberFormatException e) {
            throw new IllegalArgumentException("error no numerico de tons");
        }

        double percentageHumidity;
        try {
            percentageHumidity = Double.parseDouble(args[3].trim());
        }catch (NumberFormatException e) {
            throw new IllegalArgumentException("error no numerico de percentage");
        }

        return new BulkUnit(product, tons, percentageHumidity);
    }
}

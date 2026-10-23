package terminal;

import models.ChargeUnity;
import models.LiquidUnit;

import java.util.List;

public class LiquidsDocument implements ShippingDocument {
    @Override
    public String description() {
        return "Manifest with chemical compatibility certificate";
    }

    @Override
    public String generate(List<ChargeUnity> unities) {
        StringBuilder sb = new StringBuilder(description()).append("\n");
        for (ChargeUnity unity : unities) {
            if (unity instanceof LiquidUnit liquid) {
                sb.append(liquid.description())
                        .append(" density: ").append(liquid.getDensity()).append(" kg/L")
                        .append("\n");
            }
        }
        return sb.toString();
    }
}

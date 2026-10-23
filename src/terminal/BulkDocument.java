package terminal;

import models.BulkUnit;
import models.ChargeUnity;

import java.util.List;

public class BulkDocument implements ShippingDocument {
    @Override
    public String description() {
        return "Bill of lading with humidity certificate";
    }

    @Override
    public String generate(List<ChargeUnity> unities) {
        StringBuilder sb = new StringBuilder(description()).append("\n");
        for (ChargeUnity unity : unities) {
            if (unity instanceof BulkUnit bulk) {
                sb.append(bulk.description())
                        .append(" humidity: ").append(bulk.getPercentageHumidity()).append(" %")
                        .append("\n");
            }
        }
        return sb.toString();
    }
}

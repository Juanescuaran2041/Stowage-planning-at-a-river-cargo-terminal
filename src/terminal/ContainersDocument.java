package terminal;

import models.ChargeUnity;
import models.ContainerUnit;

import java.util.List;

public class ContainersDocument implements ShippingDocument {
    @Override
    public String description() {
        return "List of units with seal number per container";
    }

    @Override
    public String generate(List<ChargeUnity> unities) {
        StringBuilder sb = new StringBuilder(description()).append("\n");
        for (ChargeUnity unity : unities) {
            if (unity instanceof ContainerUnit container) {
                sb.append(container.description())
                        .append(" seal: ").append(container.getSeal())
                        .append("\n");
            }
        }
        return sb.toString();
    }
}

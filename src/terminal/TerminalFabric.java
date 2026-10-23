package terminal;

public interface TerminalFabric {
    String name();
    ChargeEquipment createEquipment();
    StabilityValidator createStabilityValidator();
    ShippingDocument createShippingDocument();

}

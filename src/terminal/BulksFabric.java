package terminal;

public class BulksFabric implements TerminalFabric {
    @Override
    public String name() {return "GRANEL_SOLIDO";}

    @Override
    public ChargeEquipment createEquipment() {
        return new ConveyorBelt();
    }

    @Override
    public StabilityValidator createStabilityValidator() {
        return new BulkValidator();
    }

    @Override
    public ShippingDocument createShippingDocument() {
        return new BulkDocument();
    }
}

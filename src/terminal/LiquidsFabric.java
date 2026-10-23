package terminal;

public class LiquidsFabric implements TerminalFabric {
    @Override
    public String name() {
        return "LiquidsFabric";
    }

    @Override
    public ChargeEquipment createEquipment() {
        return new LoadingArm();
    }

    @Override
    public StabilityValidator createStabilityValidator() {
        return new LiquidsValidator();
    }

    @Override
    public ShippingDocument createShippingDocument() {
        return new LiquidsDocument();
    }
}

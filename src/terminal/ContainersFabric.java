package terminal;

public class ContainersFabric implements TerminalFabric {
    @Override
    public String name() {
        return "CONTENEDORES";
    }

    @Override
    public ChargeEquipment createEquipment() {
        return new GantryCrane();
    }

    @Override
    public StabilityValidator createStabilityValidator() {
        return new ContainersValidator();
    }

    @Override
    public ShippingDocument createShippingDocument() {
        return new ContainersDocument();
    }
}

package terminal;

public class ConveyorBelt implements ChargeEquipment {
    @Override
    public String getName() {return "ConveyorBelt";}

    @Override
    public double rateTonsPerHour() {return 320;}
}

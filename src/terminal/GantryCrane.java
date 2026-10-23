package terminal;

public class GantryCrane implements ChargeEquipment {
    @Override
    public String getName() {
        return "Grua portico movil GP-1";
    }

    @Override
    public double rateTonsPerHour() {
        return 180;
    }
}

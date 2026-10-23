package terminal;

public class LoadingArm implements ChargeEquipment {
    @Override
    public String getName(){ return "Loading Arm"; }

    @Override
    public double rateTonsPerHour() {return 240;}
}

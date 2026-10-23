package models;

public class ContainerUnit implements ChargeUnity {
    private final String registration;
    private final int feet;
    private final double netWeight;
    private final String seal;

    public ContainerUnit(String registration, int feet, double netWeight, String seal) {
        this.registration = registration;
        this.feet = feet;
        this.netWeight = netWeight;
        this.seal = seal;
    }

    @Override
    public String id() {
        return registration;
    }

    @Override
    public TypeChargeUnit type() {
        return TypeChargeUnit.CONTAINTER;
    }

    @Override
    public double weight() {
        return netWeight + (feet == 20 ? 2.2 : 3.8);
    }

    @Override
    public String description() {
        return registration + "(" + feet + ")";
    }

    public String getSeal() {
        return seal;
    }
}

package models;

public class LiquidUnit implements ChargeUnity{
    private final String product;
    private final double liters;
    private final double density;

    public LiquidUnit(String product, double liters, double density) {
        this.product = product;
        this.liters = liters;
        this.density = density;
    }

    @Override
    public String id(){
        return product;
    }

    @Override
    public TypeChargeUnit type() {
        return TypeChargeUnit.LIQUIDS;
    }

    @Override
    public double weight(){
        return liters * density/1000;
    }

    @Override
    public String description(){
        return product + "( " + liters + " )";
    }

    public double getDensity() {
        return density;
    }
}

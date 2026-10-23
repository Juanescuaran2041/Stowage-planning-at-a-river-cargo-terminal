package models;

public class BulkUnit implements ChargeUnity{
    private final String product;
    private final double tons;
    private final double percentageHumidity;

    public BulkUnit(String product, double tons, double percentageHumidity) {
        this.product = product;
        this.tons = tons;
        this.percentageHumidity = percentageHumidity;
    }

    @Override
    public String id() {
        return product;
    }

    @Override
    public TypeChargeUnit type() {
        return TypeChargeUnit.BULK;
    }

    @Override
    public double weight() {
        return tons * (1 +  percentageHumidity/100);
    }

    @Override
    public String description() {
        return product + "(" + tons + ")";
    }

    public double getPercentageHumidity() {
        return percentageHumidity;
    }
}

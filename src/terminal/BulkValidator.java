package terminal;

public class BulkValidator implements StabilityValidator{
    @Override
    public double getMaxImbalance() {return 8.0;}

    @Override
    public double getMaxDraft(){return 1.80;}
}

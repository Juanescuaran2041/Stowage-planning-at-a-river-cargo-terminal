package terminal;

public class LiquidsValidator implements StabilityValidator{
    @Override
    public double getMaxImbalance(){return 3.0;}

    @Override
    public double getMaxDraft() {return 1.95;}
}

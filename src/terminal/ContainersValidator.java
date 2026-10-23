package terminal;

public class ContainersValidator implements StabilityValidator{
    @Override
    public double getMaxImbalance() {return 5.0}

    @Override
    public double getMaxDraft() {return 2.10;}
}

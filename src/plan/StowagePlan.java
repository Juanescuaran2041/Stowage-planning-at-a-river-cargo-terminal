package plan;

import models.ChargeUnity;

import java.util.ArrayList;
import java.util.List;

public class StowagePlan {
    private final String terminalName;
    private final String equipmentName;
    private final List<Hold> holds;
    private final List<ChargeUnity> unloaded;

    private double totalWeight;
    private double bowWeight;
    private double sternWeight;
    private double imbalancePct;
    private double tonsToMove;
    private double draft;
    private double maxDraft;
    private double tonsToUnload;
    private int loadingMinutes;

    public StowagePlan(String terminalName, String equipmentName, List<Hold> holds, List<ChargeUnity> unloaded) {
        this.terminalName = terminalName;
        this.equipmentName = equipmentName;
        this.holds = holds;
        this.unloaded = unloaded;
    }

    void setResults(double totalWeight, double bowWeight, double sternWeight, double imbalancePct,
                    double tonsToMove, double draft, double maxDraft, double tonsToUnload,
                    int loadingMinutes) {
        this.totalWeight = totalWeight;
        this.bowWeight = bowWeight;
        this.sternWeight = sternWeight;
        this.imbalancePct = imbalancePct;
        this.tonsToMove = tonsToMove;
        this.draft = draft;
        this.maxDraft = maxDraft;
        this.tonsToUnload = tonsToUnload;
        this.loadingMinutes = loadingMinutes;
    }

    public boolean isApproved() {
        return tonsToMove == 0 && tonsToUnload == 0;
    }

    // Numbered loading sequence: hold by hold, heaviest unit first inside each hold
    public List<String> loadingSequence() {
        List<String> sequence = new ArrayList<>();
        int n = 1;
        for (Hold hold : holds) {
            List<ChargeUnity> sorted = new ArrayList<>(hold.getUnits());
            sorted.sort((a, b) -> Double.compare(b.weight(), a.weight()));
            for (ChargeUnity unit : sorted) {
                sequence.add(n++ + ". " + hold.getName() + " <- " + unit.description()
                        + String.format(" %.2f t", unit.weight()));
            }
        }
        return sequence;
    }

    public String getTerminalName() { return terminalName; }
    public String getEquipmentName() { return equipmentName; }
    public List<Hold> getHolds() { return holds; }
    public List<ChargeUnity> getUnloaded() { return unloaded; }
    public double getTotalWeight() { return totalWeight; }
    public double getBowWeight() { return bowWeight; }
    public double getSternWeight() { return sternWeight; }
    public double getImbalancePct() { return imbalancePct; }
    public double getTonsToMove() { return tonsToMove; }
    public double getDraft() { return draft; }
    public double getMaxDraft() { return maxDraft; }
    public double getTonsToUnload() { return tonsToUnload; }
    public int getLoadingMinutes() { return loadingMinutes; }
}

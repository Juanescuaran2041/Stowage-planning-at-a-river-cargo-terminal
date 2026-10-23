package plan;

import models.ChargeUnity;
import terminal.ChargeEquipment;
import terminal.StabilityValidator;
import terminal.TerminalFabric;

import java.util.ArrayList;
import java.util.List;

public class StowagePlanner {
    private final TerminalFabric fabric;
    private final ChargeEquipment equipment;
    private final StabilityValidator validator;

    public StowagePlanner(TerminalFabric fabric) {
        this.fabric = fabric;
        this.equipment = fabric.createEquipment();
        this.validator = fabric.createStabilityValidator();
    }

    public StowagePlan createPlan(List<ChargeUnity> units) {
        // Every plan gets fresh, empty holds
        List<Hold> holds = List.of(
                new Hold("B1", 600),
                new Hold("B2", 750),
                new Hold("B3", 750),
                new Hold("B4", 600));
        List<ChargeUnity> unloaded = new ArrayList<>();

        // First Fit Decreasing
        List<ChargeUnity> sorted = new ArrayList<>(units);
        sorted.sort((a, b) -> Double.compare(b.weight(), a.weight()));
        for (ChargeUnity unit : sorted) {
            boolean placed = false;
            for (Hold hold : holds) {
                if (hold.canFit(unit)) {
                    hold.add(unit);
                    placed = true;
                    break;
                }
            }
            if (!placed) {
                unloaded.add(unit);
            }
        }

        StowagePlan plan = new StowagePlan(fabric.name(), equipment.getName(), holds, unloaded);
        evaluate(plan, validator.getMaxDraft());
        return plan;
    }

    // Balance, draft and loading time. maxDraft is a parameter so the plan can be
    // re-evaluated later with the effective draft given by the river level.
    public void evaluate(StowagePlan plan, double maxDraft) {
        List<Hold> holds = plan.getHolds();
        double bow = holds.get(0).getTotalWeight() + holds.get(1).getTotalWeight();
        double stern = holds.get(2).getTotalWeight() + holds.get(3).getTotalWeight();
        double total = bow + stern;

        double imbalance = total == 0 ? 0 : Math.abs(bow - stern) / total * 100;
        double maxImbalance = validator.getMaxImbalance();
        double tonsToMove = 0;
        if (imbalance > maxImbalance) {
            tonsToMove = (Math.abs(bow - stern) - total * maxImbalance / 100) / 2;
        }

        double draft = 0.55 + total / 4200;
        double tonsToUnload = 0;
        if (draft > maxDraft) {
            tonsToUnload = total - (maxDraft - 0.55) * 4200;
        }

        int minutes = (int) Math.round(total / equipment.rateTonsPerHour() * 60);

        plan.setResults(total, bow, stern, imbalance, tonsToMove, draft, maxDraft, tonsToUnload, minutes);
    }
}

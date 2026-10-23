package facade;

import factory.BulkRegister;
import factory.ContainerRegister;
import factory.LiquidRegister;
import factory.ManifestRegister;
import models.ChargeUnity;
import plan.Hold;
import plan.StowagePlan;
import plan.StowagePlanner;
import river.LimnimeterAdapter;
import river.RadarAdapter;
import river.RiverLevelMeter;
import terminal.BulksFabric;
import terminal.ChargeEquipment;
import terminal.ContainersFabric;
import terminal.LiquidsFabric;
import terminal.StabilityValidator;
import terminal.TerminalFabric;
import thirdparty.LimnimeterRH100;
import thirdparty.RadarSensorSR7;
import voyage.LowWatersVoyage;
import voyage.NormalWatersVoyage;
import voyage.Rio1800Tugboat;
import voyage.Rio3200Tugboat;
import voyage.RisingVoyage;
import voyage.Tugboat;
import voyage.Voyage;
import voyage.VoyagePlan;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CentralTerminal {
    private final TerminalType terminal;

    //constructor
    public CentralTerminal(TerminalType terminal) {
        this.terminal = terminal;
    }

    private ManifestRegister createRegister(){
        return switch (terminal) {
            case CONTENEDORES -> new ContainerRegister();
            case GRANEL_SOLIDO -> new BulkRegister();
            case LIQUIDOS -> new LiquidRegister();
        };
    }

    public List<ChargeUnity> processManifest(List<String> lines){
        ManifestRegister manifest = createRegister();

        //Accepted lines
        List<ChargeUnity> accepted = manifest.process(lines);

        int linesRead = manifest.getLinesRead();
        int rejectedLines = manifest.getRejected().size();
        int acceptedLines = accepted.size();

        System.out.println("linesRead: " + linesRead + " | " + "acceptedLines: " + acceptedLines + " | " + "rejectedLines: " + rejectedLines);
        for (ManifestRegister.RejectedLine rejected : manifest.getRejected()) {
            System.out.println("  [REJECTED] " + rejected.line() + "   -> " + rejected.reason());
        }
        return accepted;
    }

    private TerminalFabric createFabric(){
        return switch (terminal) {
            case CONTENEDORES -> new ContainersFabric();
            case GRANEL_SOLIDO -> new BulksFabric();
            case LIQUIDOS -> new LiquidsFabric();
        };
    }

    public StowagePlan generatePlan(List<ChargeUnity> units){
        TerminalFabric fabric = createFabric();
        StowagePlanner planner = new StowagePlanner(fabric);
        return planner.createPlan(units);
    }

    private RiverLevelMeter createMeter(RiverInstrument instr){
        return switch (instr){
            case LIMNIMETER -> new LimnimeterAdapter(new LimnimeterRH100(2.90));
            case RADAR -> new RadarAdapter(new RadarSensorSR7("NIVEL_CM=145;ESTADO=OK"));
        };
    }

    public void evaluateRiverLevel (StowagePlan plan, RiverInstrument instr){
        RiverLevelMeter meter = createMeter(instr);

        double level = meter.levelMeters();
        double availableDraft = level - 0.4;

        double terminalMaxDraft = createFabric().createStabilityValidator().getMaxDraft();

        // The effective max is the smaller of the terminal limit and the real river depth
        double effectiveMaxDraft = Math.min(terminalMaxDraft, availableDraft);

        // Re-evaluates the same plan with the effective limit
        new StowagePlanner(createFabric()).evaluate(plan, effectiveMaxDraft);

        System.out.printf("%s -> %.2f m | available draft %.2f m | effective max draft %.2f m%n",
                instr, level, availableDraft, effectiveMaxDraft);
        if (plan.getTonsToUnload() > 0) {
            System.out.printf("Plan re-evaluated: draft %.2f m (max %.2f m) -> NOT APPROVED by draft%n",
                    plan.getDraft(), plan.getMaxDraft());
            System.out.printf("  Tons to unload to stay within the limit: %.2f t%n", plan.getTonsToUnload());
        } else {
            System.out.printf("Plan re-evaluated: draft %.2f m (max %.2f m) -> OK%n",
                    plan.getDraft(), plan.getMaxDraft());
        }
    }

    private Tugboat createTugboat(TugboatType type){
        return switch (type) {
            case RIO_1800 -> new Rio1800Tugboat();
            case RIO_3200 -> new Rio3200Tugboat();
        };
    }

    // Plans the voyage under the three river regimes with the chosen tugboat.
    // Throws IllegalArgumentException if the tugboat cannot push the plan weight.
    public void planVoyage(StowagePlan plan, TugboatType tugboatType, LocalDate departure){
        Tugboat tugboat = createTugboat(tugboatType);
        List<Voyage> voyages = List.of(
                new NormalWatersVoyage(tugboat),
                new LowWatersVoyage(tugboat),
                new RisingVoyage(tugboat));

        // Computed first so an overweight error is raised before anything is printed
        List<VoyagePlan> results = new ArrayList<>();
        for (Voyage voyage : voyages) {
            results.add(voyage.plan(plan.getTotalWeight(), departure));
        }

        System.out.printf("%n--- VOYAGE %.2f t | departure %s ---%n", plan.getTotalWeight(), departure);
        System.out.printf("%-10s %-15s %-14s %-5s %s%n", "TUGBOAT", "REGIME", "NAVIGATION", "DAYS", "ARRIVAL");
        for (int i = 0; i < voyages.size(); i++) {
            VoyagePlan result = results.get(i);
            System.out.printf("%-10s %-15s %2d h %02d min    %-5d %s%n",
                    result.getTugboatName(), voyages.get(i).regimeName(),
                    result.getHours(), result.getMinutes(), result.getDays(), result.getArrival());
        }
    }

    public void printPlan(StowagePlan plan, String planId, String barge, LocalDate departure){
        TerminalFabric fabric = createFabric();
        ChargeEquipment equipment = fabric.createEquipment();
        StabilityValidator validator = fabric.createStabilityValidator();

        System.out.printf("%n=== RIVER TERMINAL OF BARRANCABERMEJA ===%n");
        System.out.printf("STOWAGE PLAN No. %s%n", planId);
        System.out.printf("Barge: %s   Departure: %s   Terminal: %s%n", barge, departure, plan.getTerminalName());
        System.out.printf("Equipment: %s (%.0f t/h)%n%n", equipment.getName(), equipment.rateTonsPerHour());

        System.out.printf("%-8s %10s %10s %11s %7s%n", "HOLD", "LOAD (t)", "CAPACITY", "OCCUPANCY", "UNITS");
        List<ChargeUnity> loaded = new ArrayList<>();
        for (Hold hold : plan.getHolds()) {
            System.out.printf("%-8s %10.2f %10.1f %9.1f %% %7d%n", hold.getName(), hold.getTotalWeight(),
                    hold.getCapacity(), hold.occupancyPercentage(), hold.getUnits().size());
            loaded.addAll(hold.getUnits());
        }
        System.out.printf("%-8s %10.2f%n%n", "TOTAL", plan.getTotalWeight());

        boolean balanced = plan.getTonsToMove() == 0;
        System.out.printf("Bow: %.2f t | Stern: %.2f t | Imbalance: %.2f %% (max %.2f %%) -> %s%n",
                plan.getBowWeight(), plan.getSternWeight(), plan.getImbalancePct(),
                validator.getMaxImbalance(), balanced ? "OK" : "NOT APPROVED");
        if (!balanced) {
            System.out.printf("  Tons to move from the heavy side to the light side: %.2f t%n", plan.getTonsToMove());
        }

        boolean draftOk = plan.getTonsToUnload() == 0;
        System.out.printf("Estimated draft: %.2f m (max %.2f m) -> %s%n",
                plan.getDraft(), plan.getMaxDraft(), draftOk ? "OK" : "NOT APPROVED");
        if (!draftOk) {
            System.out.printf("  Tons to unload to stay within the limit: %.2f t%n", plan.getTonsToUnload());
        }

        System.out.printf("Estimated loading time: %d h %02d min%n",
                plan.getLoadingMinutes() / 60, plan.getLoadingMinutes() % 60);
        System.out.printf("PLAN STATUS: %s%n", plan.isApproved() ? "APPROVED" : "NOT APPROVED");

        if (!plan.getUnloaded().isEmpty()) {
            System.out.printf("%nNOT LOADED (no hold had room):%n");
            for (ChargeUnity unit : plan.getUnloaded()) {
                System.out.printf("  %s %.2f t%n", unit.description(), unit.weight());
            }
        }

        System.out.printf("%nLOADING SEQUENCE%n");
        for (String step : plan.loadingSequence()) {
            System.out.println(" " + step);
        }

        System.out.printf("%nDOCUMENT: %s%n", fabric.createShippingDocument().generate(loaded));
    }
}

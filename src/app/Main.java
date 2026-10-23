package app;

import facade.CentralTerminal;
import facade.RiverInstrument;
import facade.TerminalType;
import facade.TugboatType;

import java.time.LocalDate;
import java.util.List;

public class Main {
    private static final LocalDate DEPARTURE = LocalDate.of(2026, 5, 12);
    private static final String BARGE = "BZ-4417";

    public static void main(String[] args) {
        // Step 1: containers manifest with two invalid lines
        CentralTerminal containers = new CentralTerminal(TerminalType.CONTENEDORES);
        var containerUnits = containers.processManifest(containersManifest());

        // Step 2: full stowage plan for the CONTENEDORES terminal
        var containerPlan = containers.generatePlan(containerUnits);
        containers.printPlan(containerPlan, "PE-2026-0148", BARGE, DEPARTURE);

        // Step 3: bulk manifest whose weight causes an imbalance above the limit
        System.out.println("\n########## BULK TERMINAL ##########");
        CentralTerminal bulk = new CentralTerminal(TerminalType.GRANEL_SOLIDO);
        var bulkUnits = bulk.processManifest(List.of(
                "GRA;CARBON-TERMICO;500;5",
                "GRA;MAIZE;400;10",
                "GRA;SOJA;120;12"));
        var bulkPlan = bulk.generatePlan(bulkUnits);
        bulk.printPlan(bulkPlan, "PE-2026-0149", BARGE, DEPARTURE);

        // Step 4: river level with both instruments, containers plan re-evaluated
        System.out.println("\n--- RIVER LEVEL (adapters) ---");
        containers.evaluateRiverLevel(containerPlan, RiverInstrument.LIMNIMETER);
        containers.evaluateRiverLevel(containerPlan, RiverInstrument.RADAR);

        // Step 5: voyage, first with the RIO-1800 (too small), then with the RIO-3200
        try {
            containers.planVoyage(containerPlan, TugboatType.RIO_1800, DEPARTURE);
        } catch (IllegalArgumentException e) {
            System.out.println("\n[CONTROLLED ERROR] " + e.getMessage());
        }
        containers.planVoyage(containerPlan, TugboatType.RIO_3200, DEPARTURE);
    }

    // 10 valid containers plus two lines that must be rejected
    private static List<String> containersManifest() {
        return List.of(
                "CNT;MSKU1234567;40;341.2;SELLO-8891",
                "CNT;TCLU8877213;40;326.2;SELLO-8892",
                "CNT;MSKU2345678;40;296.2;SELLO-8893",
                "CNT;HLXU3456789;40;276.2;SELLO-8894",
                "CNT;MSKU4567890;40;256.2;SELLO-8895",
                "CNT;TCLU5678901;40;236.2;SELLO-8896",
                "CNT;MSKU6789012;40;196.2;SELLO-8897",
                "CNT;HLXU7890123;40;186.2;SELLO-8898",
                "CNT;MSKU8901234;40;176.2;SELLO-8899",
                "CNT;TCLU9012345;40;146.2;SELLO-8900",
                "GRA;CARBON-TERMICO;850;13.5",
                "CNT;MSKU999;XX;18.5;SELLO-8901");
    }
}

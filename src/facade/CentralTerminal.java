package facade;

import factory.BulkRegister;
import factory.ContainerRegister;
import factory.LiquidRegister;
import factory.ManifestRegister;
import models.ChargeUnity;
import plan.StowagePlan;
import plan.StowagePlanner;
import terminal.BulksFabric;
import terminal.ContainersFabric;
import terminal.LiquidsFabric;
import terminal.TerminalFabric;

import java.util.List;

public class CentralTerminal {
    private TerminalType terminal;
    private List<String> lines;

    //constructor
    public CentralTerminal(TerminalType terminal) {
        this.terminal = terminal;
    }

    public ManifestRegister createRegister(){
        if (terminal.equals(TerminalType.CONTENEDORES)){
            return new ContainerRegister();
        }
        else if (terminal.equals(TerminalType.GRANEL_SOLIDO)){
            return new BulkRegister();
        }
        else if (terminal.equals(TerminalType.LIQUIDOS)){
            return new LiquidRegister();
        }
        return null;
    }

    public List<ChargeUnity> processManifiest(List<String> lines){
        ManifestRegister manifest = createRegister();

        //Accepted lines
        List<ChargeUnity> accepted = manifest.process(lines);

        int linesReaded = manifest.getLinesRead();
        int reject = manifest.getLinesRead();
        int acceptedLines = accepted.size();

        System.out.println("linesReaded: " + linesReaded + " | " + "acceptedLines: " + acceptedLines + " | " + "reject: " + reject);
        return accepted;
    }

    public TerminalFabric createFabric(){
        return switch (terminal) {
            case CONTENEDORES -> new ContainersFabric();
            case GRANEL_SOLIDO -> new BulksFabric();
            case LIQUIDOS -> new LiquidsFabric();
            default -> null;
        };
    }

    public StowagePlan generatePlan(List<ChargeUnity> units){
        TerminalFabric fabric = createFabric();
        StowagePlanner planner = new StowagePlanner(fabric);
        return planner.createPlan(units);
    }

}

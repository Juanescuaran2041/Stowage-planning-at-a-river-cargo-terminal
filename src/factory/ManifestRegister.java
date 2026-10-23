package factory;

import models.ChargeUnity;

import java.util.ArrayList;
import java.util.List;

//Factory Method
public abstract class ManifestRegister {
    public record RejectedLine(String line, String reason) {
    }

    private final List<RejectedLine> rejected = new ArrayList<>();
    private int linesRead;

    // Template method: a bad line is reported as rejected and the process goes on
    public List<ChargeUnity> process(List<String> lines){
        List<ChargeUnity> accepted = new ArrayList<>();
        rejected.clear();
        linesRead = lines.size();
        for (String line : lines){
            try {
                accepted.add(createUnit(line));
            } catch (IllegalArgumentException e) {
                rejected.add(new RejectedLine(line, e.getMessage()));
            }
        }
        return accepted;
    }

    protected abstract ChargeUnity createUnit(String line);

    public List<RejectedLine> getRejected() {
        return rejected;
    }

    public int getLinesRead() {
        return linesRead;
    }
}

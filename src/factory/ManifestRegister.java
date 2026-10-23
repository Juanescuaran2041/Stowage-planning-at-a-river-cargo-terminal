package factory;

import models.ChargeUnity;

import java.util.ArrayList;
import java.util.List;

//Factory Method
public abstract class ManifestRegister {
    public List<ChargeUnity> process(List<String> lines){
        List<ChargeUnity> chargeUnities = new ArrayList<>();
        for (String line : lines){
            chargeUnities.add(createUnit(line));
        }
        return chargeUnities;
    }

    protected abstract ChargeUnity createUnit(String line);
}

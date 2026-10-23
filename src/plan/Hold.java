package plan;

import models.ChargeUnity;

import java.util.ArrayList;
import java.util.List;

public class Hold {

    private String name;
    private double capacity;
    private List<ChargeUnity> units;
    private double totalWeight;

    public Hold(String name, double capacity){
        this.name = name;
        this.capacity = capacity;
        this.units = new ArrayList<>();
        this.totalWeight = 0;
    }

    public boolean canFit (ChargeUnity unit){
        return !(unit.weight() + totalWeight > capacity);
    }

    public void add(ChargeUnity unit){
        if (!(unit == null)){
            units.add(unit);
            totalWeight += unit.weight();
        }
    }

    public double occupancyPercentage(){
        return totalWeight / capacity * 100;
    }
    public String getName() {
        return name;
    }

    public double getCapacity() {
        return capacity;
    }

    public double getTotalWeight() {
        return totalWeight;
    }

    public List<ChargeUnity> getUnits() {
        return units;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCapacity(double capacity) {
        this.capacity = capacity;
    }

}

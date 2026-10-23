package terminal;

import models.ChargeUnity;

import java.util.List;


public interface ShippingDocument {
    String description();
    String generate(List<ChargeUnity> unities);
}

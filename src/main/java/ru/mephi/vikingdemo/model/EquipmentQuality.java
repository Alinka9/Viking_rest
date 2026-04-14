// ru/mephi/vikingdemo/model/EquipmentQuality.java
package ru.mephi.vikingdemo.model;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Качество снаряжения")
public enum EquipmentQuality {
    Common("Common"),
    Uncommon("Uncommon"),
    Rare("Rare"),
    Legendary("Legendary");

    private final String value;

    EquipmentQuality(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }
}
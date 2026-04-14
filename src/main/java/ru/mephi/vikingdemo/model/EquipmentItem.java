// ru/mephi/vikingdemo/model/EquipmentItem.java
package ru.mephi.vikingdemo.model;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Предмет снаряжения")
public record EquipmentItem(
        @Schema(description = "Название предмета", example = "Iron Axe")
        String name,
        @Schema(description = "Качество предмета")
        String quality
) {
    // Конструктор с Enum для удобства
    public EquipmentItem(String name, EquipmentQuality quality) {
        this(name, quality.getValue());
    }
}
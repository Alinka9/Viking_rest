// ru/mephi/vikingdemo/model/Viking.java
package ru.mephi.vikingdemo.model;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(description = "Модель викинга")
public record Viking(
        @Schema(description = "Уникальный идентификатор", example = "1")
        Long id,
        @Schema(description = "Имя викинга", example = "Bjorn")
        String name,
        @Schema(description = "Возраст", example = "31")
        int age,
        @Schema(description = "Рост в сантиметрах", example = "184")
        int heightCm,
        @Schema(description = "Цвет волос")
        HairColor hairColor,
        @Schema(description = "Форма бороды")
        BeardStyle beardStyle,
        @ArraySchema(schema = @Schema(implementation = EquipmentItem.class),
                arraySchema = @Schema(description = "Снаряжение викинга"))
        List<EquipmentItem> equipment
) {
    // Конструктор для создания нового викинга без ID
    public Viking(String name, int age, int heightCm, HairColor hairColor,
                  BeardStyle beardStyle, List<EquipmentItem> equipment) {
        this(null, name, age, heightCm, hairColor, beardStyle, equipment);
    }

    // Метод для обновления существующего викинга
    public Viking withUpdatedFields(String name, int age, int heightCm,
                                    HairColor hairColor, BeardStyle beardStyle,
                                    List<EquipmentItem> equipment) {
        return new Viking(this.id, name, age, heightCm, hairColor, beardStyle, equipment);
    }
}
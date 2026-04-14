// ru/mephi/vikingdemo/service/EquipmentFactory.java
package ru.mephi.vikingdemo.service;

import java.util.List;
import ru.mephi.vikingdemo.model.EquipmentItem;
import ru.mephi.vikingdemo.model.EquipmentQuality;
import java.util.Random;

public class EquipmentFactory {
    // Сделаем список публичным и неизменяемым
    public static final List<String> EQUIPMENT_NAMES = List.of(
            "Axe", "Sword", "Shield", "Helmet",
            "Spear", "Chainmail", "Hammer", "Knife",
            "Bow", "Dagger", "Battle Axe", "Greatsword",
            "Mace", "Crossbow", "Leather Armor", "Iron Boots"
    );

    private static final Random RANDOM = new Random();

    // Публичный метод для получения списка названий
    public static List<String> getEquipmentNames() {
        return EQUIPMENT_NAMES;
    }

    public static EquipmentItem createItem() {
        String name = EQUIPMENT_NAMES.get(RANDOM.nextInt(EQUIPMENT_NAMES.size()));
        EquipmentQuality quality = generateQuality();
        return new EquipmentItem(name, quality);
    }

    private static EquipmentQuality generateQuality() {
        int roll = RANDOM.nextInt(100);
        if (roll < 60) return EquipmentQuality.Common;
        if (roll < 85) return EquipmentQuality.Uncommon;
        if (roll < 97) return EquipmentQuality.Rare;
        return EquipmentQuality.Legendary;
    }
}
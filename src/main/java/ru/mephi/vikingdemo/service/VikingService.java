package ru.mephi.vikingdemo.service;

import org.springframework.stereotype.Service;
import ru.mephi.vikingdemo.model.Viking;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class VikingService {
    private final ConcurrentHashMap<Long, Viking> vikingsMap = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1); // Генератор цифровых ID
    private final VikingFactory vikingFactory;

    @Autowired
    public VikingService(VikingFactory vikingFactory) {
        this.vikingFactory = vikingFactory;
    }

    public List<Viking> findAll() {
        return new ArrayList<>(vikingsMap.values());
    }

    public Viking findById(Long id) {
        return vikingsMap.get(id);
    }

    public Viking createRandomViking() {
        long newId = idGenerator.getAndIncrement();
        Viking viking = vikingFactory.createRandomViking(newId);
        vikingsMap.put(newId, viking);
        return viking;
    }

    public Viking addViking(Viking vikingWithoutId) {
        long newId = idGenerator.getAndIncrement();
        // Создаём нового викинга с правильным ID, игнорируя то, что пришло
        Viking vikingWithId = new Viking(
                newId,
                vikingWithoutId.name(),
                vikingWithoutId.age(),
                vikingWithoutId.heightCm(),
                vikingWithoutId.hairColor(),
                vikingWithoutId.beardStyle(),
                vikingWithoutId.equipment()
        );
        vikingsMap.put(newId, vikingWithId);
        return vikingWithId;
    }
    public boolean deleteViking(Long id) {
        return vikingsMap.remove(id) != null;
    }

    public Viking updateViking(Long id, Viking updatedViking) {
        if (vikingsMap.containsKey(id)) {
            Viking vikingToUpdate = new Viking(id, updatedViking.name(), updatedViking.age(),
                    updatedViking.heightCm(), updatedViking.hairColor(),
                    updatedViking.beardStyle(), updatedViking.equipment());
            vikingsMap.put(id, vikingToUpdate);
            return vikingToUpdate;
        }
        return null;
    }
}
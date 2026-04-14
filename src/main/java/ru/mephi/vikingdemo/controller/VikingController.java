// ru/mephi/vikingdemo/controller/VikingController.java
package ru.mephi.vikingdemo.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.mephi.vikingdemo.model.Viking;
import ru.mephi.vikingdemo.service.VikingService;

import java.util.List;

@RestController
@RequestMapping("/api/vikings")
@Tag(name = "Vikings", description = "Операции с викингами")
public class VikingController {

    private final VikingService vikingService;

    public VikingController(VikingService vikingService) {
        this.vikingService = vikingService;
    }

    @GetMapping
    @Operation(summary = "Получить список созданных викингов", operationId = "getAllVikings")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Список успешно получен")})
    public List<Viking> getAllVikings() {
        System.out.println("GET /api/vikings called");
        return vikingService.findAll();
    }

    @GetMapping("/{id}")
    public Viking getVikingById(@PathVariable Long id) {
        Viking viking = vikingService.findById(id);
        if (viking == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Viking not found");
        }
        return viking;
    }

    @PutMapping("/{id}")
    public Viking updateViking(@PathVariable Long id, @RequestBody Viking viking) {
        System.out.println("PUT /api/vikings/" + id + " called");
        Viking updated = vikingService.updateViking(id, viking);
        if (updated == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Viking not found");
        }
        return updated;
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteViking(@PathVariable Long id) {
        System.out.println("DELETE /api/vikings/" + id + " called");
        boolean deleted = vikingService.deleteViking(id);
        if (!deleted) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Viking not found");
        }
    }

    @PostMapping
    @Operation(summary = "Добавить нового викинга", operationId = "addViking")
    @ResponseStatus(HttpStatus.CREATED)
    public Viking addViking(@RequestBody Viking viking) {
        System.out.println("POST /api/vikings called with: " + viking.name());
        return vikingService.addViking(viking);
    }

    @GetMapping("/test")
    @Operation(summary = "Получить список тестовых викингов", operationId = "getTest")
    public List<String> test() {
        System.out.println("GET /api/vikings/test called");
        return List.of("Ragnar", "Bjorn");
    }
}
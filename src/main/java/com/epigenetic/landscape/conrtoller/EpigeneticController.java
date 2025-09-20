package com.epigenetic.landscape.conrtoller;

import com.epigenetic.landscape.model.dto.CellDto;
import com.epigenetic.landscape.model.CellType;
import com.epigenetic.landscape.model.dto.Point3D;
import com.epigenetic.landscape.service.EpigeneticService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/api/epigenetic")
@AllArgsConstructor
public class EpigeneticController {

    private final EpigeneticService service;

    @PostMapping("/simulate")
    public Flux<CellDto> simulate(@RequestBody CellDto initial,
            @RequestParam int steps,
            @RequestParam Long simulationRunId) {
        return service.simulateDifferentiation(initial, steps, simulationRunId);
    }

    @GetMapping("/landscape")
    public Flux<Point3D> getLandscape() {
        return service.getPotentialLandscape();
    }

    @GetMapping("/states/{type}")
    public Flux<CellDto> getByType(@PathVariable CellType type) {
        return service.getStatesByType(type);
    }
}

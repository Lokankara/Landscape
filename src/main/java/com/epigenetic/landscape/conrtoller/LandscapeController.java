package com.epigenetic.landscape.conrtoller;

import com.epigenetic.landscape.model.dto.CellDto;
import com.epigenetic.landscape.model.SimulationRequest;
import com.epigenetic.landscape.model.SimulationResponse;
import com.epigenetic.landscape.service.GRNService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;

import java.util.List;

@RestController
@RequestMapping("/api/landscape")
@RequiredArgsConstructor
public class LandscapeController {

    private final GRNService grnService;

    @PostMapping(value = "/simulate", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SimulationResponse> simulate(@RequestBody SimulationRequest request) {
        SimulationResponse result = grnService.simulateTrajectory(request);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/states")
    public Flux<CellDto> getAllStates() {
        return grnService.getAllStatesDto();
    }

    @GetMapping("/states/search")
    public Flux<CellDto> searchStates(@RequestParam String query) {
        return grnService.searchStates(query);
    }

    @GetMapping("/stable-states")
    public ResponseEntity<List<double[]>> getStableStates() {
        return ResponseEntity.ok(grnService.getStableStates());
    }
}
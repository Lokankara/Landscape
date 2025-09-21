package com.epigenetic.landscape.conrtoller;

import com.epigenetic.landscape.model.dto.SimulationRequest;
import com.epigenetic.landscape.model.dto.SimulationResponse;
import com.epigenetic.landscape.model.dto.CellDto;
import com.epigenetic.landscape.model.dto.GradientRequest;
import com.epigenetic.landscape.service.SimulationService;
import com.epigenetic.landscape.service.VisualizationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

import java.util.List;

@RestController
@RequestMapping("/api/simulation")
@AllArgsConstructor
public class SimulationController {

    private final SimulationService simulationService;
    private final VisualizationService visualizationService;

    @PostMapping("/run")
    public ResponseEntity<SimulationResponse> runSimulation(@RequestBody @Valid SimulationRequest request) {
        if (request == null || request.getInitialX() == null || request.getInitialY() == null ||
                request.getSteps() == null || request.getTimeStep() == null) {
            return ResponseEntity.badRequest().build();
        }

        SimulationResponse result = simulationService.runSimulation(request);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/gradient")
    public ResponseEntity<double[]> computeGradient(@RequestBody GradientRequest request) {
        if (request == null || request.getPoint() == null || request.getPoint().length < 3 ||
                request.getPotentialPoints() == null || request.getDiffusionCoefficient() <= 0) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(simulationService.computeGradient(request.getPoint(), request.getPotentialPoints(),
                request.getDiffusionCoefficient()));
    }

    @PostMapping("/trajectory")
    public ResponseEntity<List<double[]>> simulateTrajectory(@RequestBody @Valid SimulationRequest request) {
        if (request == null || request.getStart() == null || request.getStart().length < 2) {
            return ResponseEntity.badRequest().build();
        }
        double[] x0 = request.getStart();
        double dt = request.getDelta() > 0 ? request.getDelta() : 0.01;
        int steps = request.getSampleLimit() > 0 ? request.getSampleLimit() : 500;
        double sigma = request.getDiffusionCoefficient() > 0 ? request.getDiffusionCoefficient() : 0.05;

        List<double[]> trajectory = simulationService.simulateTrajectory(x0, dt, steps, sigma);
        return ResponseEntity.ok(trajectory);
    }

    @PostMapping("/ensemble")
    public ResponseEntity<List<List<double[]>>> simulateEnsemble(@RequestBody SimulationRequest request) {
        if (request == null || request.getStart() == null || request.getStart().length < 2) {
            return ResponseEntity.badRequest().build();
        }
        List<List<double[]>> ensemble = simulationService.simulateEnsemble(
                request.getStart(),
                request.getEnsembleRuns(),
                request.getDelta(),
                request.getSampleLimit(),
                request.getDiffusionCoefficient()
        );
        return ResponseEntity.ok(ensemble);
    }

    @PostMapping("")
    public ResponseEntity<List<double[]>> simulateAndReduce(@RequestBody CellDto initialState) {
        List<CellDto> trajectory = simulationService.simulateCellTrajectory(initialState, 0.1, 100);
        return ResponseEntity.ok(visualizationService.performPCA(trajectory));
    }

    @GetMapping("/stable-states")
    public ResponseEntity<List<double[]>> getStableStates() {
        List<double[]> stableStates = simulationService.findStableStates();
        return ResponseEntity.ok(stableStates);
    }

    @PostMapping("/ensemble/grn")
    public ResponseEntity<List<double[][]>> simulateEnsembleGRNModel(
            @RequestBody SimulationRequest request) {

        if (request == null || request.getStart() == null || request.getStart().length == 0) {
            return ResponseEntity.badRequest().build();
        }

        List<double[][]> ensembleArray = simulationService.simulateEnsemble(request);
        return ResponseEntity.ok(ensembleArray);
    }
}

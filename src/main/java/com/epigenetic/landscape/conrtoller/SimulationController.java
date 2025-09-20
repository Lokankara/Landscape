package com.epigenetic.landscape.conrtoller;

import com.epigenetic.landscape.model.GRNModel;
import com.epigenetic.landscape.model.GradientRequest;
import com.epigenetic.landscape.model.SimpleMatrix;
import com.epigenetic.landscape.model.SimulationRequest;
import com.epigenetic.landscape.model.SimulationResult;
import com.epigenetic.landscape.service.SimulationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@RestController
@RequestMapping("/api/simulation")
@AllArgsConstructor
public class SimulationController {

    private final SimulationService simulationService;

    @PostMapping("/run")
    public ResponseEntity<SimulationResult> runSimulation(@RequestBody SimulationRequest request) {
        if (request == null || request.getInitialX() == null || request.getInitialY() == null ||
                request.getSteps() == null || request.getTimeStep() == null) {
            return ResponseEntity.badRequest().build();
        }

        SimulationResult result = simulationService.runSimulation(request);
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
    public ResponseEntity<List<double[]>> simulateTrajectory(@RequestBody SimulationRequest request) {
        if (request == null || request.getStart() == null || request.getStart().length < 2) {
            return ResponseEntity.badRequest().build();
        }
        double[] x0 = request.getStart();
        double dt = request.getDt() > 0 ? request.getDt() : 0.01;
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
                request.getDt(),
                request.getSampleLimit(),
                request.getDiffusionCoefficient()
        );
        return ResponseEntity.ok(ensemble);
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

        int n = request.getStart().length;
        SimpleMatrix W = new SimpleMatrix(n, n);
        SimpleMatrix basal = new SimpleMatrix(n, 1);
        SimpleMatrix decay = new SimpleMatrix(n, 1);
        IntStream.range(0, n).forEach(i -> decay.set(i, 0, 1.0));
        GRNModel model = new GRNModel(n, W, basal, decay);
        List<List<double[]>> ensemble = simulationService.simulateEnsemble(model, request);
        List<double[][]> ensembleArray = ensemble.stream()
                .map(l -> l.toArray(new double[0][]))
                .collect(Collectors.toList());

        return ResponseEntity.ok(ensembleArray);
    }
}

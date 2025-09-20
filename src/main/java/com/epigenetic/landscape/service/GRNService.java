package com.epigenetic.landscape.service;

import com.epigenetic.landscape.dao.CellStateRepository;
import com.epigenetic.landscape.model.CellDto;
import com.epigenetic.landscape.model.CellState;
import com.epigenetic.landscape.model.CellType;
import com.epigenetic.landscape.model.SimulationRequest;
import com.epigenetic.landscape.model.SimulationResult;
import org.springframework.stereotype.Service;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Service
@AllArgsConstructor
public class GRNService {

    private final CellStateRepository cellStateRepository;
    private static final double DEGRADATION_RATE = 0.2;
    private static final double NOISE_AMPLITUDE = 0.05;
    private final Random random = new Random();

    public SimulationResult simulateTrajectory(SimulationRequest request) {
        List<Double> xValues = new ArrayList<>();
        List<Double> yValues = new ArrayList<>();
        List<Double> potentialValues = new ArrayList<>();
        List<String> cellTypes = new ArrayList<>();

        double x = request.getInitialX();
        double y = request.getInitialY();

        for (int i = 0; i < request.getSteps(); i++) {
            double[] gradients = calculateGradients(x, y, request.getIncludeStochasticity());
            x += gradients[0] * request.getTimeStep();
            y += gradients[1] * request.getTimeStep();

            xValues.add(x);
            yValues.add(y);
            potentialValues.add(calculatePotential(x, y));
            cellTypes.add(determineCellType(x, y).name());
        }

        return SimulationResult.builder()
                .xValues(xValues)
                .yValues(yValues)
                .potentialValues(potentialValues)
                .cellTypes(cellTypes)
                .build();
    }

    private double[] calculateGradients(double x, double y, boolean stochastic) {
        double mutualX = 1.0 / (1.0 + Math.pow(y, 4));
        double mutualY = 1.0 / (1.0 + Math.pow(x, 4));

        double dx = mutualX - DEGRADATION_RATE * x;
        double dy = mutualY - DEGRADATION_RATE * y;

        if (stochastic) {
            dx += (random.nextDouble() - 0.5) * NOISE_AMPLITUDE;
            dy += (random.nextDouble() - 0.5) * NOISE_AMPLITUDE;
        }
        return new double[] {dx, dy};
    }

    public double calculatePotential(double x, double y) {
        return -Math.log(1.0 + Math.pow(x, 4)) / 4.0
                - Math.log(1.0 + Math.pow(y, 4)) / 4.0
                + (DEGRADATION_RATE / 2.0) * (x * x + y * y);
    }

    private CellType determineCellType(double x, double y) {
        double t = 2.5;
        if (x > t && y < t)
            return CellType.NEURON;
        if (x < t && y > t)
            return CellType.CARDIOMYOCYTE;
        if (x < 1.0 && y < 1.0)
            return CellType.PLURIPOTENT;
        return CellType.UNCOMMITTED;
    }

    public Flux<CellDto> getAllStatesDto() {
        return cellStateRepository.findAll()
                .map(this::toDto);
    }

    public Flux<CellDto> searchStates(String query) {
        if (query == null || query.trim().isEmpty())
            return getAllStatesDto();
        return cellStateRepository.findByStateNameContainingIgnoreCase(query)
                .map(this::toDto);
    }

    private CellDto toDto(CellState state) {
        CellDto dto = new CellDto(2);
        dto.setId(state.getId() == null ? null : state.getId().toString());
        dto.getGeneExpression().set(0, state.getGeneExpressionX() != null ? state.getGeneExpressionX() : 0.0);
        dto.getGeneExpression().set(1, state.getGeneExpressionY() != null ? state.getGeneExpressionY() : 0.0);
        dto.setType(state.getCellType());
        dto.setPotentialGradient(Arrays.asList(0.0, 0.0));
        return dto;
    }

    public List<double[]> getStableStates() {
        return Arrays.asList(
                new double[] {0.2, 3.8},
                new double[] {3.8, 0.2},
                new double[] {1.0, 1.0}
        );
    }

    public CellDto calculateGeneRegulatoryDynamics(CellDto cellDto) {
        List<Double> expr = cellDto.getGeneExpression();
        List<Double> grad = new ArrayList<>(expr.size());
        for (int i = 0; i < expr.size(); i++) grad.add(0.0);

        double x = expr.get(0);
        double y = expr.get(1);

        double mutualX = 1.0 / (1.0 + Math.pow(y, 4));
        double mutualY = 1.0 / (1.0 + Math.pow(x, 4));

        grad.set(0, mutualX - DEGRADATION_RATE * x + (random.nextDouble() - 0.5) * NOISE_AMPLITUDE);
        grad.set(1, mutualY - DEGRADATION_RATE * y + (random.nextDouble() - 0.5) * NOISE_AMPLITUDE);

        CellDto updated = new CellDto();
        updated.setId(cellDto.getId());
        updated.setGeneExpression(new ArrayList<>(expr));
        updated.setPotentialGradient(grad);
        updated.setType(determineCellType(x, y));

        return updated;
    }
}

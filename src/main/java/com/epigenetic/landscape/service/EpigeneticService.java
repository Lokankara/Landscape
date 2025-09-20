package com.epigenetic.landscape.service;

import com.epigenetic.landscape.model.CellDto;
import com.epigenetic.landscape.model.CellState;
import com.epigenetic.landscape.model.CellType;
import com.epigenetic.landscape.model.Point3D;
import org.springframework.stereotype.Service;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Flux;

import java.util.Random;

@Service
@AllArgsConstructor
public class EpigeneticService {
    private static final double DEGRADATION_RATE = 0.2;
    private static final double NOISE_AMPLITUDE = 0.05;
    private static final double DIFFERENTIATION_THRESHOLD = 2.5;
    private final Random random = new Random();
    private final GRNService grnService;


    private double geneRegFunction(double x, double y, int dim) {
        return dim == 0
                ? (1.0 / (1.0 + Math.pow(y, 4))) - DEGRADATION_RATE * x
                : (1.0 / (1.0 + Math.pow(x, 4))) - DEGRADATION_RATE * y;
    }

    private double calculatePotential(double x, double y) {
        return -Math.log(1.0 + Math.pow(x, 4)) / 4.0
                - Math.log(1.0 + Math.pow(y, 4)) / 4.0
                + (DEGRADATION_RATE / 2.0) * (x * x + y * y);
    }

    private CellType determineCellType(double x, double y) {
        if (x > DIFFERENTIATION_THRESHOLD && y < DIFFERENTIATION_THRESHOLD)
            return CellType.NEURON;
        if (x < DIFFERENTIATION_THRESHOLD && y > DIFFERENTIATION_THRESHOLD)
            return CellType.CARDIOMYOCYTE;
        if (x < 1.0 && y < 1.0)
            return CellType.PLURIPOTENT;
        return CellType.UNCOMMITTED;
    }

    private CellDto toDto(CellState state) {
        CellDto dto = new CellDto(2);
        dto.getGeneExpression().set(0, state.getGeneExpressionX());
        dto.getGeneExpression().set(1, state.getGeneExpressionY());
        dto.setType(state.getCellType());
        dto.getPotentialGradient().set(0, 0.0);
        dto.getPotentialGradient().set(1, 0.0);
        return dto;
    }

    public Flux<Point3D> getPotentialLandscape() {
        return grnService.getAllStatesDto()
                .map(dto -> new Point3D(
                        dto.getGeneExpression().get(0),
                        dto.getGeneExpression().get(1),
                        dto.getPotentialGradient().get(0)
                ));
    }

    public Flux<CellDto> getStatesByType(CellType type) {
        return grnService.getAllStatesDto()
                .filter(dto -> dto.getType() == type);
    }

    public Flux<CellDto> simulateDifferentiation(CellDto initialDto, int steps, Long simulationRunId) {
        CellState initialState = new CellState();
        initialState.setGeneExpressionX(initialDto.getGeneExpression().get(0));
        initialState.setGeneExpressionY(initialDto.getGeneExpression().get(1));
        initialState.setCellType(initialDto.getType());
        initialState.setId(null);

        return Flux.range(0, steps)
                .scan(initialState, (currentState, step) ->
                        calculateNextState(currentState, step, simulationRunId))
                .map(this::toDto);
    }

    private CellState calculateNextState(CellState currentState, Integer step, Long simulationRunId) {
        double x = currentState.getGeneExpressionX() == null ? 0.0 : currentState.getGeneExpressionX();
        double y = currentState.getGeneExpressionY() == null ? 0.0 : currentState.getGeneExpressionY();

        double dxdt = calculateGeneRegulatoryFunction(x, y, 0);
        double dydt = calculateGeneRegulatoryFunction(x, y, 1);

        double noiseX = (random.nextDouble() - 0.5) * NOISE_AMPLITUDE;
        double noiseY = (random.nextDouble() - 0.5) * NOISE_AMPLITUDE;

        double newX = x + dxdt + noiseX;
        double newY = y + dydt + noiseY;
        double potential = calculatePotential(newX, newY);

        CellType cellType = determineCellType(newX, newY);

        CellState next = new CellState();
        next.setId(null);
        next.setStateName("run:" + (simulationRunId == null ? "0" : simulationRunId) + ":step:" + step);
        next.setGeneExpressionX(newX);
        next.setGeneExpressionY(newY);
        next.setPotentialValue(potential);
        next.setCellType(cellType);
        return next;
    }

    private double calculateGeneRegulatoryFunction(double x, double y, int dimension) {
        if (dimension == 0) {
            return (1.0 / (1.0 + Math.pow(y, 4))) - DEGRADATION_RATE * x;
        } else {
            return (1.0 / (1.0 + Math.pow(x, 4))) - DEGRADATION_RATE * y;
        }
    }

}
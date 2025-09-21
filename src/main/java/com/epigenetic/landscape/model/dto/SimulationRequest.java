package com.epigenetic.landscape.model.dto;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SimulationRequest {
    private int burnIn;
    @Positive(message = "Time step must be positive")
    private double delta;
    @Positive(message = "Diffusion coefficient must be positive")
    private double diffusionCoefficient;
    @Positive(message = "Number of bins must be positive")
    private int bins;
    @Positive(message = "Sample limit must be positive")
    private int sampleLimit;
    @NotNull(message = "Start vector must not be null")
    @Size(min = 2, message = "Start vector must have at least 2 elements")
    private double[] start;
    private double[] nodeA;
    private double[] nodeB;
    @NotNull(message = "initial X is required")
    private Double initialX;
    @NotNull(message = "initial Y is required")
    private Double initialY;
    @NotNull(message = "initial Z is required")
    private Double initialZ;
    private Integer steps;
    private Integer ensembleRuns;
    @NotNull(message = "timeStep must not be null")
    @Positive(message = "timeStep must be positive")
    private Double timeStep;
    private Boolean includeStochasticity;
}

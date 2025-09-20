package com.epigenetic.landscape.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SimulationRequest {
    private int burnIn;
    private double dt;
    private double diffusionCoefficient;
    private int bins;
    private int sampleLimit;
    private double[] start;
    private double[] nodeA;
    private double[] nodeB;
    private Double initialX;
    private Double initialY;
    private Integer steps;
    private Integer ensembleRuns;
    private Double timeStep;
    private Boolean includeStochasticity;
}

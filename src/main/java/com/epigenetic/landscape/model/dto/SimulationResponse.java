package com.epigenetic.landscape.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SimulationResponse {
    public double[][] samples;
    public double[][] nodes;
    public double[][] potentialPoints;
    private List<Double> xValues;
    private List<Double> yValues;
    private List<Double> zValues;
    private List<Double> potentialValues;
    private List<String> cellTypes;
}

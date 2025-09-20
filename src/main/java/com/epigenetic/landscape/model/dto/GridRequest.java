package com.epigenetic.landscape.model.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GridRequest {
    private double[][][] potentialGrid;
    private double[] xGrid;
    private double[] yGrid;
    private double[] zGrid;
}
package com.epigenetic.landscape.model;

import lombok.Data;

@Data
public class GridRequest {
    private double[][][] potentialGrid;
    private double[] xGrid;
    private double[] yGrid;
    private double[] zGrid;
}
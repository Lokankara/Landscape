package com.epigenetic.landscape.model;


import lombok.Data;

import java.util.List;

@Data
public class RefineRequest {
    private List<double[]> seeds;
    private int fullDim;
    private double tolerance;
    private int maxIter;
}

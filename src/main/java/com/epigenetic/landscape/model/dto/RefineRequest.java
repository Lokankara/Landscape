package com.epigenetic.landscape.model.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class RefineRequest {
    private List<double[]> seeds;
    private int fullDim;
    private double tolerance;
    private int maxIter;
}

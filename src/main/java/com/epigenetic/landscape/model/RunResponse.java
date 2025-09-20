package com.epigenetic.landscape.model;

import lombok.Data;

import java.util.List;

@Data
public class RunResponse {
    List<List<double[]>> sampleTrajectories;
    List<?> potentialCells;
    List<double[]> attractors;
}

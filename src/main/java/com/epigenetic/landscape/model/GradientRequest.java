package com.epigenetic.landscape.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GradientRequest {
    private double[] point;
    private List<double[]> potentialPoints;
    private double diffusionCoefficient;
}

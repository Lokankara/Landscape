package com.epigenetic.landscape.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GradientRequest {
    private double[] point;
    private List<double[]> potentialPoints;
    private double diffusionCoefficient;
}

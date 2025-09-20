package com.epigenetic.landscape.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class Point3D {
    private final double x;
    private final double y;
    private final double z;
}

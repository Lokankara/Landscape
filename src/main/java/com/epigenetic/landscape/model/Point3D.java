package com.epigenetic.landscape.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public final class Point3D {
    private final double x;
    private final double y;
    private final double z;
}

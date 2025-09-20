package com.epigenetic.landscape.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Data
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CellDto {
    private String id;
    private List<Double> geneExpression;
    private List<Double> potentialGradient;
    private CellType type;

    public CellDto(int dimensions) {
        this.geneExpression = new ArrayList<>(dimensions);
        this.potentialGradient = new ArrayList<>(dimensions);
        for (int i = 0; i < dimensions; i++) {
            geneExpression.add(0.0);
            potentialGradient.add(0.0);
        }
    }
}
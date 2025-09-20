package com.epigenetic.landscape.model.dto;

import com.epigenetic.landscape.model.CellType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CellDto {
    private Long id;
    private List<Double> geneExpression;
    private List<Double> potentialGradient;
    private CellType type;

    public CellDto(int dimensions) {
        geneExpression = new ArrayList<>(dimensions);
        potentialGradient = new ArrayList<>(dimensions);
        for (int i = 0; i < dimensions; i++) {
            geneExpression.add(0.0);
            potentialGradient.add(0.0);
        }
    }
}

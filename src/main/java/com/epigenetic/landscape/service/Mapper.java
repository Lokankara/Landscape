package com.epigenetic.landscape.service;

import com.epigenetic.landscape.model.dto.CellDto;
import com.epigenetic.landscape.model.CellState;

import java.util.Arrays;

public class Mapper {
    public static CellDto toDto(CellState state) {
        CellDto dto = new CellDto(3);
        dto.setId(state.getId() != null ? state.getId() : 0);
        dto.getGeneExpression().set(0, state.getGeneExpressionX() != null ? state.getGeneExpressionX() : 0.0);
        dto.getGeneExpression().set(1, state.getGeneExpressionY() != null ? state.getGeneExpressionY() : 0.0);
        dto.getGeneExpression().set(2, state.getGeneExpressionZ() != null ? state.getGeneExpressionZ() : 0.0);
        dto.setType(state.getCellType());
        dto.setPotentialGradient(Arrays.asList(0.0, 0.0, 0.0));
        return dto;
    }
}

package com.epigenetic.landscape.service;

import com.epigenetic.landscape.model.dto.CellDto;
import com.epigenetic.landscape.analysis.PCAService;
import org.springframework.stereotype.Service;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class VisualizationService {

    private final GRNService grnService;
    private final PCAService pcaService;

    public List<double[]> performPCA(List<CellDto> cellDtos) {
        List<CellDto> source = cellDtos;
        if (source == null || source.isEmpty()) {
            source = grnService.getAllStatesDto()
                    .collectList()
                    .block();
            if (source == null)
                source = List.of();
        }

        int n = source.size();
        if (n == 0)
            return List.of();

        double[][] X = new double[n][3];
        for (int i = 0; i < n; i++) {
            List<Double> ge = source.get(i).getGeneExpression();
            double x = (ge != null && !ge.isEmpty() && ge.get(0) != null) ? ge.get(0) : 0.0;
            double y = (ge != null && ge.size() > 1 && ge.get(1) != null) ? ge.get(1) : 0.0;
            Double pot = source.get(i).getPotentialGradient().getFirst();
            X[i][0] = x;
            X[i][1] = y;
            X[i][2] = pot != null ? pot : grnService.calculatePotential(x, y);
        }
        return Arrays.stream(pcaService.project(X, 3), 0, n)
                .collect(Collectors.toCollection(() -> new ArrayList<>(n)));
    }
}

package com.epigenetic.landscape.service;

import com.epigenetic.landscape.analysis.AttractorFinder;
import org.springframework.stereotype.Service;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class AttractorService {


    public List<double[]> findGridAttractors(double[][][] potentialGrid, double[] xgrid, double[] ygrid,
            double[] zgrid) {
        AttractorFinder finder = new AttractorFinder();
        return finder.gridMinima(potentialGrid, xgrid, ygrid, zgrid);
    }

    public List<double[]> refineAttractors(List<double[]> seeds, int fullDim, double tol, int maxIter) {
        AttractorFinder finder = new AttractorFinder();
        List<double[]> refined = new ArrayList<>();
        for (double[] seed : seeds) {
            double[] attractor = finder.refine(seed, fullDim, tol, maxIter);
            refined.add(attractor);
        }
        return refined;
    }

    public double[] refineSingleAttractor(double[] seed, int fullDim, double tol, int maxIter) {
        AttractorFinder finder = new AttractorFinder();
        return finder.refine(seed, fullDim, tol, maxIter);
    }
}

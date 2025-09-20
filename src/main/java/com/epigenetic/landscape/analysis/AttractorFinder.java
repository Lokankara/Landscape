package com.epigenetic.landscape.analysis;

import com.epigenetic.landscape.model.dto.GRNModel;
import com.epigenetic.landscape.model.SimpleMatrix;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter

public class AttractorFinder {

    public List<double[]> gridMinima(double[][][] U, double[] xgrid, double[] ygrid, double[] zgrid) {
        List<double[]> minima = new ArrayList<>();
        int nx = U.length, ny = U[0].length, nz = U[0][0].length;
        for (int i = 1; i < nx - 1; i++)
            for (int j = 1; j < ny - 1; j++)
                for (int k = 1; k < nz - 1; k++) {
                    double v = U[i][j][k];
                    if (v <= U[i - 1][j][k] && v <= U[i + 1][j][k] &&
                            v <= U[i][j - 1][k] && v <= U[i][j + 1][k] &&
                            v <= U[i][j][k - 1] && v <= U[i][j][k + 1]) {
                        minima.add(new double[] {xgrid[i], ygrid[j], zgrid[k]});
                    }
                }
        return minima;
    }

    public double[] refine(double[] seed, int fullDim, double tol, int maxIter) {
        double[] x = new double[fullDim];
        System.arraycopy(seed, 0, x, 0, Math.min(seed.length, fullDim));
        for (int iter = 0; iter < maxIter; iter++) {
            double[] f = driftToArray(x);
            double[][] J = numericJacobian(x);
            double[] dx = solveLinearSystem(J, f);
            double norm = 0;
            for (double v : dx)
                norm += v * v;
            norm = Math.sqrt(norm);
            for (int i = 0; i < fullDim; i++)
                x[i] -= dx[i];
            if (norm < tol)
                break;
        }
        return x;
    }

    private double[] driftToArray(double[] x) {
        SimpleMatrix xm = SimpleMatrix.colVector(x);
        GRNModel model = GRNModel.builder().build();
        SimpleMatrix fx = model.drift(xm);
        double[] out = new double[x.length];
        for (int i = 0; i < x.length; i++)
            out[i] = fx.get(i, 0);
        return out;
    }

    private double[][] numericJacobian(double[] x) {
        int n = x.length;
        double h = 1e-6;
        double[][] J = new double[n][n];
        double[] f0 = driftToArray(x);
        for (int j = 0; j < n; j++) {
            double[] xp = x.clone();
            xp[j] += h;
            double[] fp = driftToArray(xp);
            for (int i = 0; i < n; i++)
                J[i][j] = (fp[i] - f0[i]) / h;
        }
        return J;
    }

    private double[] solveLinearSystem(double[][] A, double[] b) {
        Array2DRowRealMatrix matA = new Array2DRowRealMatrix(A);
        Array2DRowRealMatrix vecB = new Array2DRowRealMatrix(
                Arrays.stream(b).mapToObj(v -> new double[] {v}).toArray(double[][]::new)
        );

        int n = matA.getRowDimension();
        double[][] M = new double[n][n + 1];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++)
                M[i][j] = matA.getEntry(i, j);
            M[i][n] = vecB.getEntry(i, 0);
        }

        for (int i = 0; i < n; i++) {
            int pivot = i;
            for (int r = i + 1; r < n; r++)
                if (Math.abs(M[r][i]) > Math.abs(M[pivot][i]))
                    pivot = r;
            double[] tmp = M[i];
            M[i] = M[pivot];
            M[pivot] = tmp;

            double diag = M[i][i];
            if (Math.abs(diag) < 1e-12)
                return new double[n];

            for (int j = i; j <= n; j++)
                M[i][j] /= diag;

            for (int r = 0; r < n; r++)
                if (r != i) {
                    double factor = M[r][i];
                    for (int j = i; j <= n; j++)
                        M[r][j] -= factor * M[i][j];
                }
        }

        double[] x = new double[n];
        for (int i = 0; i < n; i++)
            x[i] = M[i][n];
        return x;
    }
}

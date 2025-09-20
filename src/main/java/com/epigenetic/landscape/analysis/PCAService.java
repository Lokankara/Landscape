package com.epigenetic.landscape.analysis;

import org.springframework.stereotype.Service;

@Service
public class PCAService {

    public double[][] project(double[][] data, int k) {
        int m = data.length;
        int n = data[0].length;
        double[] mean = new double[n];
        for (double[] datum : data)
            for (int j = 0; j < n; j++)
                mean[j] += datum[j];
        for (int j = 0; j < n; j++)
            mean[j] /= m;
        double[][] X = new double[m][n];
        for (int i = 0; i < m; i++)
            for (int j = 0; j < n; j++)
                X[i][j] = data[i][j] - mean[j];
        double[][] cov = new double[n][n];
        for (int i = 0; i < m; i++)
            for (int a = 0; a < n; a++)
                for (int b = 0; b < n; b++)
                    cov[a][b] += X[i][a] * X[i][b];
        for (int a = 0; a < n; a++)
            for (int b = 0; b < n; b++)
                cov[a][b] /= (m - 1.0);
        double[][] comps = new double[k][n];
        double[][] covCopy = cov;
        for (int comp = 0; comp < k; comp++) {
            double[] v = powerIteration(covCopy, n);
            System.arraycopy(v, 0, comps[comp], 0, n);
            covCopy = deflate(covCopy, v);
        }
        double[][] proj = new double[m][k];
        for (int i = 0; i < m; i++)
            for (int c = 0; c < k; c++) {
                double sum = 0;
                for (int j = 0; j < n; j++)
                    sum += X[i][j] * comps[c][j];
                proj[i][c] = sum;
            }
        return proj;
    }

    private static double[] powerIteration(double[][] A, int n) {
        double[] b = new double[n];
        for (int i = 0; i < n; i++)
            b[i] = Math.random();
        for (int it = 0; it < 1000; it++) {
            double[] nb = matVec(A, b);
            double norm = 0;
            for (double val : nb)
                norm += val * val;
            norm = Math.sqrt(norm);
            if (norm == 0)
                break;
            for (int i = 0; i < n; i++)
                nb[i] /= norm;
            double diff = 0;
            for (int i = 0; i < n; i++)
                diff += Math.abs(nb[i] - b[i]);
            b = nb;
            if (diff < 1e-9)
                break;
        }
        return b;
    }

    private static double[] matVec(double[][] A, double[] v) {
        int n = v.length;
        double[] out = new double[n];
        for (int i = 0; i < n; i++) {
            double s = 0;
            for (int j = 0; j < n; j++)
                s += A[i][j] * v[j];
            out[i] = s;
        }
        return out;
    }

    private static double[][] deflate(double[][] A, double[] v) {
        int n = v.length;
        double[][] B = new double[n][n];
        double[] Av = matVec(A, v);
        double lambda = 0;
        for (int i = 0; i < n; i++)
            lambda += v[i] * Av[i];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                B[i][j] = A[i][j] - lambda * v[i] * v[j];
        return B;
    }
}

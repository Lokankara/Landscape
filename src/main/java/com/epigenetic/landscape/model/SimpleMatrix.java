package com.epigenetic.landscape.model;

import lombok.Getter;

@Getter
public class SimpleMatrix {
    private final int rows;
    private final int cols;
    private final double[] data;

    public SimpleMatrix(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.data = new double[rows * cols];
    }

    public SimpleMatrix(int rows, int cols, double[] src) {
        this.rows = rows;
        this.cols = cols;
        this.data = src.clone();
    }

    public static SimpleMatrix colVector(double[] v) {
        return new SimpleMatrix(v.length, 1, v);
    }

    public double get(int r, int c) {
        return data[r * cols + c];
    }

    public void set(int r, int c, double v) {
        data[r * cols + c] = v;
    }

    public SimpleMatrix plus(SimpleMatrix o) {
        SimpleMatrix r = new SimpleMatrix(rows, cols);
        for (int i = 0; i < data.length; i++)
            r.data[i] = this.data[i] + o.data[i];
        return r;
    }

    public SimpleMatrix minus(SimpleMatrix o) {
        SimpleMatrix r = new SimpleMatrix(rows, cols);
        for (int i = 0; i < data.length; i++)
            r.data[i] = this.data[i] - o.data[i];
        return r;
    }

    public SimpleMatrix scale(double s) {
        SimpleMatrix r = new SimpleMatrix(rows, cols);
        for (int i = 0; i < data.length; i++)
            r.data[i] = this.data[i] * s;
        return r;
    }

    public SimpleMatrix transpose() {
        SimpleMatrix t = new SimpleMatrix(cols, rows);
        for (int r = 0; r < rows; r++)
            for (int c = 0; c < cols; c++)
                t.set(c, r, get(r, c));
        return t;
    }

    public SimpleMatrix multiply(SimpleMatrix o) {
        SimpleMatrix m = new SimpleMatrix(rows, o.cols);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < o.cols; j++) {
                double s = 0;
                for (int k = 0; k < cols; k++)
                    s += get(i, k) * o.get(k, j);
                m.set(i, j, s);
            }
        }
        return m;
    }

    public SimpleMatrix elementMult(SimpleMatrix o) {
        SimpleMatrix r = new SimpleMatrix(rows, cols);
        for (int i = 0; i < data.length; i++)
            r.data[i] = this.data[i] * o.data[i];
        return r;
    }

    public double normF() {
        double s = 0;
        for (double v : data)
            s += v * v;
        return Math.sqrt(s);
    }

    public SimpleMatrix cloneMatrix() {
        return new SimpleMatrix(rows, cols, data);
    }

    public SimpleMatrix solve(SimpleMatrix b) {
        int n = rows;
        double[][] A = new double[n][n + b.cols];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++)
                A[i][j] = get(i, j);
            for (int j = 0; j < b.cols; j++)
                A[i][n + j] = b.get(i, j);
        }
        for (int i = 0; i < n; i++) {
            int pivot = i;
            for (int r = i + 1; r < n; r++)
                if (Math.abs(A[r][i]) > Math.abs(A[pivot][i]))
                    pivot = r;
            double[] tmp = A[i];
            A[i] = A[pivot];
            A[pivot] = tmp;
            double diag = A[i][i];
            if (Math.abs(diag) < 1e-12)
                throw new RuntimeException("Singular matrix");
            for (int j = i; j < n + b.cols; j++)
                A[i][j] /= diag;
            for (int r = 0; r < n; r++)
                if (r != i) {
                    double fac = A[r][i];
                    for (int j = i; j < n + b.cols; j++)
                        A[r][j] -= fac * A[i][j];
                }
        }
        SimpleMatrix x = new SimpleMatrix(n, b.cols);
        for (int i = 0; i < n; i++)
            for (int j = 0; j < b.cols; j++)
                x.set(i, j, A[i][n + j]);
        return x;
    }
}

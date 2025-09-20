package com.epigenetic.landscape.analysis;

import lombok.Getter;

import java.util.stream.IntStream;

public class Array2DRowRealMatrix implements RealMatrix {
    private final int rows;
    private final int cols;
    @Getter
    private final double[][] data;

    public Array2DRowRealMatrix(double[][] arr) {
        this.rows = arr.length;
        this.cols = arr.length > 0 ? arr[0].length : 0;
        this.data = new double[rows][cols];
        IntStream.range(0, rows).forEach(i -> System.arraycopy(arr[i], 0, data[i], 0, cols));
    }

    public int getRowDimension() {
        return rows;
    }

    public int getColumnDimension() {
        return cols;
    }

    public double getEntry(int r, int c) {
        return data[r][c];
    }

    public void setEntry(int r, int c, double v) {
        data[r][c] = v;
    }

    public RealMatrix multiply(RealMatrix o) {
        int r = rows;
        int c = o.getColumnDimension();
        int m = cols;
        double[][] out = new double[r][c];
        for (int i = 0; i < r; i++)
            for (int j = 0; j < c; j++) {
                double s = 0;
                for (int k = 0; k < m; k++)
                    s += data[i][k] * o.getEntry(k, j);
                out[i][j] = s;
            }
        return new Array2DRowRealMatrix(out);
    }

    public RealMatrix transpose() {
        double[][] t = new double[cols][rows];
        for (int i = 0; i < rows; i++)
            for (int j = 0; j < cols; j++)
                t[j][i] = data[i][j];
        return new Array2DRowRealMatrix(t);
    }
}

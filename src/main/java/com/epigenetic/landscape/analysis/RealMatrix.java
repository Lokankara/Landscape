package com.epigenetic.landscape.analysis;

public interface RealMatrix {
    int getRowDimension();

    int getColumnDimension();

    double getEntry(int r, int c);

    void setEntry(int r, int c, double v);

    RealMatrix multiply(RealMatrix o);

    RealMatrix transpose();
}

package com.epigenetic.landscape.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GRNModel {
    private Integer n;
    private SimpleMatrix W;
    private SimpleMatrix basal;
    private SimpleMatrix decay;

    public SimpleMatrix drift(SimpleMatrix x) {
        SimpleMatrix s = W.multiply(x).plus(basal);
        SimpleMatrix out = new SimpleMatrix(n, 1);
        for (int i = 0; i < n; i++) {
            double v = s.get(i, 0);
            double sig = 1.0 / (1.0 + Math.exp(-v));
            out.set(i, 0, sig - decay.get(i, 0) * x.get(i, 0));
        }
        return out;
    }
}

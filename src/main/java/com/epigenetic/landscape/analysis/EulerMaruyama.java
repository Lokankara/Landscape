package com.epigenetic.landscape.analysis;

import com.epigenetic.landscape.model.dto.GRNModel;
import com.epigenetic.landscape.model.SimpleMatrix;
import lombok.Data;

import java.util.Random;

@Data
public class EulerMaruyama {

    private final Random rnd;
    private final GRNModel model;

    public EulerMaruyama(long seed) {
        rnd = new Random(seed);
        model = new GRNModel();
    }

    public EulerMaruyama(long seed, GRNModel model) {
        rnd = new Random(seed);
        this.model = model;
    }

    public SimpleMatrix step(SimpleMatrix x, double dt, double sigma) {
        SimpleMatrix f = GRNModel.builder().build().drift(x);
        SimpleMatrix noise = new SimpleMatrix(x.getRows(), x.getCols());
        for (int i = 0; i < x.getRows(); i++)
            noise.set(i, 0, rnd.nextGaussian() * Math.sqrt(dt) * sigma);
        return x.plus(f.scale(dt)).plus(noise);
    }
}

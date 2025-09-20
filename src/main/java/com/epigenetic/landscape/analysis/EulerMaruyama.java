package com.epigenetic.landscape.analysis;

import com.epigenetic.landscape.model.GRNModel;
import com.epigenetic.landscape.model.SimpleMatrix;
import lombok.Data;

import java.util.Random;

@Data
public class EulerMaruyama {

    private final Random rnd = new Random();

    public SimpleMatrix step(SimpleMatrix x, double dt, double sigma) {
        SimpleMatrix f = GRNModel.builder().build().drift(x);
        SimpleMatrix noise = new SimpleMatrix(x.getRows(), x.getCols());
        for (int i = 0; i < x.getRows(); i++)
            noise.set(i, 0, rnd.nextGaussian() * Math.sqrt(dt) * sigma);
        return x.plus(f.scale(dt)).plus(noise);
    }
}

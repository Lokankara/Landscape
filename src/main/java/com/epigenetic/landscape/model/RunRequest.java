package com.epigenetic.landscape.model;

import lombok.Data;

@Data
public class RunRequest {
    int n = 10;
    double dt = 0.01;
    int steps = 500;
    int runs = 200;
    double sigma = 0.1;
}

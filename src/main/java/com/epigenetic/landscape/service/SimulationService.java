package com.epigenetic.landscape.service;

import com.epigenetic.landscape.analysis.EulerMaruyama;
import com.epigenetic.landscape.model.CellDto;
import com.epigenetic.landscape.model.GRNModel;
import com.epigenetic.landscape.model.SimpleMatrix;
import com.epigenetic.landscape.model.SimulationRequest;
import com.epigenetic.landscape.model.SimulationResult;
import org.springframework.stereotype.Service;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@AllArgsConstructor
public class SimulationService {

    private final GRNService grnService;

    public double[] computeGradient(double[] point, List<double[]> potentialPoints, double D) {
        return gradPotential(point, potentialPoints, D);
    }

    public List<List<double[]>> simulateEnsemble(double[] x0, int runs, double dt, int steps, double sigma) {
        return IntStream.range(0, runs).mapToObj(r -> simulateTrajectory(x0, dt, steps, sigma))
                .collect(Collectors.toList());
    }

    public List<double[]> simulateTrajectory(double[] x0, double dt, int steps, double sigma) {
        SimpleMatrix x = SimpleMatrix.colVector(x0);
        List<double[]> traj = new ArrayList<>();
        for (int t = 0; t < steps; t++) {
            x = new EulerMaruyama().step(x, dt, sigma);
            double[] state = new double[x.getRows()];
            for (int i = 0; i < x.getRows(); i++)
                state[i] = x.get(i, 0);
            traj.add(state);
        }
        return traj;
    }

    public List<CellDto> simulateCellTrajectory(CellDto initialState, double timeStep, int totalSteps) {
        List<CellDto> trajectory = new ArrayList<>();
        CellDto current = initialState;
        trajectory.add(current);
        for (int step = 1; step <= totalSteps; step++) {
            current = grnService.calculateGeneRegulatoryDynamics(current);
            trajectory.add(current);
        }
        return trajectory;
    }

    public List<double[]> findStableStates() {
        return List.of(
                new double[] {0.2, 3.8},
                new double[] {3.8, 0.2},
                new double[] {1.0, 1.0}
        );
    }

    private double[][] initLinearNodes(double[] a, double[] b, int m) {
        double[][] nodes = new double[m][3];
        for (int i = 0; i < m; i++) {
            double t = (double) i / (m - 1);
            nodes[i][0] = a[0] * (1 - t) + b[0] * t;
            nodes[i][1] = a[1] * (1 - t) + b[1] * t;
            nodes[i][2] = a[2] * (1 - t) + b[2] * t;
        }
        return nodes;
    }

    private double[] gradPotential(double[] x, List<double[]> potentialPoints, double D) {
        double gx = 0.0, gy = 0.0, gz = 0.0;
        double eps = 1e-8;
        for (double[] p : potentialPoints) {
            double dx = x[0] - p[0];
            double dy = x[1] - p[1];
            double dz = x[2] - p[2];
            double u = p[3];
            double w = Math.exp(-u / (D + eps));
            gx += w * dx;
            gy += w * dy;
            gz += w * dz;
        }
        double norm = Math.max(1e-8, Math.sqrt(gx * gx + gy * gy + gz * gz));
        return new double[] {gx / norm, gy / norm, gz / norm};
    }

    private void reparametrize(double[][] arr) {
        int m = arr.length;
        double[] dist = new double[m];
        dist[0] = 0.0;
        for (int i = 1; i < m; i++) {
            double dx = arr[i][0] - arr[i - 1][0];
            double dy = arr[i][1] - arr[i - 1][1];
            double dz = arr[i][2] - arr[i - 1][2];
            dist[i] = dist[i - 1] + Math.sqrt(dx * dx + dy * dy + dz * dz);
        }
        double L = dist[m - 1];
        if (L <= 0)
            return;
        double[][] interp = new double[m][3];
        for (int i = 0; i < m; i++) {
            double s = (double) i / (m - 1) * L;
            int idx = 0;
            while (idx < m - 1 && dist[idx + 1] < s)
                idx++;
            if (idx == m - 1)
                interp[i] = arr[m - 1].clone();
            else {
                double d0 = dist[idx];
                double d1 = dist[idx + 1];
                double alpha = (s - d0) / (d1 - d0);
                interp[i][0] = arr[idx][0] * (1 - alpha) + arr[idx + 1][0] * alpha;
                interp[i][1] = arr[idx][1] * (1 - alpha) + arr[idx + 1][1] * alpha;
                interp[i][2] = arr[idx][2] * (1 - alpha) + arr[idx + 1][2] * alpha;
            }
        }
        System.arraycopy(interp, 0, arr, 0, m);
    }

    private double[][] downsample(double[][] arr, int limit) {
        if (arr.length <= limit)
            return arr;
        double[][] out = new double[limit][3];
        double step = (double) arr.length / limit;
        for (int i = 0; i < limit; i++)
            out[i] = arr[(int) (i * step)];
        return out;
    }

    public List<double[]> simulateTrajectory(GRNModel model, double[] x0, double dt, int steps, double sigma,
            long seed) {
        EulerMaruyama em = new EulerMaruyama();
        int n = x0.length;
        double[] x = x0.clone();
        List<double[]> traj = new ArrayList<>();
        for (int t = 0; t < steps; t++) {
            SimpleMatrix xm = SimpleMatrix.colVector(x);
            xm = em.step(xm, dt, sigma);
            for (int i = 0; i < n; i++)
                x[i] = xm.get(i, 0);
            double[] copy = new double[n];
            System.arraycopy(x, 0, copy, 0, n);
            traj.add(copy);
        }
        return traj;
    }

    public SimulationResult runSimulation(SimulationRequest request) {
        double initX = (request.getStart() != null && request.getStart().length > 0) ? request.getStart()[0] : 0.1;
        double initY = (request.getStart() != null && request.getStart().length > 1) ? request.getStart()[1] : 0.1;
        double timeStep = request.getDt() > 0 ? request.getDt() : 0.01;
        int steps = request.getSampleLimit() > 0 ? request.getSampleLimit() : 500;
        int ensembleRuns = Math.max(request.getBins(), 0);
        double sigma = request.getDiffusionCoefficient() > 0 ? request.getDiffusionCoefficient() : 0.05;

        CellDto initial = new CellDto(2);
        initial.getGeneExpression().set(0, initX);
        initial.getGeneExpression().set(1, initY);
        initial.setType(null);

        List<CellDto> cellTrajectory = simulateCellTrajectory(initial, timeStep, steps);

        double[][] samples = new double[cellTrajectory.size()][3];
        List<double[]> potentialList = new ArrayList<>();
        for (int i = 0; i < cellTrajectory.size(); i++) {
            CellDto c = cellTrajectory.get(i);
            double x = c.getGeneExpression().get(0);
            double y = c.getGeneExpression().get(1);
            Double potVal = (c.getPotentialGradient() != null && !c.getPotentialGradient().isEmpty())
                    ? c.getPotentialGradient().getFirst() : null;
            double pot = potVal != null ? potVal : grnService.calculatePotential(x, y);
            samples[i][0] = x;
            samples[i][1] = y;
            samples[i][2] = pot;
            potentialList.add(new double[] {x, y, 0.0, pot});
        }

        double[][] nodes;
        if (request.getNodeA() != null && request.getNodeB() != null
                && request.getNodeA().length >= 3 && request.getNodeB().length >= 3) {
            nodes = initLinearNodes(new double[] {request.getNodeA()[0], request.getNodeA()[1], request.getNodeA()[2]},
                    new double[] {request.getNodeB()[0], request.getNodeB()[1], request.getNodeB()[2]},
                    Math.max(2, request.getBins()));
        } else {
            List<double[]> stable = grnService.getStableStates();
            double[] a = stable.get(0);
            double[] b = stable.size() > 1 ? stable.get(1) : stable.get(0);
            double[] a3 = new double[] {a[0], a[1], 0.0};
            double[] b3 = new double[] {b[0], b[1], 0.0};
            nodes = initLinearNodes(a3, b3, Math.max(2, request.getBins()));
        }

        if (request.getBurnIn() > 0)
            reparametrize(nodes);
        int downsampleLimit =
                request.getSampleLimit() > 0 ? Math.min(request.getSampleLimit(), nodes.length) : nodes.length;
        nodes = downsample(nodes, downsampleLimit);

        double[][] potentialPoints = new double[potentialList.size()][4];
        for (int i = 0; i < potentialList.size(); i++)
            potentialPoints[i] = potentialList.get(i);

        SimulationResult result = SimulationResult.builder()
                .samples(samples)
                .nodes(nodes)
                .potentialPoints(potentialPoints)
                .build();

        if (ensembleRuns > 0) {
            double[] x0 = new double[] {initX, initY};
            List<List<double[]>> ensemble = simulateEnsemble(x0, ensembleRuns, timeStep, steps, sigma);
            List<double[]> first = ensemble.isEmpty() ? List.of() : ensemble.getFirst();
            result.setSamples(first.toArray(new double[first.size()][]));
        }

        return result;
    }

    public List<List<double[]>> simulateEnsemble(GRNModel model, SimulationRequest request) {
        double[] x0 = request.getStart();
        double dt = request.getDt() > 0 ? request.getDt() : 0.01;
        int steps = request.getSampleLimit() > 0 ? request.getSampleLimit() : 500;
        double sigma = request.getDiffusionCoefficient() > 0 ? request.getDiffusionCoefficient() : 0.05;
        int runs = request.getBins() > 0 ? request.getBins() : 1;

        List<List<double[]>> ensemble = new ArrayList<>();
        for (int r = 0; r < runs; r++)
            ensemble.add(simulateTrajectory(model, x0, dt, steps, sigma, r + 1));

        return ensemble;
    }
}

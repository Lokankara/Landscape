package com.epigenetic.landscape.conrtoller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyInt;

import com.epigenetic.landscape.model.dto.CellDto;
import com.epigenetic.landscape.model.dto.GradientRequest;
import com.epigenetic.landscape.model.dto.SimulationRequest;
import com.epigenetic.landscape.model.dto.SimulationResponse;
import com.epigenetic.landscape.service.SimulationService;
import com.epigenetic.landscape.service.VisualizationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.Collections;
import java.util.List;

public class SimulationControllerTest extends BaseControllerTest {

    private SimulationService simulationService;
    private VisualizationService visualizationService;

    @BeforeEach
    void setUp() {
        simulationService = Mockito.mock(SimulationService.class);
        visualizationService = Mockito.mock(VisualizationService.class);
        SimulationController controller = new SimulationController(simulationService, visualizationService);
        webTestClient = WebTestClient.bindToController(controller).build();
    }

    @Test
    void runSimulationPositive() {
        Mockito.when(simulationService.runSimulation(any(SimulationRequest.class)))
                .thenReturn(SimulationResponse.builder().build());

        SimulationRequest request = new SimulationRequest();
        request.setSteps(10);
        request.setTimeStep(0.1);
        request.setInitialX(0.0);
        request.setInitialY(0.0);
        request.setInitialZ(0.0);

        webTestClient.post().uri("/api/simulation/run")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isOk();
    }


    @Test
    void runSimulationNegative() {
        webTestClient.post().uri("/api/simulation/run")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new SimulationRequest())
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void computeGradientPositive() {
        double[] grad = new double[] {0.1, 0.2, 0.3};
        Mockito.when(simulationService.computeGradient(any(), any(), anyDouble())).thenReturn(grad);

        GradientRequest request = new GradientRequest();
        request.setPoint(new double[] {1, 2, 3});
        request.setDiffusionCoefficient(0.1);

        webTestClient.post().uri("/api/simulation/gradient")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void computeGradientNegative() {
        webTestClient.post().uri("/api/simulation/gradient")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new GradientRequest())
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void simulateTrajectoryPositive() {
        List<double[]> trajectory = Collections.singletonList(new double[] {1.0, 2.0});
        Mockito.when(simulationService.simulateTrajectory(any(), anyDouble(), anyInt(), anyDouble()))
                .thenReturn(trajectory);

        SimulationRequest request = new SimulationRequest();
        request.setStart(new double[] {0.0, 0.0});
        request.setDelta(0.01);
        request.setSampleLimit(10);
        request.setDiffusionCoefficient(0.05);

        webTestClient.post().uri("/api/simulation/trajectory")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void simulateTrajectoryNegative() {
        webTestClient.post().uri("/api/simulation/trajectory")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new SimulationRequest())
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void simulateEnsemblePositive() {
        List<List<double[]>> ensemble = Collections.singletonList(Collections.singletonList(new double[] {1.0, 2.0}));
        Mockito.when(simulationService.simulateEnsemble(any(), anyInt(), anyDouble(), anyInt(), anyDouble()))
                .thenReturn(ensemble);

        SimulationRequest request = new SimulationRequest();
        request.setStart(new double[] {0.0, 0.0});
        request.setEnsembleRuns(5);
        request.setDelta(0.01);
        request.setSampleLimit(10);
        request.setDiffusionCoefficient(0.05);

        webTestClient.post().uri("/api/simulation/ensemble")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void simulateEnsembleNegative() {
        webTestClient.post().uri("/api/simulation/ensemble")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new SimulationRequest())
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void simulateAndReducePositive() {
        List<CellDto> trajectory = Collections.singletonList(new CellDto());
        Mockito.when(simulationService.simulateCellTrajectory(any(), anyDouble(), anyInt()))
                .thenReturn(trajectory);
        Mockito.when(visualizationService.performPCA(any()))
                .thenReturn(Collections.singletonList(new double[] {1.0, 2.0}));

        webTestClient.post().uri("/api/simulation")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new CellDto())
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void simulateAndReduceNegative() {
        webTestClient.post().uri("/api/simulation")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("")
                .exchange()
                .expectStatus().is4xxClientError();
    }

    @Test
    void getStableStatesPositive() {
        Mockito.when(simulationService.findStableStates())
                .thenReturn(Collections.singletonList(new double[] {1.0, 2.0}));

        webTestClient.get().uri("/api/simulation/stable-states")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void simulateEnsembleGRNModelPositive() {
        List<double[][]> ensembleArray = Collections.singletonList(new double[][] {{1.0, 2.0}});
        Mockito.when(simulationService.simulateEnsemble(any(SimulationRequest.class))).thenReturn(ensembleArray);

        SimulationRequest request = new SimulationRequest();
        request.setStart(new double[] {0.0, 0.0});

        webTestClient.post().uri("/api/simulation/ensemble/grn")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void simulateEnsembleGRNModelNegative() {
        webTestClient.post().uri("/api/simulation/ensemble/grn")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new SimulationRequest())
                .exchange()
                .expectStatus().isBadRequest();
    }
}
package com.epigenetic.landscape.conrtoller;

import static org.mockito.ArgumentMatchers.any;

import com.epigenetic.landscape.model.dto.CellDto;
import com.epigenetic.landscape.model.SimulationRequest;
import com.epigenetic.landscape.model.SimulationResponse;
import com.epigenetic.landscape.service.GRNService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;

import java.util.List;

public class LandscapeControllerTest extends BaseControllerTest {

    private GRNService grnService;

    @BeforeEach
    void setUp() {
        grnService = Mockito.mock(GRNService.class);
        LandscapeController controller = new LandscapeController(grnService);
        webTestClient = WebTestClient.bindToController(controller).build();
    }

    @Test
    void simulatePositive() {
        SimulationResponse result = new SimulationResponse();
        Mockito.when(grnService.simulateTrajectory(any(SimulationRequest.class)))
                .thenReturn(result);

        webTestClient.post().uri("/api/landscape/simulate")
                .bodyValue(new SimulationRequest())
                .exchange()
                .expectStatus().isOk()
                .expectBody(SimulationResponse.class).isEqualTo(result);
    }

    @Test
    void simulateNegativeNullRequest() {
        webTestClient.post().uri("/api/landscape/simulate")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("{}")
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void getAllStatesPositive() {
        Mockito.when(grnService.getAllStatesDto()).thenReturn(Flux.just(new CellDto(1)));

        webTestClient.get()
                .uri("/api/landscape/states")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void searchStatesPositive() {
        CellDto dto = new CellDto(3);
        Mockito.when(grnService.searchStates(any())).thenReturn(Flux.just(dto));

        webTestClient.get().uri(uriBuilder ->
                        uriBuilder.path("/api/landscape/states/search")
                                .queryParam("query", "test")
                                .build())
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(CellDto.class).hasSize(1);
    }

    @Test
    void getStableStatesPositive() {
        Mockito.when(grnService.getStableStates()).thenReturn(List.of(new double[] {0.1, 0.2}));

        webTestClient.get()
                .uri("/api/landscape/stable-states")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void simulateTrajectoryPositive() throws Exception {
        SimulationResponse result = SimulationResponse.builder().build();
        Mockito.when(grnService.simulateTrajectory(any(SimulationRequest.class))).thenReturn(result);

        SimulationRequest request = new SimulationRequest();
        webTestClient.post()
                .uri("/api/landscape/simulate")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(mapper.writeValueAsString(request))
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void simulateTrajectoryNegative() {
        webTestClient.post()
                .uri("/api/landscape/simulate")
                .contentType(MediaType.TEXT_PLAIN)
                .bodyValue("invalid")
                .exchange()
                .expectStatus().is4xxClientError();
    }

    @Test
    void getAllStatesNegative() {
        Mockito.when(grnService.getAllStatesDto()).thenThrow(new RuntimeException());
        webTestClient.get()
                .uri("/api/landscape/states")
                .exchange()
                .expectStatus().is5xxServerError();
    }

    @Test
    void searchStatesNegative() {
        webTestClient.get()
                .uri("/api/landscape/states/search")
                .exchange()
                .expectStatus().is4xxClientError();
    }

    @Test
    void getStableStatesNegative() {
        Mockito.when(grnService.getStableStates()).thenThrow(new RuntimeException());

        webTestClient.get()
                .uri("/api/landscape/stable-states")
                .exchange()
                .expectStatus().is5xxServerError();
    }
}

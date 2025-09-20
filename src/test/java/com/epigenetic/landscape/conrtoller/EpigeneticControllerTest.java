package com.epigenetic.landscape.conrtoller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;

import com.epigenetic.landscape.model.dto.CellDto;
import com.epigenetic.landscape.model.CellType;
import com.epigenetic.landscape.model.dto.Point3D;
import com.epigenetic.landscape.service.EpigeneticService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;

public class EpigeneticControllerTest extends BaseControllerTest {

    private EpigeneticService service;

    @BeforeEach
    void setUp() {
        service = Mockito.mock(EpigeneticService.class);
        EpigeneticController controller = new EpigeneticController(service);
        webTestClient = WebTestClient.bindToController(controller).build();
    }

    @Test
    void simulateNegativeMissingBody() {
        webTestClient.post()
                .uri(uriBuilder -> uriBuilder.path("/api/epigenetic/simulate")
                        .queryParam("steps", 2)
                        .queryParam("simulationRunId", 1L)
                        .build())
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void simulatePositive() {
        CellDto input = new CellDto();
        Mockito.when(service.simulateDifferentiation(any(), anyInt(), anyLong()))
                .thenReturn(Flux.just(input));

        webTestClient.post().uri(uriBuilder -> uriBuilder
                        .path("/api/epigenetic/simulate")
                        .queryParam("steps", 10)
                        .queryParam("simulationRunId", 1L)
                        .build())
                .bodyValue(input)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(CellDto.class)
                .hasSize(1);
    }

    @Test
    void simulateNegative() {
        webTestClient.post().uri("/api/epigenetic/simulate")
                .bodyValue(new CellDto())
                .exchange()
                .expectStatus().is4xxClientError();
    }

    @Test
    void simulateNegativeMissingParams() {
        CellDto input = new CellDto();

        webTestClient.post()
                .uri("/api/epigenetic/simulate")
                .bodyValue(input)
                .exchange()
                .expectStatus().is4xxClientError();
    }

    @Test
    void getLandscapePositive() {
        Point3D point = new Point3D(1, 2, 3);
        Mockito.when(service.getPotentialLandscape()).thenReturn(Flux.just(point));

        webTestClient.get().uri("/api/epigenetic/landscape")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Point3D.class)
                .hasSize(1);
    }

    @Test
    void getLandscapeEmpty() {
        Mockito.when(service.getPotentialLandscape()).thenReturn(Flux.empty());

        webTestClient.get().uri("/api/epigenetic/landscape")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Point3D.class)
                .hasSize(0);
    }

    @Test
    void getByTypePositive() {
        CellDto dto = new CellDto();
        Mockito.when(service.getStatesByType(any(CellType.class)))
                .thenReturn(Flux.just(dto));

        webTestClient.get().uri("/api/epigenetic/states/NEURON")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(CellDto.class)
                .hasSize(1);
    }

    @Test
    void getByTypeEmpty() {
        Mockito.when(service.getStatesByType(any(CellType.class))).thenReturn(Flux.empty());

        webTestClient.get().uri("/api/epigenetic/states/STEM")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(CellDto.class)
                .hasSize(0);
    }
}

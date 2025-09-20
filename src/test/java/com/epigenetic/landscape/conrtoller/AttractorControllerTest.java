package com.epigenetic.landscape.conrtoller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyInt;

import com.epigenetic.landscape.model.dto.DoubleArrayResponse;
import com.epigenetic.landscape.service.AttractorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.List;

public class AttractorControllerTest extends BaseControllerTest {

    private AttractorService service;

    @BeforeEach
    void setUp() {
        service = Mockito.mock(AttractorService.class);
        AttractorController controller = new AttractorController(service);
        webTestClient = WebTestClient.bindToController(controller).build();
    }

    @Test
    void findGridAttractorsPositive() {
        Mockito.when(service.findGridAttractors(any(), any(), any(), any()))
                .thenReturn(List.of(new double[] {1.0, 2.0, 3.0}));

        webTestClient.post().uri("/api/attractors/grid")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("{}")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(DoubleArrayResponse.class).hasSize(1);

    }

    @Test
    void refineAttractorsNegativeEmpty() {
        Mockito.when(service.refineAttractors(any(), anyInt(), anyDouble(), anyInt()))
                .thenReturn(List.of());

        webTestClient.post().uri("/api/attractors/refine")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("{}")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(DoubleArrayResponse.class).hasSize(0);
    }
}

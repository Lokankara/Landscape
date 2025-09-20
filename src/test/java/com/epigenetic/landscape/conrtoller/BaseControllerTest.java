package com.epigenetic.landscape.conrtoller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.test.web.reactive.server.WebTestClient;

public abstract class BaseControllerTest {
    protected WebTestClient webTestClient;
    protected final ObjectMapper mapper = new ObjectMapper();
}

package com.epigenetic.landscape;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;

@SpringBootApplication
@EnableR2dbcRepositories(basePackages = "com.epigenetic.landscape.dao")
@EntityScan(basePackages = "com.epigenetic.landscape.dao")
public class LandscapeApplication {
    public static void main(String[] args) {
        SpringApplication.run(LandscapeApplication.class, args);
    }
}

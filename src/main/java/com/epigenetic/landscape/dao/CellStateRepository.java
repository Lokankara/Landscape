package com.epigenetic.landscape.dao;

import com.epigenetic.landscape.model.CellState;
import com.epigenetic.landscape.model.CellType;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface CellStateRepository extends ReactiveCrudRepository<CellState, Long> {
    @Query("SELECT * FROM cell_state WHERE simulation_run_id = :runId ORDER BY id")
    Flux<CellState> findBySimulationRunId(Long runId);

    @Query("SELECT * FROM cell_state WHERE cell_type = :cellType")
    Flux<CellState> findByCellType(CellType cellType);

    Flux<CellState> findByStateNameContainingIgnoreCase(String query);

    Flux<CellState> findByStateNameStartsWithIgnoreCase(String filterText);
}
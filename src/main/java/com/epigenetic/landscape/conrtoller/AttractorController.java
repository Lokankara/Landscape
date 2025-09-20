package com.epigenetic.landscape.conrtoller;

import com.epigenetic.landscape.model.GridRequest;
import com.epigenetic.landscape.model.RefineRequest;
import com.epigenetic.landscape.service.AttractorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import lombok.AllArgsConstructor;

import java.util.List;

@RestController
@RequestMapping("/attractors")
@AllArgsConstructor
public class AttractorController {

    private final AttractorService attractorService;

    @PostMapping("/grid")
    public ResponseEntity<List<double[]>> findGridAttractors(@RequestBody GridRequest request) {
        List<double[]> minima = attractorService.findGridAttractors(
                request.getPotentialGrid(), request.getXGrid(), request.getYGrid(), request.getZGrid()
        );
        return ResponseEntity.ok(minima);
    }

    @PostMapping("/refine")
    public ResponseEntity<List<double[]>> refineAttractors(@RequestBody RefineRequest request) {
        List<double[]> refined = attractorService.refineAttractors(
                request.getSeeds(), request.getFullDim(), request.getTolerance(), request.getMaxIter()
        );
        return ResponseEntity.ok(refined);
    }
}

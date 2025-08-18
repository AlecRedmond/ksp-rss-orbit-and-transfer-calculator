package org.artools.orbitcalculator.controller;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

import org.artools.orbitcalculator.application.jpa.AstralPositionDTO;
import org.artools.orbitcalculator.service.AstralPositionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("astralpositions")
@CrossOrigin(origins = "http://localhost:5173") // React default port
public class AstralPositionController {
  @Autowired private AstralPositionService service;

  // Save
  @PostMapping(consumes = "application/json", produces = "application/json")
  public AstralPositionDTO saveState(@RequestBody AstralPositionDTO state) {
    return service.saveSolarSystemState(state);
  }

  @GetMapping()
  public List<AstralPositionDTO> fetchAllStates() {
    return service.fetchSolarSystemStates();
  }

  @GetMapping(value = "/{id}")
  public AstralPositionDTO getStateByID(@PathVariable("id") String  id) {
    return service.getSolarSystemStateByID(id);
  }

  @PutMapping(value = "/{id}", consumes = "application/json", produces = "application/json")
  public AstralPositionDTO updateState(
          @RequestBody AstralPositionDTO state, @PathVariable("id") String  id) {
    return service.updateSolarSystemState(state, id);
  }

  @DeleteMapping(value = "/{id}")
  public String deleteStateByID(@PathVariable("id") String id) {
    service.deleteSolarSystemState(id);
    return "Deleted Successfully!";
  }

  @GetMapping(value = "/stepto/{timestamp}")
  public List<AstralPositionDTO> stepToNewEpoch(@PathVariable("timestamp") String newTimestamp){
    Instant newEpoch = Timestamp.valueOf(newTimestamp).toInstant();
    return service.statesAtNewEpoch(newEpoch);
  }


}

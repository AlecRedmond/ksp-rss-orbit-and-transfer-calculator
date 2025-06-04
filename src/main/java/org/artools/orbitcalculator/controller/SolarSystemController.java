package org.artools.orbitcalculator.controller;

import java.util.List;
import org.artools.orbitcalculator.application.BodyPosition;
import org.artools.orbitcalculator.service.BodyPositionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class SolarSystemController {
  @Autowired private BodyPositionService service;

  // Save
  @PostMapping(value = "api/states", consumes = "application/json", produces = "application/json")
  public BodyPosition saveState(@RequestBody BodyPosition state) {
    return service.saveSolarSystemState(state);
  }

  @GetMapping(value = "api/states")
  public List<BodyPosition> fetchAllStates() {
    return service.fetchSolarSystemStates();
  }

  @GetMapping(value = "api/states/{id}")
  public BodyPosition getStateByID(@PathVariable("id") String  id) {
    return service.getSolarSystemStateByID(id);
  }

  @PutMapping(value = "api/states/{id}", consumes = "application/json", produces = "application/json")
  public BodyPosition updateState(
          @RequestBody BodyPosition state, @PathVariable("id") String  id) {
    return service.updateSolarSystemState(state, id);
  }

  @DeleteMapping(value = "api/states/{id}")
  public String deleteStateByID(@PathVariable("id") String id) {
    service.deleteSolarSystemState(id);
    return "Deleted Successfully!";
  }


}

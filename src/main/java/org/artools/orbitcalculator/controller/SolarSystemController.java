package org.artools.orbitcalculator.controller;

import java.util.List;
import org.artools.orbitcalculator.entity.SolarSystemState;
import org.artools.orbitcalculator.service.SolarSystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class SolarSystemController {
  @Autowired private SolarSystemService service;

  // Save
  @PostMapping("/systemstates")
  public SolarSystemState saveState(@RequestBody SolarSystemState state) {
    return service.saveSolarSystemState(state);
  }

  @GetMapping("/systemstates")
  public List<SolarSystemState> fetchAllStates() {
    return service.fetchSolarSystemStates();
  }

  @GetMapping("/systemstates/{id}")
  public SolarSystemState getStateByID(@PathVariable("id") String id) {
    return service.getSolarSystemStateByID(id);
  }

  @PutMapping("/systemstates/{id}")
  public SolarSystemState updateState(
      @RequestBody SolarSystemState state, @PathVariable("id") String id) {
    return service.updateSolarSystemState(state, id);
  }

  @DeleteMapping("/systemstates/{id}")
  public String deleteStateByID(@PathVariable("id") String id) {
    service.deleteSolarSystemState(id);
    return "Deleted Successfully!";
  }
}

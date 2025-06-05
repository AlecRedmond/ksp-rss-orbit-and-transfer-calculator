package org.artools.orbitcalculator.controller;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.artools.orbitcalculator.application.AstralState;
import org.artools.orbitcalculator.service.AstralStateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("astralstates")
public class AstralStateController {
  @Autowired private AstralStateService service;

  // Save
  @PostMapping(consumes = "application/json", produces = "application/json")
  public AstralState saveState(@RequestBody AstralState state) {
    return service.saveSolarSystemState(state);
  }

  @GetMapping()
  public List<AstralState> fetchAllStates() {
    return service.fetchSolarSystemStates();
  }

  @GetMapping(value = "/{id}")
  public AstralState getStateByID(@PathVariable("id") String  id) {
    return service.getSolarSystemStateByID(id);
  }

  @PutMapping(value = "/{id}", consumes = "application/json", produces = "application/json")
  public AstralState updateState(
          @RequestBody AstralState state, @PathVariable("id") String  id) {
    return service.updateSolarSystemState(state, id);
  }

  @DeleteMapping(value = "/{id}")
  public String deleteStateByID(@PathVariable("id") String id) {
    service.deleteSolarSystemState(id);
    return "Deleted Successfully!";
  }

  @GetMapping(value = "/stepto/{epoch}")
  public List<AstralState> stepToNewEpoch(@PathVariable("epoch") String newEpoch){
    return service.statesAtNewEpoch(newEpoch);
  }


}

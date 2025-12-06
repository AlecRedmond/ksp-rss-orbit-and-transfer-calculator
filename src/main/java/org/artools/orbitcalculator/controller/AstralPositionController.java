package org.artools.orbitcalculator.controller;

import java.util.List;
import org.artools.orbitcalculator.application.jpa.AstralStateDTO;
import org.artools.orbitcalculator.service.AstralPositionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("astralpositions")
@CrossOrigin(origins = "http://localhost:5173") // React default port
public class AstralPositionController {
  @Autowired private AstralPositionService service;

  @PostMapping(consumes = "application/json", produces = "application/json")
  public AstralStateDTO saveState(@RequestBody AstralStateDTO state) {
    return service.saveAstralPosition(state);
  }

  @GetMapping()
  public List<AstralStateDTO> fetchAllStates() {
    return service.fetchAll();
  }

  @GetMapping(value = "/{id}")
  public AstralStateDTO getStateByID(@PathVariable("id") String id) {
    return service.getSolarSystemStateByID(id);
  }

  @PutMapping(value = "/{id}", consumes = "application/json", produces = "application/json")
  public AstralStateDTO updateState(
      @RequestBody AstralStateDTO state, @PathVariable("id") String id) {
    return service.updateAstralPosition(state, id);
  }

  @DeleteMapping(value = "/{id}")
  public String deleteStateByID(@PathVariable("id") String id) {
    service.deleteAstralPosition(id);
    return "Deleted Successfully!";
  }
}

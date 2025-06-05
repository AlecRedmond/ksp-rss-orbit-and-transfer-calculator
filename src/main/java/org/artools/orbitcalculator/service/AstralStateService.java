package org.artools.orbitcalculator.service;

import jakarta.annotation.PostConstruct;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import org.artools.orbitcalculator.application.AstralState;
import org.artools.orbitcalculator.application.vector.Orrery;
import org.artools.orbitcalculator.exceptions.AstralStateNotFoundException;
import org.artools.orbitcalculator.method.AstralStateMapper;
import org.artools.orbitcalculator.method.integrator.OrreryIntegrator;
import org.artools.orbitcalculator.method.vector.OrreryBuilder;
import org.artools.orbitcalculator.repository.AstralStateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AstralStateService {

  @Autowired private AstralStateRepository repository;
  @Autowired private AstralStateMapper astralStateMapper;
  private Orrery orrery;

  public AstralState saveSolarSystemState(AstralState systemState) {
    return repository.save(systemState);
  }

  public AstralState getSolarSystemStateByID(String id) {
    Optional<AstralState> stateOpt = repository.findById(id);
    if (stateOpt.isEmpty()) {
      throw new AstralStateNotFoundException(id);
    }
    return stateOpt.get();
  }

  public AstralState updateSolarSystemState(AstralState astralState, String id) {
    Optional<AstralState> stateOpt = repository.findById(id);
    if (stateOpt.isEmpty()) {
      throw new AstralStateNotFoundException(id);
    }
    astralState.setId(id);
    return repository.save(astralState);
  }

  public void deleteSolarSystemState(String id) {
    repository.deleteById(id);
  }

  @PostConstruct
  public void initializePositions() {
    orrery = new OrreryBuilder().setTo1951Jan1().getOrrery();
    saveOrreryState();
  }

  private void saveOrreryState() {
    orrery.getBodyStateMap().entrySet().stream()
        .map(astralStateMapper::orreryToAstralState)
        .forEach(repository::save);
  }

  public List<AstralState> statesAtNewEpoch(String epochString) {
    Instant epoch = Instant.parse(epochString);
    stepToDate(epoch);
    return fetchSolarSystemStates().stream()
        .filter(sameEpochAs(epoch))
        .toList();
  }

  private void stepToDate(Instant epoch) {
    if(epoch.equals(orrery.getEpoch())){
      return;
    }
    orrery = new OrreryIntegrator(orrery).stepToDate(epoch).getOrrery();
    saveOrreryState();
  }

  public List<AstralState> fetchSolarSystemStates() {
    return (List<AstralState>) repository.findAll();
  }

  private static Predicate<AstralState> sameEpochAs(Instant epoch) {
    return astralState -> astralState.getTimestamp().equals(Timestamp.from(epoch));
  }
}

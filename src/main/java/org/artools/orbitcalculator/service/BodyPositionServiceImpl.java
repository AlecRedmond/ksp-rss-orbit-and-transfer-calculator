package org.artools.orbitcalculator.service;

import java.util.List;
import java.util.Optional;
import org.artools.orbitcalculator.application.BodyPosition;
import org.artools.orbitcalculator.repository.BodyPositionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BodyPositionServiceImpl implements BodyPositionService {

  @Autowired private BodyPositionRepository repository;

  @Override
  public BodyPosition saveSolarSystemState(BodyPosition systemState) {
    return repository.save(systemState);
  }

  @Override
  public List<BodyPosition> fetchSolarSystemStates() {
    return (List<BodyPosition>) repository.findAll();
  }

  @Override
  public BodyPosition getSolarSystemStateByID(String id) {
    Optional<BodyPosition> stateOpt = repository.findById(id);
    if (stateOpt.isEmpty()) {
      throw new RuntimeException("No state by this ID");
    }
    return stateOpt.get();
  }

  @Override
  public BodyPosition updateSolarSystemState(BodyPosition bodyPosition, String id) {
    Optional<BodyPosition> stateOpt = repository.findById(id);
    if (stateOpt.isEmpty()) {
      throw new RuntimeException("No state by this ID");
    }
    bodyPosition.setId(id);
    return repository.save(bodyPosition);
  }

  @Override
  public void deleteSolarSystemState(String id) {
    repository.deleteById(id);
  }
}

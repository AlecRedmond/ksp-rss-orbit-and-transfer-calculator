package org.artools.orbitcalculator.service;

import java.util.List;
import java.util.Optional;
import org.artools.orbitcalculator.entity.SolarSystemState;
import org.artools.orbitcalculator.repository.SolarSystemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SolarSystemServiceImpl implements SolarSystemService {

  @Autowired private SolarSystemRepository repository;

  @Override
  public SolarSystemState saveSolarSystemState(SolarSystemState systemState) {
    return repository.save(systemState);
  }

  @Override
  public List<SolarSystemState> fetchSolarSystemStates() {
    return (List<SolarSystemState>) repository.findAll();
  }

  @Override
  public SolarSystemState getSolarSystemStateByID(String id) {
    Optional<SolarSystemState> stateOpt = repository.findById(id);
    if (stateOpt.isEmpty()) {
      throw new RuntimeException("No state by this ID");
    }
    return stateOpt.get();
  }

  @Override
  public SolarSystemState updateSolarSystemState(SolarSystemState solarSystemState, String id) {
    Optional<SolarSystemState> stateOpt = repository.findById(id);
    if (stateOpt.isEmpty()) {
      throw new RuntimeException("No state by this ID");
    }
    SolarSystemState state = stateOpt.get();
    state.setEpoch(solarSystemState.getEpoch());
    state.setOrrery(solarSystemState.getOrrery());
    return repository.save(state);
  }

  @Override
  public void deleteSolarSystemState(String id) {
    repository.deleteById(id);
  }
}

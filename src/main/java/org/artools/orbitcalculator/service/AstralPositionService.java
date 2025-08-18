package org.artools.orbitcalculator.service;

import jakarta.annotation.PostConstruct;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import org.artools.orbitcalculator.application.jpa.AstralPositionDTO;
import org.artools.orbitcalculator.application.vector.Orrery;
import org.artools.orbitcalculator.exceptions.AstralStateNotFoundException;
import org.artools.orbitcalculator.method.jpa.AstralStateMapper;
import org.artools.orbitcalculator.method.integrator.OrreryIntegrator;
import org.artools.orbitcalculator.method.vector.OrreryBuilder;
import org.artools.orbitcalculator.repository.AstralPositionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AstralPositionService {

  @Autowired private AstralPositionRepository repository;
  @Autowired private AstralStateMapper astralStateMapper;
  private Orrery orrery;

  public AstralPositionDTO saveSolarSystemState(AstralPositionDTO systemState) {
    return repository.save(systemState);
  }

  public AstralPositionDTO getSolarSystemStateByID(String id) {
    Optional<AstralPositionDTO> stateOpt = repository.findById(id);
    if (stateOpt.isEmpty()) {
      throw new AstralStateNotFoundException(id);
    }
    return stateOpt.get();
  }

  public AstralPositionDTO updateSolarSystemState(AstralPositionDTO astralPositionDTO, String id) {
    Optional<AstralPositionDTO> stateOpt = repository.findById(id);
    if (stateOpt.isEmpty()) {
      throw new AstralStateNotFoundException(id);
    }
    astralPositionDTO.setId(id);
    return repository.save(astralPositionDTO);
  }

  public void deleteSolarSystemState(String id) {
    repository.deleteById(id);
  }

  @PostConstruct
  public void initializePositions() {
    orrery = new OrreryBuilder().getOrrery();
    saveOrreryState();
  }

  private void saveOrreryState() {
    orrery.getAstralBodies().stream()
        .map(astralStateMapper::orreryToAstralState)
        .forEach(repository::save);
  }

  public List<AstralPositionDTO> statesAtNewEpoch(Instant epoch) {
    stepToDate(epoch);
    return fetchSolarSystemStates().stream().filter(sameEpochAs(epoch)).toList();
  }

  private void stepToDate(Instant epoch) {
    if (epoch.equals(orrery.getEpoch())) {
      return;
    }
    orrery = new OrreryIntegrator(orrery).stepToDate(epoch).getOrrery();
    saveOrreryState();
  }

  public List<AstralPositionDTO> fetchSolarSystemStates() {
    return (List<AstralPositionDTO>) repository.findAll();
  }

  private static Predicate<AstralPositionDTO> sameEpochAs(Instant epoch) {
    return astralPosition -> astralPosition.getTimestamp().equals(Timestamp.from(epoch));
  }
}

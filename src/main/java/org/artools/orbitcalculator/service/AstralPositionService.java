package org.artools.orbitcalculator.service;

import jakarta.annotation.PostConstruct;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.artools.orbitcalculator.application.bodies.planets.BodyType;
import org.artools.orbitcalculator.application.bodies.planets.Planet;
import org.artools.orbitcalculator.application.jpa.AstralStateDTO;
import org.artools.orbitcalculator.application.vector.Orrery;
import org.artools.orbitcalculator.exceptions.AstralStateNotFoundException;
import org.artools.orbitcalculator.method.integrator.OrreryIntegrator;
import org.artools.orbitcalculator.method.jpa.AstralStateMapper;
import org.artools.orbitcalculator.method.vector.OrreryBuilder;
import org.artools.orbitcalculator.repository.AstralPositionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AstralPositionService {

  private Orrery orrery;
  @Autowired private AstralPositionRepository repository;
  @Autowired private AstralStateMapper astralStateMapper;

  @Transactional
  public AstralStateDTO saveAstralPosition(AstralStateDTO astralStateDTO) {
    return repository.save(astralStateDTO);
  }

  public AstralStateDTO getSolarSystemStateByID(String id) {
    return repository.findById(id).orElseThrow(() -> new AstralStateNotFoundException(id));
  }

  @Transactional
  public AstralStateDTO updateAstralPosition(AstralStateDTO astralStateDTO, String id) {
    Optional<AstralStateDTO> stateOpt = repository.findById(id);
    if (stateOpt.isEmpty()) {
      throw new AstralStateNotFoundException(id);
    }
    astralStateDTO.setId(id);
    return repository.save(astralStateDTO);
  }

  @Transactional
  public void deleteAstralPosition(String id) {
    repository.deleteById(id);
  }

  @PostConstruct
  public void initializePositions() {
    orrery = new OrreryBuilder().getOrrery();
    saveOrreryState(orrery);
  }

  public void saveOrreryState(Orrery orrery) {
    orrery.getAstralBodies().stream()
        .map(astralStateMapper::orreryToAstralState)
        .forEach(repository::save);
  }

  public List<AstralStateDTO> statesAtNewEpoch(Instant epoch) {
    stepToDate(epoch);
    return fetchSolarSystemStates().stream()
        .filter(astralPosition -> astralPosition.getTimestamp().equals(Timestamp.from(epoch)))
        .toList();
  }

  public void stepToDate(Instant epoch) {
    if (epoch.equals(orrery.getEpoch())) {
      return;
    }
    orrery = new OrreryIntegrator(orrery).stepToTime(epoch).getOrrery();
    saveOrreryState(orrery);
  }

  public List<AstralStateDTO> fetchSolarSystemStates() {
    return (List<AstralStateDTO>) repository.findAll();
  }

  public Planet getPlanetByType(BodyType bodyType) {
    return orrery.getPlanetByType(bodyType);
  }
}

package org.artools.orbitcalculator.service;

import java.util.List;
import java.util.Optional;
import org.artools.orbitcalculator.application.kepler.KeplerOrbit;
import org.artools.orbitcalculator.exceptions.OrbitNotFoundException;
import org.artools.orbitcalculator.repository.OrbitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrbitService {
  private final OrbitRepository orbitRepository;

  @Autowired
  public OrbitService(OrbitRepository orbitRepository) {
    this.orbitRepository = orbitRepository;
  }

  @Transactional
  public KeplerOrbit saveOrbitState(KeplerOrbit orbit) {
    return orbitRepository.save(orbit);
  }

  public Optional<KeplerOrbit> getOrbitStateByID(String id) {
    return orbitRepository.findById(id);
  }

  public List<KeplerOrbit> fetchAllOrbitStates() {
    return (List<KeplerOrbit>) orbitRepository.findAll();
  }

  @Transactional
  public KeplerOrbit updateOrbitState(KeplerOrbit orbit, String id) {

    if (!orbitRepository.existsById(id)) {
      throw new OrbitNotFoundException(id);
    }
    orbit.setId(id);

    return orbitRepository.save(orbit);
  }

  @Transactional
  public void deleteOrbitState(String id) {
    orbitRepository.deleteById(id);
  }
}

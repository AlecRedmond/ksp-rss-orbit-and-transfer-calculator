package org.artools.orbitcalculator.service;

import java.util.List;
import org.artools.orbitcalculator.application.jpa.OrbitStateDTO;
import org.artools.orbitcalculator.exceptions.OrbitStateNotFoundException;
import org.artools.orbitcalculator.repository.OrbitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrbitService {
  private final OrbitRepository orbitRepository;

  @Autowired
  public OrbitService(
      OrbitRepository orbitRepository, AstralPositionService astralPositionService) {
    this.orbitRepository = orbitRepository;
  }

  @Transactional
  public OrbitStateDTO saveOrbitState(OrbitStateDTO orbitStateDTO) {
    return orbitRepository.save(orbitStateDTO);
  }

  public OrbitStateDTO getOrbitStateByID(String id) {
    return orbitRepository.findById(id).orElseThrow(() -> new OrbitStateNotFoundException(id));
  }

  public List<OrbitStateDTO> fetchAllOrbitStates() {
    return (List<OrbitStateDTO>) orbitRepository.findAll();
  }

  @Transactional
  public OrbitStateDTO updateOrbitState(OrbitStateDTO orbitStateDTO, String id) {

    if (!orbitRepository.existsById(id)) {
      throw new OrbitStateNotFoundException(id);
    }
    orbitStateDTO.setId(id);

    return orbitRepository.save(orbitStateDTO);
  }

  @Transactional
  public void deleteOrbitState(String id) {
    orbitRepository.deleteById(id);
  }
}

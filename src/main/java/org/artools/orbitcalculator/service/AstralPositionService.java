package org.artools.orbitcalculator.service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;
import org.artools.orbitcalculator.application.jpa.AstralStateDTO;
import org.artools.orbitcalculator.application.vector.Orrery;
import org.artools.orbitcalculator.exceptions.AstralStateNotFoundException;
import org.artools.orbitcalculator.method.jpa.AstralStateMapper;
import org.artools.orbitcalculator.repository.AstralPositionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AstralPositionService {
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

  @Transactional
  public List<AstralStateDTO> saveOrreryState(Orrery orrery) {
    //    List<AstralStateDTO> savedStates = new ArrayList<>();
    //    orrery.getAstralBodies().stream()
    //        .map(astralStateMapper::motionStateToDto)
    //        .forEach(astralStateDTO -> savedStates.add(repository.save(astralStateDTO)));
    //    return savedStates;
    return null;
  }

  public List<AstralStateDTO> fetchAll() {
    return (List<AstralStateDTO>) repository.findAll();
  }

  @Transactional
  public void deletePositionsAfterTime(Timestamp timestamp) {
    StreamSupport.stream(repository.findAll().spliterator(), true)
        .filter(astralStateDTO -> astralStateDTO.getTimestamp().after(timestamp))
        .forEach(astralStateDTO -> repository.delete(astralStateDTO));
  }
}

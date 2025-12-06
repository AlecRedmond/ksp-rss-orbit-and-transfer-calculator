package org.artools.orbitcalculator.service;

import java.util.List;
import java.util.Optional;
import org.artools.orbitcalculator.application.jpa.AstralBodyDTO;
import org.artools.orbitcalculator.application.vector.Orrery;
import org.artools.orbitcalculator.exceptions.CraftNotFoundException;
import org.artools.orbitcalculator.method.jpa.AstralBodyMapper;
import org.artools.orbitcalculator.repository.AstralBodyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AstralBodyService {
  private final AstralBodyRepository astralBodyRepository;
  private final AstralBodyMapper astralBodyMapper;

  @Autowired
  public AstralBodyService(
      AstralBodyRepository astralBodyRepository, AstralBodyMapper astralBodyMapper) {
    this.astralBodyRepository = astralBodyRepository;
    this.astralBodyMapper = astralBodyMapper;
  }

  @Transactional
  public AstralBodyDTO save(AstralBodyDTO astralBodyDTO) {
    return astralBodyRepository.save(astralBodyDTO);
  }

  public Optional<AstralBodyDTO> findById(String id) {
    return astralBodyRepository.findById(id);
  }

  public List<AstralBodyDTO> fetchAll() {
    return (List<AstralBodyDTO>) astralBodyRepository.findAll();
  }

  @Transactional
  public AstralBodyDTO update(AstralBodyDTO astralBodyDTO, String id) {

    if (!astralBodyRepository.existsById(id)) {
      throw new CraftNotFoundException(id);
    }
    astralBodyDTO.setId(id);

    return astralBodyRepository.save(astralBodyDTO);
  }

  @Transactional
  public void deleteById(String id) {
    astralBodyRepository.deleteById(id);
  }

  public List<AstralBodyDTO> convertOrreryBodiesToDto(Orrery orrery) {
    return orrery.getAstralBodies().stream().map(astralBodyMapper::bodyToDto).toList();
  }
}

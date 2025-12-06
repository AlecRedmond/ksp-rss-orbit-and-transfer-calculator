package org.artools.orbitcalculator.service;

import java.util.List;
import java.util.Optional;
import org.artools.orbitcalculator.application.bodies.Craft;
import org.artools.orbitcalculator.application.jpa.CraftDTO;
import org.artools.orbitcalculator.application.vector.Orrery;
import org.artools.orbitcalculator.exceptions.CraftNotFoundException;
import org.artools.orbitcalculator.method.jpa.AstralBodyMapper;
import org.artools.orbitcalculator.repository.CraftRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CraftService {
  private final CraftRepository craftRepository;
  private final AstralBodyMapper craftMapper;

  @Autowired
  public CraftService(CraftRepository craftRepository, AstralBodyMapper craftMapper) {
    this.craftRepository = craftRepository;
    this.craftMapper = craftMapper;
  }

  @Transactional
  public CraftDTO save(CraftDTO craftDTO) {
    return craftRepository.save(craftDTO);
  }

  public Optional<CraftDTO> findById(String id) {
    return craftRepository.findById(id);
  }

  public List<CraftDTO> fetchAll() {
    return (List<CraftDTO>) craftRepository.findAll();
  }

  @Transactional
  public CraftDTO update(CraftDTO craftDTO, String id) {

    if (!craftRepository.existsById(id)) {
      throw new CraftNotFoundException(id);
    }
    craftDTO.setId(id);

    return craftRepository.save(craftDTO);
  }

  @Transactional
  public void deleteById(String id) {
    craftRepository.deleteById(id);
  }

  public Craft dtoToCraft(CraftDTO craftDTO, Orrery orrery) {
    return craftMapper.dtoToCraft(craftDTO, orrery);
  }
}

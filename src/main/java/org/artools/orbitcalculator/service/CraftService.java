package org.artools.orbitcalculator.service;

import java.util.List;
import java.util.Optional;
import org.artools.orbitcalculator.application.jpa.CraftDTO;
import org.artools.orbitcalculator.exceptions.CraftNotFoundException;
import org.artools.orbitcalculator.repository.CraftRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

public class CraftService {
  private final CraftRepository craftRepository;

  @Autowired
  public CraftService(CraftRepository craftRepository) {
    this.craftRepository = craftRepository;
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
}

package org.artools.orbitcalculator.service;

import java.util.List;
import java.util.Optional;
import org.artools.orbitcalculator.application.kepler.KeplerHolds;
import org.artools.orbitcalculator.exceptions.OrbitNotFoundException;
import org.artools.orbitcalculator.repository.KeplerHoldsRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class KeplerHoldsService {

  private final KeplerHoldsRepository keplerHoldsRepository;

  public KeplerHoldsService(KeplerHoldsRepository keplerHoldsRepository) {
    this.keplerHoldsRepository = keplerHoldsRepository;
  }

  @Transactional
  public KeplerHolds save(KeplerHolds keplerHolds) {
    return keplerHoldsRepository.save(keplerHolds);
  }

  public Optional<KeplerHolds> findById(String id) {
    return keplerHoldsRepository.findById(id);
  }

  public List<KeplerHolds> findAll() {
    return (List<KeplerHolds>) keplerHoldsRepository.findAll();
  }

  @Transactional
  public void deleteById(String id) {
    keplerHoldsRepository.deleteById(id);
  }

  @Transactional
  public KeplerHolds updateHold(KeplerHolds holds, String id) {

    if (!keplerHoldsRepository.existsById(id)) {
      throw new OrbitNotFoundException(id);
    }
    holds.setId(id);

    return keplerHoldsRepository.save(holds);
  }
}

package org.artools.orbitcalculator.repository;

import org.artools.orbitcalculator.application.kepler.KeplerHolds;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KeplerHoldsRepository extends CrudRepository<KeplerHolds, String> {}

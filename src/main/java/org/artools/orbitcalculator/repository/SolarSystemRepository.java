package org.artools.orbitcalculator.repository;

import org.artools.orbitcalculator.entity.SolarSystemState;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SolarSystemRepository extends CrudRepository<SolarSystemState, String> {}

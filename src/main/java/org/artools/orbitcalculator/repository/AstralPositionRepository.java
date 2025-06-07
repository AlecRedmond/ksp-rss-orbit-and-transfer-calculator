package org.artools.orbitcalculator.repository;

import org.artools.orbitcalculator.application.AstralPosition;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AstralPositionRepository extends CrudRepository<AstralPosition, String> {}

package org.artools.orbitcalculator.repository;

import org.artools.orbitcalculator.application.vector.entity.AstralPositionDTO;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AstralPositionRepository extends CrudRepository<AstralPositionDTO, String> {}

package org.artools.orbitcalculator.repository;

import org.artools.orbitcalculator.application.BodyPosition;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BodyPositionRepository extends CrudRepository<BodyPosition, String> {}

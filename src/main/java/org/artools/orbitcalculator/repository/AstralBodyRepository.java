package org.artools.orbitcalculator.repository;

import org.artools.orbitcalculator.application.jpa.AstralBodyDTO;
import org.springframework.data.repository.CrudRepository;

public interface AstralBodyRepository extends CrudRepository<AstralBodyDTO, String> {}

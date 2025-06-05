package org.artools.orbitcalculator.repository;

import org.artools.orbitcalculator.application.AstralState;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AstralStateRepository extends CrudRepository<AstralState, String> {}

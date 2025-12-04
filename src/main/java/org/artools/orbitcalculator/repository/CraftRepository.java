package org.artools.orbitcalculator.repository;

import org.artools.orbitcalculator.application.jpa.CraftDTO;
import org.springframework.data.repository.CrudRepository;

public interface CraftRepository extends CrudRepository<CraftDTO, String> {}

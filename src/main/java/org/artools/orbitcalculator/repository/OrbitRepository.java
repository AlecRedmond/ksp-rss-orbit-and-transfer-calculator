package org.artools.orbitcalculator.repository;

import org.artools.orbitcalculator.application.jpa.OrbitStateDTO;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrbitRepository extends CrudRepository<OrbitStateDTO, String> {}

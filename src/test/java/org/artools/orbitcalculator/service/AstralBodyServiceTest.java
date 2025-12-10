package org.artools.orbitcalculator.service;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import org.artools.orbitcalculator.application.bodies.planets.BodyType;
import org.artools.orbitcalculator.application.jpa.AstralBodyDTO;
import org.artools.orbitcalculator.application.jpa.PlanetDTO;
import org.artools.orbitcalculator.application.vector.Orrery;
import org.artools.orbitcalculator.method.jpa.AstralBodyMapper;
import org.artools.orbitcalculator.method.jpa.AstralBodyMapperImpl;
import org.artools.orbitcalculator.method.vector.OrreryBuilder;
import org.artools.orbitcalculator.repository.AstralBodyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

@DataJpaTest
@Import(AstralBodyMapperImpl.class)
class AstralBodyServiceTest {
  @Autowired private AstralBodyRepository repository;
  @Autowired private AstralBodyMapper mapper;
  private AstralBodyService test;
  private Orrery orrery;

  @BeforeEach
  void initOrrery() {
    orrery = new OrreryBuilder().getOrrery();
    test = new AstralBodyService(repository, mapper);
  }

  @Test
  @Transactional
  void save() {
    List<AstralBodyDTO> bodies = test.convertOrreryBodiesToDto(orrery);
    for (AstralBodyDTO bodyDTO : bodies) {
      PlanetDTO newDTO = (PlanetDTO) test.save(bodyDTO);
      System.out.printf("SAVED %s with ID %s%n", newDTO.getBodyType(), newDTO.getId());
    }
  }

  @Test
  void findById() {
    List<AstralBodyDTO> bodies = test.convertOrreryBodiesToDto(orrery);
    String id = "";
    for (AstralBodyDTO bodyDTO : bodies) {
      PlanetDTO newDTO = (PlanetDTO) test.save(bodyDTO);
      if (newDTO.getBodyType().equals(BodyType.EARTH)) {
        id = newDTO.getId();
      }
    }
    PlanetDTO planetDTO = (PlanetDTO) test.findById(id).orElseThrow();
    assertEquals(BodyType.EARTH, planetDTO.getBodyType());
  }

  @Test
  void fetchAll() {}

  @Test
  void update() {}

  @Test
  void deleteById() {}
}

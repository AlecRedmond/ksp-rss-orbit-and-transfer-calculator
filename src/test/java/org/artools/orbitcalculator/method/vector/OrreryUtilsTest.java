package org.artools.orbitcalculator.method.vector;

import org.artools.orbitcalculator.application.bodies.AstralBodies;
import org.artools.orbitcalculator.application.vector.Orrery;
import org.artools.orbitcalculator.exceptions.NotOrbitalStateException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OrreryUtilsTest {
  OrreryUtils test;

  @BeforeEach
  void initialize(){
    Orrery orrery = new OrreryBuilder().setTo1951Jan1().getOrrery();
    test = new OrreryUtils(orrery);
  }

  @Test
  void convertToOrbitalStates() {
    Orrery orrery = test.convertToOrbitalStates();
    assertThrows(NotOrbitalStateException.class,() -> orrery.getOrbitalVectors(AstralBodies.SUN));
    assertDoesNotThrow(() -> orrery.getOrbitalVectors(AstralBodies.EARTH));
  }
}

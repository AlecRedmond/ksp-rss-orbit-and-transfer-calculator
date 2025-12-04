package org.artools.orbitcalculator.method.vector;

import org.artools.orbitcalculator.application.bodies.planets.BodyType;
import org.artools.orbitcalculator.application.vector.MotionState;
import org.artools.orbitcalculator.application.vector.OrbitalState;
import org.artools.orbitcalculator.application.vector.Orrery;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OrreryUtilsTest {
  OrreryUtils test;

  @BeforeEach
  void initialize(){
    Orrery orrery = new OrreryBuilder().getOrrery();
    test = new OrreryUtils(orrery);
  }

  @Test
  void convertToOrbitalStates() {
    Orrery orrery = test.convertToOrbitalStates();
    MotionState sunState = orrery.getPlanetByType(BodyType.SUN).getCurrentMotionState();
    MotionState earthState = orrery.getPlanetByType(BodyType.EARTH).getCurrentMotionState();
    assertThrows(ClassCastException.class,() -> {OrbitalState ignored = (OrbitalState) sunState;});
    assertDoesNotThrow(() -> {OrbitalState ignored = (OrbitalState) earthState;});
  }
}

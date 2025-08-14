package org.artools.orbitcalculator.method.vector;

import org.artools.orbitcalculator.application.bodies.BodyType;
import org.artools.orbitcalculator.application.vector.MotionState;
import org.artools.orbitcalculator.application.vector.OrbitalState;
import org.artools.orbitcalculator.application.vector.Orrery;
import org.artools.orbitcalculator.exceptions.NotOrbitalStateException;
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
    MotionState sunState = orrery.getPlanetByName(BodyType.SUN).orElseThrow().getMotionState();
    MotionState earthState = orrery.getPlanetByName(BodyType.EARTH).orElseThrow().getMotionState();
    assertThrows(ClassCastException.class,() -> {OrbitalState state = (OrbitalState) sunState;});
    assertDoesNotThrow(() -> {OrbitalState state = (OrbitalState) earthState;});
  }
}

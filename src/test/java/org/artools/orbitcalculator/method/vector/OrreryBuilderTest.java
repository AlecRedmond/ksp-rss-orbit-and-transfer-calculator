package org.artools.orbitcalculator.method.vector;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import org.artools.orbitcalculator.application.bodies.AstralBodies;
import org.artools.orbitcalculator.application.vector.Orrery;
import org.junit.jupiter.api.Test;

class OrreryBuilderTest {
  OrreryBuilder test = new OrreryBuilder();

  @Test
  void setTo1951Jan1() {
    //ASSERT NONE ARE NULL
    Orrery orrery = test.setTo1951Jan1().getOrrery();
    Arrays.stream(AstralBodies.values())
        .filter(ab -> !ab.equals(AstralBodies.CRAFT))
        .forEach(body -> assertNotNull(orrery.getMotionVectors(body)));
  }
}

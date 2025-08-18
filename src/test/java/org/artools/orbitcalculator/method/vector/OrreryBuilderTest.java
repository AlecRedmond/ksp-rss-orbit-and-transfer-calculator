package org.artools.orbitcalculator.method.vector;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import org.artools.orbitcalculator.application.bodies.planets.BodyType;
import org.artools.orbitcalculator.application.vector.Orrery;
import org.junit.jupiter.api.Test;

class OrreryBuilderTest {
  OrreryBuilder test = new OrreryBuilder();

  @Test
  void setToStartingEpoch() {
    // ASSERT NONE ARE NULL
    Orrery orrery = test.getOrrery();
    Arrays.stream(BodyType.values())
        .forEach(
            bodyType -> {
              assertDoesNotThrow(
                  () -> {
                    orrery.getPlanetByType(bodyType);
                  });
            });
  }
}

package org.artools.orbitcalculator.method.vector;

import org.artools.orbitcalculator.application.vector.Orrery;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OrreryBuilderTest {
  OrreryBuilder test = new OrreryBuilder();

  @Test
  void setTo1951Jan1() {
    Orrery orrery = test.setTo1951Jan1().getOrrery();
    orrery.getBodyStateMap().forEach((key,val) -> System.out.println(key + ": Pos =" + val.getPosition() + " Vel =" +val.getVelocity()));
  }
}

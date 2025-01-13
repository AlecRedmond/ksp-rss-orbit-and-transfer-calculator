package org.example.equations.method.utility;

import org.example.equations.application.Orbit;
import org.example.equations.method.OrbitBuilder;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TransferStringWriterTest {

  @Test
  void orbitToString() {
    Orbit orbit = new OrbitBuilder(250e3,200e3,5.25).getOrbit();

  }
}

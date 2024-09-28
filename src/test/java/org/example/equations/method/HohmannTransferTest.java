package org.example.equations.method;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HohmannTransferTest {
  static HohmannTransfer hohmannTransfer;
  static double sma = 6.621e6;
  static double altitude = 250e3;
  static double velocity = 7758.555;

  @BeforeAll
  static void startUp() {
    hohmannTransfer = new HohmannTransfer();
  }

  @Test
  void velocityFromAltitudeAndSMA() {
    double testVelocity = HohmannTransfer.velocityFromAltitudeAndSMA(altitude,sma);
    assertEquals((int) velocity,(int) testVelocity);
  }

  @Test
  void smaFromVelocityAndAltitude(){
    double testSMA = HohmannTransfer.smaFromVelocityAndAltitude(velocity,altitude);
    assertEquals(Math.round(sma),Math.round(testSMA));
  }

  @Test
  void altitudeFromVelocityAndSMA(){
    double testAltitude = HohmannTransfer.altitudeFromVelocityAndSMA(velocity,sma);
    assertEquals(Math.round(altitude),Math.round(testAltitude));
  }
}

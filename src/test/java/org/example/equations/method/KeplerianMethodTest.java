package org.example.equations.method;

import static org.example.equations.application.keplerianelements.Kepler.KeplerEnums.*;
import static org.junit.jupiter.api.Assertions.*;

import org.example.equations.application.Keplerian;
import org.example.equations.application.KeplerianHolds;
import org.example.equations.application.keplerianelements.Kepler.KeplerEnums;
import org.junit.jupiter.api.Test;

class KeplerianMethodTest {

  private static KeplerianMethod keplerianMethod;
  private static Keplerian keplerian;
  private static KeplerianHolds keplerianHolds;
  private static final double apoapsis = 500000;
  private static final double periapsis = 300000;
  private static final double semiMajorAxis = 6771400.0;
  private static final double eccentricity = 0.01476799480166583;
  private static final double orbitalPeriod = 5545.346450053145;
  private static final double velocityApoapsis = 7559.890904918995;
  private static final double velocityPeriapsis = 7786.526720637404;


  void setAsNew() {
    keplerian = new Keplerian();
    keplerianHolds = new KeplerianHolds();

  }

  @Test
  void twoApses() {
    runComparatorTest(APOAPSIS,apoapsis,PERIAPSIS,periapsis);
  }

  @Test
  void apsisAndSMA() {
    runComparatorTest(APOAPSIS,apoapsis,SEMI_MAJOR_AXIS,semiMajorAxis);
    runComparatorTest(PERIAPSIS,periapsis,SEMI_MAJOR_AXIS,semiMajorAxis);
  }

  @Test
  void apsisAndEcc() {
    runComparatorTest(APOAPSIS,apoapsis,ECCENTRICITY,eccentricity);
    runComparatorTest(PERIAPSIS,periapsis,ECCENTRICITY,eccentricity);
  }

  @Test
  void apsisAndVelocity() {
    runComparatorTest(APOAPSIS,apoapsis,VELOCITY_APOAPSIS,velocityApoapsis);
    runComparatorTest(PERIAPSIS,periapsis,VELOCITY_PERIAPSIS,velocityPeriapsis);
  }

  @Test
  void smaAndEcc() {
    runComparatorTest(SEMI_MAJOR_AXIS,semiMajorAxis,ECCENTRICITY,eccentricity);
  }

  @Test
  void smaAndVelocity() {
    runComparatorTest(SEMI_MAJOR_AXIS,semiMajorAxis,VELOCITY_APOAPSIS,velocityApoapsis);
    runComparatorTest(SEMI_MAJOR_AXIS,semiMajorAxis,VELOCITY_PERIAPSIS,velocityPeriapsis);
  }

  private void runComparatorTest(KeplerEnums keplerEnum1, double double1, KeplerEnums keplerEnum2, double double2) {
    setAsNew();
    keplerian.setDataFor(keplerEnum1,double1);
    keplerian.setDataFor(keplerEnum2,double2);
    keplerianHolds.setHold(keplerEnum1,true);
    keplerianHolds.setHold(keplerEnum2,true);
    build();
    runAssertEqualsOnAll();
  }

  private void runAssertEqualsOnAll() {
    assertTrue(withinPointFivePercent(apoapsis,keplerian.getDataFor(APOAPSIS)));
    assertTrue(withinPointFivePercent(periapsis,keplerian.getDataFor(PERIAPSIS)));
    assertTrue(withinPointFivePercent(semiMajorAxis,keplerian.getDataFor(SEMI_MAJOR_AXIS)));
    assertTrue(withinPointFivePercent(eccentricity,keplerian.getDataFor(ECCENTRICITY)));
    assertTrue(withinPointFivePercent(orbitalPeriod,keplerian.getDataFor(ORBITAL_PERIOD)));
    assertTrue(withinPointFivePercent(velocityApoapsis,keplerian.getDataFor(VELOCITY_APOAPSIS)));
    assertTrue(withinPointFivePercent(velocityPeriapsis,keplerian.getDataFor(VELOCITY_PERIAPSIS)));
  }

  private boolean withinPointFivePercent(double myData, double foundData) {
    double difference = Math.abs(myData - foundData);
    double magnitude = difference / myData;
    if(magnitude < 0.0005){
      return true;
    }
    return false;
  }

  private static void build() {
    keplerianMethod = new KeplerianMethod(keplerian, keplerianHolds);
  }
}

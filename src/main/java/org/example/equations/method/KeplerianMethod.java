//package org.example.equations.method;
//
//import java.util.HashMap;
//import java.util.Map;
//
//import lombok.Data;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import lombok.Setter;
//import org.example.equations.application.Keplerian;
//import org.example.equations.application.keplerianelements.*;
//
//@Data
//@Getter
//@Setter
//@NoArgsConstructor
//public class KeplerianMethod {
//  private Keplerian keplerian = new Keplerian();
//  private HashMap<Class, String> dataToParse = new HashMap<>();
//  private boolean fromConstructor = false;
//  private boolean apoapsis;
//  private boolean periapsis;
//  private boolean eccentricity;
//  private boolean semiMajorAxis;
//  private boolean orbitalPeriod;
//  private boolean velocityAP;
//  private boolean velocityPE;
//
//  public KeplerianMethod(double apsis1, double apsis2) {
//    fromConstructor = true;
//    apoapsis = true;
//    periapsis = true;
//
//    if (apsis1 > apsis2) {
//      keplerian.getApoapsis().set(apsis1);
//      keplerian.getPeriapsis().set(apsis2);
//    }
//    else{
//      keplerian.getApoapsis().set(apsis2);
//      keplerian.getPeriapsis().set(apsis1);
//    }
//
//    calculateMissing();
//  }
//
//  public void calculateMissing() {
//
//    if (!fromConstructor) {
//      setAllInputData();
//      calculateHoldsIfNotConstructor();
//    }
//
//    if (orbitalPeriod) {
//      this.keplerian = FillEquations.convertOrbitalPeriodToSMA(this.keplerian);
//      semiMajorAxis = true;
//    }
//
//    if (velocityAP || velocityPE) {
//      if (apoapsis || periapsis) {
//        this.keplerian =
//            FillEquations.calculateSMAFromVelocityAndAltitude(this.keplerian, periapsis);
//        semiMajorAxis = true;
//      }
//      if (semiMajorAxis) {
//        this.keplerian =
//            FillEquations.calculateAltitudeFromVelocityAndSMA(this.keplerian, velocityPE);
//        if (velocityPE) {
//          periapsis = true;
//        } else {
//          apoapsis = true;
//        }
//      }
//    }
//
//    if (apoapsis && periapsis) {
//      this.keplerian = FillEquations.findPeriapsisApoapsis(this.keplerian);
//    }
//
//    if (periapsis && eccentricity || apoapsis && eccentricity) {
//      this.keplerian = FillEquations.findApsisEccentricity(this.keplerian, periapsis);
//    }
//
//    if (periapsis && semiMajorAxis || apoapsis && semiMajorAxis) {
//      this.keplerian = FillEquations.findApsisSemiMajorAxis(this.keplerian, periapsis);
//    }
//
//    if (semiMajorAxis && eccentricity) {
//      this.keplerian = FillEquations.findEccentricitySemiMajorAxis(this.keplerian);
//    }
//  }
//
//  private void calculateHoldsIfNotConstructor() {
//    apoapsis = heldValue(Apoapsis.class);
//    periapsis = heldValue(Periapsis.class);
//    eccentricity = heldValue(Eccentricity.class);
//    semiMajorAxis = heldValue(SemiMajorAxis.class);
//    orbitalPeriod = heldValue(OrbitalPeriod.class);
//    velocityAP = heldValue(VelocityApoapsis.class);
//    velocityPE = heldValue(VelocityPeriapsis.class);
//  }
//
//  private void setAllInputData() {
//    for (Map.Entry<Class, String> entry : this.dataToParse.entrySet()) {
//      setFromString(entry.getValue(), entry.getKey());
//    }
//  }
//
//  private boolean heldValue(Class aClass) {
//    return dataToParse.containsKey(aClass);
//  }
//
//  public void setFromString(String string, Class aClass) {
//    this.keplerian.setFromString(string, aClass);
//  }
//
//  public String getAsString(Class aClass) {
//    return this.keplerian.getAsString(aClass);
//  }
//}

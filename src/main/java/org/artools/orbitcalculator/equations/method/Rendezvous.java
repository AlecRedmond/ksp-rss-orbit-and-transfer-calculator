package org.artools.equations.method;

import java.util.ArrayList;
import lombok.Data;
import org.artools.equations.application.Intercept;
import org.artools.equations.application.keplerianelements.OrbitalPeriod;

@Data
public class Rendezvous {
  private static final String SIDEREAL_DAY_STR = "23:56:04";
  private ArrayList<Intercept> intercepts;

  public Rendezvous(double orbitalPeriodSecs, double orbitalProcessionDayRads) {
    OrbitalPeriod siderealDayPeriod = new OrbitalPeriod();
    siderealDayPeriod.setFromString(SIDEREAL_DAY_STR);
    double processionAdjustedDay =
        siderealDayPeriod.getData()
            + getExtraTime(siderealDayPeriod.getData(), orbitalProcessionDayRads);

    double halfOrbit = orbitalPeriodSecs / 2;

    double timeVal = 0;
    double timeValMax = 3600 * 24 * 30.0;
    ArrayList<Double> orbitSinVals = new ArrayList<>();
    ArrayList<Double> orbitCosVals = new ArrayList<>();
    ArrayList<Double> earthSinVals = new ArrayList<>();
    ArrayList<Double> earthCosVals = new ArrayList<>();
    for (int i = 0; timeVal < timeValMax; i++) {
      timeVal = i * halfOrbit;
      // Sin values will show locations where the launch location passes under the orbital plane
      // when sin(x) = 0
      orbitSinVals.add(Math.sin((2 * Math.PI * timeVal) / orbitalPeriodSecs));
      orbitCosVals.add(Math.cos((2 * Math.PI * timeVal) / orbitalPeriodSecs));
      // Cos values will show if it's on the ascending (Cos(t) = 1) or descending (Cos(t) = -1)
      // side.
      // Cos values must be on the same side.
      earthSinVals.add(Math.sin((2 * Math.PI * timeVal) / processionAdjustedDay));
      earthCosVals.add(Math.cos((2 * Math.PI * timeVal) / processionAdjustedDay));
    }

    intercepts = new ArrayList<>();

    for (int i = 0; i < orbitSinVals.size(); i++) {
      if ((Math.abs(orbitSinVals.get(i) - earthSinVals.get(i))) < 0.05
          && (Math.abs(orbitCosVals.get(i) - earthCosVals.get(i))) < 0.05) {
        boolean isAscending = i % 2 == 0;
        double earthLeadRads = Math.asin(earthSinVals.get(i));
        if (!isAscending) {
          earthLeadRads = -earthLeadRads;
        }
        double satelliteLeadTime = getLeadTime(processionAdjustedDay, earthLeadRads);
        if (satelliteLeadTime > 0) {
          intercepts.add(new Intercept((i * halfOrbit + satelliteLeadTime), isAscending, satelliteLeadTime));
        }
      }
    }
  }

  private double getLeadTime(double processionAdjustedDay, double earthLeadRads) {
    double periodFraction = earthLeadRads / (2 * Math.PI);
    return -periodFraction * processionAdjustedDay;
  }

  private double getExtraTime(double siderealDayPeriod, double orbitalProcessionDayRads) {
    double extraRatio = orbitalProcessionDayRads / (2 * Math.PI);
    return siderealDayPeriod * (1 + extraRatio);
  }
}

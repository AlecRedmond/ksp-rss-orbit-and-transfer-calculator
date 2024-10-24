package org.example.equations.method;

import lombok.Data;
import org.example.equations.application.keplerianelements.OrbitalPeriod;

import java.util.ArrayList;

@Data
public class Rendezvous {
  private double orbitalPeriodSecs;
  private double frequencyOrbit;
  private static final String SIDEREAL_DAY_STR = "23:56:04";
  private double siderealDaySecs;
  private double frequencyDay;
  private ArrayList<Integer> intercepts;

  public Rendezvous(double orbitalPeriodSecs) {
    this.orbitalPeriodSecs = orbitalPeriodSecs;
    OrbitalPeriod siderealDayPeriod = new OrbitalPeriod();
    siderealDayPeriod.setFromString(SIDEREAL_DAY_STR);
    siderealDaySecs = siderealDayPeriod.getData();

    double halfOrbit = orbitalPeriodSecs / 2;

    double timeVal = 0;
    double timeValMax = 3600*24*30;
    ArrayList<Double> orbitSinVals = new ArrayList<>();
    ArrayList<Double> orbitCosVals = new ArrayList<>();
    ArrayList<Double> earthSinVals = new ArrayList<>();
    ArrayList<Double> earthCosVals = new ArrayList<>();
    for (int i = 0; timeVal < timeValMax; i++) {
      timeVal = i * orbitalPeriodSecs;
      orbitSinVals.add(Math.sin((2 * Math.PI * timeVal) / orbitalPeriodSecs));
      orbitCosVals.add(Math.cos((2 * Math.PI * timeVal) / orbitalPeriodSecs));
      earthSinVals.add(Math.sin((2 * Math.PI * timeVal) / siderealDaySecs));
      earthCosVals.add(Math.cos((2 * Math.PI * timeVal) / siderealDaySecs));
    }

    intercepts = new ArrayList<>();
    for (int i = 0; i < orbitSinVals.size(); i++) {
      if ((Math.abs(orbitSinVals.get(i) - earthSinVals.get(i))) < 0.05
          && (Math.abs(orbitCosVals.get(i) - earthCosVals.get(i))) < 0.05) {
        intercepts.add(i);
      }
    }


  }
}

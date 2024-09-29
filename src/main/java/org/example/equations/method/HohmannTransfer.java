package org.example.equations.method;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.equations.application.Keplerian;
import org.example.equations.application.keplerianelements.Velocity;

import java.util.ArrayList;

@Data
@NoArgsConstructor
public class HohmannTransfer {
  private Keplerian orbitA = new Keplerian();
  private Keplerian orbitB = new Keplerian();

  private Velocity[] velocities1 = {new Velocity(),new Velocity()};
  private Keplerian transferOrbit1 = new Keplerian();
  private Velocity[] velocities2 = {new Velocity(),new Velocity()};
  private Keplerian transferOrbit2 = new Keplerian();
  
  private boolean transfer1IsMoreEfficient;

  public HohmannTransfer(KeplerianMethod orbitAMethod, KeplerianMethod orbitBMethod) {
    this.orbitA = orbitAMethod.getKeplerian();
    this.orbitB = orbitBMethod.getKeplerian();

    this.transferOrbit1.setPeriapsis(this.orbitA.getPeriapsis());
    this.transferOrbit1.setApoapsis(this.orbitB.getApoapsis());
    this.transferOrbit2.setPeriapsis(this.orbitB.getPeriapsis());
    this.transferOrbit2.setApoapsis(this.orbitA.getApoapsis());

    KeplerianMethod transferOrbit1Method = new KeplerianMethod(transferOrbit1.getApoapsis().get(),transferOrbit1.getPeriapsis().get());
    this.transferOrbit1 = transferOrbit1Method.getKeplerian();

    velocities1[0].set(
        this.transferOrbit1.getVelocityPeriapsis().get()
            - this.orbitA.getVelocityPeriapsis().get());
    velocities1[1].set(
        this.orbitB.getVelocityApoapsis().get() - this.transferOrbit1.getVelocityApoapsis().get());

    KeplerianMethod transferOrbit2Method = new KeplerianMethod(transferOrbit2.getApoapsis().get(),transferOrbit2.getPeriapsis().get());
    this.transferOrbit2 = transferOrbit2Method.getKeplerian();

    velocities2[0].set(
        this.transferOrbit1.getVelocityApoapsis().get() - this.orbitA.getVelocityApoapsis().get());
    velocities2[1].set(
        this.orbitB.getVelocityPeriapsis().get()
            - this.transferOrbit1.getVelocityPeriapsis().get());

      this.transfer1IsMoreEfficient = calculateMags(velocities1) <= calculateMags(velocities2);
    
  }
  
  public Velocity[] getMostEfficientVelocity(){
    if(transfer1IsMoreEfficient){
      return velocities1;
    }
    else{
      return velocities2;
    }
  }
  
  public Keplerian getMostEfficientTransferOrbit(){
    if(transfer1IsMoreEfficient){
      return transferOrbit1;
    }
    else{
      return transferOrbit2;
    }
  }

  private double calculateMags(Velocity[] velocities) {
    return Math.abs(velocities[0].get()) + Math.abs(velocities[1].get());
  }

  public ArrayList<ArrayList<String>> hohmannStringOutput(){
    Velocity[] mostEfficient = getMostEfficientVelocity();

    ArrayList<String> firstBurnString = new ArrayList<>();
    ArrayList<String> secondBurnString = new ArrayList<>();
    ArrayList<String> transferOrbitStrings = new ArrayList<>();
    if(transfer1IsMoreEfficient){
      firstBurnString.add(orbitA.getPeriapsis().getAsString());
      firstBurnString.add(mostEfficient[0].getAsString());
      secondBurnString.add(orbitB.getApoapsis().getAsString());
      secondBurnString.add(mostEfficient[1].getAsString());
      transferOrbitStrings.add("Periapsis\t\t: " + transferOrbit1.getPeriapsis().getAsString());
      transferOrbitStrings.add("Apoapsis\t\t: " + transferOrbit1.getApoapsis().getAsString());
      transferOrbitStrings.add("Orbit Period\t: " + transferOrbit1.getOrbitalPeriod().getAsString());
    } else {
      firstBurnString.add(orbitB.getPeriapsis().getAsString());
      firstBurnString.add(mostEfficient[0].getAsString());
      secondBurnString.add(orbitA.getApoapsis().getAsString());
      secondBurnString.add(mostEfficient[1].getAsString());
      transferOrbitStrings.add("Periapsis%t%t: " + transferOrbit2.getPeriapsis().getAsString());
      transferOrbitStrings.add("Apoapsis%t%t: " + transferOrbit2.getApoapsis().getAsString());
      transferOrbitStrings.add("Orbit Period%t: " + transferOrbit2.getOrbitalPeriod().getAsString());
    }

    ArrayList<ArrayList<String>> hohmannStringsOutput = new ArrayList<>();
    hohmannStringsOutput.add(firstBurnString);
    hohmannStringsOutput.add(transferOrbitStrings);
    hohmannStringsOutput.add(secondBurnString);

    return hohmannStringsOutput;
  }
}

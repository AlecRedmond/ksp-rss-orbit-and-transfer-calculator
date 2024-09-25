package org.example.equations.method;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.equations.application.Body;
import org.example.equations.application.Keplerian;
import org.example.gui.method.VVDataElement;

import java.util.ArrayList;

@Data
@Getter
@Setter
@NoArgsConstructor
public class KeplerianMethod {
  private Keplerian keplerian = new Keplerian();
  private ArrayList<VVDataElement> listOfHeldParameters = new ArrayList<>();

  public KeplerianMethod(Body body, double apsis1, double apsis2, boolean isFromSeaLevel) {
    double apoapsis = apsis1;
    double periapsis = apsis2;

    if (apsis1 < apsis2) {
      periapsis = apsis1;
      apoapsis = apsis2;
    }

    this.keplerian.setBody(body);

    if (isFromSeaLevel) {
      apoapsis += body.getRadius();
      periapsis += body.getRadius();
    }

    setEllipseElements(apoapsis, periapsis);

    apoapsis -= body.getRadius();
    periapsis -= body.getRadius();

    this.keplerian.setApoapsis(apoapsis);
    this.keplerian.setPeriapsis(periapsis);
  }

  private void setEllipseElements(double trueApoapsis, double truePeriapsis) {
    double semiMajorAxis = (trueApoapsis + truePeriapsis) / 2;
    double eccentricity = (trueApoapsis - truePeriapsis) / (trueApoapsis + truePeriapsis);
    this.keplerian.setSemiMajorAxis(semiMajorAxis);
    this.keplerian.setEccentricity(eccentricity);
  }

  public void setFromDataElement(VVDataElement vvDataElement){
    String elementName = vvDataElement.getParameterName().toLowerCase();
    boolean holdEnabled = vvDataElement.isHeld();
    double data = vvDataElement.getData();
    if(holdEnabled){
      this.listOfHeldParameters.add(vvDataElement);
      setFromString(elementName,data);
    }
  }

  public void calculateMissing(){
    boolean periapsis = false;
    boolean apoapsis = false;
    boolean semiMajorAxis = false;
    boolean eccentricity = false;

    if(listOfHeldParameters.size() != 2){
      this.keplerian.setAllToZero();
      return;
    }

    for(VVDataElement element : listOfHeldParameters){
      if(element.getParameterName().toLowerCase().equals("periapsis")){
        periapsis = true;
      }
      if(element.getParameterName().toLowerCase().equals("apoapsis")){
        apoapsis = true;
      }
      if(element.getParameterName().toLowerCase().equals("eccentricity")){
        eccentricity = true;
      }
      if(element.getParameterName().toLowerCase().equals("semi-major axis")){
        semiMajorAxis = true;
      }
    }

    if(periapsis && apoapsis){
      this.keplerian = FillEquations.findPeriapsisApoapsis(this.keplerian);
    }

    if(periapsis && eccentricity || apoapsis && eccentricity){
      this.keplerian = FillEquations.findApsisEccentricity(this.keplerian,periapsis);
    }

    if(periapsis && semiMajorAxis || apoapsis && semiMajorAxis){
      this.keplerian = FillEquations.findApsisSemiMajorAxis(this.keplerian,periapsis);
    }

    if(semiMajorAxis && eccentricity){
      this.keplerian = FillEquations.findEccentricitySemiMajorAxis(this.keplerian);
    }



  }

  private void setFromString(String string, double data) {
    switch (string) {
      case "periapsis" -> this.keplerian.setPeriapsis(data);
      case "apoapsis" -> this.keplerian.setApoapsis(data);
      case "eccentricity" -> this.keplerian.setEccentricity(data);
      case "semi-major axis" -> this.keplerian.setSemiMajorAxis(data);
    }
  }

  public double getFromParameterName(String parameter) {
    switch (parameter) {
      case "periapsis" -> {
        return this.keplerian.getPeriapsis();
      }
      case "apoapsis" -> {
        return this.keplerian.getApoapsis();
      }
      case "eccentricity" -> {
        return this.keplerian.getEccentricity();
      }
      case "semi-major axis" -> {
        return this.keplerian.getSemiMajorAxis();
      }
    }
    return 0;
  }


}

package org.example.equations.application.keplerianelements;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.formatting.StringUnitParser;

@Data
@NoArgsConstructor
public class OrbitalPeriod extends KeplerElement<Double> {
  private double data;

  public OrbitalPeriod(double data) {
    this.data = data;
    this.setHold(false);
  }

  @Override
  public void set(Double aDouble) {
    this.data = aDouble;
  }

  @Override
  public Double get() {
    return this.data;
  }

  @Override
  public String getAsString() {
    return StringUnitParser.doubleToString(this.data,unitSI(),false,0,displayName());
  }

  @Override
  public void setFromString(String string) {

  }

  @Override
  public String displayName() {
    return "orbital period";
  }

  @Override
  public String unitSI() {
    return "";
  }
}

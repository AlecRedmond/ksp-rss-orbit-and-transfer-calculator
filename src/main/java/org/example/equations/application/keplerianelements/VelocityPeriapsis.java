package org.example.equations.application.keplerianelements;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.formatting.StringUnitParser;

@Data
@NoArgsConstructor
public class VelocityPeriapsis extends KeplerElement<Double> {
  private double data;

  public VelocityPeriapsis(double data) {
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
  public String displayName() {
    return "Velocity Pe";
  }

  @Override
  public String unitSI() {
    return "m/s";
  }

  @Override
  public String getAsString() {
    return StringUnitParser.doubleToString(
        this.data, unitSI(), false, 0, (new Velocity().displayName()));
  }

  @Override
  public void setFromString(String string) {
    double dataFromString;
    dataFromString = StringUnitParser.stringToDouble(string);
    this.data = dataFromString;
  }
}

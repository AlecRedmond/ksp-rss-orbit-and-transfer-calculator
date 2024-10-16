package org.example.equations.application.keplerianelements;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.formatting.StringUnitParser;

@Data
@NoArgsConstructor
public class Periapsis implements KeplerInterface<Double> {
  private double data;

  public Periapsis(double data) {
    this.data = data;
  }

  @Override
  public void set(Double data) {
    this.data = data;
  }

  @Override
  public Double get() {
    return this.data;
  }

  @Override
  public String getAsString() {
    return StringUnitParser.doubleToString(this.data,unitSI(),true,2,"apsis");
  }

  @Override
  public void setFromString(String string) {
    double dataFromString;
    dataFromString = StringUnitParser.stringToDouble(string);
    this.data = dataFromString;
  }

  @Override
  public String displayName() {
    return "Periapsis";
  }

  @Override
  public String unitSI() {
    return "m";
  }
}

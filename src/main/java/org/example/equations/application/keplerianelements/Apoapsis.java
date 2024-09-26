package org.example.equations.application.keplerianelements;

import lombok.Data;
import org.example.formatting.StringUnitParser;

@Data
public class Apoapsis extends KeplerElement<Double> {
  private double data;

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
    return StringUnitParser.doubleToString(this.data, this.getClass().getSimpleName());
  }

  @Override
  public void setFromString(String string) {
    double dataFromString;
    dataFromString = StringUnitParser.stringToDouble(string);
    this.data = dataFromString;
  }

  @Override
  public String displayName() {
    return "Apoapsis";
  }
}

package org.example.equations.application.keplerianelements;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.formatting.StringUnitParser;

@Data
@NoArgsConstructor
public class Velocity implements KeplerInterface<Double> {
  public double data = 0.0;

  @Override
  public void set(Double velocity) {
    this.data = velocity;
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
    double dataFromString;
    dataFromString = StringUnitParser.stringToDouble(string);
    this.data = dataFromString;
  }

  @Override
  public String displayName() {
    return "Velocity";
  }

  @Override
  public String unitSI() {
    return "m/s";
  }
}

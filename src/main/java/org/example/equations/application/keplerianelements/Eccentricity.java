package org.example.equations.application.keplerianelements;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.formatting.StringUnitParser;

@Data
@NoArgsConstructor
public class Eccentricity implements KeplerInterface<Double> {
  private double data;

  public Eccentricity(double data) {
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
    return StringUnitParser.doubleToString(this.data,unitSI(),false,3,displayName());
  }

  @Override
  public void setFromString(String string) {
    double dataFromString;
    dataFromString = StringUnitParser.stringToDouble(string);
    if (dataFromString >= 1 || dataFromString < 0) {
      dataFromString = 0;
    }
    this.data = dataFromString;
  }

  @Override
  public String displayName() {
    return "Eccentricity";
  }

  @Override
  public String unitSI() {
    return "";
  }
}

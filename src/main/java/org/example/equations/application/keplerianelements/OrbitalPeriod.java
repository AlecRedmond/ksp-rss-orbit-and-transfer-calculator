package org.example.equations.application.keplerianelements;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.formatting.StringUnitParser;

@Data
@NoArgsConstructor
public class OrbitalPeriod extends KeplerBase {
  private double data;

  public OrbitalPeriod(double data) {
    this.data = data;
  }

  @Override
  public KeplarianElement getType() {
    return KeplarianElement.ORBITAL_PERIOD;
  }

  @Override
  public String getAsString() {
    return StringUnitParser.doubleToString(this.data, unitSI(), false, 0, displayName());
  }

  @Override
  public void setFromString(String string) {
    double dataFromString;
    dataFromString = StringUnitParser.stringToDouble(string);
    this.data = dataFromString;
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

package org.artools.orbitcalculator.application.writeableorbit.keplerianelements;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.artools.orbitcalculator.method.writeableorbit.stringformatting.StringUnitParser;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class OrbitalPeriod extends KeplerBase {
  private double data;

  public OrbitalPeriod(double data) {
    this.data = data;
  }

  @Override
  public KeplerEnums getType() {
    return KeplerEnums.ORBITAL_PERIOD;
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

package org.artools.orbitcalculator.application.writeableorbit.keplerianelements;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.artools.orbitcalculator.method.writeableorbit.stringformatting.StringUnitParser;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class Eccentricity extends KeplerBase {
  private double data;

  public Eccentricity(double data) {
    this.data = data;
  }

  @Override
  public KeplerEnums getType() {
    return KeplerEnums.ECCENTRICITY;
  }

  @Override
  public String getAsString() {
    return StringUnitParser.doubleToString(this.data, unitSI(), false, 3, displayName());
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

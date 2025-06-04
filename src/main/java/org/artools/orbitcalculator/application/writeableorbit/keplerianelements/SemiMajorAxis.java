package org.artools.orbitcalculator.application.writeableorbit.keplerianelements;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.artools.orbitcalculator.method.writeableorbit.stringformatting.StringUnitParser;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class SemiMajorAxis extends KeplerBase {
  private double data;

  public SemiMajorAxis(double data) {
    this.data = data;
  }

  @Override
  public KeplerEnums getType() {
    return KeplerEnums.SEMI_MAJOR_AXIS;
  }

  @Override
  public String getAsString() {
    return StringUnitParser.doubleToString(this.data, unitSI(), true, 3, "");
  }

  @Override
  public void setFromString(String string) {
    double dataFromString;
    dataFromString = StringUnitParser.stringToDouble(string);
    this.data = dataFromString;
  }

  @Override
  public String displayName() {
    return "Semi-Major Axis";
  }

  @Override
  public String unitSI() {
    return "m";
  }

}

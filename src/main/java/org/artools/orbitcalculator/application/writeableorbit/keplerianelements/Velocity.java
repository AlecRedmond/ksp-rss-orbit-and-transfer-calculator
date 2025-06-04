package org.artools.orbitcalculator.application.writeableorbit.keplerianelements;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.artools.orbitcalculator.method.writeableorbit.stringformatting.StringUnitParser;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class Velocity extends KeplerBase {
  public double data = 0.0;

  @Override
  public KeplerEnums getType() {
    return KeplerEnums.VELOCITY;
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

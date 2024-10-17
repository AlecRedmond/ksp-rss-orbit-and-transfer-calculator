package org.example.equations.application.keplerianelements;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.formatting.StringUnitParser;

@Data
@NoArgsConstructor
public class VelocityApoapsis extends KeplerBase {
  private double data;

  public VelocityApoapsis(double data) {
    this.data = data;
  }

  @Override
  public String displayName() {
    return "Velocity Ap";
  }

  @Override
  public String unitSI() {
    return "m/s";
  }

  @Override
  public KeplerEnums getType() {
    return KeplerEnums.VELOCITY_APOAPSIS;
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

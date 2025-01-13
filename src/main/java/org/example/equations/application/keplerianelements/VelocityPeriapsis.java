package org.example.equations.application.keplerianelements;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.equations.method.holdlogic.ToggleAction;
import org.example.stringformatting.StringUnitParser;

import java.util.Map;

@Data
@NoArgsConstructor
public class VelocityPeriapsis extends KeplerBase {
  private double data;

  public VelocityPeriapsis(double data) {
    this.data = data;
  }

  @Override
  public String displayName() {
    return "Velocity Pe";
  }

  @Override
  public String unitSI() {
    return "m/s";
  }

  @Override
  public Map<KeplerEnums, ToggleAction> toggleCompatibility() {
    return Map.of(
            KeplerEnums.APOAPSIS,
            ToggleAction.INCOMPATIBLE,
            KeplerEnums.ECCENTRICITY,
            ToggleAction.INCOMPATIBLE,
            KeplerEnums.INCLINATION,
            ToggleAction.NO_INTERFERENCE,
            KeplerEnums.NODAL_PRECESSION,
            ToggleAction.NO_INTERFERENCE);
  }

  @Override
  public KeplerEnums getType() {
    return KeplerEnums.VELOCITY_PERIAPSIS;
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

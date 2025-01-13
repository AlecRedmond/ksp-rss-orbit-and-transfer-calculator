package org.example.equations.application.keplerianelements;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.equations.method.holdlogic.ToggleAction;
import org.example.stringformatting.StringUnitParser;

import java.util.Map;

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

  @Override
  public Map<KeplerEnums, ToggleAction> toggleCompatibility() {
    return Map.of(
            KeplerEnums.SEMI_MAJOR_AXIS,
            ToggleAction.EITHER_OR,
            KeplerEnums.INCLINATION,
            ToggleAction.NO_INTERFERENCE,
            KeplerEnums.NODAL_PRECESSION,
            ToggleAction.NO_INTERFERENCE);
  }
}

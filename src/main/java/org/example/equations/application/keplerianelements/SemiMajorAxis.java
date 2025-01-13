package org.example.equations.application.keplerianelements;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.equations.method.holdlogic.ToggleAction;
import org.example.stringformatting.StringUnitParser;

import java.util.Map;

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

  @Override
  public Map<KeplerEnums, ToggleAction> toggleCompatibility() {
    return Map.of(
        KeplerEnums.ORBITAL_PERIOD,
        ToggleAction.EITHER_OR,
        KeplerEnums.INCLINATION,
        ToggleAction.NO_INTERFERENCE,
        KeplerEnums.NODAL_PRECESSION,
        ToggleAction.NO_INTERFERENCE);
  }
}

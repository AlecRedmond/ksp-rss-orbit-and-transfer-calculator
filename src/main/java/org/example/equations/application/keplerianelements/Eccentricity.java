package org.example.equations.application.keplerianelements;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.stringformatting.StringUnitParser;

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

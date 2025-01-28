package org.example.equations.application.keplerianelements;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.stringformatting.StringUnitParser;

@Data
@NoArgsConstructor
public class Apoapsis extends KeplerBase {
  private double data;

  public Apoapsis(double data) {
    this.data = data;
  }

  @Override
  public KeplerEnums getType() {
    return KeplerEnums.APOAPSIS;
  }

  @Override
  public String getAsString() {
    return StringUnitParser.doubleToString(this.data, unitSI(), true, 2, "apsis");
  }

  @Override
  public void setFromString(String string) {
    double dataFromString;
    dataFromString = StringUnitParser.stringToDouble(string);
    this.data = dataFromString;
  }

  @Override
  public String displayName() {
    return "Apoapsis";
  }

  @Override
  public String unitSI() {
    return "m";
  }

}

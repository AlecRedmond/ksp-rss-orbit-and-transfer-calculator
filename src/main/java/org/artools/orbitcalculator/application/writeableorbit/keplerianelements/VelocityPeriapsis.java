package org.artools.orbitcalculator.application.writeableorbit.keplerianelements;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.artools.orbitcalculator.method.writeableorbit.stringformatting.StringUnitParser;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class VelocityPeriapsis extends KeplerBase {
  private double data;
  private boolean isDeltaV;

  private double tangentialVelocity;
  private double normalVelocity;
  private double totalVelocity;

  public VelocityPeriapsis(double data) {
    this.data = data;
    this.isDeltaV = false;
  }

  public void setToDeltaV(double tangentialVelocity, double normalVelocity, double totalVelocity){
    isDeltaV = true;
    this.tangentialVelocity = tangentialVelocity;
    this.normalVelocity = normalVelocity;
    this.totalVelocity = totalVelocity;
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

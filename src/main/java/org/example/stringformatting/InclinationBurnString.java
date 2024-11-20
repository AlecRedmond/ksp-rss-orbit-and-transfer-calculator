package org.example.stringformatting;

import org.example.equations.application.keplerianelements.Velocity;
import org.example.equations.method.InclinationBurn;

public class InclinationBurnString {
  public static String getIncChangeBurnString(InclinationBurn inclinationBurn) {
    double[] vectorDV = inclinationBurn.getVectorDV();
    Velocity tangentialVelocity = new Velocity();
    tangentialVelocity.setData(vectorDV[0]);
    Velocity normalVelocity = new Velocity();
    normalVelocity.setData(vectorDV[1]);
    Velocity totalVelocity = new Velocity();
    totalVelocity.setData(Math.sqrt(vectorDV[0] * vectorDV[0] + vectorDV[1] * vectorDV[1]));
    return String.format(
        "Prograde: %s%nNormal: %s%nTotal: %s%n",
        tangentialVelocity.getAsString(),
        normalVelocity.getAsString(),
        totalVelocity.getAsString());
  }

    public static String getOtherBurnString(InclinationBurn inclinationBurn) {
        double[] vectorDV = inclinationBurn.getOtherBurnDV();
        Velocity tangentialVelocity = new Velocity();
        tangentialVelocity.setData(vectorDV[0]);
        return String.format(
                "Prograde: %s%n",
                tangentialVelocity.getAsString());
    }
}

package org.example.equations.method;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.example.equations.application.Body;
import org.example.equations.application.Keplerian;

@Data
@Getter
@Setter
public class HohmannTransfer {
  private Keplerian keplerianOne;
  private Keplerian keplerianTwo;
  private static Body body = Body.EARTH;

  public static double velocityFromAltitudeAndSMA(double altitude, double sma) {
    double radius = addRadiusOfBody(altitude);
    return Math.sqrt(body.getMu() * ((2 / radius) - (1 / sma)));
  }

  private static double addRadiusOfBody(double altitude) {
    return altitude += body.getRadius();
  }

  public static double smaFromVelocityAndAltitude(double velocity, double altitude) {
    double radius = addRadiusOfBody(altitude);
    return 1 / ((2 / radius) - ((velocity * velocity) / body.getMu()));
  }

  public static double altitudeFromVelocityAndSMA(double velocity, double sma) {
    double radius = 2 / (((velocity * velocity) / body.getMu()) + (1 / sma));
    radius -= body.getRadius();
    return radius;
  }
}

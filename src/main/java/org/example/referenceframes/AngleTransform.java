package org.example.referenceframes;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;

public class AngleTransform {

  public RealMatrix toOrbitalFrame(
      RealMatrix vector, double rightAscension, double inclination, double argumentPE) {
    return zxzTransform(rightAscension, inclination, argumentPE).multiply(vector);
  }

  public RealMatrix toInertialFrame(
          RealMatrix vector, double rightAscension, double inclination, double argumentPE) {
    return zxzTransform(-argumentPE, -inclination, -rightAscension).multiply(vector);
  }

  protected RealMatrix zxzTransform(double angle1, double angle2, double angle3) {
    return zRotate(angle1).multiply(xRotate(angle2)).multiply(zRotate(angle3));
  }

  protected RealMatrix zRotate(double angle) {
    double[][] data = {
      {cos(angle), sin(angle), 0},
      {-sin(angle), cos(angle), 0},
      {0, 0, 1}
    };
    return MatrixUtils.createRealMatrix(data);
  }

  protected RealMatrix xRotate(double angle) {
    double[][] data = {
      {1, 0, 0},
      {0, cos(angle), sin(angle)},
      {0, -sin(angle), cos(angle)}
    };
    return MatrixUtils.createRealMatrix(data);
  }
}

package org.example.referenceframes;

import java.util.List;
import java.util.stream.IntStream;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.junit.jupiter.api.Test;

class AngleTransformTest {
  AngleTransform test = new AngleTransform();
  RealMatrix inertialVector = MatrixUtils.createRealMatrix(new double[][]{
          {0},
          {0},
          {0}
  });
  double rightAscension = Math.toRadians(0);
  double inclination = Math.toRadians(0);
  double argumentPE = Math.toRadians(0);

  @Test
  void toOrbitalFrame() {
    System.out.println(test.toOrbitalFrame(inertialVector,rightAscension,inclination,argumentPE));
  }

  @Test
  void toInertialFrame() {
    getPlanarMatrixes(150000).forEach(vector -> {
      System.out.println(test.toInertialFrame(vector,rightAscension,inclination,argumentPE));
    });
  }

  public List<RealMatrix> getPlanarMatrixes(double radius){
    return IntStream.range(0,361).filter(i -> i % 90 == 0).mapToObj(i -> getPlanarMatrix(i,radius)).toList();
  }

  public RealMatrix getPlanarMatrix(double angle, double radius){
    angle = Math.toRadians(angle);
    var xCoord = radius * Math.cos(angle);
    var yCoord = radius * Math.sin(angle);
    return MatrixUtils.createRealMatrix(new double[][]{
            {xCoord},
            {yCoord},
            {0}
    });
  }
}

package org.example.equations.method.vector;

import org.example.equations.application.Body;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class OrreryBuilderTest {
  OrreryBuilder test = new OrreryBuilder();

  @Test
  void initialPositions() {
    //I wrote this in a rush to make a console visualisation where I could check my positions vs Horizons data
    //Avert your eyes.
    //Please.
    test.initialPositions();
    var orrery = test.getOrrery().getMap();
    orrery.remove(Body.NEPTUNE);
    orrery.remove(Body.URANUS);
    var xLowest = orrery.values().stream().min(Comparator.comparing(val -> val.getRadius().getX())).get().getRadius().getX();
    var xHighest = orrery.values().stream().max(Comparator.comparing(val -> val.getRadius().getX())).get().getRadius().getX();
    var yHighest = orrery.values().stream().max(Comparator.comparing(val -> val.getRadius().getY())).get().getRadius().getY();
    var xMax = 100;
    var stepSize = (xHighest - xLowest) / xMax;
    Map<Body,int[]> positionMap = new HashMap<>();
    positionMap.put(Body.SUN,new int[]{(int) ((0 - xLowest) / stepSize),(int)((0 + yHighest) /stepSize)});
    orrery.forEach((key,val) -> {
      int xVal = (int) ((val.getRadius().getX() - xLowest) /stepSize);
      int yVal = (int) ((- val.getRadius().getY() + yHighest) /stepSize);
      positionMap.put(key,new int[]{xVal,yVal});
    });
    var yMax = positionMap.values().stream().max(Comparator.comparing(ints -> ints[1])).get()[1];
    for(int y = 0; y < yMax + 1; y++){
      StringBuilder sb = new StringBuilder();
      for(int x = 0; x < xMax + 1; x++){
        buildLineString(x, y, positionMap, sb);
      }
      System.out.println(sb);
    }
  }

  private static void buildLineString(int x, int y, Map<Body, int[]> positionMap, StringBuilder sb) {
    var position = new int[]{x, y};
    var first = positionMap.entrySet().stream().filter(bodyEntry -> Arrays.equals(bodyEntry.getValue(), position)).findFirst();
    if(first.isPresent()){
      var body = first.get().getKey();
      var string = body.equals(Body.SUN) ? "X" : first.get().getKey().toString();
      if(body.equals(Body.MOON)){
        string = "Earth";
      }
      char[] chars = string.toCharArray();
      sb.append(chars[0]);
    } else {
      sb.append(".");
    }
  }
}

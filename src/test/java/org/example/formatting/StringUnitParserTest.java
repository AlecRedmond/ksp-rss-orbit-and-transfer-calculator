package org.example.formatting;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class StringUnitParserTest {

  double timeHour = 176463.6;
  String stringHour = "02:01:01:03.6";
  double timeHour2 = 43200.00;
  String stringHour2 = "12:00:00.0";
  
  
  @Test
  void doubleToString() {
//    String testStringHour = StringUnitParser.doubleToString(timeHour,"",false,0,"orbital period");
//    assertEquals(stringHour,testStringHour);
    String testStringHour2 = StringUnitParser.doubleToString(timeHour2,"",false,0,"orbital period");
    assertEquals(stringHour2,testStringHour2);
  }

  @Test
  void stringToDouble(){
//    double testTimeHour = StringUnitParser.stringToDouble(stringHour);
//    assertEquals(timeHour,testTimeHour);
    double testTimeHour2 = StringUnitParser.stringToDouble(stringHour2);
    assertEquals(timeHour2,testTimeHour2);
  }
}

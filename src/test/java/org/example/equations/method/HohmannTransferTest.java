package org.example.equations.method;

import org.example.equations.application.Keplerian;
import org.example.equations.application.keplerianelements.Apoapsis;
import org.example.equations.application.keplerianelements.Periapsis;
import org.example.equations.application.keplerianelements.Velocity;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

class HohmannTransferTest {
  static KeplerianMethod keplerianMethod1;
  static KeplerianMethod keplerianMethod2;
  static Keplerian keplerian1 = new Keplerian();
  static Keplerian keplerian2 = new Keplerian();
  static double orbit1PE = 250e3;
  static double orbit1AP = 250e3;
  static double orbit2PE = 35786e3;
  static double orbit2AP = 35786e3;

  @BeforeAll
  static void startUp() {

    keplerianMethod1 = new KeplerianMethod(orbit1AP,orbit1PE);
    keplerianMethod2 = new KeplerianMethod(orbit2AP,orbit2PE);

  }

  @Test
  void testHohmannTransfer(){
    HohmannTransfer hohmannTransfer = new HohmannTransfer(keplerianMethod1,keplerianMethod2);
    ArrayList<ArrayList<String>> hohmannText = hohmannTransfer.hohmannStringOutput();
    for(ArrayList<String> element : hohmannText){
      for(String subElement : element){
        System.out.println(subElement);
      }
    }
  }


}

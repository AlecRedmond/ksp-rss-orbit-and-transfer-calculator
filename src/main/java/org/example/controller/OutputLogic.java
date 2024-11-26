package org.example.controller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.example.equations.application.Orbit;
import org.example.equations.application.keplerianelements.Kepler;
import org.example.equations.application.keplerianelements.Kepler.KeplerEnums;
import org.example.equations.method.HohmannTransfer;
import org.example.equations.method.InclinationBurn;
import org.example.gui.nodemethod.OrbitalGridPane;
import org.example.gui.scenebuilder.NodeFunctions;
import org.example.stringformatting.InclinationBurnString;

public class OutputLogic {
  public static void writeVisVivaResults() {
    Orbit[] orbits = WorkingLogic.getOrbits();
    writeToGridPane(orbits);
  }

  private static void writeToGridPane(Orbit[] orbits) {
    boolean isLeft = true;
    for (Orbit orbit : orbits) {
      HashMap<KeplerEnums, String> newText = new HashMap<>();
      HashMap<KeplerEnums, Kepler> keplerMap =
          (HashMap<KeplerEnums, Kepler>) orbit.getKeplarianElements();
      for (Map.Entry<KeplerEnums, Kepler> entry : keplerMap.entrySet()) {
        newText.put(entry.getKey(), entry.getValue().getAsString());
      }
      OrbitalGridPane.setFieldText(newText, isLeft);
      isLeft = false;
    }
  }

  public static void clearFields() {
    OrbitalGridPane.setAllToZero();
  }

  public static void writeHohmannTransferResults() {
    HohmannTransfer hohmannTransfer = WorkingLogic.getHohmannTransfer();
    String firstBurnString =
        "First Burn: "
            + Math.round(hohmannTransfer.getFirstBurnDV())
            + " m/s at "
            + hohmannTransfer.getApsisOfFirstBurn().toString()
            + "\n";
    String orbitString =
        "Transfer Orbit: "
            + hohmannTransfer.getTransferOrbit().getAsString(KeplerEnums.APOAPSIS)
            + " X "
            + hohmannTransfer.getTransferOrbit().getAsString(KeplerEnums.PERIAPSIS)
            + "\n";
    String secondBurnString =
        "Second Burn: "
            + Math.round(hohmannTransfer.getSecondBurnDV())
            + " m/s at "
            + hohmannTransfer.getApsisOfSecondBurn().toString()
            + "\n";
    String totalBurn = "Total Burn: " + Math.round(hohmannTransfer.getTotalBurnDV()) + " m/s";

    String output = firstBurnString + orbitString + secondBurnString + totalBurn;

    NodeFunctions.setText(output);
  }

  public static void writeInclinationChangeResults() {
    InclinationBurn inclinationBurn = WorkingLogic.getInclinationBurn();
    HohmannTransfer hohmannTransfer = WorkingLogic.getHohmannTransfer();

    String inclinationChangeString = InclinationBurnString.getIncChangeBurnString(inclinationBurn);
    String otherBurnString = InclinationBurnString.getOtherBurnString(inclinationBurn);
    String orbitString =
            "Transfer Orbit: "
                    + hohmannTransfer.getTransferOrbit().getAsString(KeplerEnums.APOAPSIS)
                    + " X "
                    + hohmannTransfer.getTransferOrbit().getAsString(KeplerEnums.PERIAPSIS)
                    + "\n";
    String totalBurn =
        "Total Burn: "
            + (Math.round(
                inclinationBurn.getMagnitudeDV() + inclinationBurn.getMagnitudeOtherBurnDV()))
            + " m/s";

    String output =
        (inclinationBurn.isFirstBurn())
            ? (inclinationChangeString + orbitString + otherBurnString + totalBurn)
            : (otherBurnString + orbitString + inclinationChangeString + totalBurn);

    NodeFunctions.setText(output);
  }
}

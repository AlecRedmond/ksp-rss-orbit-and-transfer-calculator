package org.example.gui.method.scene;

import java.util.HashMap;
import org.example.equations.application.keplerianelements.Kepler;
import org.example.gui.application.scene.GuiButtons;

public class ButtonActions {

  public static void debugButton() {
    GuiButtons.getDeBugButton()
        .setOnAction(
            actionEvent -> {
              HashMap<Kepler.KeplerEnums, Boolean> currentHolds = OrbitalGridPane.getHolds(true);
            });
  }
}

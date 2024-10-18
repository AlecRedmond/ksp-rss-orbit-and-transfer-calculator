package org.example.gui.method.scene;

import static org.example.equations.application.keplerianelements.Kepler.KeplerEnums.*;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.GridPane;
import org.example.equations.application.Orbit;
import org.example.equations.application.keplerianelements.Kepler.KeplerEnums;
import org.example.gui.application.scene.SceneElements;

public class OrbitalGridPane {
  private static final KeplerEnums[] keplerEnumsList = {
    APOAPSIS,
    PERIAPSIS,
    ECCENTRICITY,
    SEMI_MAJOR_AXIS,
    ORBITAL_PERIOD,
    VELOCITY_APOAPSIS,
    VELOCITY_PERIAPSIS
  };

  public static void buildGridPane() {
    Orbit orbit = new Orbit();
    GridPane gridPane = new GridPane();
    for (int i = 0; i < keplerEnumsList.length; i++) {
      Label label = new Label(orbit.getDisplayName(keplerEnumsList[i]));
      gridPane.add(label, 0, i);
      gridPane.add(new ToggleButton("Hold"), 1, i);
      gridPane.add(new TextField(""), 2, i);
      gridPane.add(new TextField(""), 3, i);
      gridPane.add(new ToggleButton("Hold"), 4, i);
    }
    SceneElements.setGridPane(gridPane);
  }
}

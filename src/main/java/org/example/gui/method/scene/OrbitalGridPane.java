package org.example.gui.method.scene;

import static org.example.equations.application.keplerianelements.Kepler.KeplerEnums.*;
import static org.example.gui.method.scene.OrbitalGridPane.GridColumns.*;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.GridPane;
import lombok.Data;
import org.example.equations.application.Orbit;
import org.example.equations.application.keplerianelements.Kepler.KeplerEnums;
import org.example.gui.application.scene.SceneElements;

import java.util.ArrayList;
import java.util.HashMap;

@Data
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

  public enum GridColumns {
    LABEL(0),
    LEFT_HOLD(1),
    LEFT_TEXTFIELD(2),
    RIGHT_TEXTFIELD(3),
    RIGHT_HOLD(4);

    private int gridIndex;

    GridColumns(int gridIndex) {
      this.gridIndex = gridIndex;
    }
  }

  public static void buildGridPane() {
    Orbit orbit = new Orbit();
    GridPane gridPane = new GridPane();
    for (int i = 0; i < keplerEnumsList.length; i++) {
      Label label = new Label(orbit.getDisplayName(keplerEnumsList[i]));
      gridPane.add(label, LABEL.gridIndex, i);
      gridPane.add(new ToggleButton("Hold"), LEFT_HOLD.gridIndex, i);
      gridPane.add(new TextField(""), LEFT_TEXTFIELD.gridIndex, i);
      gridPane.add(new TextField(""), RIGHT_TEXTFIELD.gridIndex, i);
      gridPane.add(new ToggleButton("Hold"), RIGHT_HOLD.gridIndex, i);
    }
    SceneElements.setGridPane(gridPane);
  }

  public static HashMap<KeplerEnums, Boolean> getHolds(boolean leftMap) {
    Node[][] gridPaneArray = getGridPaneArray();

    int colIndex;
    if (leftMap) {
      colIndex = LEFT_HOLD.gridIndex;
    } else {
      colIndex = RIGHT_HOLD.gridIndex;
    }

    HashMap<KeplerEnums, Boolean> holdsMap = new HashMap<>();
    for (int i = 0; i < keplerEnumsList.length; i++) {
      ToggleButton toggleButton = (ToggleButton) gridPaneArray[i][colIndex];
      boolean isSelected = toggleButton.isSelected();
      holdsMap.put(keplerEnumsList[i],isSelected);
    }
    return holdsMap;
  }

  private static Node[][] getGridPaneArray() {
    int nRows = SceneElements.getGridPane().getRowCount();
    int nCols = SceneElements.getGridPane().getColumnCount();
    Node[][] gridPaneArray = new Node[nRows][nCols];
    for (Node node : SceneElements.getGridPane().getChildren()) {
      gridPaneArray[GridPane.getRowIndex(node)][GridPane.getColumnIndex(node)] = node;
    }

    return gridPaneArray;
  }
}

package org.example.gui.scene.sceneelements;

import static org.example.gui.scene.sceneelements.OrbitalGridPane.GridColumn.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.GridPane;
import lombok.Data;
import org.example.equations.application.Orbit;
import org.example.equations.method.holdlogic.OrbitalParameterHolds;
import org.example.equations.application.keplerianelements.Kepler;
import org.example.equations.application.keplerianelements.Kepler.KeplerEnums;
import org.example.equations.method.HohmannTransfer;
import org.example.equations.method.OrbitBuilder;

@Data
public class OrbitalGridPane {
  private Node[][] nodesArray;
  private GridPane gridPane;
  private Orbit orbit;
  private OrbitalParameterHolds orbitalParameterHolds;

  public enum GridColumn {
    LABEL,
    HOLD,
    TEXT_FIELD,
    BLOCKED_TEXT_FIELD
  }

  private List<GridColumn> gridColumns = new ArrayList<>(List.of(LABEL, HOLD, TEXT_FIELD));

  // Default to a basic orbit
  public OrbitalGridPane() {
    orbit = new OrbitBuilder(250000, 250000, 5.25).getOrbit();
    orbitalParameterHolds = new OrbitalParameterHolds();
    initialiseGridPane();
  }

  public OrbitalGridPane(OrbitalGridPane initialOrbitPane, OrbitalGridPane finalOrbitPane) {
    gridColumns = new ArrayList<>(List.of(BLOCKED_TEXT_FIELD));
    orbit =
        new HohmannTransfer(
                initialOrbitPane.getOrbit(), finalOrbitPane.getOrbit())
            .getTransferOrbit();
    initialiseGridPane();
  }

  public void setTransferOrbit(OrbitalGridPane initialOrbitPane, OrbitalGridPane finalOrbitPane) {
    gridColumns = new ArrayList<>(List.of(BLOCKED_TEXT_FIELD));
    setOrbit(
        new HohmannTransfer(
                initialOrbitPane.getOrbit(), finalOrbitPane.getOrbit())
            .getTransferOrbit());
  }

  public OrbitalGridPane(boolean isRight) {
    if (isRight) {
      orbit = new OrbitBuilder(35786000, 35786000, 0).getOrbit();
      gridColumns = new ArrayList<>(List.of(TEXT_FIELD, HOLD));
    } else {
      orbit = new OrbitBuilder(250000, 250000, 5.25).getOrbit();
    }
    orbitalParameterHolds = new OrbitalParameterHolds();
    initialiseGridPane();
  }

  public void setOrbit(Orbit orbit) {
    this.orbit = orbit;
    updateGridPane();
  }

  public Orbit getOrbitFromHolds() {
    int row = 0;
    int textFieldColumn = gridColumns.indexOf(TEXT_FIELD);
    for (Map.Entry<KeplerEnums, Kepler> entry : orbit.getKeplarianElements().entrySet()) {
      if (orbitalParameterHolds.getHold(entry.getKey())) {
        String string = ((TextField) nodesArray[row][textFieldColumn]).getText();
        orbit.setFromString(entry.getKey(), string);
      }
      row++;
    }
    orbit = new OrbitBuilder(orbit, orbitalParameterHolds).getOrbit();
    return orbit;
  }

  private void updateGridPane() {
    int row = 0;
    int column;
    if (gridColumns.contains(TEXT_FIELD)) {
      column = gridColumns.indexOf(TEXT_FIELD);
    } else if (gridColumns.contains(BLOCKED_TEXT_FIELD)) {
      column = gridColumns.indexOf(BLOCKED_TEXT_FIELD);
    } else {
      throw new RuntimeException("No column found to update!");
    }
    for (Map.Entry<KeplerEnums, Kepler> entry : orbit.getKeplarianElements().entrySet()) {
      ((TextField) nodesArray[row][column]).setText(entry.getValue().getAsString());
      row++;
    }
  }

  private void initialiseGridPane() {
    buildNodeArrayFromOrbit();
    gridPane = new GridPane();
    for (int row = 0; row < nodesArray.length; row++) {
      for (int col = 0; col < nodesArray[row].length; col++) {
        gridPane.add(nodesArray[row][col], col, row);
      }
    }
  }

  private void buildNodeArrayFromOrbit() {
    nodesArray = new Node[getRowsCount()][getColumnsCount()];
    int row = 0;
    for (Map.Entry<KeplerEnums, Kepler> entry : orbit.getKeplarianElements().entrySet()) {
      for (int col = 0; col < gridColumns.size(); col++) {
        insertNodeIntoNodeArray(row, col, entry.getKey());
      }
      row++;
    }
  }

  private int getRowsCount() {
    return orbit.getKeplarianElements().entrySet().size();
  }

  private int getColumnsCount() {
    return gridColumns.size();
  }

  private void insertNodeIntoNodeArray(int row, int column, KeplerEnums keplerEnum) {
    GridColumn gridColumn = gridColumns.get(column);
    switch (gridColumn) {
      case LABEL -> nodesArray[row][column] = new Label(orbit.getDisplayName(keplerEnum));
      case HOLD -> {
        nodesArray[row][column] = new ToggleButton("Hold");
        ((ToggleButton) nodesArray[row][column])
            .setOnAction(
                actionEvent -> {
                  orbitalParameterHolds.toggleParameter(keplerEnum);
                  mapHolds();
                });
      }
      case TEXT_FIELD -> nodesArray[row][column] = new TextField(orbit.getAsString(keplerEnum));
      case BLOCKED_TEXT_FIELD -> {
        nodesArray[row][column] = new TextField(orbit.getAsString(keplerEnum));
        nodesArray[row][column].setDisable(true);
      }
    }
  }

  private void mapHolds() {
    int row = 0;
    int column = gridColumns.indexOf(HOLD);
    for (Map.Entry<KeplerEnums, Kepler> entry : orbit.getKeplarianElements().entrySet()) {
      ((ToggleButton) nodesArray[row][column])
          .setSelected(orbitalParameterHolds.getHold(entry.getKey()));
      row++;
    }
  }
}

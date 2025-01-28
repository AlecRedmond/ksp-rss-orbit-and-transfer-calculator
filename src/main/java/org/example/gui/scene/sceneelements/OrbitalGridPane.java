package org.example.gui.scene.sceneelements;

import static org.example.gui.scene.sceneelements.OrbitalGridPane.GridColumn.*;

import java.util.List;
import java.util.Map;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.GridPane;
import lombok.Data;
import org.example.equations.application.Orbit;
import org.example.equations.application.keplerianelements.Kepler;
import org.example.equations.application.keplerianelements.Kepler.KeplerEnums;
import org.example.equations.method.HohmannTransfer;
import org.example.equations.method.OrbitBuilder;
import org.example.equations.method.holdlogic.OrbitalParameterHolds;

@Data
public class OrbitalGridPane {

  private Node[][] nodesArray;
  private GridPane gridPane;
  private Orbit orbit;
  private OrbitalParameterHolds orbitalParameterHolds;

  private List<GridColumn> gridColumns;

  public enum GridColumn {
    LABEL,
    HOLD,
    TEXT_FIELD,
    BLOCKED_TEXT_FIELD
  }

  // Constructor for initial orbit GridPane
  public OrbitalGridPane() {
    this.orbit = createDefaultInitialOrbit();
    this.orbitalParameterHolds = new OrbitalParameterHolds();
    this.gridColumns = defaultGridColumns();
    initializeGridPane();
  }

  // Constructor for intermediate transfer orbit GridPane
  public OrbitalGridPane(OrbitalGridPane initialOrbitPane, OrbitalGridPane finalOrbitPane) {
    this.orbit = createTransferOrbit(initialOrbitPane, finalOrbitPane);
    this.gridColumns = List.of(BLOCKED_TEXT_FIELD);
    initializeGridPane();
  }

  // Constructor for either initial or final orbit GridPane
  public OrbitalGridPane(boolean isRight) {
    this.orbit = isRight ? createDefaultFinalOrbit() : createDefaultInitialOrbit();
    this.orbitalParameterHolds = new OrbitalParameterHolds();
    this.gridColumns = isRight ? List.of(TEXT_FIELD, HOLD) : defaultGridColumns();
    initializeGridPane();
  }

  public void updateTransferOrbit(
      OrbitalGridPane initialOrbitPane, OrbitalGridPane finalOrbitPane) {
    this.orbit = createTransferOrbit(initialOrbitPane, finalOrbitPane);
    this.gridColumns = List.of(BLOCKED_TEXT_FIELD);
    refreshGridPane();
  }

  public void setOrbit(Orbit orbit) {
    this.orbit = orbit;
    refreshGridPane();
  }

  public Orbit extractOrbitFromHolds() {
    updateOrbitFromHolds();
    return new OrbitBuilder(this.orbit, this.orbitalParameterHolds).getOrbit();
  }

  private void initializeGridPane() {
    createNodeArrayFromOrbit();
    buildGridPane();
  }

  private void refreshGridPane() {
    updateGridPaneValues();
  }

  private Orbit createDefaultInitialOrbit() {
    return new OrbitBuilder(250_000, 250_000, 5.25).getOrbit();
  }

  private Orbit createDefaultFinalOrbit() {
    return new OrbitBuilder(35_786_000, 35_786_000, 0).getOrbit();
  }

  private Orbit createTransferOrbit(
      OrbitalGridPane initialOrbitPane, OrbitalGridPane finalOrbitPane) {
    return new HohmannTransfer(initialOrbitPane.getOrbit(), finalOrbitPane.getOrbit())
        .getTransferOrbit();
  }

  private List<GridColumn> defaultGridColumns() {
    return List.of(LABEL, HOLD, TEXT_FIELD);
  }

  private void createNodeArrayFromOrbit() {
    int rows = orbit.getKeplarianElements().size();
    int cols = gridColumns.size();
    nodesArray = new Node[rows][cols];

    int rowIndex = 0;
    for (Map.Entry<KeplerEnums, Kepler> entry : orbit.getKeplarianElements().entrySet()) {
      for (int colIndex = 0; colIndex < cols; colIndex++) {
        nodesArray[rowIndex][colIndex] = createNodeForCell(colIndex, entry.getKey());
      }
      rowIndex++;
    }
  }

  private Node createNodeForCell(int col, KeplerEnums keplerEnum) {
    return switch (gridColumns.get(col)) {
      case LABEL -> new Label(orbit.getDisplayName(keplerEnum));
      case HOLD -> createToggleButton(keplerEnum);
      case TEXT_FIELD -> new TextField(orbit.getAsString(keplerEnum));
      case BLOCKED_TEXT_FIELD -> createDisabledTextField(orbit.getAsString(keplerEnum));
    };
  }

  private ToggleButton createToggleButton(KeplerEnums keplerEnum) {
    ToggleButton toggleButton = new ToggleButton("Hold");
    toggleButton.setOnAction(
        event -> {
          orbitalParameterHolds.toggleParameter(keplerEnum);
          syncHoldsWithUI();
        });
    return toggleButton;
  }

  private TextField createDisabledTextField(String text) {
    TextField textField = new TextField(text);
    textField.setDisable(true);
    return textField;
  }

  private void buildGridPane() {
    gridPane = new GridPane();
    for (int row = 0; row < nodesArray.length; row++) {
      for (int col = 0; col < nodesArray[row].length; col++) {
        gridPane.add(nodesArray[row][col], col, row);
      }
    }
  }

  private void updateGridPaneValues() {
    int colIndex = getColumnIndexForTextField();
    int rowIndex = 0;
    for (Map.Entry<KeplerEnums, Kepler> entry : orbit.getKeplarianElements().entrySet()) {
      ((TextField) nodesArray[rowIndex][colIndex]).setText(entry.getValue().getAsString());
      rowIndex++;
    }
  }

  private int getColumnIndexForTextField() {
    if (gridColumns.contains(TEXT_FIELD)) {
      return gridColumns.indexOf(TEXT_FIELD);
    } else if (gridColumns.contains(BLOCKED_TEXT_FIELD)) {
      return gridColumns.indexOf(BLOCKED_TEXT_FIELD);
    }
    throw new IllegalStateException("No text field column found for updating values.");
  }

  private void updateOrbitFromHolds() {
    int colIndex = gridColumns.indexOf(TEXT_FIELD);
    int rowIndex = 0;
    for (Map.Entry<KeplerEnums, Kepler> entry : orbit.getKeplarianElements().entrySet()) {
      if (orbitalParameterHolds.getHold(entry.getKey())) {
        String textValue = ((TextField) nodesArray[rowIndex][colIndex]).getText();
        orbit.setFromString(entry.getKey(), textValue);
      }
      rowIndex++;
    }
  }

  private void syncHoldsWithUI() {
    int colIndex = gridColumns.indexOf(HOLD);
    int rowIndex = 0;
    for (Map.Entry<KeplerEnums, Kepler> entry : orbit.getKeplarianElements().entrySet()) {
      ((ToggleButton) nodesArray[rowIndex][colIndex])
          .setSelected(orbitalParameterHolds.getHold(entry.getKey()));
      rowIndex++;
    }
  }
}

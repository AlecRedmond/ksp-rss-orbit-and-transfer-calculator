package org.example.gui.nodemethod;

import static org.example.equations.application.keplerianelements.Kepler.KeplerEnums.*;
import static org.example.gui.nodemethod.OrbitalGridPane.GridColumns.*;

import java.util.HashMap;
import java.util.Map;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.GridPane;
import lombok.Data;
import org.example.equations.application.Orbit;
import org.example.equations.application.keplerianelements.Kepler.KeplerEnums;
import org.example.gui.nodegroups.TopNodeGroup;

@Data
public class OrbitalGridPane {
  private static Node[][] gridPaneArray;

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
    RIGHT_HOLD(4),
    COLUMNS(5);

    private int gridIndex;

    GridColumns(int gridIndex) {
      this.gridIndex = gridIndex;
    }
  }

  public static void buildGridPane() {
    Orbit orbit = new Orbit();
    GridPane gridPane = new GridPane();
    gridPaneArray = new Node[keplerEnumsList.length][COLUMNS.gridIndex];
    for (int i = 0; i < keplerEnumsList.length; i++) {
      Label label = new Label(orbit.getDisplayName(keplerEnumsList[i]));
      gridPaneArray[i][LABEL.gridIndex] = label;
      gridPaneArray[i][LEFT_HOLD.gridIndex] = new ToggleButton("Hold");
      gridPaneArray[i][LEFT_TEXTFIELD.gridIndex] = new TextField("");
      gridPaneArray[i][RIGHT_TEXTFIELD.gridIndex] = new TextField("");
      gridPaneArray[i][RIGHT_HOLD.gridIndex] = new ToggleButton("Hold");
    }

    for(int col = 0; col < COLUMNS.gridIndex; col++){
      for(int row = 0; row < keplerEnumsList.length; row++){
        gridPane.add(gridPaneArray[row][col],col,row);
      }
    }
    TopNodeGroup.setGridPane(gridPane);
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

  public static HashMap<KeplerEnums, String> getFieldText(boolean leftText) {

    int colIndex;
    if (leftText) {
      colIndex = LEFT_TEXTFIELD.gridIndex;
    } else {
      colIndex = RIGHT_TEXTFIELD.gridIndex;
    }

    HashMap<KeplerEnums, String> fieldTextMap = new HashMap<>();
    for (int i = 0; i < keplerEnumsList.length; i++) {
      TextField textField = (TextField) gridPaneArray[i][colIndex];
      String text = textField.getText();
      fieldTextMap.put(keplerEnumsList[i],text);
    }
    return fieldTextMap;
  }

  public static void setFieldText(HashMap<KeplerEnums, String> newText,boolean leftText){
    int colIndex;
    if (leftText) {
      colIndex = LEFT_TEXTFIELD.gridIndex;
    } else {
      colIndex = RIGHT_TEXTFIELD.gridIndex;
    }

    Orbit orbit = new Orbit();
    for(Node[] gridNode : gridPaneArray){
      for(Map.Entry<KeplerEnums,String> entry : newText.entrySet()){
        KeplerEnums keplerEnum = entry.getKey();
        String mapLabel = orbit.getDisplayName(keplerEnum);
        String gridLabel = ((Label) gridNode[LABEL.gridIndex]).getText();
        if(mapLabel.equalsIgnoreCase(gridLabel)){
          String newTextEntry = entry.getValue();
          ((TextField) gridNode[colIndex]).setText(newTextEntry);
        }
      }
    }
  }

  private static void setGridPaneArray(Node[][] gridPaneArray) {
    GridPane gridPane = new GridPane();
    int col = 0;
    int row = 0;
    for(Node[] gridPaneRow : gridPaneArray){
      for(Node gridElement : gridPaneRow){
        gridPane.add(gridPaneArray[row][col],row,col);
        col++;
      }
      row++;
    }

    TopNodeGroup.setGridPane(gridPane);
  }


  private static Node[][] getGridPaneArray() {
    int nRows = TopNodeGroup.getGridPane().getRowCount();
    int nCols = TopNodeGroup.getGridPane().getColumnCount();
    Node[][] gridPaneArray = new Node[nRows][nCols];
    for (Node node : TopNodeGroup.getGridPane().getChildren()) {
      gridPaneArray[GridPane.getRowIndex(node)][GridPane.getColumnIndex(node)] = node;
    }

    return gridPaneArray;
  }
}

package org.example.gui.scene;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import lombok.Data;
import org.example.equations.application.Orbit;
import org.example.equations.method.OrbitBuilder;
import org.example.gui.scene.sceneelements.OrbitalGridPane;
import org.example.gui.scene.sceneelements.TransferResults;

@Data
public class SceneLayout {
  private OrbitalGridPane initialOrbitPane;
  private OrbitalGridPane finalOrbitPane;
  private OrbitalGridPane transferOrbitPane;
  private TransferResults transferResults;

  public Scene buildScene() {
    HBox firstRow = firstRowHBox();
    HBox secondRow = secondRowHBox();
    HBox thirdRow = thirdRowHBox();
    VBox vBox = new VBox(firstRow, secondRow, thirdRow);
    return new Scene(vBox, 640, 480);
  }

  private HBox firstRowHBox() {
    initialOrbitPane = new OrbitalGridPane();
    finalOrbitPane = new OrbitalGridPane(true);
    transferOrbitPane = new OrbitalGridPane(initialOrbitPane,finalOrbitPane);
    return new HBox(initialOrbitPane.getGridPane(),transferOrbitPane.getGridPane(), finalOrbitPane.getGridPane());
  }

  private HBox secondRowHBox() {
    Button resetButton = new Button("Reset");
    resetButton.setOnAction(
        actionEvent -> {
          Orbit orbit = new OrbitBuilder(250e3, 250e3, 5.25).getOrbit();
          initialOrbitPane.setOrbit(orbit);
          Orbit orbit1 = new OrbitBuilder(35786e3, 35786e3, 0).getOrbit();
          finalOrbitPane.setOrbit(orbit1);
        });
    Button button2 = new Button("Change Orbits From Holds");
    button2.setOnAction(
        actionEvent -> {
          Orbit orbitA = initialOrbitPane.extractOrbitFromHolds();
          initialOrbitPane.setOrbit(orbitA);
          Orbit orbitB = finalOrbitPane.extractOrbitFromHolds();
          finalOrbitPane.setOrbit(orbitB);
          transferOrbitPane.updateTransferOrbit(initialOrbitPane,finalOrbitPane);
          transferResults.updateText(orbitA,orbitB);
        });
    return new HBox(resetButton, button2);
  }

  private HBox thirdRowHBox() {
      transferResults = new TransferResults();
      return transferResults.getLayout();
  }
}

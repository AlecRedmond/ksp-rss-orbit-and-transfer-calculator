package org.example.gui.scenebuilder;

import javafx.scene.control.Button;
import javafx.scene.text.Text;
import org.example.gui.nodemethod.OrbitalGridPane;
import org.example.gui.nodegroups.buttonsgroup.GuiButtons;
import org.example.gui.nodegroups.TopNodeGroup;

public class SceneNodeFill {
  public static void fillAll() {
    fillGridPane();
    fillButtons();
    fillText();
  }

  private static void fillText() {
    Text text = new Text("Hello World");
    TopNodeGroup.setOutputText(text);
  }

  private static void fillButtons() {
    Button clearButton = new Button("MyFirstButton");
    GuiButtons.setClearButton(clearButton);

    Button calculateButton = new Button("MySecondButton");
    GuiButtons.setCalculateButton(calculateButton);
  }

  private static void fillGridPane() {
    OrbitalGridPane.buildGridPane();
  }
}

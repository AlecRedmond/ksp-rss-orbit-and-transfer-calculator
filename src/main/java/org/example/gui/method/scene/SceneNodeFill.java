package org.example.gui.method.scene;

import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import org.example.gui.application.scene.GuiButtons;
import org.example.gui.application.scene.SceneElements;

public class SceneNodeFill {
  public static void fillAll() {
    fillGridPane();
    fillButtons();
    fillText();
  }

  private static void fillText() {
    Text text = new Text("Hello World");
    SceneElements.setOutputText(text);
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

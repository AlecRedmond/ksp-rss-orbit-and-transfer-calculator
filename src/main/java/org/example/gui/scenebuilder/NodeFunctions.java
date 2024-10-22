package org.example.gui.scenebuilder;

import javafx.scene.text.Text;
import org.example.gui.nodemethod.OrbitalGridPane;
import org.example.gui.nodegroups.TopNodeGroup;

public class NodeFunctions {
  public static void fillAll() {
    fillGridPane();
    fillButtons();
    initialiseText("hello world");
  }

  private static void initialiseText(String string) {
    Text text = new Text(string);
    TopNodeGroup.setOutputText(text);
  }

  public static void setText(String string){
    TopNodeGroup.getOutputText().setText(string);
  }

  private static void fillButtons() {
  }

  private static void fillGridPane() {
    OrbitalGridPane.buildGridPane();
  }
}

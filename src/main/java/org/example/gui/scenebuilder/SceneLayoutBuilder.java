package org.example.gui.scenebuilder;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import lombok.Data;
import org.example.gui.nodegroups.TopNodeGroup;
import org.example.gui.nodegroups.buttonsgroup.GuiButtons;
import org.example.gui.nodemethod.buttonsgroup.ButtonActions;

@Data
public class SceneLayoutBuilder {
  private static Scene scene;
  private static VBox vBox;

  public static Scene buildScene() {
    NodeFunctions.fillAll();
    vBox = stackSceneElements();
    vBox.setPadding(new Insets(20));
    return new Scene(vBox, 640, 480);
  }

  private SceneLayoutBuilder() {}

  private static VBox stackSceneElements() {
    VBox vBox = new VBox();
    HBox guiButtons =
        new HBox(
            GuiButtons.getCalculateButton(),
            GuiButtons.getClearButton(),
            GuiButtons.getDeBugButton());
    vBox.getChildren().addAll(TopNodeGroup.getGridPane(), guiButtons, TopNodeGroup.getOutputText());
    ButtonActions.allButtons();
    return vBox;
  }
}

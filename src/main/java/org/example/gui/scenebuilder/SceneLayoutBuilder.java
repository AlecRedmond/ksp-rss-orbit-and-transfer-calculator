package org.example.gui.scenebuilder;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import lombok.Data;
import org.example.gui.nodemethod.buttonsgroup.ButtonActions;
import org.example.gui.nodegroups.buttonsgroup.GuiButtons;
import org.example.gui.nodegroups.TopNodeGroup;

@Data
public class SceneLayoutBuilder {
  private static Scene scene;

  public static Scene buildScene() {
    SceneNodeFill.fillAll();
    VBox vBox = stackSceneElements();
    vBox.setPadding( new Insets(10));
    return new Scene(vBox, 480, 640);
  }

  private static VBox stackSceneElements() {
    VBox vBox = new VBox();
    HBox guiButtons = new HBox(GuiButtons.getCalculateButton(),GuiButtons.getClearButton(),GuiButtons.getDeBugButton());
    vBox.getChildren().addAll(
            TopNodeGroup.getGridPane(),
            guiButtons,
            TopNodeGroup.getOutputText()
    );
    buttonActions();
    return vBox;
  }

  private static void buttonActions() {
    ButtonActions.debugButton();
  }
}

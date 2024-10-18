package org.example.gui.method.scene;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import lombok.Data;
import org.example.gui.application.scene.GuiButtons;
import org.example.gui.application.scene.SceneElements;

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
    HBox guiButtons = new HBox(GuiButtons.getCalculateButton(),GuiButtons.getClearButton());
    vBox.getChildren().addAll(
            SceneElements.getGridPane(),
            guiButtons,
            SceneElements.getOutputText()
    );
    return vBox;
  }
}

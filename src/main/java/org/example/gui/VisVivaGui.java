package org.example.gui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.gui.scene.SceneLayout;

public class VisVivaGui extends Application {
  private Stage stage;

  public static void main(String[] args){
    launch(args);
  }
  @Override
  public void start(Stage stage) throws Exception {
    this.stage = stage;

    SceneLayout sceneLayout = new SceneLayout();

    Scene scene = sceneLayout.buildScene();
    stage.setScene(scene);
    stage.show();

  }
}

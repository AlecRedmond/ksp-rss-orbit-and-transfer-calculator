package org.example.gui.application.scene;

import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class SceneElements {

    @Getter
    @Setter
    private static GridPane gridPane = new GridPane();
    @Getter
    @Setter
    private static GuiButtons guiButtons = new GuiButtons();
    @Getter
    @Setter
    private static Text outputText = new Text("");

}

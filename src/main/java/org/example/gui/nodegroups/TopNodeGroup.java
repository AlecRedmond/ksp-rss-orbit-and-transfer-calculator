package org.example.gui.nodegroups;

import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.example.gui.nodegroups.buttonsgroup.GuiButtons;

@Data
public class TopNodeGroup {

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

package org.example.gui.application.scene;

import javafx.scene.control.Button;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class GuiButtons {
    @Getter
    @Setter
    private static Button clearButton = new Button("");
    @Getter
    @Setter
    private static Button calculateButton = new Button("");


}

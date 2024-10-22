package org.example.gui.nodegroups.buttonsgroup;

import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class GuiButtons {
    @Getter
    @Setter
    private static Button clearButton = new Button("Clear");
    @Getter
    @Setter
    private static Button calculateButton = new Button("Calculate");
    @Getter
    @Setter
    private static Button inclinationChangeButton = new Button("Calc w/ inclination:");
    @Getter
    @Setter
    private static TextField inclinationField = new TextField("0");




}

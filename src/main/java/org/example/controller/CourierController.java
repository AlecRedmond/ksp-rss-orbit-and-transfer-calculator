package org.example.controller;

import lombok.Data;

@Data
public class CourierController {

    public static void calculateVisViva(){
        InputLogic.parseVisVivaData();
        WorkingLogic.doVisVivaTransfer();
        OutputLogic.writeVisVivaResults();
        OutputLogic.writeHohmannTransferResults();
    }

    public static void debug(){
    }


    public static void clearAll() {
        OutputLogic.clearFields();
    }
}

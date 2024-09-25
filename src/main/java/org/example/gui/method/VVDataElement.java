package org.example.gui.method;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.example.formatting.StringUnitParser;

@Data
@Getter
@Setter
public class VVDataElement {
    private String parameterName;
    private boolean isHeld;
    private String dataString;
    private double data;

    public VVDataElement(String parameterName, boolean isHeld, String dataString){
        this.parameterName = parameterName;
        this.isHeld = isHeld;
        this.dataString = dataString;
    }

    public void setData(){
        try{
            this.data = StringUnitParser.stringToDouble(this.dataString);
        } catch (Exception e){
            this.data = 0;
        }
    }
    
}

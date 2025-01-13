package org.example.equations.method.holdlogic;

import java.util.List;
import java.util.Map;
import lombok.Data;
import org.example.equations.application.Orbit;
import org.example.equations.application.OrbitalParameterHolds;
import org.example.equations.application.keplerianelements.Kepler.KeplerEnums;

@Data
public class HoldLogic {
    private OrbitalParameterHolds orbitalParameterHolds;

    public OrbitalParameterHolds check(OrbitalParameterHolds orbitalParameterHolds){
        this.orbitalParameterHolds = orbitalParameterHolds;
        performLogic();
        return orbitalParameterHolds;
    }

    private void performLogic() {
        List<KeplerEnums> orderedClickList = orbitalParameterHolds.getOrderedClickList();
        KeplerEnums lastClicked = orderedClickList.get(orderedClickList.size()-1);
        Map<KeplerEnums,Boolean> holdsMapCopy = orbitalParameterHolds.getHoldsMap();
        holdsMapCopy.forEach((keplerEnum,isHeld) -> {
            if(isHeld && orderedClickList.contains(keplerEnum)){
                toggleMethod(new Orbit().getHoldCompatibility(lastClicked).get(keplerEnum),lastClicked,keplerEnum);
            }
        });
    }

    private void toggleMethod(ToggleAction toggleAction, KeplerEnums lastClicked, KeplerEnums keplerEnum) {
        
    }
}

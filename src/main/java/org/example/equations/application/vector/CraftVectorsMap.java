package org.example.equations.application.vector;

import lombok.Getter;
import org.example.equations.application.Body;

import java.util.EnumMap;
import java.util.Map;
import java.util.Optional;

@Getter
public class CraftVectorsMap {
    private final Map<Body,CraftVectors> map = new EnumMap<>(Body.class);

    public void putData(CraftVectors craftVectors){
        map.put(craftVectors.getBody(),craftVectors);
    }

    public Optional<CraftVectors> getCraftVectors(Body body){
        return Optional.ofNullable(map.get(body));
    }
}

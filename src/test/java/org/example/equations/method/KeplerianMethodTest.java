package org.example.equations.method;

import org.example.equations.application.Body;
import org.example.equations.application.Keplerian;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class KeplerianMethodTest {
    KeplerianMethod keplerianMethod;

    @Test
    public void testKeplerianMethod(){
        keplerianMethod = new KeplerianMethod(Body.EARTH,300000,250000,true);
        System.out.println(keplerianMethod.getKeplerian());
        assertEquals(keplerianMethod.getKeplerian().getEccentricity(),0);
    }
    @Test
    public void testFieldNames(){
        List fields = Arrays.stream(Keplerian.class.getDeclaredFields()).map(Field::getName).toList();
        System.out.println(fields);
    }
}
